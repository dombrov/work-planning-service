drop table if exists workers;

CREATE TABLE workers (
                         id varchar(255) NOT NULL PRIMARY KEY,
                         first_name varchar(255),
                         last_name varchar(255),
                         active boolean
                     );