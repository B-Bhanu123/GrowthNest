-- Phase 4: Extended Financial Services Schema Definition
-- PostgreSQL Schemas: lending, credit, investment, insurance

CREATE SCHEMA IF NOT EXISTS lending;
CREATE SCHEMA IF NOT EXISTS credit;
CREATE SCHEMA IF NOT EXISTS investment;
CREATE SCHEMA IF NOT EXISTS insurance;

-- Lending Lifecycle Schema
CREATE TABLE IF NOT EXISTS lending.loan_applications (
    loan_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id UUID NOT NULL,
    loan_product VARCHAR(100) NOT NULL, -- 'PERSONAL_EXPRESS', 'HOME_MORTGAGE', 'SME_WORKING_CAPITAL'
    principal_amount DECIMAL(18, 4) NOT NULL,
    interest_rate DECIMAL(5, 4) NOT NULL, -- e.g. 0.0850 (8.5%)
    tenure_months INT NOT NULL,
    status VARCHAR(50) DEFAULT 'SUBMITTED', -- 'SUBMITTED', 'UNDERWRITING', 'APPROVED', 'DISBURSED', 'CLOSED', 'REJECTED'
    approved_amount DECIMAL(18, 4),
    disbursed_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS lending.repayment_schedules (
    schedule_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    loan_id UUID NOT NULL REFERENCES lending.loan_applications(loan_id),
    installment_no INT NOT NULL,
    due_date DATE NOT NULL,
    emi_amount DECIMAL(18, 4) NOT NULL,
    principal_component DECIMAL(18, 4) NOT NULL,
    interest_component DECIMAL(18, 4) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING' -- 'PENDING', 'PAID', 'OVERDUE'
);

-- Credit Scoring Profile
CREATE TABLE IF NOT EXISTS credit.scores (
    score_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id UUID NOT NULL UNIQUE,
    credit_score INT NOT NULL, -- Range: 300 to 850
    risk_category VARCHAR(20) NOT NULL, -- 'LOW', 'MEDIUM', 'HIGH'
    debt_to_income_ratio DECIMAL(5, 4) NOT NULL,
    on_time_payment_pct DECIMAL(5, 4) NOT NULL,
    calculated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Investment Platform Schema
CREATE TABLE IF NOT EXISTS investment.portfolios (
    portfolio_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id UUID NOT NULL,
    portfolio_name VARCHAR(100) DEFAULT 'Default Wealth Portfolio',
    total_invested DECIMAL(18, 4) DEFAULT 0.0000,
    current_valuation DECIMAL(18, 4) DEFAULT 0.0000,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS investment.holdings (
    holding_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    portfolio_id UUID NOT NULL REFERENCES investment.portfolios(portfolio_id),
    asset_symbol VARCHAR(20) NOT NULL, -- 'AAPL', 'VOO', 'US10Y_BOND', 'GLOBAL_TECH_ETF'
    asset_class VARCHAR(50) NOT NULL, -- 'EQUITY', 'BOND', 'MUTUAL_FUND', 'ETF'
    quantity DECIMAL(18, 6) NOT NULL,
    average_buy_price DECIMAL(18, 4) NOT NULL,
    current_price DECIMAL(18, 4) NOT NULL
);

-- Insurance System Schema
CREATE TABLE IF NOT EXISTS insurance.policies (
    policy_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id UUID NOT NULL,
    policy_number VARCHAR(100) UNIQUE NOT NULL,
    product_type VARCHAR(100) NOT NULL, -- 'HEALTH_SHIELD', 'LIFE_PROTECT', 'CYBER_PAYMENT_PROTECT'
    sum_insured DECIMAL(18, 4) NOT NULL,
    annual_premium DECIMAL(18, 4) NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE', -- 'ACTIVE', 'EXPIRED', 'CLAIMED', 'CANCELLED'
    issued_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL
);
