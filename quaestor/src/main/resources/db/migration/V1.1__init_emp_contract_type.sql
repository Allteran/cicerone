CREATE SEQUENCE bn_calc_period_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE employee_contract_type_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE employee_contract_type(
    id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('employee_contract_type_seq'),
    name varchar(64)
);

INSERT INTO employee_contract_type (name) VALUES ('Umowa o Pracę');
INSERT INTO employee_contract_type (name) VALUES ('Umowa o Zlecenie');
INSERT INTO employee_contract_type (name) VALUES ('Umowa o Dzieło');

CREATE TABLE bn_calc_period (
                                id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('bn_calc_period_seq'),
                                name varchar(64)
);

INSERT INTO bn_calc_period (name) VALUES ('Рік');
INSERT INTO bn_calc_period (name) VALUES ('Місяць');
INSERT INTO bn_calc_period (name) VALUES ('День');
INSERT INTO bn_calc_period (name) VALUES ('Година');