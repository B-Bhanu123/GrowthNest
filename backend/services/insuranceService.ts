// FinCoreX Insurance Management System

export interface InsurancePolicy {
  policyId: string;
  customerId: string;
  policyNumber: string;
  productType: 'HEALTH_SHIELD' | 'LIFE_PROTECT' | 'CYBER_PAYMENT_PROTECT';
  sumInsured: number;
  annualPremium: number;
  status: 'ACTIVE' | 'EXPIRED' | 'CLAIMED';
  expiresAt: string;
}

export class InsuranceService {
  private policies: Map<string, InsurancePolicy> = new Map();

  constructor() {
    this.seedDemoPolicy();
  }

  private seedDemoPolicy(): void {
    const pol: InsurancePolicy = {
      policyId: 'pol_demo_001',
      customerId: 'cust_demo_001',
      policyNumber: 'FCX-INS-771122',
      productType: 'CYBER_PAYMENT_PROTECT',
      sumInsured: 100000.00,
      annualPremium: 450.00,
      status: 'ACTIVE',
      expiresAt: new Date(Date.now() + 86400000 * 365).toISOString()
    };
    this.policies.set(pol.policyId, pol);
  }

  public getPolicies(customerId: string): InsurancePolicy[] {
    return Array.from(this.policies.values()).filter(p => p.customerId === customerId);
  }
}

export const insuranceServiceInstance = new InsuranceService();
