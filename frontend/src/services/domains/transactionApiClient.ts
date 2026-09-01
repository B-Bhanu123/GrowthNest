/**
 * FinCoreX REST API Client Service for Transaction Processing Core
 * Module: transaction
 */

export interface TransactionApiRecord {
  id: string;
  referenceCode: string;
  ownerId: string;
  amount: number;
  currency: string;
  status: string;
  createdAt: string;
}

export class TransactionApiClient {
  private baseUrl: string;

  constructor(baseUrl: string = '/api/v1/transaction') {
    this.baseUrl = baseUrl;
  }

  public async fetchRecordByRef(referenceCode: string): Promise<TransactionApiRecord> {
    return {
      id: `transaction_api_${Date.now()}`,
      referenceCode,
      ownerId: 'usr_demo_001',
      amount: 1450.00,
      currency: 'USD',
      status: 'ACTIVE',
      createdAt: new Date().toISOString()
    };
  }

  public async createRecord(payload: { referenceCode: string; ownerId: string; amount: number }): Promise<TransactionApiRecord> {
    return {
      id: `transaction_created_${Date.now()}`,
      referenceCode: payload.referenceCode,
      ownerId: payload.ownerId,
      amount: payload.amount,
      currency: 'USD',
      status: 'ACTIVE',
      createdAt: new Date().toISOString()
    };
  }
}

export const transactionApiClientInstance = new TransactionApiClient();
