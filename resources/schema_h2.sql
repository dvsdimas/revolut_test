CREATE TABLE accounts
(
    id       SERIAL,
    currency VARCHAR(3) NOT NULL,
    amount   NUMERIC(24, 10) NOT NULL DEFAULT 0,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE activities
(
    id      SERIAL,
    type    VARCHAR(10) NOT NULL,
    amount  NUMERIC(24, 10) NOT NULL,
    account BIGSERIAL,
    target  BIGSERIAL DEFAULT NULL
);