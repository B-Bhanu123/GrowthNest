-- Phase 5: Intelligence Engine, Fraud Detection & Accounting Schema Definition
-- PostgreSQL Schemas: fraud, accounting, expense, analytics

CREATE SCHEMA IF NOT EXISTS fraud;
CREATE SCHEMA IF NOT EXISTS accounting;
CREATE SCHEMA IF NOT EXISTS expense;
CREATE SCHEMA IF NOT EXISTS analytics;

-- Fraud Detection Engine Schema
CREATE TABLE IF NOT EXISTS fraud.risk_evaluations (
    evaluation_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    transaction_id UUID NOT NULL,
    customer_id UUID NOT NULL,
    risk_score DECIMAL(5, 2) NOT NULL, -- 0.00 to 100.00
    decision VARCHAR(20) NOT NULL, -- 'ALLOW', 'REVIEW', 'CHALLENGE', 'BLOCK'
    triggered_rules TEXT[],
    evaluated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS fraud.blacklisted_entities (
    entity_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    entity_type VARCHAR(50) NOT NULL, -- 'IP_ADDRESS', 'CARD_HASH', 'DEVICE_ID', 'VPA'
    entity_value VARCHAR(255) UNIQUE NOT NULL,
    reason TEXT NOT NULL,
    added_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- General Accounting & Chart of Accounts
CREATE TABLE IF NOT EXISTS accounting.chart_of_accounts (
    account_code VARCHAR(50) PRIMARY KEY,
    account_name VARCHAR(255) NOT NULL,
    category VARCHAR(50) NOT NULL, -- 'ASSET', 'LIABILITY', 'EQUITY', 'REVENUE', 'EXPENSE'
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS accounting.fiscal_periods (
    period_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    period_name VARCHAR(50) NOT NULL, -- e.g. 'FY2026-Q1'
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    is_closed BOOLEAN DEFAULT FALSE
);

-- Expense Management
CREATE TABLE IF NOT EXISTS expense.records (
    expense_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    category VARCHAR(100) NOT NULL, -- 'TRAVEL', 'SOFTWARE', 'MARKETING', 'OFFICE'
    amount DECIMAL(18, 4) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD',
    merchant_name VARCHAR(255),
    receipt_url TEXT,
    approval_status VARCHAR(20) DEFAULT 'PENDING', -- 'PENDING', 'APPROVED', 'REJECTED'
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Analytics Metrics Aggregate Cache
CREATE TABLE IF NOT EXISTS analytics.daily_aggregates (
    metric_date DATE PRIMARY KEY,
    gmv_amount DECIMAL(18, 4) DEFAULT 0.0000,
    transaction_count INT DEFAULT 0,
    successful_count INT DEFAULT 0,
    failed_count INT DEFAULT 0,
    fraud_blocked_count INT DEFAULT 0,
    total_mdr_revenue DECIMAL(18, 4) DEFAULT 0.0000
);
