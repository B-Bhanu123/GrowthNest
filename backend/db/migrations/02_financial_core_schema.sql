-- Phase 2: Financial Core & Account Systems Schema Definition
-- PostgreSQL Schemas: customer, merchant, account, wallet, transaction

CREATE SCHEMA IF NOT EXISTS customer;
CREATE SCHEMA IF NOT EXISTS merchant;
CREATE SCHEMA IF NOT EXISTS account;
CREATE SCHEMA IF NOT EXISTS wallet;
CREATE SCHEMA IF NOT EXISTS transaction;

-- Customer Profile
CREATE TYPE customer.kyc_status AS ENUM ('UNVERIFIED', 'SUBMITTED', 'VERIFIED', 'REJECTED', 'EXPIRED');

CREATE TABLE IF NOT EXISTS customer.profiles (
    customer_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL UNIQUE,
    kyc_status customer.kyc_status NOT NULL DEFAULT 'UNVERIFIED',
    national_id_hash VARCHAR(255),
    tax_id_hash VARCHAR(255),
    address_line1 VARCHAR(255),
    city VARCHAR(100),
    country VARCHAR(100),
    postal_code VARCHAR(20),
    risk_rating VARCHAR(20) DEFAULT 'LOW',
    daily_transaction_limit DECIMAL(18, 4) DEFAULT 50000.00,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Merchant Profile
CREATE TABLE IF NOT EXISTS merchant.stores (
    merchant_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL UNIQUE,
    business_name VARCHAR(255) NOT NULL,
    business_registration_no VARCHAR(100) UNIQUE NOT NULL,
    mcc_code VARCHAR(10) NOT NULL, -- Merchant Category Code
    settlement_account_no VARCHAR(100) NOT NULL,
    settlement_routing_no VARCHAR(100) NOT NULL,
    fee_percentage DECIMAL(5, 4) DEFAULT 0.0150, -- 1.5% merchant MDR
    status VARCHAR(50) DEFAULT 'ACTIVE',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Financial Accounts
CREATE TYPE account.account_type AS ENUM ('SAVINGS', 'CURRENT', 'SETTLEMENT', 'ESCROW', 'VIRTUAL');

CREATE TABLE IF NOT EXISTS account.financial_accounts (
    account_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    owner_id UUID NOT NULL, -- References customer_id or merchant_id
    account_number VARCHAR(34) UNIQUE NOT NULL,
    iban VARCHAR(34) UNIQUE,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    type account.account_type NOT NULL,
    available_balance DECIMAL(18, 4) NOT NULL DEFAULT 0.0000,
    reserved_balance DECIMAL(18, 4) NOT NULL DEFAULT 0.0000,
    is_frozen BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Stored-Value Digital Wallet
CREATE TABLE IF NOT EXISTS wallet.digital_wallets (
    wallet_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id UUID NOT NULL UNIQUE,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    available_balance DECIMAL(18, 4) NOT NULL DEFAULT 0.0000,
    reserved_balance DECIMAL(18, 4) NOT NULL DEFAULT 0.0000,
    tier VARCHAR(20) DEFAULT 'STANDARD',
    max_capacity DECIMAL(18, 4) DEFAULT 100000.00,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Transaction State Machine Core
CREATE TYPE transaction.tx_state AS ENUM (
    'CREATED',
    'INITIATED',
    'AUTHORIZED',
    'CAPTURED',
    'SETTLED',
    'FAILED',
    'CANCELLED',
    'EXPIRED',
    'REFUNDED',
    'PARTIALLY_REFUNDED'
);

CREATE TABLE IF NOT EXISTS transaction.records (
    transaction_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    idempotency_key VARCHAR(255) UNIQUE NOT NULL,
    sender_account_id UUID,
    receiver_account_id UUID,
    amount DECIMAL(18, 4) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    fee_amount DECIMAL(18, 4) DEFAULT 0.0000,
    state transaction.tx_state NOT NULL DEFAULT 'CREATED',
    description TEXT,
    reference_code VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_transactions_state ON transaction.records(state);
CREATE INDEX idx_transactions_idempotency ON transaction.records(idempotency_key);
