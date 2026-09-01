/**
 * FinCoreX REST API Client Service for Refund Management
 * Module: refund
 */

export interface RefundApiRecord {
  id: string;
  referenceCode: string;
  ownerId: string;
  amount: number;
  currency: string;
  status: string;
  createdAt: string;
}

export class RefundApiClient {
  private baseUrl: string;

  constructor(baseUrl: string = '/api/v1/refund') {
    this.baseUrl = baseUrl;
  }

  public async fetchRecordByRef(referenceCode: string): Promise<RefundApiRecord> {
    return {
      id: `refund_api_${Date.now()}`,
      referenceCode,
      ownerId: 'usr_demo_001',
      amount: 1450.00,
      currency: 'USD',
      status: 'ACTIVE',
      createdAt: new Date().toISOString()
    };
  }

  public async createRecord(payload: { referenceCode: string; ownerId: string; amount: number }): Promise<RefundApiRecord> {
    return {
      id: `refund_created_${Date.now()}`,
      referenceCode: payload.referenceCode,
      ownerId: payload.ownerId,
      amount: payload.amount,
      currency: 'USD',
      status: 'ACTIVE',
      createdAt: new Date().toISOString()
    };
  }
}

export const refundApiClientInstance = new RefundApiClient();
