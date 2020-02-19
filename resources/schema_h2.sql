CREATE TABLE accounts
(
    id SERIAL,
    currency VARCHAR(3) DEFAULT NOT NULL,
    amount   NUMERIC(24, 10) NOT NULL DEFAULT 0,
    uuid TEXT NOT NULL

--     CONSTRAINT accounts_uuid UNIQUE (uuid)
);