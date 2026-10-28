-- SQL TODO Task 03: Relationships
--
-- Goal:
-- Add relationships between the school tables.
--
-- TODO:
-- Connect courses.teacher_id to teachers.id.


ALTER TABLE courses
    ADD CONSTRAINT fk_courses_teacher
        FOREIGN KEY (teacher_id) REFERENCES teachers(id)
            ON DELETE SET NULL;

-- TODO:
-- Connect enrollments.student_id to students.id.

ALTER TABLE enrollments
    ADD CONSTRAINT fk_enrollments_student
        FOREIGN KEY (student_id) REFERENCES students(id)
            ON DELETE CASCADE;


-- TODO:
-- Connect enrollments.course_id to courses.id.

ALTER TABLE enrollments
    ADD CONSTRAINT fk_enrollments_course
        FOREIGN KEY (course_id) REFERENCES courses(id)
            ON DELETE CASCADE;

-- TODO:
-- Prevent duplicate enrollment.
-- A student should not be enrolled in the same course twice.

ALTER TABLE enrollments
    ADD CONSTRAINT unique_student_course UNIQUE (student_id, course_id);

-- TODO:
-- Decide delete behavior.
-- Example:
-- What should happen to courses when a teacher is deleted?
-- What should happen to enrollments when a student is deleted?
-- What should happen to enrollments when a course is deleted?
