package com.example.mongo.mapper;

import com.example.mongo.entity.Address;
import com.example.mongo.entity.Student;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Turns a Student (a Java object) into a Document (what MongoDB stores) and back.
 * A Document is like a Map: names and values. The driver does the rest (it writes it as BSON, a binary JSON).
 *
 * PROVIDED CODE - students do not need to change it.
 */
public final class StudentMapper {

    private StudentMapper() {
    }

    /**
     * The document of a student. If the student has an id, the document gets that "_id".
     * If the id is null, there is NO "_id": MongoDB makes one when you insert the document.
     */
    public static Document toDocument(Student student) {
        Document document = new Document();
        if (student.getId() != null) {
            document.put("_id", new ObjectId(student.getId()));
        }
        document.put("firstName", student.getFirstName());
        document.put("lastName", student.getLastName());
        document.put("email", student.getEmail());
        document.put("age", student.getAge());
        if (student.getAddress() != null) {
            document.put("address", new Document("city", student.getAddress().getCity())
                    .append("country", student.getAddress().getCountry()));
        }
        document.put("tags", new ArrayList<>(student.getTags()));
        return document;
    }

    /**
     * The student of a document. A document may MISS fields (MongoDB has no fixed columns):
     * a missing age is 0, a missing address is null, missing tags are an empty list.
     */
    public static Student fromDocument(Document document) {
        Student student = new Student();
        ObjectId id = document.getObjectId("_id");
        student.setId(id == null ? null : id.toHexString());
        student.setFirstName(document.getString("firstName"));
        student.setLastName(document.getString("lastName"));
        student.setEmail(document.getString("email"));
        student.setAge(document.getInteger("age", 0));

        Document address = document.get("address", Document.class);
        if (address != null) {
            student.setAddress(new Address(address.getString("city"), address.getString("country")));
        }

        List<String> tags = document.getList("tags", String.class);
        student.setTags(tags == null ? new ArrayList<>() : new ArrayList<>(tags));
        return student;
    }
}
