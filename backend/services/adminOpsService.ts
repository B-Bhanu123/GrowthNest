// FinCoreX Admin & Operations Control Service

export interface FeatureFlag {
  key: string;
  description: string;
  isEnabled: boolean;
}

export class AdminOpsService {
  private featureFlags: Map<string, FeatureFlag> = new Map([
    ['ENABLE_UPI_QR_VPA', { key: 'ENABLE_UPI_QR_VPA', description: 'Enable instant QR payment scan & pay', isEnabled: true }],
    ['ENABLE_REALTIME_FRAUD_BLOCK', { key: 'ENABLE_REALTIME_FRAUD_BLOCK', description: 'Auto-block transactions scoring > 80 risk', isEnabled: true }],
    ['ENABLE_EXPRESS_LOAN_DISBURSAL', { key: 'ENABLE_EXPRESS_LOAN_DISBURSAL', description: 'Instant wallet loan disbursement', isEnabled: true }],
    ['ENABLE_INVESTMENT_FRACTIONAL_SHARES', { key: 'ENABLE_INVESTMENT_FRACTIONAL_SHARES', description: 'Support fractional share order execution', isEnabled: false }]
  ]);

  public getFeatureFlags(): FeatureFlag[] {
    return Array.from(this.featureFlags.values());
  }

  public toggleFeatureFlag(key: string): FeatureFlag {
    const flag = this.featureFlags.get(key);
    if (!flag) throw new Error(`Feature flag ${key} not found`);
    flag.isEnabled = !flag.isEnabled;
    return flag;
  }
}

export const adminOpsServiceInstance = new AdminOpsService();
