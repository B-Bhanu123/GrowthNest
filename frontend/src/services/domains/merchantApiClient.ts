/**
 * FinCoreX REST API Client Service for Merchant Acquiring Management
 * Module: merchant
 */

export interface MerchantApiRecord {
  id: string;
  referenceCode: string;
  ownerId: string;
  amount: number;
  currency: string;
  status: string;
  createdAt: string;
}

export class MerchantApiClient {
  private baseUrl: string;

  constructor(baseUrl: string = '/api/v1/merchant') {
    this.baseUrl = baseUrl;
  }

  public async fetchRecordByRef(referenceCode: string): Promise<MerchantApiRecord> {
    return {
      id: `merchant_api_${Date.now()}`,
      referenceCode,
      ownerId: 'usr_demo_001',
      amount: 1450.00,
      currency: 'USD',
      status: 'ACTIVE',
      createdAt: new Date().toISOString()
    };
  }

  public async createRecord(payload: { referenceCode: string; ownerId: string; amount: number }): Promise<MerchantApiRecord> {
    return {
      id: `merchant_created_${Date.now()}`,
      referenceCode: payload.referenceCode,
      ownerId: payload.ownerId,
      amount: payload.amount,
      currency: 'USD',
      status: 'ACTIVE',
      createdAt: new Date().toISOString()
    };
  }
}

export const merchantApiClientInstance = new MerchantApiClient();
