package com.example.mongo;

import com.example.mongo.entity.Address;
import com.example.mongo.entity.Student;
import com.example.mongo.mapper.StudentMapper;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A look at how a Student becomes a MongoDB document, and back. These tests need NO server.
 * They show what "flexible schema" means: a document may miss fields, and may have fields that nobody knows.
 *
 * PROVIDED CODE - do not change.
 */
class StudentMapperTest {

    @Test
    void aStudentWithoutId_becomesADocumentWithoutId() {
        Student student = new Student("John", "Smith", "john@test.com", 21);

        Document document = StudentMapper.toDocument(student);

        assertFalse(document.containsKey("_id"), "MongoDB makes the _id when the document is inserted");
        assertEquals("John", document.getString("firstName"));
        assertEquals(21, document.getInteger("age"));
    }

    @Test
    void theAddressIsAnEmbeddedDocument_notATable() {
        Student student = new Student("John", "Smith", "john@test.com", 21, new Address("Paris", "France"), List.of());

        Document document = StudentMapper.toDocument(student);

        assertEquals("Paris", document.get("address", Document.class).getString("city"));
    }

    @Test
    void aStudentWithoutAddress_hasNoAddressField() {
        Document document = StudentMapper.toDocument(new Student("John", "Smith", "john@test.com", 21));

        assertFalse(document.containsKey("address"));
    }

    @Test
    void theIdIsATextInTheStudent_andAnObjectIdInTheDocument() {
        ObjectId objectId = new ObjectId();
        Document document = new Document("_id", objectId).append("firstName", "Anna");

        Student student = StudentMapper.fromDocument(document);

        assertEquals(objectId.toHexString(), student.getId());
        assertEquals(objectId, StudentMapper.toDocument(student).getObjectId("_id"));
    }

    @Test
    void aDocumentThatMissesFields_getsDefaults() {
        Student student = StudentMapper.fromDocument(new Document("firstName", "Anna"));

        assertEquals(0, student.getAge());
        assertNull(student.getAddress());
        assertTrue(student.getTags().isEmpty());
    }

    @Test
    void aDocumentWithUnknownFields_isStillReadable() {
        Document document = new Document("firstName", "Anna").append("nickname", "Annie").append("shoeSize", 38);

        assertEquals("Anna", StudentMapper.fromDocument(document).getFirstName());
    }

    @Test
    void tagsMakeARoundTrip() {
        Student student = new Student("John", "Smith", "john@test.com", 21, null, List.of("java", "mongodb"));

        Student back = StudentMapper.fromDocument(StudentMapper.toDocument(student));

        assertEquals(List.of("java", "mongodb"), back.getTags());
    }
}
