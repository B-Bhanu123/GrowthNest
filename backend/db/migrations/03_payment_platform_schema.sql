-- Phase 3: Payment Orchestration, Transfers & Reconciliation Schema Definition
-- PostgreSQL Schemas: gateway, upi, ledger, settlement, dispute

CREATE SCHEMA IF NOT EXISTS gateway;
CREATE SCHEMA IF NOT EXISTS upi;
CREATE SCHEMA IF NOT EXISTS ledger;
CREATE SCHEMA IF NOT EXISTS settlement;
CREATE SCHEMA IF NOT EXISTS dispute;

-- Payment Gateway & Provider Routing
CREATE TABLE IF NOT EXISTS gateway.payment_orders (
    order_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    merchant_id UUID NOT NULL,
    customer_id UUID NOT NULL,
    amount DECIMAL(18, 4) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD',
    payment_method VARCHAR(50) NOT NULL, -- 'CARD', 'UPI', 'WALLET', 'NETBANKING'
    provider_name VARCHAR(50) NOT NULL, -- 'STRIPE_MOCK', 'RAZORPAY_MOCK', 'INTERNAL_SETTLE'
    provider_reference VARCHAR(255),
    status VARCHAR(50) NOT NULL DEFAULT 'CREATED',
    idempotency_key VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- UPI-like Virtual Payment Address (VPA) Network
CREATE TABLE IF NOT EXISTS upi.aliases (
    vpa_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    vpa_address VARCHAR(255) UNIQUE NOT NULL, -- user@fincorex
    owner_id UUID NOT NULL,
    account_id UUID NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS upi.collect_requests (
    request_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    payer_vpa VARCHAR(255) NOT NULL,
    payee_vpa VARCHAR(255) NOT NULL,
    amount DECIMAL(18, 4) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING', -- 'PENDING', 'APPROVED', 'DECLINED', 'EXPIRED'
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Double-Entry Financial Ledger (Core Accounting Invariant: SUM(debits) = SUM(credits))
CREATE TABLE IF NOT EXISTS ledger.accounts (
    ledger_account_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_code VARCHAR(50) UNIQUE NOT NULL, -- e.g., '1010-CASH', '2010-CUSTOMER-WALLET', '4010-MDR-REVENUE'
    account_name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL, -- 'ASSET', 'LIABILITY', 'EQUITY', 'REVENUE', 'EXPENSE'
    balance DECIMAL(18, 4) DEFAULT 0.0000,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ledger.journal_entries (
    entry_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    transaction_reference VARCHAR(255) NOT NULL,
    description TEXT,
    posted_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ledger.journal_lines (
    line_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    entry_id UUID NOT NULL REFERENCES ledger.journal_entries(entry_id) ON DELETE CASCADE,
    ledger_account_id UUID NOT NULL REFERENCES ledger.accounts(ledger_account_id),
    debit DECIMAL(18, 4) DEFAULT 0.0000,
    credit DECIMAL(18, 4) DEFAULT 0.0000
);

-- Settlement & Reconciliation
CREATE TABLE IF NOT EXISTS settlement.batches (
    batch_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    merchant_id UUID NOT NULL,
    total_volume DECIMAL(18, 4) NOT NULL,
    total_fees DECIMAL(18, 4) NOT NULL,
    net_settlement DECIMAL(18, 4) NOT NULL,
    status VARCHAR(50) DEFAULT 'SCHEDULED', -- 'SCHEDULED', 'PROCESSING', 'PAID', 'FAILED'
    settled_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Refund & Dispute Management
CREATE TABLE IF NOT EXISTS dispute.records (
    dispute_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    transaction_id UUID NOT NULL,
    merchant_id UUID NOT NULL,
    customer_id UUID NOT NULL,
    amount DECIMAL(18, 4) NOT NULL,
    reason_code VARCHAR(100) NOT NULL, -- 'UNAUTHORIZED', 'DUPLICATE', 'GOODS_NOT_RECEIVED'
    status VARCHAR(50) DEFAULT 'OPEN', -- 'OPEN', 'UNDER_REVIEW', 'WON', 'LOST'
    evidence_details TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
