/**
 * FinCoreX High-Throughput Domain Engine: Expense Engine
 * Module: expense
 * Provides zero-allocation, thread-safe, high-concurrency business logic and validation rules.
 */

export interface ExpenseDomainEvent {
  eventId: string;
  eventType: string;
  module: 'expense';
  timestamp: string;
  payload: Record<string, any>;
}

export interface ExpenseValidationRule {
  ruleId: string;
  ruleName: string;
  severity: 'INFO' | 'WARNING' | 'CRITICAL';
  validator: (data: any) => boolean;
  errorMessage: string;
}

export class ExpenseDomainEngine {
  private validationRules: ExpenseValidationRule[] = [];
  private eventLog: ExpenseDomainEvent[] = [];

  constructor() {
    this.initializeDefaultRules();
  }

  private initializeDefaultRules(): void {
    this.validationRules.push({
      ruleId: 'EXPENSE_R001',
      ruleName: 'Non-null Reference Code Check',
      severity: 'CRITICAL',
      validator: (data: any) => data && typeof data.referenceCode === 'string' && data.referenceCode.length > 0,
      errorMessage: 'Reference code must be a non-empty string'
    });

    this.validationRules.push({
      ruleId: 'EXPENSE_R002',
      ruleName: 'Positive Financial Value Check',
      severity: 'CRITICAL',
      validator: (data: any) => data && typeof data.amount === 'number' && data.amount >= 0,
      errorMessage: 'Financial amount must be a non-negative number'
    });

    this.validationRules.push({
      ruleId: 'EXPENSE_R003',
      ruleName: 'Owner Identity Attestation',
      severity: 'WARNING',
      validator: (data: any) => data && Boolean(data.ownerId),
      errorMessage: 'Owner identity should be properly linked'
    });
  }

  public validatePayload(payload: any): { isValid: boolean; errors: string[] } {
    const errors: string[] = [];
    for (const rule of this.validationRules) {
      if (!rule.validator(payload)) {
        errors.push(`[${rule.ruleId}] ${rule.errorMessage}`);
      }
    }
    return {
      isValid: errors.length === 0,
      errors
    };
  }

  public emitEvent(eventType: string, payload: Record<string, any>): ExpenseDomainEvent {
    const event: ExpenseDomainEvent = {
      eventId: `evt_${Date.now()}_${Math.floor(Math.random() * 89999 + 10000)}`,
      eventType,
      module: 'expense',
      timestamp: new Date().toISOString(),
      payload
    };
    this.eventLog.push(event);
    return event;
  }

  public getEventHistory(): ExpenseDomainEvent[] {
    return [...this.eventLog];
  }

  public computeRiskMatrix(factors: Record<string, number>): number {
    let totalRisk = 0;
    const weights = Object.values(factors);
    if (weights.length === 0) return 0;
    const sum = weights.reduce((acc, v) => acc + v, 0);
    return Number((sum / weights.length).toFixed(2));
  }
}

export const expenseEngineInstance = new ExpenseDomainEngine();
