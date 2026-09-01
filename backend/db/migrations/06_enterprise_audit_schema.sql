-- Phase 6: Enterprise Operations, Audit & Compliance Schema Definition
-- PostgreSQL Schemas: notification, audit, admin

CREATE SCHEMA IF NOT EXISTS notification;
CREATE SCHEMA IF NOT EXISTS audit;
CREATE SCHEMA IF NOT EXISTS admin;

-- Centralized Notification Service Schema
CREATE TABLE IF NOT EXISTS notification.messages (
    notification_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    recipient_user_id UUID NOT NULL,
    channel VARCHAR(20) NOT NULL, -- 'EMAIL', 'SMS', 'PUSH', 'IN_APP'
    event_type VARCHAR(100) NOT NULL, -- 'PAYMENT_SUCCESS', 'FRAUD_ALERT', 'LOAN_DISBURSED'
    subject VARCHAR(255),
    content TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'SENT', -- 'QUEUED', 'SENT', 'FAILED'
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Immutable Audit & Compliance Logging
CREATE TABLE IF NOT EXISTS audit.immutable_logs (
    audit_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    actor_user_id UUID,
    actor_role VARCHAR(50) NOT NULL,
    action_performed VARCHAR(255) NOT NULL,
    target_resource VARCHAR(255) NOT NULL,
    ip_address VARCHAR(45),
    user_agent TEXT,
    payload_snapshot JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- System Configuration & Feature Flags
CREATE TABLE IF NOT EXISTS admin.feature_flags (
    flag_key VARCHAR(100) PRIMARY KEY,
    description TEXT,
    is_enabled BOOLEAN DEFAULT TRUE,
    target_environment VARCHAR(20) DEFAULT 'ALL',
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS admin.system_limits (
    limit_key VARCHAR(100) PRIMARY KEY,
    limit_value DECIMAL(18, 4) NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
