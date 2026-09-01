// FinCoreX Unified TypeScript Types & Interfaces

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

export interface UserSession {
  userId: string;
  email: string;
  fullName: string;
  role: UserRole;
  token: string;
  isAuthenticated: boolean;
}

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

export interface MerchantStore {
  merchantId: string;
  businessName: string;
  registrationNumber: string;
  mccCode: string;
  settlementAccount: string;
  mdrFeeRate: number;
  status: 'ACTIVE' | 'SUSPENDED';
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

export interface DigitalWallet {
  walletId: string;
  customerId: string;
  currency: string;
  availableBalance: number;
  reservedBalance: number;
  tier: 'STANDARD' | 'PREMIUM' | 'VIP';
  maxCapacity: number;
  isFrozen: boolean;
}

export type PaymentMethodType = 'CARD' | 'UPI' | 'WALLET' | 'NETBANKING' | 'TOKENIZED_CARD';

export interface PaymentOrder {
  orderId: string;
  merchantId: string;
  customerId: string;
  amount: number;
  currency: string;
  paymentMethod: PaymentMethodType;
  providerName: string;
  status: 'CREATED' | 'INITIATED' | 'AUTHORIZED' | 'CAPTURED' | 'SETTLED' | 'FAILED';
  idempotencyKey: string;
  createdAt: string;
}

export interface VPAAlias {
  vpaAddress: string;
  ownerId: string;
  accountNo: string;
  status: 'ACTIVE' | 'BLOCKED';
}

export interface JournalEntry {
  entryId: string;
  reference: string;
  description: string;
  postedAt: string;
  lines: { accountCode: string; debit: number; credit: number }[];
}

export interface LoanApplication {
  loanId: string;
  customerId: string;
  loanProduct: string;
  principalAmount: number;
  annualInterestRate: number;
  tenureMonths: number;
  status: 'SUBMITTED' | 'UNDERWRITING' | 'APPROVED' | 'DISBURSED' | 'CLOSED' | 'REJECTED';
  emiAmount: number;
}

export interface CreditProfile {
  customerId: string;
  creditScore: number;
  riskCategory: 'LOW' | 'MEDIUM' | 'HIGH';
  dtiRatio: number;
  onTimePaymentPct: number;
  maxEligibleLoanAmount: number;
  explainableFactors: string[];
}

export interface InvestmentAsset {
  symbol: string;
  name: string;
  assetClass: 'EQUITY' | 'BOND' | 'MUTUAL_FUND' | 'ETF';
  currentPrice: number;
  dayChangePct: number;
}

export interface InsurancePolicy {
  policyId: string;
  customerId: string;
  policyNumber: string;
  productType: 'HEALTH_SHIELD' | 'LIFE_PROTECT' | 'CYBER_PAYMENT_PROTECT';
  sumInsured: number;
  annualPremium: number;
  status: 'ACTIVE' | 'EXPIRED' | 'CLAIMED';
  expiresAt: string;
}

export interface FraudEvaluationResult {
  evaluationId: string;
  transactionId: string;
  customerId: string;
  riskScore: number;
  decision: 'ALLOW' | 'REVIEW' | 'CHALLENGE' | 'BLOCK';
  triggeredRules: string[];
  evaluatedAt: string;
}

export interface FeatureFlag {
  key: string;
  description: string;
  isEnabled: boolean;
}
