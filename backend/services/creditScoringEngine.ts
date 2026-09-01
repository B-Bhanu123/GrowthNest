// FinCoreX Credit Scoring Engine

export interface CreditProfile {
  customerId: string;
  creditScore: number; // 300 - 850
  riskCategory: 'LOW' | 'MEDIUM' | 'HIGH';
  dtiRatio: number; // Debt-to-Income ratio
  onTimePaymentPct: number;
  maxEligibleLoanAmount: number;
  explainableFactors: string[];
}

export class CreditScoringEngine {
  public calculateScore(
    monthlyIncome: number,
    monthlyDebt: number,
    onTimePaymentPct: number,
    existingAccounts: number
  ): CreditProfile {
    const dtiRatio = monthlyIncome > 0 ? monthlyDebt / monthlyIncome : 1.0;
    
    // Base score computation (FICO-like formula)
    let score = 650;

    // Payment history weight (+150 max)
    score += Math.round((onTimePaymentPct / 100) * 150);

    // Debt-to-Income penalty
    if (dtiRatio < 0.2) score += 50;
    else if (dtiRatio > 0.5) score -= 80;

    // Account depth
    score += Math.min(existingAccounts * 10, 50);

    // Bound between 300 and 850
    const finalScore = Math.max(300, Math.min(850, score));

    let riskCategory: CreditProfile['riskCategory'] = 'LOW';
    if (finalScore < 600) riskCategory = 'HIGH';
    else if (finalScore < 720) riskCategory = 'MEDIUM';

    const maxEligibleLoanAmount = Number((monthlyIncome * (1 - dtiRatio) * 36).toFixed(2));

    const explainableFactors: string[] = [];
    if (onTimePaymentPct >= 98) explainableFactors.push('Exceptional on-time payment track record');
    if (dtiRatio <= 0.3) explainableFactors.push('Healthy Debt-To-Income ratio below 30%');
    if (dtiRatio > 0.45) explainableFactors.push('High debt obligation relative to monthly income');

    return {
      customerId: 'cust_demo_001',
      creditScore: finalScore,
      riskCategory,
      dtiRatio: Number(dtiRatio.toFixed(4)),
      onTimePaymentPct,
      maxEligibleLoanAmount,
      explainableFactors
    };
  }
}

export const creditScoringEngineInstance = new CreditScoringEngine();
