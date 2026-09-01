// FinCoreX Customer & Account Management Service

export interface CustomerProfile {
  customerId: string;
  userId: string;
  kycStatus: 'UNVERIFIED' | 'SUBMITTED' | 'VERIFIED' | 'REJECTED';
  nationalIdHash: string;
  addressLine: string;
  city: string;
  country: string;
  dailyLimit: number;
}

export interface FinancialAccount {
  accountId: string;
  ownerId: string;
  accountNumber: string;
  currency: string;
  type: 'SAVINGS' | 'CURRENT' | 'SETTLEMENT' | 'ESCROW' | 'VIRTUAL';
  availableBalance: number;
  reservedBalance: number;
  isFrozen: boolean;
}

export class CustomerService {
  private customers: Map<string, CustomerProfile> = new Map();
  private accounts: Map<string, FinancialAccount> = new Map();

  constructor() {
    this.seedDemoCustomer();
  }

  private seedDemoCustomer(): void {
    const custId = 'cust_demo_001';
    const profile: CustomerProfile = {
      customerId: custId,
      userId: 'usr_demo_customer_001',
      kycStatus: 'VERIFIED',
      nationalIdHash: 'hash_nat_998877',
      addressLine: '742 Evergreen Terrace',
      city: 'Springfield',
      country: 'USA',
      dailyLimit: 50000.0
    };
    this.customers.set(custId, profile);

    const account: FinancialAccount = {
      accountId: 'acc_sav_001',
      ownerId: custId,
      accountNumber: 'FCX-1002938481',
      currency: 'USD',
      type: 'SAVINGS',
      availableBalance: 24850.75,
      reservedBalance: 1500.0,
      isFrozen: false
    };
    this.accounts.set(account.accountId, account);
  }

  public getCustomerProfile(customerId: string): CustomerProfile | undefined {
    return this.customers.get(customerId);
  }

  public getAccount(accountId: string): FinancialAccount | undefined {
    return this.accounts.get(accountId);
  }

  public updateKYCStatus(customerId: string, status: CustomerProfile['kycStatus']): boolean {
    const cust = this.customers.get(customerId);
    if (!cust) return false;
    cust.kycStatus = status;
    return true;
  }
}

export const customerServiceInstance = new CustomerService();
