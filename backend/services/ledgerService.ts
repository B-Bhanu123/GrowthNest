// FinCoreX Double-Entry Financial Ledger Service
// Accounting Invariant: Total Debits must strictly equal Total Credits

export type LedgerAccountType = 'ASSET' | 'LIABILITY' | 'EQUITY' | 'REVENUE' | 'EXPENSE';

export interface LedgerAccount {
  code: string;
  name: string;
  type: LedgerAccountType;
  balance: number;
}

export interface JournalLine {
  accountCode: string;
  debit: number;
  credit: number;
}

export interface JournalEntry {
  entryId: string;
  reference: string;
  description: string;
  postedAt: string;
  lines: JournalLine[];
}

export class LedgerService {
  private accounts: Map<string, LedgerAccount> = new Map();
  private entries: JournalEntry[] = [];

  constructor() {
    this.seedChartOfAccounts();
  }

  private seedChartOfAccounts(): void {
    const defaultAccounts: LedgerAccount[] = [
      { code: '1010', name: 'Settlement Cash Account', type: 'ASSET', balance: 1000000.0 },
      { code: '2010', name: 'Customer Wallet Liabilities', type: 'LIABILITY', balance: 500000.0 },
      { code: '2020', name: 'Merchant Payable Liabilities', type: 'LIABILITY', balance: 450000.0 },
      { code: '4010', name: 'Payment Processing Fee Revenue', type: 'REVENUE', balance: 50000.0 }
    ];

    for (const acc of defaultAccounts) {
      this.accounts.set(acc.code, acc);
    }
  }

  public getAccounts(): LedgerAccount[] {
    return Array.from(this.accounts.values());
  }

  public postJournalEntry(reference: string, description: string, lines: JournalLine[]): JournalEntry {
    let totalDebit = 0;
    let totalCredit = 0;

    for (const line of lines) {
      if (!this.accounts.has(line.accountCode)) {
        throw new Error(`Ledger Account ${line.accountCode} does not exist`);
      }
      totalDebit += line.debit;
      totalCredit += line.credit;
    }

    // Strict Double-Entry Invariant Check
    if (Math.abs(totalDebit - totalCredit) > 0.0001) {
      throw new Error(
        `Double-entry imbalance! Total Debits ($${totalDebit.toFixed(2)}) != Total Credits ($${totalCredit.toFixed(2)})`
      );
    }

    // Apply line balances
    for (const line of lines) {
      const acc = this.accounts.get(line.accountCode)!;
      if (acc.type === 'ASSET' || acc.type === 'EXPENSE') {
        acc.balance += line.debit - line.credit;
      } else {
        acc.balance += line.credit - line.debit;
      }
    }

    const entry: JournalEntry = {
      entryId: `je_${Date.now()}_${this.entries.length + 1}`,
      reference,
      description,
      postedAt: new Date().toISOString(),
      lines
    };

    this.entries.push(entry);
    return entry;
  }

  public getJournalHistory(): JournalEntry[] {
    return this.entries;
  }
}

export const ledgerServiceInstance = new LedgerService();
