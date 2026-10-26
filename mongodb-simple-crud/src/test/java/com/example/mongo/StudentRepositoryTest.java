package com.example.mongo;

import com.example.mongo.entity.Address;
import com.example.mongo.entity.Student;
import com.example.mongo.repository.StudentRepository;
import com.example.mongo.util.MongoUtil;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Checks your StudentRepository against a REAL MongoDB. PROVIDED CODE - do not change.
 *
 * The tests use their OWN database (named nosql_crud_test_<random>) and delete it at the end,
 * so your own data in nosql_crud is never touched.
 * If MongoDB is not running, the tests are SKIPPED (not failed) and tell you why: start MongoDB first.
 */
class StudentRepositoryTest {

    private static MongoClient client;
    private static MongoDatabase database;
    private static boolean mongoIsRunning;

    private MongoCollection<Document> collection;
    private StudentRepository repository;

    @BeforeAll
    static void connect() {
        client = MongoUtil.createClient(MongoUtil.getUri());
        mongoIsRunning = MongoUtil.isRunning(client);
        if (mongoIsRunning) {
            database = client.getDatabase("nosql_crud_test_" + UUID.randomUUID().toString().replace("-", ""));
        }
    }

    @AfterAll
    static void disconnect() {
        if (mongoIsRunning) {
            database.drop();
        }
        client.close();
    }

    @BeforeEach
    void freshCollection() {
        assumeTrue(mongoIsRunning, "MongoDB is not running at " + MongoUtil.getUri() + ". Install and start it (see README.md).");
        collection = database.getCollection("students");
        collection.drop();
        repository = new StudentRepository(collection);
    }

    private static Student john() {
        return new Student("John", "Smith", "john@test.com", 21, new Address("Paris", "France"), List.of("java"));
    }

    // ------------------------------------------------------------------ create

    @Test
    void create_givesTheStudentAnId_withTheLookOfAnObjectId() {
        Student saved = repository.create(john());

        assertNotNull(saved.getId(), "create must set the id of the student");
        assertTrue(ObjectId.isValid(saved.getId()));
    }

    @Test
    void create_reallySavesTheDocument() {
        Student saved = repository.create(john());

        assertEquals(1, collection.countDocuments());
        Document stored = collection.find().first();
        assertEquals(saved.getId(), stored.getObjectId("_id").toHexString());
        assertEquals("John", stored.getString("firstName"));
        assertEquals("Paris", stored.get("address", Document.class).getString("city"));
    }

    @Test
    void create_twoStudents_twoDifferentIds() {
        Student first = repository.create(john());
        Student second = repository.create(new Student("Anna", "Brown", "anna@test.com", 31));

        assertNotEquals(first.getId(), second.getId());
        assertEquals(2, collection.countDocuments());
    }

    // ------------------------------------------------------------------ findById

    @Test
    void findById_returnsTheSavedStudent_withAllItsFields() {
        Student saved = repository.create(john());

        Student found = repository.findById(saved.getId());

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals("Smith", found.getLastName());
        assertEquals("john@test.com", found.getEmail());
        assertEquals(21, found.getAge());
        assertEquals("Paris", found.getAddress().getCity());
        assertEquals(List.of("java"), found.getTags());
    }

    @Test
    void findById_anIdThatDoesNotExist_isNull() {
        assertNull(repository.findById(new ObjectId().toHexString()));
    }

    @Test
    void findById_aTextThatIsNotAnId_isNull_andNotAnException() {
        assertNull(repository.findById("abc"));
        assertNull(repository.findById(null));
    }

    // ------------------------------------------------------------------ findAll

    @Test
    void findAll_emptyCollection_isAnEmptyList() {
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void findAll_returnsEveryStudent_inTheOrderOfCreation() {
        repository.create(new Student("A", "x", "a@test.com", 20));
        repository.create(new Student("B", "x", "b@test.com", 20));
        repository.create(new Student("C", "x", "c@test.com", 20));

        List<Student> all = repository.findAll();

        assertEquals(3, all.size());
        assertEquals("A", all.get(0).getFirstName());
        assertEquals("B", all.get(1).getFirstName());
        assertEquals("C", all.get(2).getFirstName());
    }

    // ------------------------------------------------------------------ update

    @Test
    void update_changesTheStoredStudent() {
        Student saved = repository.create(john());
        saved.setAge(22);
        saved.setEmail("new@test.com");

        Student updated = repository.update(saved);

        assertNotNull(updated);
        Student stored = repository.findById(saved.getId());
        assertEquals(22, stored.getAge());
        assertEquals("new@test.com", stored.getEmail());
    }

    @Test
    void update_doesNotCreateANewDocument() {
        Student saved = repository.create(john());
        saved.setAge(30);

        repository.update(saved);

        assertEquals(1, collection.countDocuments());
    }

    @Test
    void update_aStudentThatDoesNotExist_isNull_andNothingIsSaved() {
        Student ghost = john();
        ghost.setId(new ObjectId().toHexString());

        assertNull(repository.update(ghost));
        assertEquals(0, collection.countDocuments());
    }

    @Test
    void update_aStudentWithoutId_isNull() {
        assertNull(repository.update(john()));
        assertEquals(0, collection.countDocuments());
    }

    // ------------------------------------------------------------------ deleteById

    @Test
    void deleteById_removesTheStudent() {
        Student saved = repository.create(john());

        assertTrue(repository.deleteById(saved.getId()));

        assertEquals(0, collection.countDocuments());
        assertNull(repository.findById(saved.getId()));
    }

    @Test
    void deleteById_twice_secondTimeIsFalse() {
        Student saved = repository.create(john());
        repository.deleteById(saved.getId());

        assertFalse(repository.deleteById(saved.getId()));
    }

    @Test
    void deleteById_onlyDeletesThatStudent() {
        Student first = repository.create(john());
        Student second = repository.create(new Student("Anna", "Brown", "anna@test.com", 31));

        repository.deleteById(first.getId());

        assertEquals(1, collection.countDocuments());
        assertNotNull(repository.findById(second.getId()));
    }

    @Test
    void deleteById_aTextThatIsNotAnId_isFalse() {
        assertFalse(repository.deleteById("abc"));
        assertFalse(repository.deleteById(null));
    }

    // ------------------------------------------------------------------ findByAgeGreaterThan

    @Test
    void findByAgeGreaterThan_isStrict_theBoundaryAgeIsNotIncluded() {
        repository.create(new Student("A", "x", "a@test.com", 20));
        repository.create(new Student("B", "x", "b@test.com", 21));
        repository.create(new Student("C", "x", "c@test.com", 22));

        List<Student> older = repository.findByAgeGreaterThan(21);

        assertEquals(1, older.size());
        assertEquals("C", older.get(0).getFirstName());
    }

    @Test
    void findByAgeGreaterThan_nobody_isAnEmptyList() {
        repository.create(new Student("A", "x", "a@test.com", 20));

        assertTrue(repository.findByAgeGreaterThan(50).isEmpty());
    }

    @Test
    void findByAgeGreaterThan_keepsTheOrderOfCreation() {
        repository.create(new Student("A", "x", "a@test.com", 40));
        repository.create(new Student("B", "x", "b@test.com", 30));
        repository.create(new Student("C", "x", "c@test.com", 50));

        List<Student> older = repository.findByAgeGreaterThan(25);

        assertEquals(List.of("A", "B", "C"), List.of(older.get(0).getFirstName(), older.get(1).getFirstName(), older.get(2).getFirstName()));
    }

    // ------------------------------------------------------------------ findByCity (a field INSIDE an embedded document)

    @Test
    void findByCity_findsTheStudentsOfThatCity() {
        repository.create(new Student("A", "x", "a@test.com", 20, new Address("Paris", "France"), List.of()));
        repository.create(new Student("B", "x", "b@test.com", 20, new Address("Rome", "Italy"), List.of()));
        repository.create(new Student("C", "x", "c@test.com", 20, new Address("Paris", "France"), List.of()));

        List<Student> inParis = repository.findByCity("Paris");

        assertEquals(2, inParis.size());
        assertEquals("A", inParis.get(0).getFirstName());
        assertEquals("C", inParis.get(1).getFirstName());
    }

    @Test
    void findByCity_aStudentWithoutAddress_neverMatches() {
        repository.create(new Student("A", "x", "a@test.com", 20));

        assertTrue(repository.findByCity("Paris").isEmpty());
    }

    @Test
    void findByCity_unknownCity_isAnEmptyList() {
        repository.create(new Student("A", "x", "a@test.com", 20, new Address("Paris", "France"), List.of()));

        assertTrue(repository.findByCity("Tokyo").isEmpty());
    }

    // ------------------------------------------------------------------ addTag (a list INSIDE a document)

    @Test
    void addTag_addsANewTag_andKeepsTheOldOnes() {
        Student saved = repository.create(john());

        assertTrue(repository.addTag(saved.getId(), "mongodb"));

        assertEquals(List.of("java", "mongodb"), repository.findById(saved.getId()).getTags());
    }

    @Test
    void addTag_theSameTagTwice_isFalse_andStoredOnlyOnce() {
        Student saved = repository.create(john());

        assertTrue(repository.addTag(saved.getId(), "mongodb"));
        assertFalse(repository.addTag(saved.getId(), "mongodb"));

        assertEquals(List.of("java", "mongodb"), repository.findById(saved.getId()).getTags());
    }

    @Test
    void addTag_aTagThatIsThereFromTheStart_isFalse() {
        Student saved = repository.create(john());

        assertFalse(repository.addTag(saved.getId(), "java"));
    }

    @Test
    void addTag_doesNotChangeTheOtherFields() {
        Student saved = repository.create(john());

        repository.addTag(saved.getId(), "mongodb");

        Student stored = repository.findById(saved.getId());
        assertEquals("John", stored.getFirstName());
        assertEquals(21, stored.getAge());
        assertEquals("Paris", stored.getAddress().getCity());
    }

    @Test
    void addTag_unknownOrInvalidStudent_isFalse() {
        assertFalse(repository.addTag(new ObjectId().toHexString(), "java"));
        assertFalse(repository.addTag("abc", "java"));
        assertFalse(repository.addTag(null, "java"));
        assertEquals(0, collection.countDocuments());
    }
}
