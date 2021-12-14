drop table if exists workers;
CREATE TABLE workers (
                         id varchar(255) NOT NULL PRIMARY KEY,
                         first_name varchar(255),
                         last_name varchar(255),
                         active boolean
                     );

drop table if exists shifts;
CREATE TABLE shifts (
                         id bigint NOT NULL,
                         worker_id varchar(255) NOT NULL,
                         start_time timestamp
);