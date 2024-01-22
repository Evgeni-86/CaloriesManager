
DELETE FROM users_meals;
ALTER SEQUENCE global_meals_seq RESTART WITH 1;

INSERT INTO users_meals (description, calories, user_id) VALUES
('MEAL TEST 1', 1000, 100000),
('MEAL TEST 2', 500, 100001);