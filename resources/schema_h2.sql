CREATE TABLE accounts
(
    id       BIGSERIAL,
    currency VARCHAR(10) NOT NULL,
    amount   NUMERIC(24, 10) NOT NULL DEFAULT 0,
    version  BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE activities
(
    id          BIGSERIAL,
    type        VARCHAR(20) NOT NULL,
    currency    VARCHAR(10) NOT NULL,
    amount      NUMERIC(24, 10) NOT NULL,
    account     BIGINT,
    counterpart BIGINT DEFAULT NULL,

    CONSTRAINT "activities_fk1" FOREIGN KEY (account) REFERENCES accounts (id),
    CONSTRAINT "activities_fk2" FOREIGN KEY (counterpart) REFERENCES accounts (id)
);