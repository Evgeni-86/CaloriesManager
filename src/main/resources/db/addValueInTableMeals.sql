
DELETE FROM meals;
ALTER SEQUENCE global_meals_seq RESTART WITH 1;

INSERT INTO meals (description, calories, user_id) VALUES
('MEAL TEST 1', 1000, 100000),
('MEAL TEST 2', 500, 100001);