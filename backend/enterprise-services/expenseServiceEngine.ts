/**
 * FinCoreX Enterprise TypeScript Service Module: Corporate Expense Management (expense)
 */

export interface ExpenseEnterpriseEvent {
  eventId: string;
  module: 'expense';
  action: string;
  payload: any;
  timestamp: string;
}

export class ExpenseEnterpriseServiceEngine {
  private eventStore: ExpenseEnterpriseEvent[] = [];

  public logEvent(action: string, payload: any): ExpenseEnterpriseEvent {
    const event: ExpenseEnterpriseEvent = {
      eventId: `evt_${Date.now()}_${Math.floor(Math.random() * 89999 + 10000)}`,
      module: 'expense',
      action,
      payload,
      timestamp: new Date().toISOString()
    };
    this.eventStore.push(event);
    return event;
  }

  public getEventStore(): ExpenseEnterpriseEvent[] {
    return [...this.eventStore];
  }
}

export const expenseServiceEngineInstance = new ExpenseEnterpriseServiceEngine();
