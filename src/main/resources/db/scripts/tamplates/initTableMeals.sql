BEGIN;
DROP TABLE IF EXISTS meals;
DROP SEQUENCE IF EXISTS global_meals_seq;
CREATE SEQUENCE global_meals_seq START WITH 1;

CREATE TABLE meals
(
  id               INTEGER PRIMARY KEY DEFAULT nextval('global_meals_seq'),
  date_time        TIMESTAMP DEFAULT now() NOT NULL,
  description      VARCHAR                 NOT NULL,
  calories         SMALLINT                NOT NULL,
  user_id          INTEGER                 NOT NULL
);

CREATE INDEX meals_idx ON meals (user_id, date_time)

COMMIT;