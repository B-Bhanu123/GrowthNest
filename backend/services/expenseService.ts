// FinCoreX Expense Management Service

export interface ExpenseRecord {
  expenseId: string;
  userId: string;
  category: 'TRAVEL' | 'SOFTWARE' | 'MARKETING' | 'OFFICE';
  amount: number;
  currency: string;
  merchantName: string;
  approvalStatus: 'PENDING' | 'APPROVED' | 'REJECTED';
  createdAt: string;
}

export class ExpenseService {
  private expenses: Map<string, ExpenseRecord> = new Map();

  constructor() {
    this.seedDemoExpenses();
  }

  private seedDemoExpenses(): void {
    const exp: ExpenseRecord = {
      expenseId: 'exp_demo_001',
      userId: 'usr_demo_customer_001',
      category: 'SOFTWARE',
      amount: 299.00,
      currency: 'USD',
      merchantName: 'AWS Cloud Services',
      approvalStatus: 'APPROVED',
      createdAt: new Date(Date.now() - 86400000 * 3).toISOString()
    };
    this.expenses.set(exp.expenseId, exp);
  }

  public createExpense(
    userId: string,
    category: ExpenseRecord['category'],
    amount: number,
    merchantName: string
  ): ExpenseRecord {
    const expId = `exp_${Date.now()}`;
    const record: ExpenseRecord = {
      expenseId: expId,
      userId,
      category,
      amount,
      currency: 'USD',
      merchantName,
      approvalStatus: amount < 500 ? 'APPROVED' : 'PENDING',
      createdAt: new Date().toISOString()
    };
    this.expenses.set(expId, record);
    return record;
  }

  public getExpenses(userId: string): ExpenseRecord[] {
    return Array.from(this.expenses.values()).filter(e => e.userId === userId);
  }
}

export const expenseServiceInstance = new ExpenseService();
