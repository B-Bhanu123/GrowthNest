// FinCoreX Immutable Audit & Compliance Logging Service

export interface AuditLogRecord {
  auditId: string;
  actorUserId: string;
  actorRole: string;
  actionPerformed: string;
  targetResource: string;
  ipAddress: string;
  payloadSnapshot: any;
  createdAt: string;
}

export class AuditComplianceService {
  private logs: AuditLogRecord[] = [];

  constructor() {
    this.seedDemoAuditLogs();
  }

  private seedDemoAuditLogs(): void {
    this.logs.push(
      {
        auditId: 'aud_001',
        actorUserId: 'usr_demo_admin_001',
        actorRole: 'ADMIN',
        actionPerformed: 'UPDATE_SYSTEM_FEE_SCHEDULE',
        targetResource: 'merchant.stores/fee_percentage',
        ipAddress: '10.0.0.1',
        payloadSnapshot: { oldRate: 0.018, newRate: 0.015 },
        createdAt: new Date(Date.now() - 3600000 * 5).toISOString()
      },
      {
        auditId: 'aud_002',
        actorUserId: 'usr_demo_customer_001',
        actorRole: 'CUSTOMER',
        actionPerformed: 'INITIATE_UPI_TRANSFER',
        targetResource: 'upi.aliases/alex@fincorex',
        ipAddress: '172.16.0.4',
        payloadSnapshot: { amount: 250.0, payee: 'techcorp@fincorex' },
        createdAt: new Date(Date.now() - 3600000 * 2).toISOString()
      }
    );
  }

  public recordAuditLog(
    actorUserId: string,
    actorRole: string,
    actionPerformed: string,
    targetResource: string,
    ipAddress: string,
    payloadSnapshot: any
  ): AuditLogRecord {
    const record: AuditLogRecord = {
      auditId: `aud_${Date.now()}`,
      actorUserId,
      actorRole,
      actionPerformed,
      targetResource,
      ipAddress,
      payloadSnapshot,
      createdAt: new Date().toISOString()
    };
    this.logs.unshift(record);
    return record;
  }

  public getAuditTrail(): AuditLogRecord[] {
    return this.logs;
  }
}

export const auditComplianceServiceInstance = new AuditComplianceService();
