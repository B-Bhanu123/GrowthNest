// FinCoreX Investment & Portfolio Platform

export interface InvestmentAsset {
  symbol: string;
  name: string;
  assetClass: 'EQUITY' | 'BOND' | 'MUTUAL_FUND' | 'ETF';
  currentPrice: number;
  dayChangePct: number;
}

export interface PortfolioHolding {
  symbol: string;
  assetName: string;
  assetClass: string;
  quantity: number;
  avgBuyPrice: number;
  currentPrice: number;
  totalValuation: number;
  unrealizedPnL: number;
}

export class InvestmentService {
  private catalog: InvestmentAsset[] = [
    { symbol: 'AAPL', name: 'Apple Inc.', assetClass: 'EQUITY', currentPrice: 189.50, dayChangePct: 1.42 },
    { symbol: 'VOO', name: 'Vanguard S&P 500 ETF', assetClass: 'ETF', currentPrice: 462.10, dayChangePct: 0.78 },
    { symbol: 'US10Y', name: 'US Treasury 10-Year Bond', assetClass: 'BOND', currentPrice: 98.25, dayChangePct: -0.15 },
    { symbol: 'FID_GROWTH', name: 'Fidelity Tech Growth Fund', assetClass: 'MUTUAL_FUND', currentPrice: 84.30, dayChangePct: 2.10 }
  ];

  public getCatalog(): InvestmentAsset[] {
    return this.catalog;
  }

  public getDemoPortfolio(customerId: string): PortfolioHolding[] {
    return [
      {
        symbol: 'AAPL',
        assetName: 'Apple Inc.',
        assetClass: 'EQUITY',
        quantity: 50,
        avgBuyPrice: 165.00,
        currentPrice: 189.50,
        totalValuation: 9475.00,
        unrealizedPnL: 1225.00
      },
      {
        symbol: 'VOO',
        assetName: 'Vanguard S&P 500 ETF',
        assetClass: 'ETF',
        quantity: 30,
        avgBuyPrice: 420.00,
        currentPrice: 462.10,
        totalValuation: 13863.00,
        unrealizedPnL: 1263.00
      }
    ];
  }
}

export const investmentServiceInstance = new InvestmentService();
