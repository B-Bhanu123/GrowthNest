// FinCoreX Refund & Dispute Management Service

export interface DisputeRecord {
  disputeId: string;
  transactionId: string;
  merchantId: string;
  customerId: string;
  amount: number;
  reasonCode: 'UNAUTHORIZED' | 'DUPLICATE' | 'GOODS_NOT_RECEIVED';
  status: 'OPEN' | 'UNDER_REVIEW' | 'WON' | 'LOST';
  evidenceDetails: string;
  createdAt: string;
}

export class DisputeService {
  private disputes: Map<string, DisputeRecord> = new Map();

  public raiseDispute(
    transactionId: string,
    merchantId: string,
    customerId: string,
    amount: number,
    reasonCode: DisputeRecord['reasonCode'],
    evidence: string
  ): DisputeRecord {
    const disputeId = `dsp_${Date.now()}`;
    const dispute: DisputeRecord = {
      disputeId,
      transactionId,
      merchantId,
      customerId,
      amount,
      reasonCode,
      status: 'OPEN',
      evidenceDetails: evidence,
      createdAt: new Date().toISOString()
    };
    this.disputes.set(disputeId, dispute);
    return dispute;
  }

  public resolveDispute(disputeId: string, result: 'WON' | 'LOST'): DisputeRecord {
    const dispute = this.disputes.get(disputeId);
    if (!dispute) throw new Error('Dispute not found');
    dispute.status = result;
    return dispute;
  }
}

export const disputeServiceInstance = new DisputeService();
