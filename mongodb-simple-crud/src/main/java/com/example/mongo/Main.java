package com.example.mongo;

import com.example.mongo.entity.Address;
import com.example.mongo.entity.Student;
import com.example.mongo.repository.StudentRepository;
import com.example.mongo.util.MongoUtil;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        if (!MongoUtil.isRunning()) {
            System.out.println("Cannot connect to MongoDB at " + MongoUtil.getUri());
            System.out.println("Is the MongoDB server installed and started? See README.md, part \"Install MongoDB\".");
            MongoUtil.close();
            return;
        }
        System.out.println("Connected to MongoDB at " + MongoUtil.getUri()
                + ", database \"" + MongoUtil.getDatabaseName() + "\"");

        try {
            run(new StudentRepository());
        } finally {
            MongoUtil.close();
        }
    }

    private static void run(StudentRepository repository) {
        Student student = new Student("John", "Smith", "john@test.com", 21,
                new Address("Paris", "France"), List.of("java"));

        System.out.println();
        System.out.println("Create student:");
        Student saved = repository.create(student);
        System.out.println(saved);

        System.out.println();
        System.out.println("Find all students:");
        List<Student> students = repository.findAll();
        for (int i = 0; i < students.size(); i++) {
            System.out.println(students.get(i));
        }

        System.out.println();
        System.out.println("Find student by id:");
        System.out.println(repository.findById(saved.getId()));

        System.out.println();
        System.out.println("Update student (age 22):");
        saved.setAge(22);
        System.out.println(repository.update(saved));

        System.out.println();
        System.out.println("Part 2 - queries:");
        System.out.println("Older than 21: " + repository.findByAgeGreaterThan(21).size() + " student(s)");
        System.out.println("Living in Paris: " + repository.findByCity("Paris").size() + " student(s)");
        System.out.println("Add tag \"mongodb\": " + repository.addTag(saved.getId(), "mongodb"));
        System.out.println("Add the same tag again: " + repository.addTag(saved.getId(), "mongodb") + "   (must be false)");
        System.out.println(repository.findById(saved.getId()));

        System.out.println();
        System.out.println("Delete student:");
        boolean deleted = repository.deleteById(saved.getId());
        System.out.println("Deleted: " + deleted);
    }
}
