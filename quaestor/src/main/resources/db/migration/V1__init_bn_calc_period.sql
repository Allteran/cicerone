CREATE TABLE bn_calc_period (
                                id BIGSERIAL NOT NULL PRIMARY KEY,
                                name varchar(64)
);

INSERT INTO bn_calc_period (name) VALUES ('Рік');
INSERT INTO bn_calc_period (name) VALUES ('Місяць');
INSERT INTO bn_calc_period (name) VALUES ('День');
INSERT INTO bn_calc_period (name) VALUES ('Година');