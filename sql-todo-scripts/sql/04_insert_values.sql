-- SQL TODO Task 04: Insert Values
--
-- Goal:
-- Insert sample data into all tables.
--
SET search_path TO homework_db;

-- TODO:
-- Insert at least 5 students.
INSERT INTO students (id, first_name, last_name, email, age) VALUES
            (1, 'Alice', 'Smith', 'alice.smith@example.com', 20),
            (2, 'Bob', 'Johnson', 'bob.johnson@example.com', 22),
            (3, 'Charlie', 'Brown', 'charlie.brown@example.com', 19),
            (4, 'Diana', 'Prince', 'diana.prince@example.com', 21),
            (5, 'Ethan', 'Hunt', 'ethan.hunt@example.com', 23);

-- TODO:
-- Insert at least 3 teachers.
INSERT INTO teachers (id, first_name, last_name, email, hire_date) VALUES
            (1, 'Robert', 'Oppenheimer', 'robert.oppenheimer@example.com', '2020-09-01'),
            (2, 'Marie', 'Curie', 'marie.curie@example.com', '2018-03-15'),
            (3, 'Alan', 'Turing', 'alan.turing@example.com', '2021-01-10');

-- TODO:
-- Insert at least 4 courses.
-- Each course should reference a teacher.

INSERT INTO courses (id, name, description, price, teacher_id) VALUES
            (1, 'Quantum Physics', 'Introduction to quantum mechanics', 150.00, 1),
            (2, 'Organic Chemistry', 'Study of carbon compounds', 120.50, 2),
            (3, 'Data Structures', 'Fundamental programming concepts', 200.00, 3),
            (4, 'Advanced Calculus', 'Differential and integral calculus', 175.00, 1);


-- TODO:
-- Insert at least 8 enrollments.
-- Each enrollment should connect one student with one course.
INSERT INTO enrollments (id, student_id, course_id, status) VALUES
            (1, 1, 1, 'ACTIVE'),
            (2, 1, 3, 'ACTIVE'),
            (3, 2, 2, 'ACTIVE'),
            (4, 3, 3, 'ACTIVE'),
            (5, 3, 4, 'ACTIVE'),
            (6, 4, 1, 'ACTIVE'),
            (7, 4, 2, 'ACTIVE'),
            (8, 5, 4, 'ACTIVE');


-- TODO:
-- Add one insert that should fail because of a constraint.
-- Keep it commented out.
-- Add a short comment explaining why it should fail.

-- INSERT INTO school_db.enrollments (id, student_id, course_id) VALUES (9, 1, 1);

-- This should fail because student_id 1 is already enrolled in course_id 1,
-- which violates our unique composite constraint (student_id, course_id).

