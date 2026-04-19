CREATE TABLE IF NOT EXISTS jobs (
    id           VARCHAR(36) PRIMARY KEY,
    type         VARCHAR(255) NOT NULL,
    status       VARCHAR(20)  NOT NULL,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at   TIMESTAMP WITH TIME ZONE NOT NULL,
    payload      TEXT,
    result       TEXT,
    error_message TEXT
);

CREATE INDEX IF NOT EXISTS idx_jobs_status ON jobs (status);
