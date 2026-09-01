// FinCoreX Stored-Value Digital Wallet Service

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

export interface WalletTransaction {
  id: string;
  walletId: string;
  type: 'TOPUP' | 'WITHDRAWAL' | 'TRANSFER_OUT' | 'TRANSFER_IN' | 'PAYMENT';
  amount: number;
  fee: number;
  balanceAfter: number;
  reference: string;
  timestamp: string;
}

export class WalletService {
  private wallets: Map<string, DigitalWallet> = new Map();
  private transactions: Map<string, WalletTransaction[]> = new Map();

  constructor() {
    this.seedDemoWallet();
  }

  private seedDemoWallet(): void {
    const wallet: DigitalWallet = {
      walletId: 'wal_demo_001',
      customerId: 'cust_demo_001',
      currency: 'USD',
      availableBalance: 4250.50,
      reservedBalance: 150.00,
      tier: 'PREMIUM',
      maxCapacity: 100000.00,
      isFrozen: false
    };
    this.wallets.set(wallet.walletId, wallet);
    this.transactions.set(wallet.walletId, [
      {
        id: 'wtx_001',
        walletId: wallet.walletId,
        type: 'TOPUP',
        amount: 5000.00,
        fee: 0,
        balanceAfter: 5000.00,
        reference: 'Bank Deposit #88219',
        timestamp: new Date(Date.now() - 86400000 * 2).toISOString()
      },
      {
        id: 'wtx_002',
        walletId: wallet.walletId,
        type: 'PAYMENT',
        amount: 749.50,
        fee: 0,
        balanceAfter: 4250.50,
        reference: 'TechCorp Purchase #9912',
        timestamp: new Date(Date.now() - 3600000 * 4).toISOString()
      }
    ]);
  }

  public getWalletByCustomer(customerId: string): DigitalWallet | undefined {
    for (const wallet of this.wallets.values()) {
      if (wallet.customerId === customerId) return wallet;
    }
    return undefined;
  }

  public topUpWallet(walletId: string, amount: number): DigitalWallet {
    const wallet = this.wallets.get(walletId);
    if (!wallet) throw new Error('Wallet not found');
    if (wallet.isFrozen) throw new Error('Wallet is frozen');

    wallet.availableBalance += amount;
    
    const history = this.transactions.get(walletId) || [];
    history.unshift({
      id: `wtx_${Date.now()}`,
      walletId,
      type: 'TOPUP',
      amount,
      fee: 0,
      balanceAfter: wallet.availableBalance,
      reference: `TopUp Ref #${Math.floor(Math.random() * 899999 + 100000)}`,
      timestamp: new Date().toISOString()
    });
    this.transactions.set(walletId, history);

    return wallet;
  }

  public getHistory(walletId: string): WalletTransaction[] {
    return this.transactions.get(walletId) || [];
  }
}

export const walletServiceInstance = new WalletService();
