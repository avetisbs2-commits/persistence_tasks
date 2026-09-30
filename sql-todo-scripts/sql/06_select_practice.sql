-- SQL TODO Task 06: Select Practice
--
-- Goal:
-- Practice reading data after the schema and inserts are complete.
--
SET search_path TO homework_db;

-- TODO:
-- Select all students.
    SELECT *
    FROM students;


-- TODO:
-- Select all courses with their teacher names.
SELECT
    c.id AS course_id,
    c.name AS course_name,
    t.first_name AS teacher_first_name,
    t.last_name AS teacher_last_name
FROM courses c
         LEFT JOIN teachers t ON c.teacher_id = t.id;


-- TODO:
-- Select all students enrolled in one specific course.
-- TODO:
-- Select all students enrolled in one specific course.
SELECT
    s.id AS student_id,
    s.first_name,
    s.last_name,
    c.name AS course_name
FROM students s
         JOIN enrollments e ON s.id = e.student_id
         JOIN courses c ON e.course_id = c.id
WHERE c.id = 'Quantum Physics';


-- TODO:
-- Count how many students are enrolled in each course.
SELECT
    c.name AS course_name,
    COUNT(e.student_id) AS enrolled_students_count
FROM courses c
         LEFT JOIN enrollments e ON c.id = e.course_id
GROUP BY c.id, c.name;


-- TODO:
-- Find students who are not enrolled in any course.
SELECT *
FROM students
WHERE id NOT IN (SELECT student_id FROM enrollments);


-- TODO:
-- Find courses that have no students.
SELECT *
FROM courses
WHERE id NOT IN (SELECT course_id FROM enrollments);


-- TODO:
-- Find the most expensive course.
SELECT *
FROM courses
ORDER BY price DESC
LIMIT 1;


-- TODO:
-- Find all active enrollments.
SELECT *
FROM enrollments
WHERE status = 'ACTIVE';