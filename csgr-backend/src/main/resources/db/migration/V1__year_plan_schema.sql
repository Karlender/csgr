CREATE SCHEMA csgr;

CREATE TABLE csgr.year_plan(
    uuid varchar(255) PRIMARY KEY,
    year int NOT NULL,
    rotation varchar(255) NOT NULL,
    employees text NOT NULL
);

CREATE TABLE csgr.appointment(
    uuid varchar(255) PRIMARY KEY,
    year_plan_uuid varchar(255) NOT NULL,
    from_date date NOT NULL
);

ALTER TABLE csgr.appointment ADD CONSTRAINT fk_appointment_year_plan FOREIGN KEY (year_plan_uuid) REFERENCES csgr.year_plan(uuid);
CREATE INDEX idx_appointment_year_plan_uuid ON csgr.appointment(year_plan_uuid);

CREATE TABLE csgr.appointment_group(
    uuid varchar(255) PRIMARY KEY,
    appointment_uuid varchar(255) NOT NULL,
    employees text NOT NULL
);

ALTER TABLE csgr.appointment_group ADD CONSTRAINT fk_appointment_group_appointment FOREIGN KEY (appointment_uuid) REFERENCES csgr.appointment(uuid);
CREATE INDEX idx_appointment_group_appointment_uuid ON csgr.appointment_group(appointment_uuid);
