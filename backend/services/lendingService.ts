// FinCoreX Loan Management & Underwriting Engine

export interface LoanApplication {
  loanId: string;
  customerId: string;
  loanProduct: string;
  principalAmount: number;
  annualInterestRate: number; // e.g. 0.09 (9%)
  tenureMonths: number;
  status: 'SUBMITTED' | 'UNDERWRITING' | 'APPROVED' | 'DISBURSED' | 'CLOSED' | 'REJECTED';
  emiAmount: number;
}

export interface EMIScheduleItem {
  installmentNo: number;
  dueDate: string;
  emiAmount: number;
  principalComponent: number;
  interestComponent: number;
}

export class LendingService {
  private applications: Map<string, LoanApplication> = new Map();

  public calculateEMI(principal: number, annualRate: number, tenureMonths: number): number {
    const monthlyRate = annualRate / 12;
    if (monthlyRate === 0) return principal / tenureMonths;

    const emi =
      (principal * monthlyRate * Math.pow(1 + monthlyRate, tenureMonths)) /
      (Math.pow(1 + monthlyRate, tenureMonths) - 1);
    return Number(emi.toFixed(2));
  }

  public generateRepaymentSchedule(principal: number, annualRate: number, tenureMonths: number): EMIScheduleItem[] {
    const emi = this.calculateEMI(principal, annualRate, tenureMonths);
    const monthlyRate = annualRate / 12;
    let remainingPrincipal = principal;
    const schedule: EMIScheduleItem[] = [];

    const now = new Date();

    for (let i = 1; i <= tenureMonths; i++) {
      const interestComp = Number((remainingPrincipal * monthlyRate).toFixed(2));
      const principalComp = Number((emi - interestComp).toFixed(2));
      remainingPrincipal = Number(Math.max(0, remainingPrincipal - principalComp).toFixed(2));

      const dueDate = new Date(now.getFullYear(), now.getMonth() + i, 1).toISOString().split('T')[0];
      schedule.push({
        installmentNo: i,
        dueDate,
        emiAmount: emi,
        principalComponent: principalComp,
        interestComponent: interestComp
      });
    }

    return schedule;
  }

  public submitLoanApplication(
    customerId: string,
    loanProduct: string,
    principalAmount: number,
    annualInterestRate: number,
    tenureMonths: number
  ): LoanApplication {
    const loanId = `loan_${Date.now()}`;
    const emiAmount = this.calculateEMI(principalAmount, annualInterestRate, tenureMonths);

    const app: LoanApplication = {
      loanId,
      customerId,
      loanProduct,
      principalAmount,
      annualInterestRate,
      tenureMonths,
      status: 'SUBMITTED',
      emiAmount
    };

    this.applications.set(loanId, app);
    return app;
  }

  public approveAndDisburse(loanId: string): LoanApplication {
    const app = this.applications.get(loanId);
    if (!app) throw new Error('Loan application not found');
    app.status = 'DISBURSED';
    return app;
  }
}

export const lendingServiceInstance = new LendingService();
