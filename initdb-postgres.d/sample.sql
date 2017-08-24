-- creates a sample table

CREATE SCHEMA IF NOT EXISTS TEST;

CREATE TABLE IF NOT EXISTS TEST.daten
(
  id CHAR(36) NOT NULL,

  name VARCHAR(100),
  count INTEGER,
  PRIMARY KEY(id)
);
