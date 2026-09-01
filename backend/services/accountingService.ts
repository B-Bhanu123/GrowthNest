// FinCoreX General Accounting Service

export interface ChartAccount {
  code: string;
  name: string;
  category: 'ASSET' | 'LIABILITY' | 'EQUITY' | 'REVENUE' | 'EXPENSE';
}

export class AccountingService {
  private chartOfAccounts: ChartAccount[] = [
    { code: '1000', name: 'Cash and Liquid Reserves', category: 'ASSET' },
    { code: '1100', name: 'Accounts Receivable', category: 'ASSET' },
    { code: '2000', name: 'Customer Escrow Liabilities', category: 'LIABILITY' },
    { code: '2100', name: 'Accounts Payable', category: 'LIABILITY' },
    { code: '3000', name: 'Paid-In Capital', category: 'EQUITY' },
    { code: '4000', name: 'Payment Gateway Fee Revenue', category: 'REVENUE' },
    { code: '5000', name: 'Infrastructure & Cloud Operational Expense', category: 'EXPENSE' }
  ];

  public getChartOfAccounts(): ChartAccount[] {
    return this.chartOfAccounts;
  }
}

export const accountingServiceInstance = new AccountingService();
