CREATE TABLE employee_contract_type(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name varchar(64)
);

INSERT INTO employee_contract_type (name) VALUES ('Umowa o Pracę');
INSERT INTO employee_contract_type (name) VALUES ('Umowa o Zlecenie');
INSERT INTO employee_contract_type (name) VALUES ('Umowa o Dzieło');

CREATE TABLE bn_calc_period (
                                id BIGSERIAL NOT NULL PRIMARY KEY,
                                name varchar(64)
);

INSERT INTO bn_calc_period (name) VALUES ('Рік');
INSERT INTO bn_calc_period (name) VALUES ('Місяць');
INSERT INTO bn_calc_period (name) VALUES ('День');
INSERT INTO bn_calc_period (name) VALUES ('Година');

-- When you create your ID as BIGSERIAL - PSQL generate second table that 