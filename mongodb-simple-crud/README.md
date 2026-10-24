# MongoDB Simple CRUD

## Goal

Create a simple Java console application that uses **MongoDB** to manage students, and learn the basic ideas of **NoSQL** databases.

Students should practice:

```text
NoSQL ideas: documents, collections, flexible schema, embedding
installing and starting a MongoDB server
connecting from Java (MongoClient, connection string)
CRUD: insertOne, find, replaceOne, deleteOne
filters, updates and sorting (Filters, Updates, Sorts)
queries on an embedded document and on a list
Maven dependencies
```

## What Is NoSQL? (short)

**SQL** databases (PostgreSQL, MySQL ...) keep data in **tables** with fixed **columns**. Every row has the same columns. To connect data from two tables you use a **JOIN**.

**NoSQL** ("not only SQL") means databases that keep data in **other shapes**. There are four main families:

| Family | The data looks like | Example databases | Good for |
|---|---|---|---|
| **Document** | JSON-like documents | **MongoDB**, Couchbase | most applications, data that has a nested shape |
| **Key-value** | a big map: key → value | Redis | cache, sessions, counters |
| **Wide-column** | rows with flexible columns | Cassandra | huge amounts of data, many writes |
| **Graph** | nodes and connections | Neo4j | social networks, recommendations |

This module uses **MongoDB**, a document database. Compare the words:

| SQL (PostgreSQL) | MongoDB |
|---|---|
| database | database |
| table | **collection** |
| row | **document** |
| column | **field** |
| fixed columns (schema) | **flexible schema**: two documents of one collection can have different fields |
| primary key `id` | `_id` (an `ObjectId`, made by MongoDB) |
| JOIN another table | **embed** the data inside the document (or `$lookup`) |
| `WHERE age > 21` | `{ age: { $gt: 21 } }` |

One student is **one document**. The address and the tags live **inside** it, so one read gives you everything, without a JOIN:

```json
{
  "_id": "6650f1c2a1b2c3d4e5f60718",
  "firstName": "John",
  "lastName": "Smith",
  "email": "john@test.com",
  "age": 21,
  "address": { "city": "Paris", "country": "France" },
  "tags": ["java", "mongodb"]
}
```

**When to choose a document database:** the data is naturally nested, the shape changes often, and you read a whole "thing" at once (a user with its address, an order with its lines).

**What you pay for it:**

- there is no automatic JOIN, so related data is often **copied** (duplicated) and you keep the copies correct yourself
- the database does not force a schema, so **your code** must handle documents that miss fields (look at `StudentMapper`)
- for data with many connections and strict rules (a bank), a SQL database is often the better choice

In one line: SQL gives you **strict structure and strong rules**, NoSQL gives you **flexibility and easy scaling**. A good developer knows both, and chooses by how the data is used.

## Install MongoDB

You need a MongoDB **server** on your own computer. Do this once.

### macOS

Use [Homebrew](https://brew.sh):

```bash
brew tap mongodb/brew
brew install mongodb-community
brew install mongosh
brew services start mongodb-community
```

To stop it later: `brew services stop mongodb-community`.

### Windows

1. Download **MongoDB Community Server** (the MSI installer): https://www.mongodb.com/try/download/community
2. Run the installer. Choose **Complete**. Keep **"Install MongoDB as a Service"** checked (so the server starts by itself).
3. Keep **"Install MongoDB Compass"** checked: it is a program with windows to look at your data.
4. Download the shell **mongosh**: https://www.mongodb.com/try/download/shell (optional, but useful).

### Linux (Ubuntu / Debian)

Follow the official steps for your system: https://www.mongodb.com/docs/manual/administration/install-on-linux/

After the installation:

```bash
sudo systemctl start mongod
sudo systemctl status mongod
```

### Check that it works

Open a terminal and run:

```bash
mongosh
```

If you see a prompt like `test>`, the server is running and you are connected. Type `exit` to leave.

You can also use **MongoDB Compass** (https://www.mongodb.com/try/download/compass): click **Connect** with the address `mongodb://localhost:27017`. You see your databases in a window.

## Connect From Java

The application connects with a **connection string**:

```text
mongodb://localhost:27017
        │       │      └── the port (27017 is the default)
        │       └── the computer where MongoDB runs ("localhost" = your own computer)
        └── the protocol
```

The default MongoDB has **no user and no password**, so this string works as it is. It is in the file:

```text
src/main/resources/mongo.properties
```

```properties
mongo.uri=mongodb://localhost:27017
mongo.database=nosql_crud
```

If your MongoDB needs a user and a password, write them in the address: `mongodb://myuser:mypassword@localhost:27017`.

You do **not** create the database `nosql_crud` or the collection `students`. MongoDB creates them the first time you save a document.

## Project

```text
src/main/java/com/example/mongo/
├── Main.java                           runs everything, step by step
├── entity/Student.java, Address.java   the data
├── mapper/StudentMapper.java           Student <-> Document   (already written)
├── util/MongoUtil.java                 the connection        (already written)
└── repository/StudentRepository.java   YOUR TASK
```

## Student Tasks

Complete the TODO methods in:

```text
src/main/java/com/example/mongo/repository/StudentRepository.java
```

**Part 1 - CRUD:**

```java
public Student create(Student student)
public Student findById(String id)
public List<Student> findAll()
public Student update(Student student)
public boolean deleteById(String id)
```

**Part 2 - queries that NoSQL makes easy:**

```java
public List<Student> findByAgeGreaterThan(int age)
public List<Student> findByCity(String city)          // a field INSIDE the embedded document "address"
public boolean addTag(String id, String tag)          // a change INSIDE the list "tags"
```

Every method has a comment with the exact rules and the driver methods that you need.

### The Same Operations In The Shell

Try each operation in `mongosh` or Compass first. It is the same thing that your Java code does:

| Your Java method | In `mongosh` |
|---|---|
| `create` | `db.students.insertOne({ firstName: "John", age: 21 })` |
| `findById` | `db.students.findOne({ _id: ObjectId("6650f1c2a1b2c3d4e5f60718") })` |
| `findAll` | `db.students.find().sort({ _id: 1 })` |
| `update` | `db.students.replaceOne({ _id: ObjectId("...") }, { firstName: "John", age: 22 })` |
| `deleteById` | `db.students.deleteOne({ _id: ObjectId("...") })` |
| `findByAgeGreaterThan` | `db.students.find({ age: { $gt: 21 } })` |
| `findByCity` | `db.students.find({ "address.city": "Paris" })` |
| `addTag` | `db.students.updateOne({ _id: ObjectId("...") }, { $addToSet: { tags: "mongodb" } })` |

To start: `mongosh`, then `use nosql_crud`, then the commands above.

## Rules

Use only:

```text
Java
MongoDB (the official Java driver, mongodb-driver-sync)
Maven
```

Do not use:

```text
Spring / Spring Data MongoDB
Morphia or any other mapping library
reading all the documents and filtering in Java (use a filter in the query!)
```

The filter must be done **by MongoDB**, not by your Java code. That is the point of a database.

## Run From IntelliJ IDEA

Create an `Application` configuration:

- Name: `MongoDB Simple CRUD`
- Main class: `com.example.mongo.Main`
- Module classpath: `mongodb-simple-crud`
- Working directory:

```text
/Users/tigranho/Projects/test/persistence_tasks/mongodb-simple-crud
```

Or from a terminal, in this folder:

```bash
mvn -q compile org.codehaus.mojo:exec-maven-plugin:3.5.0:java -Dexec.mainClass=com.example.mongo.Main
```

## Check Your Work

```bash
mvn test
```

The tests use their **own temporary database** and delete it at the end. Your data in `nosql_crud` is not touched.

- `StudentMapperTest` needs no server. It shows how a student becomes a document.
- `StudentRepositoryTest` checks your methods against the real MongoDB. If MongoDB is **not running**, these tests are **skipped** (the console says `Skipped: 27`). Start MongoDB and run again.

## Expected Practice Flow

1. Install and start MongoDB. Check with `mongosh`.
2. Run `Main`. You should see `Connected to MongoDB`.
3. Implement `create`. Look at the result in Compass or `db.students.find()`.
4. Implement `findById`.
5. Implement `findAll`.
6. Implement `update`.
7. Implement `deleteById`.
8. Implement `findByAgeGreaterThan`, `findByCity` and `addTag`.
9. Run `mvn test`. All the tests must pass.

## Common Mistakes

| Problem | Reason |
|---|---|
| `Cannot connect to MongoDB` | The server is not running. Start it (see "Install MongoDB"). |
| `Timed out after 3000 ms while waiting for a server` | Wrong host or port in `mongo.properties`, or the server is not running. |
| `Skipped: 27` in the tests | MongoDB is not running, so the repository tests could not start. |
| `IllegalArgumentException: invalid hexadecimal representation of an ObjectId` | The text is not a valid id. Check `ObjectId.isValid(id)` first. |
| `findById` returns `null` for an id that you just saved | You searched with the text. A filter on `_id` needs an `ObjectId`: `new ObjectId(id)`. |
| The new student has no id | `insertOne` puts `_id` into the document. Read it from the document and save it in the student. |
| `update` creates a second document | Use `replaceOne` with a filter on `_id`, not `insertOne`. |
| `findByCity` finds nothing | The city is inside `address`. The field name is `"address.city"` (with a dot). |
| `addTag` adds the same tag twice | Use `Updates.addToSet`, not `Updates.push`. |
