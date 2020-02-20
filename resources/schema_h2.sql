CREATE TABLE accounts
(
    id       BIGSERIAL,
    currency VARCHAR(3) NOT NULL,
    amount   NUMERIC(24, 10) NOT NULL DEFAULT 0,
    version  BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE activities
(
    id       SERIAL,
    type     VARCHAR(10) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    amount   NUMERIC(24, 10) NOT NULL,
    account  BIGINT,
    target   BIGINT DEFAULT NULL,

    CONSTRAINT "activities_fk1" FOREIGN KEY (account) REFERENCES accounts (id),
    CONSTRAINT "activities_fk2" FOREIGN KEY (target)  REFERENCES accounts (id)
);