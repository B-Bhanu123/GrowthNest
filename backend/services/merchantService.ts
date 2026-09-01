// FinCoreX Merchant Management Service

export interface MerchantProfile {
  merchantId: string;
  userId: string;
  businessName: string;
  registrationNumber: string;
  mccCode: string;
  settlementAccount: string;
  mdrFeeRate: number; // Merchant Discount Rate
  status: 'ACTIVE' | 'SUSPENDED' | 'UNDER_REVIEW';
}

export class MerchantService {
  private merchants: Map<string, MerchantProfile> = new Map();

  constructor() {
    this.seedDemoMerchant();
  }

  private seedDemoMerchant(): void {
    const merchant: MerchantProfile = {
      merchantId: 'mer_demo_001',
      userId: 'usr_demo_merchant_001',
      businessName: 'TechCorp Global Store',
      registrationNumber: 'REG-99182374',
      mccCode: '5732', // Electronics
      settlementAccount: 'FCX-SETTLE-889900',
      mdrFeeRate: 0.015, // 1.5%
      status: 'ACTIVE'
    };
    this.merchants.set(merchant.merchantId, merchant);
  }

  public getMerchant(merchantId: string): MerchantProfile | undefined {
    return this.merchants.get(merchantId);
  }

  public calculateMerchantFee(amount: number, merchantId: string): number {
    const merchant = this.merchants.get(merchantId);
    const rate = merchant ? merchant.mdrFeeRate : 0.02;
    return Number((amount * rate).toFixed(2));
  }
}

export const merchantServiceInstance = new MerchantService();
