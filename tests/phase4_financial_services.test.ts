import { LendingService } from '../backend/services/lendingService';
import { CreditScoringEngine } from '../backend/services/creditScoringEngine';
import { InvestmentService } from '../backend/services/investmentService';

describe('Phase 4: Extended Financial Services Tests', () => {
  let lending: LendingService;
  let credit: CreditScoringEngine;
  let investment: InvestmentService;

  beforeEach(() => {
    lending = new LendingService();
    credit = new CreditScoringEngine();
    investment = new InvestmentService();
  });

  test('should calculate correct EMI for $10,000 loan over 12 months at 8.5%', () => {
    const emi = lending.calculateEMI(10000, 0.085, 12);
    expect(emi).toBe(872.20);

    const schedule = lending.generateRepaymentSchedule(10000, 0.085, 12);
    expect(schedule.length).toBe(12);
    expect(schedule[0].emiAmount).toBe(872.20);
  });

  test('should calculate explainable credit score for applicant', () => {
    const profile = credit.calculateScore(8500, 1500, 99.0, 4);
    expect(profile.creditScore).toBeGreaterThan(700);
    expect(profile.riskCategory).toBe('LOW');
    expect(profile.explainableFactors.length).toBeGreaterThan(0);
  });

  test('should retrieve investment asset catalog and demo portfolio', () => {
    const catalog = investment.getCatalog();
    expect(catalog.length).toBeGreaterThan(0);

    const portfolio = investment.getDemoPortfolio('cust_demo_001');
    expect(portfolio.length).toBe(2);
    expect(portfolio[0].unrealizedPnL).toBeGreaterThan(0);
  });
});
