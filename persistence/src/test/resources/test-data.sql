insert into workers (id, first_name, last_name, active) values ('test.user', 'Test', 'User', true);

insert into shifts (id, worker_id, start_time) values (1639324800, 'test.user', CURRENT_TIMESTAMP());