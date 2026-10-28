package com.example.mongo.repository;

import com.example.mongo.entity.Student;
import com.example.mongo.mapper.StudentMapper;
import com.example.mongo.util.MongoUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Long.toHexString;

/**
 * Saves and loads students in the MongoDB collection "students".
 *
 * A MongoDB COLLECTION is like a table, and a DOCUMENT is like a row. But a collection has no fixed columns.
 *
 * STUDENT TASK - complete the TODO methods (8 of them). Part 1 is the CRUD. Part 2 uses what only a
 * NoSQL database does easily: a filter on a field inside an embedded document, and on a list inside a document.
 *
 * The tools. Imports that you will need (add them when your IDE asks):
 *
 *   com.mongodb.client.model.Filters   Filters.eq("age", 21)   Filters.gt("age", 21)      the "where" of a query
 *   com.mongodb.client.model.Updates   Updates.addToSet("tags", "java")                    the "set ..." of an update
 *   com.mongodb.client.model.Sorts     Sorts.ascending("_id")                              the "order by"
 *
 *   collection.insertOne(document)              INSERT. It adds the field "_id" to the document when it has none.
 *   collection.find(filter).first()             the first document that matches, or null
 *   collection.find().sort(...).into(list)      all the documents, copied into a list
 *   collection.replaceOne(filter, document)     UPDATE: replaces the whole document. Returns an UpdateResult (getMatchedCount())
 *   collection.updateOne(filter, update)        UPDATE: changes only some fields (getModifiedCount())
 *   collection.deleteOne(filter)                DELETE. Returns a DeleteResult (getDeletedCount())
 *   StudentMapper.toDocument(student) / StudentMapper.fromDocument(document)    Student <-> Document (already written)
 *
 * The id of a document is an ObjectId. In a Student it is kept as TEXT (24 hexadecimal characters):
 *   new ObjectId(text)            text -> ObjectId             (it throws if the text is not a valid id)
 *   ObjectId.isValid(text)        is this text a valid id?
 *   objectId.toHexString()        ObjectId -> text
 */
public class StudentRepository {

    private final MongoCollection<Document> collection;

    /** The normal constructor: the collection "students" of the database from mongo.properties. */
    public StudentRepository() {
        this(MongoUtil.getDatabase().getCollection("students"));
    }

    /** The tests use this one, with a collection of a test database. */
    public StudentRepository(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    // =====================================================================================
    // PART 1 - CRUD
    // =====================================================================================

    /**
     * INSERT. Saves the student as a new document and returns the same student, now WITH its id.
     */
    public Student create(Student student) {
        // TODO:
        // 1. Make the document:
        Document document = StudentMapper.toDocument(student);
        // 2. Insert it:
        collection.insertOne(document);
        // 3. insertOne put the new "_id" into the document. Read it:
        ObjectId id = document.getObjectId("_id");
        // 4. Save it in the student as TEXT:
        student.setId(id.toHexString());
        // 5. Return the student.

        return student;
    }

    /**
     * SELECT one. Returns the student with this id, or null if there is none.
     * Returns null also when the text is not a valid id at all (check ObjectId.isValid(id) first).
     */
    public Student findById(String id) {
        // TODO:
        // 1. If id is null or not ObjectId.isValid(id), return null.
        if(id == null || !ObjectId.isValid(id)){
            return null;
        }
        // 2. Find the document:   collection.find(Filters.eq("_id", new ObjectId(id))).first()
        Document document = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        // 3. If it is null, return null. Otherwise return StudentMapper.fromDocument(document).
        if (document == null){
            return null;
        }

        return StudentMapper.fromDocument(document);
    }

    /**
     * SELECT all. All the students, in the order they were created (sort by "_id": the ids grow with time).
     * An empty collection gives an empty list (not null).
     */
    public List<Student> findAll() {
        // TODO:
        // 1. Get the documents:   collection.find().sort(Sorts.ascending("_id")).into(new ArrayList<>())
        ArrayList<Document> documents = collection.find().sort(Sorts.ascending("_id")).into(new ArrayList<>());
        // 2. Turn each Document into a Student (StudentMapper.fromDocument) in a new list, and return it.
        ArrayList<Student> students = new ArrayList<>();
        for (Document document : documents){
            students.add( StudentMapper.fromDocument(document));
        }

        return students;
    }

    /**
     * UPDATE. Replaces the stored student that has the same id. Returns the student, or null if there is no
     * such student (also when the id is null or not valid). It must NOT create a new document.
     */
    public Student update(Student student) {
        // TODO:
        // 1. If the id is null or not valid, return null.
        if (student.getId() == null || !ObjectId.isValid(student.getId())){
            return null;
        }
        // 2. Replace:  collection.replaceOne(Filters.eq("_id", new ObjectId(student.getId())), StudentMapper.toDocument(student))
        UpdateResult result = collection.replaceOne(
                Filters.eq("_id", new ObjectId(student.getId())),
                StudentMapper.toDocument(student)
        );

        // 3. The result tells how many documents matched:   result.getMatchedCount()
        //    0 -> return null.   Otherwise return the student.


        if (result.getMatchedCount() == 0) {
            return null;
        }

        return student;
    }

    /**
     * DELETE. Removes the student with this id. Returns true if one was removed, false if there was none
     * (also when the id is not valid).
     */
    public boolean deleteById(String id) {
        // TODO:
        // 1. If id is null or not valid, return false
        if (id == null || !ObjectId.isValid(id)){
            return false;
        }
        // 2. collection.deleteOne(Filters.eq("_id", new ObjectId(id)))   -> result.getDeletedCount()
            DeleteResult result = collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
        // 3. Return true when the count is more than 0.
        if (result.getDeletedCount() > 0 ){
            return true;
        }
        return false;
    }

    // =====================================================================================
    // PART 2 - QUERIES THAT NOSQL MAKES EASY
    // =====================================================================================

    /**
     * The students who are STRICTLY older than this age (age 21 is not "older than 21"), in the order of creation.
     * Tip: Filters.gt("age", age)   (it is "age > 21" in SQL, "$gt" in MongoDB)
     */
    public List<Student> findByAgeGreaterThan(int age) {
        // TODO

        ArrayList<Document> documents = collection.
                find(Filters.gt("age", age)).
                sort(Sorts.ascending("_id")).
                into(new ArrayList<>());
        ArrayList<Student> students = new ArrayList<>();
        for (Document document : documents){
            students.add( StudentMapper.fromDocument(document));
        }

        return students;
}
    /**
     * The students who live in this city, in the order of creation. The city is INSIDE the embedded document
     * "address". Reach it with a DOT:   Filters.eq("address.city", city)
     * A student without an address never matches. (In SQL this would be a JOIN with another table.)
     */
    public List<Student> findByCity(String city) {
        // TODO
        ArrayList<Document> documents = collection
                .find(Filters.eq("address.city", city))
                .sort(Sorts.ascending("_id"))
                .into(new ArrayList<>());

        ArrayList<Student> students = new ArrayList<>();

        for (Document document : documents) {
            students.add(StudentMapper.fromDocument(document));
        }

        return students;
    }

    /**
     * Adds a tag to the list "tags" of a student, but only if the tag is not there yet (a set, not a list with duplicates).
     * Returns true if the tag was added. Returns false if the student has it already, or if there is no such student
     * (also when the id is not valid).
     * Tip: collection.updateOne(Filters.eq("_id", ...), Updates.addToSet("tags", tag))  then  result.getModifiedCount()
     *      (the tag is the only thing that changes: the rest of the document is not touched)
     */
    public boolean addTag(String id, String tag) {
        // TODO
        if (id == null || !ObjectId.isValid(id)) {
            return false;
        }
        UpdateResult result = collection.updateOne(
                Filters.eq("_id", new ObjectId(id)),
                Updates.addToSet("tags", tag)
        );
        if (result.getModifiedCount() == 1 ){
            return true;
        }
        return false;
    }
}
