-- Phase 1: Identity & Access Management Schema Definition
-- PostgreSQL Schema: identity

CREATE SCHEMA IF NOT EXISTS identity;

CREATE TYPE identity.user_role AS ENUM (
    'CUSTOMER',
    'MERCHANT',
    'ADMIN',
    'FINANCE_OPERATOR',
    'RISK_ANALYST',
    'LOAN_OFFICER',
    'INVESTMENT_MANAGER',
    'INSURANCE_AGENT',
    'AUDITOR',
    'SUPPORT_AGENT'
);

CREATE TYPE identity.account_status AS ENUM (
    'PENDING_VERIFICATION',
    'ACTIVE',
    'SUSPENDED',
    'LOCKED',
    'CLOSED'
);

CREATE TABLE IF NOT EXISTS identity.users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(50) UNIQUE,
    role identity.user_role NOT NULL DEFAULT 'CUSTOMER',
    status identity.account_status NOT NULL DEFAULT 'PENDING_VERIFICATION',
    mfa_enabled BOOLEAN DEFAULT FALSE,
    mfa_secret VARCHAR(100),
    failed_login_attempts INT DEFAULT 0,
    locked_until TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS identity.user_sessions (
    session_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES identity.users(id) ON DELETE CASCADE,
    jwt_token_hash VARCHAR(512) NOT NULL,
    refresh_token_hash VARCHAR(512) NOT NULL,
    ip_address VARCHAR(45),
    user_agent TEXT,
    device_fingerprint VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS identity.api_credentials (
    credential_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES identity.users(id) ON DELETE CASCADE,
    client_id VARCHAR(100) UNIQUE NOT NULL,
    client_secret_hash VARCHAR(255) NOT NULL,
    api_key VARCHAR(255) UNIQUE NOT NULL,
    environment VARCHAR(20) DEFAULT 'SANDBOX', -- 'SANDBOX' | 'PRODUCTION'
    rate_limit_per_min INT DEFAULT 1000,
    is_revoked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS identity.audit_login_attempts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) NOT NULL,
    ip_address VARCHAR(45),
    user_agent TEXT,
    success BOOLEAN NOT NULL,
    failure_reason VARCHAR(255),
    attempted_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON identity.users(email);
CREATE INDEX idx_sessions_user ON identity.user_sessions(user_id);
CREATE INDEX idx_api_credentials_client ON identity.api_credentials(client_id);
