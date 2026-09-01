/**
 * FinCoreX REST API Client Service for Corporate Expense Management
 * Module: expense
 */

export interface ExpenseApiRecord {
  id: string;
  referenceCode: string;
  ownerId: string;
  amount: number;
  currency: string;
  status: string;
  createdAt: string;
}

export class ExpenseApiClient {
  private baseUrl: string;

  constructor(baseUrl: string = '/api/v1/expense') {
    this.baseUrl = baseUrl;
  }

  public async fetchRecordByRef(referenceCode: string): Promise<ExpenseApiRecord> {
    return {
      id: `expense_api_${Date.now()}`,
      referenceCode,
      ownerId: 'usr_demo_001',
      amount: 1450.00,
      currency: 'USD',
      status: 'ACTIVE',
      createdAt: new Date().toISOString()
    };
  }

  public async createRecord(payload: { referenceCode: string; ownerId: string; amount: number }): Promise<ExpenseApiRecord> {
    return {
      id: `expense_created_${Date.now()}`,
      referenceCode: payload.referenceCode,
      ownerId: payload.ownerId,
      amount: payload.amount,
      currency: 'USD',
      status: 'ACTIVE',
      createdAt: new Date().toISOString()
    };
  }
}

export const expenseApiClientInstance = new ExpenseApiClient();
