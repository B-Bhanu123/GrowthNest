import { FraudDetectionEngine } from '../backend/services/fraudDetectionEngine';
import { ExpenseService } from '../backend/services/expenseService';
import { AnalyticsEngine } from '../backend/services/analyticsEngine';

describe('Phase 5: Intelligence Engine & Analytics Tests', () => {
  let fraudEngine: FraudDetectionEngine;
  let expenseService: ExpenseService;
  let analytics: AnalyticsEngine;

  beforeEach(() => {
    fraudEngine = new FraudDetectionEngine();
    expenseService = new ExpenseService();
    analytics = new AnalyticsEngine();
  });

  test('should block transaction from blacklisted IP', () => {
    const res = fraudEngine.evaluateTransaction('tx_001', 'cust_001', 500, '192.168.1.99', 1, false);
    expect(res.decision).toBe('BLOCK');
    expect(res.riskScore).toBeGreaterThanOrEqual(80);
    expect(res.triggeredRules.length).toBeGreaterThan(0);
  });

  test('should allow normal transaction within threshold', () => {
    const res = fraudEngine.evaluateTransaction('tx_002', 'cust_001', 250, '172.16.0.1', 1, false);
    expect(res.decision).toBe('ALLOW');
  });

  test('should auto-approve small expenses', () => {
    const exp = expenseService.createExpense('usr_demo_customer_001', 'SOFTWARE', 120, 'Github Subscription');
    expect(exp.approvalStatus).toBe('APPROVED');
  });

  test('should return executive metrics', () => {
    const metrics = analytics.getExecutiveMetrics();
    expect(metrics.gmvTotal).toBeGreaterThan(0);
    expect(metrics.successRatePct).toBeGreaterThan(90);
  });
});
