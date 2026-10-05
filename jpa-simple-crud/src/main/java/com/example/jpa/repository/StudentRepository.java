package com.example.jpa.repository;

import com.example.jpa.entity.Student;
import com.example.jpa.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;

public class StudentRepository {

    public Student create(Student student) {
        // TODO:
        // 1. Create EntityManager.
        // 2. Begin transaction.
        // 3. Save student using persist().
        // 4. Commit transaction.
        // 5. Close EntityManager.
        // 6. Return saved student.
        EntityManager em = JpaUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(student);
            em.getTransaction().commit();
            return student;
        } catch (Exception e) {
            rollbackIfActive(em);
            throw e;
        } finally {
            em.close();
        }
    }

    public Student findById(Long id) {
        // TODO:
        // 1. Create EntityManager.
        // 2. Find Student by id.
        // 3. Close EntityManager.
        // 4. Return found Student.
        // Return null if student does not exist.
            EntityManager em = JpaUtil.createEntityManager();
            try {
                return em.find(Student.class, id);
            } finally {
                em.close();
            }

    }

    public List<Student> findAll() {
        // TODO:
        // 1. Create EntityManager.
        // 2. Write JPQL query.
        // 3. Return all students.
        // 4. Close EntityManager.
        EntityManager em = JpaUtil.createEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery("SELECT s FROM Student s", Student.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Student update(Student student) {
        // TODO:
        // 1. Create EntityManager.
        // 2. Begin transaction.
        // 3. Update student using merge().
        // 4. Commit transaction.
        // 5. Close EntityManager.
        // 6. Return updated student.
        EntityManager em = JpaUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            Student updatedStudent = em.merge(student);
            em.getTransaction().commit();
            return updatedStudent;
        } catch (Exception e) {
            rollbackIfActive(em);
            throw e;
        } finally {
            em.close();
        }
    }

    public boolean deleteById(Long id) {
        // TODO:
        // 1. Create EntityManager.
        // 2. Begin transaction.
        // 3. Find Student by id.
        // 4. Remove student if found.
        // 5. Commit transaction.
        // 6. Close EntityManager.
        // 7. Return true if removed.
        EntityManager em = JpaUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            Student student = em.find(Student.class, id);
            if (student != null) {
                em.remove(student);
                em.getTransaction().commit();
                return true;
            }
            em.getTransaction().commit();
            return false;
        } catch (Exception e) {
            rollbackIfActive(em);
            throw e;
        } finally {
            em.close();
        }
    }

    private void rollbackIfActive(EntityManager entityManager) {
        // TODO:
        // Check whether transaction is active.
        // Roll back the transaction if it is active.
        if (entityManager != null && entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }
}

