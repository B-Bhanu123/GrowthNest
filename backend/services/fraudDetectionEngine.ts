// FinCoreX Real-Time Fraud Detection & Risk Scoring Engine

export type FraudDecision = 'ALLOW' | 'REVIEW' | 'CHALLENGE' | 'BLOCK';

export interface FraudEvaluationResult {
  evaluationId: string;
  transactionId: string;
  customerId: string;
  riskScore: number; // 0 to 100
  decision: FraudDecision;
  triggeredRules: string[];
  evaluatedAt: string;
}

export class FraudDetectionEngine {
  private blacklistedIPs: Set<string> = new Set(['192.168.1.99', '10.0.0.66']);

  public evaluateTransaction(
    transactionId: string,
    customerId: string,
    amount: number,
    ipAddress: string,
    velocityPerMinute: number,
    isNewDevice: boolean
  ): FraudEvaluationResult {
    let score = 0;
    const triggeredRules: string[] = [];

    // Rule 1: IP Blacklist Check
    if (this.blacklistedIPs.has(ipAddress)) {
      score += 100;
      triggeredRules.push('CRITICAL: IP Address flagged in global blacklist');
    }

    // Rule 2: Unusually high transaction amount
    if (amount > 25000) {
      score += 40;
      triggeredRules.push('HIGH_VALUE: Single transaction exceeds $25,000 threshold');
    } else if (amount > 10000) {
      score += 20;
      triggeredRules.push('ELEVATED_VALUE: Transaction exceeds $10,000');
    }

    // Rule 3: High transaction velocity
    if (velocityPerMinute > 5) {
      score += 35;
      triggeredRules.push('VELOCITY_SPIKE: High frequency transaction velocity');
    }

    // Rule 4: New unrecognized device combined with high value
    if (isNewDevice && amount > 5000) {
      score += 25;
      triggeredRules.push('DEVICE_RISK: High-value payment on newly registered device');
    }

    let decision: FraudDecision = 'ALLOW';
    if (score >= 80) decision = 'BLOCK';
    else if (score >= 50) decision = 'REVIEW';
    else if (score >= 30) decision = 'CHALLENGE';

    return {
      evaluationId: `eval_${Date.now()}`,
      transactionId,
      customerId,
      riskScore: score,
      decision,
      triggeredRules,
      evaluatedAt: new Date().toISOString()
    };
  }
}

export const fraudDetectionEngineInstance = new FraudDetectionEngine();
