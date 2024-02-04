DELETE FROM meals;
DELETE FROM users;
DELETE FROM user_roles;

ALTER TABLE users ALTER COLUMN id RESTART WITH 1;
ALTER TABLE meals ALTER COLUMN id RESTART WITH 1;

INSERT INTO users (name, email, password, calories_per_day, registered, enabled) VALUES
('user1', 'user1@yandex.ru', 'password', 1000, current_date , true),
('user2', 'user2@yandex.ru', 'password', 1000, current_date , true),
('user3', 'user3@yandex.ru', 'password', 1000, current_date , true),
('user4', 'user4@yandex.ru', 'password', 1000, current_date , true),
('user5', 'user5@yandex.ru', 'password', 1000, current_date , true),
('user6', 'user6@yandex.ru', 'password', 1000, current_date , true),
('user7', 'user7@yandex.ru', 'password', 1000, current_date , true),
('admin', 'admin@gmail.com', 'admin', 2000, current_date , true);

INSERT INTO user_roles (role, user_id) VALUES
('ROLE_USER', 1),
('ROLE_USER', 2),
('ROLE_USER', 3),
('ROLE_USER', 4),
('ROLE_USER', 5),
('ROLE_USER', 6),
('ROLE_USER', 7),
('ROLE_ADMIN', 8);