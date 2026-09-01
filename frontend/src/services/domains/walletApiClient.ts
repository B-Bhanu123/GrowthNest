/**
 * FinCoreX REST API Client Service for Stored-Value Digital Wallet
 * Module: wallet
 */

export interface WalletApiRecord {
  id: string;
  referenceCode: string;
  ownerId: string;
  amount: number;
  currency: string;
  status: string;
  createdAt: string;
}

export class WalletApiClient {
  private baseUrl: string;

  constructor(baseUrl: string = '/api/v1/wallet') {
    this.baseUrl = baseUrl;
  }

  public async fetchRecordByRef(referenceCode: string): Promise<WalletApiRecord> {
    return {
      id: `wallet_api_${Date.now()}`,
      referenceCode,
      ownerId: 'usr_demo_001',
      amount: 1450.00,
      currency: 'USD',
      status: 'ACTIVE',
      createdAt: new Date().toISOString()
    };
  }

  public async createRecord(payload: { referenceCode: string; ownerId: string; amount: number }): Promise<WalletApiRecord> {
    return {
      id: `wallet_created_${Date.now()}`,
      referenceCode: payload.referenceCode,
      ownerId: payload.ownerId,
      amount: payload.amount,
      currency: 'USD',
      status: 'ACTIVE',
      createdAt: new Date().toISOString()
    };
  }
}

export const walletApiClientInstance = new WalletApiClient();
