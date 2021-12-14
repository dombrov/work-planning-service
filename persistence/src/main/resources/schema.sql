drop table if exists shifts;
drop table if exists workers;

CREATE TABLE workers (
                         id varchar(255) NOT NULL PRIMARY KEY,
                         first_name varchar(255),
                         last_name varchar(255),
                         active boolean
                     );

CREATE TABLE shifts (
                        id bigint NOT NULL,
                        worker_id varchar(255) NOT NULL,
                        start_time timestamp,
                        FOREIGN KEY (worker_id) REFERENCES workers(id)
);

drop index if exists idx_shifts_start_time;
CREATE INDEX idx_shifts_start_time ON shifts (start_time);