BEGIN;

DROP TABLE IF EXISTS user_roles;

CREATE TABLE user_roles
(
  user_id INTEGER NOT NULL,
  role    VARCHAR(20),
  CONSTRAINT user_roles_idx UNIQUE (user_id, role),
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

COMMIT;