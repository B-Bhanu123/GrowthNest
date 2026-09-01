// FinCoreX UPI-Like Peer-to-Peer & Peer-to-Merchant Transfer Network Simulation

export interface VPAAlias {
  vpaAddress: string;
  ownerId: string;
  accountNo: string;
  status: 'ACTIVE' | 'BLOCKED';
}

export interface CollectRequest {
  requestId: string;
  payerVPA: string;
  payeeVPA: string;
  amount: number;
  status: 'PENDING' | 'APPROVED' | 'DECLINED' | 'EXPIRED';
  expiresAt: string;
}

export class UPIService {
  private aliases: Map<string, VPAAlias> = new Map();
  private collectRequests: Map<string, CollectRequest> = new Map();

  constructor() {
    this.seedDemoAliases();
  }

  private seedDemoAliases(): void {
    this.aliases.set('alex@fincorex', {
      vpaAddress: 'alex@fincorex',
      ownerId: 'cust_demo_001',
      accountNo: 'FCX-1002938481',
      status: 'ACTIVE'
    });

    this.aliases.set('techcorp@fincorex', {
      vpaAddress: 'techcorp@fincorex',
      ownerId: 'mer_demo_001',
      accountNo: 'FCX-SETTLE-889900',
      status: 'ACTIVE'
    });
  }

  public resolveVPA(vpaAddress: string): VPAAlias | undefined {
    return this.aliases.get(vpaAddress.toLowerCase());
  }

  public createCollectRequest(payeeVPA: string, payerVPA: string, amount: number): CollectRequest {
    const reqId = `req_upi_${Date.now()}`;
    const request: CollectRequest = {
      requestId: reqId,
      payerVPA,
      payeeVPA,
      amount,
      status: 'PENDING',
      expiresAt: new Date(Date.now() + 600000).toISOString() // 10 mins expiry
    };
    this.collectRequests.set(reqId, request);
    return request;
  }

  public approveCollectRequest(requestId: string): CollectRequest {
    const req = this.collectRequests.get(requestId);
    if (!req) throw new Error('Collect request not found');
    req.status = 'APPROVED';
    return req;
  }
}

export const upiServiceInstance = new UPIService();
