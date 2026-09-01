// FinCoreX Identity & Access Management Service

export type UserRole =
  | 'CUSTOMER'
  | 'MERCHANT'
  | 'ADMIN'
  | 'FINANCE_OPERATOR'
  | 'RISK_ANALYST'
  | 'LOAN_OFFICER'
  | 'INVESTMENT_MANAGER'
  | 'INSURANCE_AGENT'
  | 'AUDITOR'
  | 'SUPPORT_AGENT';

export type AccountStatus = 'PENDING_VERIFICATION' | 'ACTIVE' | 'SUSPENDED' | 'LOCKED' | 'CLOSED';

export interface UserProfile {
  id: string;
  email: string;
  fullName: string;
  phoneNumber: string;
  role: UserRole;
  status: AccountStatus;
  mfaEnabled: boolean;
  createdAt: string;
}

export interface AuthTokenResponse {
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
  tokenType: string;
  user: UserProfile;
}

export class IdentityService {
  private users: Map<string, UserProfile & { passwordHash: string }> = new Map();
  private sessions: Map<string, { userId: string; expiresAt: Date }> = new Map();

  constructor() {
    this.seedDemoUsers();
  }

  private seedDemoUsers(): void {
    const demoUser: UserProfile & { passwordHash: string } = {
      id: 'usr_demo_customer_001',
      email: 'customer@fincorex.com',
      fullName: 'Alex Vance',
      phoneNumber: '+1-555-0192',
      role: 'CUSTOMER',
      status: 'ACTIVE',
      mfaEnabled: true,
      createdAt: new Date().toISOString(),
      passwordHash: 'hash_demo_123'
    };

    const demoMerchant: UserProfile & { passwordHash: string } = {
      id: 'usr_demo_merchant_001',
      email: 'merchant@fincorex.com',
      fullName: 'TechCorp Global',
      phoneNumber: '+1-555-0844',
      role: 'MERCHANT',
      status: 'ACTIVE',
      mfaEnabled: true,
      createdAt: new Date().toISOString(),
      passwordHash: 'hash_demo_123'
    };

    const demoAdmin: UserProfile & { passwordHash: string } = {
      id: 'usr_demo_admin_001',
      email: 'admin@fincorex.com',
      fullName: 'Chief Systems Administrator',
      phoneNumber: '+1-555-0000',
      role: 'ADMIN',
      status: 'ACTIVE',
      mfaEnabled: true,
      createdAt: new Date().toISOString(),
      passwordHash: 'hash_demo_123'
    };

    this.users.set(demoUser.email, demoUser);
    this.users.set(demoMerchant.email, demoMerchant);
    this.users.set(demoAdmin.email, demoAdmin);
  }

  public async login(email: string, passwordHash: string): Promise<AuthTokenResponse> {
    const user = this.users.get(email);
    if (!user) {
      throw new Error('Invalid credentials');
    }
    if (user.status !== 'ACTIVE') {
      throw new Error(`Account is currently ${user.status}`);
    }

    const sessionToken = `jwt_access_token_${user.id}_${Date.now()}`;
    const refreshToken = `jwt_refresh_token_${user.id}_${Date.now()}`;
    
    this.sessions.set(sessionToken, {
      userId: user.id,
      expiresAt: new Date(Date.now() + 86400000)
    });

    const { passwordHash: _, ...profile } = user;

    return {
      accessToken: sessionToken,
      refreshToken: refreshToken,
      expiresIn: 86400,
      tokenType: 'Bearer',
      user: profile
    };
  }

  public async verifyOTP(email: string, otpCode: string): Promise<boolean> {
    // Simulated MFA verification logic
    return otpCode === '123456' || otpCode.length === 6;
  }

  public getUserProfile(email: string): UserProfile | undefined {
    const user = this.users.get(email);
    if (!user) return undefined;
    const { passwordHash: _, ...profile } = user;
    return profile;
  }
}

export const identityServiceInstance = new IdentityService();
