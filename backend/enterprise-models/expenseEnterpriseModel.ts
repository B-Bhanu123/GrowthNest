/**
 * FinCoreX Enterprise TypeScript Schema & State Manager
 * Module: expense
 */

export interface EnterpriseExpenseSchema {
  id: string;
  referenceCode: string;
  ownerId: string;
  amount: number;
  currency: string;
  status: 'PENDING' | 'ACTIVE' | 'COMPLETED' | 'FAILED' | 'REJECTED';
  metadata: {
    ipAddress?: string;
    userAgent?: string;
    channel?: string;
    riskScore?: number;
    auditTags?: string[];
  };
  createdAt: string;
  updatedAt: string;
}

export class EnterpriseExpenseModelManager {
  private records: Map<string, EnterpriseExpenseSchema> = new Map();

  public registerRecord(data: Omit<EnterpriseExpenseSchema, 'id' | 'createdAt' | 'updatedAt'>): EnterpriseExpenseSchema {
    const id = `expense_rec_${Date.now()}_${Math.floor(Math.random() * 89999 + 10000)}`;
    const record: EnterpriseExpenseSchema = {
      ...data,
      id,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    };
    this.records.set(id, record);
    return record;
  }

  public findById(id: string): EnterpriseExpenseSchema | undefined {
    return this.records.get(id);
  }

  public filterByStatus(status: EnterpriseExpenseSchema['status']): EnterpriseExpenseSchema[] {
    return Array.from(this.records.values()).filter(r => r.status === status);
  }

  public computeAggregateTotal(): number {
    let total = 0;
    for (const record of this.records.values()) {
      total += record.amount;
    }
    return Number(total.toFixed(2));
  }
}

export const expenseEnterpriseManagerInstance = new EnterpriseExpenseModelManager();
