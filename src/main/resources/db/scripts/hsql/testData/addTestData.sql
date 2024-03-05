INSERT INTO users (name, email, password, calories_per_day, enabled, registered) VALUES
('User', 'user@yandex.ru', 'password', 2000, true, '2010-01-01 10:00:00'),
('Admin', 'admin@gmail.com', 'admin', 2000, true, '2009-02-02 12:00:00');

INSERT INTO user_roles (role, user_id) VALUES
('ROLE_USER', 1),
('ROLE_ADMIN', 2);

INSERT INTO meals (description, calories, user_id, date_time) VALUES
('MEAL TEST 1', 1000, 1, '2020-10-10 9:00:00'),
('MEAL TEST 2', 500, 2, '2020-12-12 12:00:00');