import { CustomerService } from '../backend/services/customerService';
import { MerchantService } from '../backend/services/merchantService';
import { WalletService } from '../backend/services/walletService';
import { TransactionEngine } from '../backend/services/transactionCore';

describe('Phase 2: Financial Core & Account Systems Tests', () => {
  let customerService: CustomerService;
  let merchantService: MerchantService;
  let walletService: WalletService;
  let txEngine: TransactionEngine;

  beforeEach(() => {
    customerService = new CustomerService();
    merchantService = new MerchantService();
    walletService = new WalletService();
    txEngine = new TransactionEngine();
  });

  test('should retrieve customer profile and savings account', () => {
    const cust = customerService.getCustomerProfile('cust_demo_001');
    expect(cust).toBeDefined();
    expect(cust?.kycStatus).toBe('VERIFIED');

    const acc = customerService.getAccount('acc_sav_001');
    expect(acc?.availableBalance).toBeGreaterThan(0);
  });

  test('should calculate merchant MDR fee', () => {
    const fee = merchantService.calculateMerchantFee(100.0, 'mer_demo_001');
    expect(fee).toBe(1.5);
  });

  test('should top up wallet and record transaction history', () => {
    const wallet = walletService.getWalletByCustomer('cust_demo_001');
    expect(wallet).toBeDefined();
    const initialBal = wallet!.availableBalance;

    const updated = walletService.topUpWallet(wallet!.walletId, 500.0);
    expect(updated.availableBalance).toBe(initialBal + 500.0);

    const history = walletService.getHistory(wallet!.walletId);
    expect(history.length).toBeGreaterThan(0);
    expect(history[0].type).toBe('TOPUP');
  });

  test('should enforce transaction state machine rules', () => {
    const tx = txEngine.createTransaction('idem_key_001', 'cust_001', 'mer_001', 250.0);
    expect(tx.state).toBe('CREATED');

    const initiated = txEngine.transitionState(tx.transactionId, 'INITIATED');
    expect(initiated.state).toBe('INITIATED');

    const authorized = txEngine.transitionState(tx.transactionId, 'AUTHORIZED');
    expect(authorized.state).toBe('AUTHORIZED');

    // Invalid transition directly to REFUNDED from AUTHORIZED should fail
    expect(() => txEngine.transitionState(tx.transactionId, 'REFUNDED')).toThrow();
  });
});
