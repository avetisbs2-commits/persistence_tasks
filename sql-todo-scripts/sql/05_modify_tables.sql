-- SQL TODO Task 05: Modify Tables
--
-- Goal:
-- Practice ALTER TABLE commands.
--
SET search_path TO homework_db;

-- TODO:
-- Add a phone column to students.
ALTER TABLE students
    ADD COLUMN phone VARCHAR(20);

-- TODO:
-- Add a level column to courses.
-- Example values can be BEGINNER, INTERMEDIATE, ADVANCED.
ALTER TABLE courses
    ADD COLUMN level VARCHAR(30);


-- TODO:
-- Rename one column.
ALTER TABLE students
    RENAME COLUMN phone TO phone_number;


-- TODO:
-- Change one column data type.
ALTER TABLE courses
    ALTER COLUMN price TYPE NUMERIC(12, 2);

-- TODO:
-- Add a new CHECK constraint after table creation.
ALTER TABLE courses
    ADD CONSTRAINT check_max_price CHECK (price <= 10000.00);


-- TODO:
-- Drop one constraint.
ALTER TABLE courses
    DROP CONSTRAINT check_max_price;


-- TODO:
-- Add the dropped constraint again.
ALTER TABLE courses
    ADD CONSTRAINT check_max_price CHECK (price <= 10000.00);

