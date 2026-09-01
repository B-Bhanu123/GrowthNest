import { PaymentGatewayService } from '../backend/services/paymentGatewayService';
import { UPIService } from '../backend/services/upiService';
import { LedgerService } from '../backend/services/ledgerService';
import { SettlementService } from '../backend/services/settlementService';

describe('Phase 3: Payment Orchestration, Transfers & Reconciliation Tests', () => {
  let gateway: PaymentGatewayService;
  let upi: UPIService;
  let ledger: LedgerService;
  let settlement: SettlementService;

  beforeEach(() => {
    gateway = new PaymentGatewayService();
    upi = new UPIService();
    ledger = new LedgerService();
    settlement = new SettlementService();
  });

  test('should authorize and capture payment order', () => {
    const order = gateway.createPaymentOrder('idem_pay_1', 'mer_001', 'cust_001', 150.0);
    expect(order.status).toBe('CREATED');
    const captured = gateway.authorizeAndCapture(order.orderId);
    expect(captured.status).toBe('CAPTURED');
  });

  test('should resolve valid UPI VPA alias', () => {
    const vpa = upi.resolveVPA('alex@fincorex');
    expect(vpa).toBeDefined();
    expect(vpa?.ownerId).toBe('cust_demo_001');
  });

  test('should post balanced double-entry journal entry', () => {
    const lines = [
      { accountCode: '1010', debit: 100.0, credit: 0 },
      { accountCode: '2010', debit: 0, credit: 100.0 }
    ];
    const entry = ledger.postJournalEntry('tx_ref_99', 'Customer wallet deposit', lines);
    expect(entry.entryId).toBeDefined();
  });

  test('should reject imbalanced double-entry journal entry', () => {
    const badLines = [
      { accountCode: '1010', debit: 100.0, credit: 0 },
      { accountCode: '2010', debit: 0, credit: 50.0 } // Imbalance of 50
    ];
    expect(() => ledger.postJournalEntry('tx_bad', 'Imbalanced entry', badLines)).toThrow(/imbalance/i);
  });

  test('should create and execute merchant settlement batch', () => {
    const batch = settlement.createBatch('mer_001', 1000.0, 0.015);
    expect(batch.netSettlement).toBe(985.0);
    const executed = settlement.executeSettlement(batch.batchId);
    expect(executed.status).toBe('PAID');
  });
});
