CREATE SEQUENCE tax_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE tax(
    id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('tax_seq'),
    payer VARCHAR(32),
    percentage FLOAT,
    name VARCHAR(64)
);

INSERT INTO tax(payer, percentage, name) VALUES ('EMPLOYEE', 0.0976, 'Składka Emerytalna (пенсійні відрахування)');
INSERT INTO tax(payer, percentage, name) VALUES ('EMPLOYEE', 0.015, 'Składka Rentowa (пенсійний внесок)');
INSERT INTO tax(payer, percentage, name) VALUES ('EMPLOYEE', 0.0245, 'Składka na Ubezpieczenie Chorobowe (Медичне страхування)');
INSERT INTO tax(payer, percentage, name) VALUES ('EMPLOYEE', 0.09, 'Składka na Ubezpieczenie Zdrowotne (Медичне страхування)');
INSERT INTO tax(payer, percentage, name) VALUES ('EMPLOYEE', 0.12, 'Podatek Dochodowy (Податок на прибуток)');
