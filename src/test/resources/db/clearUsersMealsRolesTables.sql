DELETE FROM meals;
DELETE FROM users;
DELETE FROM user_roles;
ALTER TABLE users ALTER COLUMN id RESTART WITH 1;
ALTER TABLE meals ALTER COLUMN id RESTART WITH 1;