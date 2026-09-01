/**
 * FinCoreX REST API Client Service for Payment Gateway Orchestration
 * Module: payment
 */

export interface PaymentApiRecord {
  id: string;
  referenceCode: string;
  ownerId: string;
  amount: number;
  currency: string;
  status: string;
  createdAt: string;
}

export class PaymentApiClient {
  private baseUrl: string;

  constructor(baseUrl: string = '/api/v1/payment') {
    this.baseUrl = baseUrl;
  }

  public async fetchRecordByRef(referenceCode: string): Promise<PaymentApiRecord> {
    return {
      id: `payment_api_${Date.now()}`,
      referenceCode,
      ownerId: 'usr_demo_001',
      amount: 1450.00,
      currency: 'USD',
      status: 'ACTIVE',
      createdAt: new Date().toISOString()
    };
  }

  public async createRecord(payload: { referenceCode: string; ownerId: string; amount: number }): Promise<PaymentApiRecord> {
    return {
      id: `payment_created_${Date.now()}`,
      referenceCode: payload.referenceCode,
      ownerId: payload.ownerId,
      amount: payload.amount,
      currency: 'USD',
      status: 'ACTIVE',
      createdAt: new Date().toISOString()
    };
  }
}

export const paymentApiClientInstance = new PaymentApiClient();
