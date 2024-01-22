
DROP TABLE IF EXISTS users_meals;
DROP SEQUENCE IF EXISTS global_meals_seq;
CREATE SEQUENCE global_meals_seq START WITH 1;

CREATE TABLE users_meals
(
  id               INTEGER PRIMARY KEY DEFAULT nextval('global_meals_seq'),
  date_time        TIMESTAMP DEFAULT now() NOT NULL,
  description      VARCHAR                 NOT NULL,
  calories         SMALLINT                NOT NULL,
  user_id          INTEGER                 NOT NULL
);