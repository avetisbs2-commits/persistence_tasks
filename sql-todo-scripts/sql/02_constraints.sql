-- SQL TODO Task 02: Constraints
--
-- Goal:
-- Add constraints to the tables created in 01_create_tables.sql.

-- TODO:
-- Add primary key constraints.
ALTER TABLE students ADD PRIMARY KEY (id);
ALTER TABLE teachers ADD PRIMARY KEY (id);
ALTER TABLE courses ADD PRIMARY KEY (id);
ALTER TABLE enrollments ADD PRIMARY KEY (id);

-- TODO:
-- Add NOT NULL constraints for required columns.
ALTER TABLE students ALTER COLUMN first_name SET NOT NULL,
ALTER COLUMN last_name SET NOT NULL,
                     ALTER COLUMN email SET NOT NULL;

ALTER TABLE teachers ALTER COLUMN first_name SET NOT NULL,
ALTER COLUMN last_name SET NOT NULL,
                     ALTER COLUMN email SET NOT NULL;

ALTER TABLE courses ALTER COLUMN name SET NOT NULL,
ALTER COLUMN price SET NOT NULL;

ALTER TABLE enrollments ALTER COLUMN student_id SET NOT NULL,
ALTER COLUMN course_id SET NOT NULL;

-- TODO:
-- Add UNIQUE constraints.
-- Example ideas:
-- students.email should be unique.
-- teachers.email should be unique.
-- courses.name should be unique.

ALTER TABLE students ADD CONSTRAINT unique_student_email UNIQUE (email);
ALTER TABLE teachers ADD CONSTRAINT unique_teacher_email UNIQUE (email);
ALTER TABLE courses ADD CONSTRAINT unique_course_name UNIQUE (name);


-- TODO:
-- Add CHECK constraints.
-- Example ideas:
-- students.age must be greater than 0.
-- courses.price must be greater than or equal to 0.

ALTER TABLE students ADD CONSTRAINT check_student_age CHECK (age > 0);
ALTER TABLE courses ADD CONSTRAINT check_course_price CHECK (price >= 0);


-- TODO:
-- Add DEFAULT values.
-- Example ideas:
-- students.created_at should default to current timestamp.
-- courses.created_at should default to current timestamp.
-- enrollments.enrollment_date should default to current date.
-- enrollments.status should default to 'ACTIVE'.

ALTER TABLE students ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE courses ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE enrollments ALTER COLUMN enrollment_date SET DEFAULT CURRENT_DATE;
ALTER TABLE enrollments ALTER COLUMN status SET DEFAULT 'ACTIVE';