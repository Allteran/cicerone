CREATE TABLE employee_contract_type(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name varchar(64)
);

INSERT INTO employee_contract_type (name) VALUES ('Umowa o Pracę');
INSERT INTO employee_contract_type (name) VALUES ('Umowa o Zlecenie');
INSERT INTO employee_contract_type (name) VALUES ('Umowa o Dzieło');