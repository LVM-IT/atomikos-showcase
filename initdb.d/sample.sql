-- creates a sample table in the default database db2inst1

CONNECT TO db2inst1;

--#SET TERMINATOR @
BEGIN

IF (NOT EXISTS (SELECT 1 
    FROM SYSCAT.TABLES
    WHERE TRIM(TABSCHEMA)||'.'||TRIM(TABNAME) = 'TEST.DATEN'))
THEN
    EXECUTE IMMEDIATE 'CREATE TABLE TEST.daten
    (
      id CHAR(36) NOT NULL,

      name VARCHAR(100),
      count INTEGER,
      PRIMARY KEY(id)
    )';
END IF;

END@ 
