-- Enterprise Schema & Indexes for Double-Entry Financial Ledger (ledger)
-- Target RDBMS: PostgreSQL 15+ / CockroachDB

CREATE SCHEMA IF NOT EXISTS ledger;

CREATE TABLE IF NOT EXISTS ledger.ledger_enterprise_records (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    reference_code VARCHAR(100) UNIQUE NOT NULL,
    owner_id UUID NOT NULL,
    amount NUMERIC(18, 4) NOT NULL DEFAULT 0.0000,
    currency_code VARCHAR(3) NOT NULL DEFAULT 'USD',
    status_code VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    risk_score NUMERIC(5, 2) DEFAULT 0.00,
    idempotency_hash VARCHAR(255) UNIQUE,
    metadata_json JSONB,
    audit_trail JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    version_lock INT DEFAULT 1 NOT NULL
);

-- Performance Optimization Indexes
CREATE INDEX IF NOT EXISTS idx_ledger_ref_code ON ledger.ledger_enterprise_records(reference_code);
CREATE INDEX IF NOT EXISTS idx_ledger_owner ON ledger.ledger_enterprise_records(owner_id);
CREATE INDEX IF NOT EXISTS idx_ledger_status ON ledger.ledger_enterprise_records(status_code);
CREATE INDEX IF NOT EXISTS idx_ledger_created ON ledger.ledger_enterprise_records(created_at DESC);
