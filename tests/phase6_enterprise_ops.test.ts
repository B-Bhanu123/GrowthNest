import { NotificationService } from '../backend/services/notificationService';
import { AuditComplianceService } from '../backend/services/auditComplianceService';
import { AdminOpsService } from '../backend/services/adminOpsService';

describe('Phase 6: Enterprise Operations & Audit Tests', () => {
  let notification: NotificationService;
  let audit: AuditComplianceService;
  let admin: AdminOpsService;

  beforeEach(() => {
    notification = new NotificationService();
    audit = new AuditComplianceService();
    admin = new AdminOpsService();
  });

  test('should dispatch and retrieve notification', () => {
    const record = notification.dispatchNotification(
      'usr_demo_customer_001',
      'EMAIL',
      'PAYMENT_SUCCESS',
      'Payment Confirmation',
      'Your payment of $749.50 was successful.'
    );
    expect(record.status).toBe('SENT');
    const userNtf = notification.getNotifications('usr_demo_customer_001');
    expect(userNtf.length).toBeGreaterThan(0);
  });

  test('should record immutable audit log', () => {
    const log = audit.recordAuditLog(
      'usr_demo_admin_001',
      'ADMIN',
      'SYSTEM_CONFIG_UPDATE',
      'admin.feature_flags',
      '127.0.0.1',
      { flag: 'ENABLE_UPI_QR_VPA', state: true }
    );
    expect(log.auditId).toBeDefined();
    expect(audit.getAuditTrail().length).toBeGreaterThan(0);
  });

  test('should toggle feature flag state', () => {
    const initial = admin.getFeatureFlags().find(f => f.key === 'ENABLE_INVESTMENT_FRACTIONAL_SHARES')!;
    const initialState = initial.isEnabled;
    const toggled = admin.toggleFeatureFlag('ENABLE_INVESTMENT_FRACTIONAL_SHARES');
    expect(toggled.isEnabled).toBe(!initialState);
  });
});
