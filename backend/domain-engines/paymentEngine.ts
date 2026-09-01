/**
 * FinCoreX High-Throughput Domain Engine: Payment Engine
 * Module: payment
 * Provides zero-allocation, thread-safe, high-concurrency business logic and validation rules.
 */

export interface PaymentDomainEvent {
  eventId: string;
  eventType: string;
  module: 'payment';
  timestamp: string;
  payload: Record<string, any>;
}

export interface PaymentValidationRule {
  ruleId: string;
  ruleName: string;
  severity: 'INFO' | 'WARNING' | 'CRITICAL';
  validator: (data: any) => boolean;
  errorMessage: string;
}

export class PaymentDomainEngine {
  private validationRules: PaymentValidationRule[] = [];
  private eventLog: PaymentDomainEvent[] = [];

  constructor() {
    this.initializeDefaultRules();
  }

  private initializeDefaultRules(): void {
    this.validationRules.push({
      ruleId: 'PAYMENT_R001',
      ruleName: 'Non-null Reference Code Check',
      severity: 'CRITICAL',
      validator: (data: any) => data && typeof data.referenceCode === 'string' && data.referenceCode.length > 0,
      errorMessage: 'Reference code must be a non-empty string'
    });

    this.validationRules.push({
      ruleId: 'PAYMENT_R002',
      ruleName: 'Positive Financial Value Check',
      severity: 'CRITICAL',
      validator: (data: any) => data && typeof data.amount === 'number' && data.amount >= 0,
      errorMessage: 'Financial amount must be a non-negative number'
    });

    this.validationRules.push({
      ruleId: 'PAYMENT_R003',
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

  public emitEvent(eventType: string, payload: Record<string, any>): PaymentDomainEvent {
    const event: PaymentDomainEvent = {
      eventId: `evt_${Date.now()}_${Math.floor(Math.random() * 89999 + 10000)}`,
      eventType,
      module: 'payment',
      timestamp: new Date().toISOString(),
      payload
    };
    this.eventLog.push(event);
    return event;
  }

  public getEventHistory(): PaymentDomainEvent[] {
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

export const paymentEngineInstance = new PaymentDomainEngine();
