CREATE SCHEMA IF NOT EXISTS homework_db;

SET search_path TO homework_db;

-- SQL TODO Task 01: Create Tables
--
-- Goal:
-- Create the base tables for a small school database.
--
-- Tables to create:
-- 1. students
-- 2. teachers
-- 3. courses
-- 4. enrollments
--
-- TODO:
-- Create table: students
-- Suggested columns:
-- id
-- first_name
-- last_name
-- email
-- age
-- created_at
CREATE TABLE students (
                          id INT,
                          first_name VARCHAR(50),
                          last_name VARCHAR(50),
                          email VARCHAR(100),
                          age INT,
                          created_at TIMESTAMP
);


-- TODO:
-- Create table: teachers
-- Suggested columns:
-- id
-- first_name
-- last_name
-- email
-- hire_date
CREATE TABLE teachers (
                          id INT,
                          first_name VARCHAR(50),
                          last_name VARCHAR(50),
                          email VARCHAR(100),
                          hire_date DATE
);


-- TODO:
-- Create table: courses
-- Suggested columns:
-- id
-- name
-- description
-- price
-- teacher_id
-- created_at
CREATE TABLE courses (
                         id INT,
                         name VARCHAR(100),
                         description TEXT,
                         price DECIMAL(10, 2),
                         teacher_id INT,
                         created_at TIMESTAMP
);


-- TODO:
-- Create table: enrollments
-- Suggested columns:
-- id
-- student_id
-- course_id
-- enrollment_date
-- status    :::: All without constraints
CREATE TABLE enrollments (
                             id INT,
                             student_id INT,
                             course_id INT,
                             enrollment_date DATE,
                             status VARCHAR(50)
);