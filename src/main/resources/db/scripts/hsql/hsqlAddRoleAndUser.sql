DELETE FROM user_roles;
DELETE FROM users;

INSERT INTO users (name, email, password, calories_per_day, registered, enabled) VALUES
('user', 'user@yandex.ru', 'password', 1000, current_date , true),
('admin', 'admin@gmail.com', 'admin', 2000, current_date , true);

INSERT INTO user_roles (role, user_id) VALUES
('ROLE_USER', 1),
('ROLE_ADMIN', 2);