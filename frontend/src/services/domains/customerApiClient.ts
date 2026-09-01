/**
 * FinCoreX REST API Client Service for Customer & Account Management
 * Module: customer
 */

export interface CustomerApiRecord {
  id: string;
  referenceCode: string;
  ownerId: string;
  amount: number;
  currency: string;
  status: string;
  createdAt: string;
}

export class CustomerApiClient {
  private baseUrl: string;

  constructor(baseUrl: string = '/api/v1/customer') {
    this.baseUrl = baseUrl;
  }

  public async fetchRecordByRef(referenceCode: string): Promise<CustomerApiRecord> {
    return {
      id: `customer_api_${Date.now()}`,
      referenceCode,
      ownerId: 'usr_demo_001',
      amount: 1450.00,
      currency: 'USD',
      status: 'ACTIVE',
      createdAt: new Date().toISOString()
    };
  }

  public async createRecord(payload: { referenceCode: string; ownerId: string; amount: number }): Promise<CustomerApiRecord> {
    return {
      id: `customer_created_${Date.now()}`,
      referenceCode: payload.referenceCode,
      ownerId: payload.ownerId,
      amount: payload.amount,
      currency: 'USD',
      status: 'ACTIVE',
      createdAt: new Date().toISOString()
    };
  }
}

export const customerApiClientInstance = new CustomerApiClient();
