# Persistence Tasks

This project contains separate Java persistence practice modules.

The modules will grow from simple to harder topics:

```text
JPA
Hibernate
JDBC
NoSQL (MongoDB)
transactions
relationships
queries
repositories
```

Each task should be runnable separately from IntelliJ IDEA or Maven.

## Project Structure

```text
persistence_tasks/
├── README.md
├── pom.xml
├── jpa-simple-crud/
│   ├── README.md
│   ├── pom.xml
│   └── src/
├── jdbc-simple-crud/
│   ├── README.md
│   ├── pom.xml
│   └── src/
├── mongodb-simple-crud/
│   ├── README.md
│   ├── pom.xml
│   └── src/
└── sql-todo-scripts/
    ├── README.md
    ├── pom.xml
    └── sql/
```

## Current Modules

```text
jpa-simple-crud
jdbc-simple-crud
mongodb-simple-crud
sql-todo-scripts
```

## JPA Simple CRUD

Practice basic CRUD using:

```text
JPA
PostgreSQL
Maven
```

No Spring is used.

## JDBC Simple CRUD

Practice basic CRUD using:

```text
JDBC
PostgreSQL
Maven
```

No Spring, JPA, or Hibernate is used.

## MongoDB Simple CRUD

Practice basic CRUD with a **NoSQL** (document) database:

```text
Java
MongoDB
Maven
```

No Spring and no mapping library is used. The module starts with a short explanation of NoSQL (documents, collections, flexible schema, embedding) and shows how to **install MongoDB on your own computer** and connect to it. Students need a running MongoDB server.

## SQL TODO Scripts

`sql-todo-scripts` is a pure SQL practice module. Students write PostgreSQL scripts for DDL, constraints, relationships, inserts, table changes, and select queries.

## IntelliJ IDEA Setup

Open this folder in IntelliJ IDEA:

```text
/Users/tigranho/Projects/test/persistence_tasks
```

Import it as a Maven project.

If IntelliJ does not import automatically:

1. Open the Maven tool window.
2. Click `+`.
3. Select:

```text
/Users/tigranho/Projects/test/persistence_tasks/pom.xml
```

## Run/Debug Configuration

Create an `Application` configuration.

Use these values:

- Name: `JPA Simple CRUD`
- Main class: `com.example.jpa.Main`
- Module classpath: `jpa-simple-crud`
- JRE: project default JDK 21
- Program arguments: leave empty
- VM options: leave empty
- Working directory:

```text
/Users/tigranho/Projects/test/persistence_tasks/jpa-simple-crud
```

Create an `Application` configuration for MongoDB (a MongoDB server must be installed and running):

- Name: `MongoDB Simple CRUD`
- Main class: `com.example.mongo.Main`
- Module classpath: `mongodb-simple-crud`
- JRE: project default JDK 21
- Working directory:

```text
/Users/tigranho/Projects/test/persistence_tasks/mongodb-simple-crud
```

Create another `Application` configuration for JDBC:

- Name: `JDBC Simple CRUD`
- Main class: `com.example.jdbc.Main`
- Module classpath: `jdbc-simple-crud`
- JRE: project default JDK 21
- Program arguments: leave empty
- VM options: leave empty
- Working directory:

```text
/Users/tigranho/Projects/test/persistence_tasks/jdbc-simple-crud
```

## Maven Commands

From the root project:

```bash
mvn clean install
```

From only the first module:

```bash
cd /Users/tigranho/Projects/test/persistence_tasks/jpa-simple-crud
mvn clean package
```

From only the JDBC module:

```bash
cd /Users/tigranho/Projects/test/persistence_tasks/jdbc-simple-crud
mvn clean package
```

From only the MongoDB module:

```bash
cd /Users/tigranho/Projects/test/persistence_tasks/mongodb-simple-crud
mvn clean package
```

The MongoDB tests are skipped when no MongoDB server is running, so the root build does not fail on a computer without MongoDB.

For the SQL module, open the files under:

```text
/Users/tigranho/Projects/test/persistence_tasks/sql-todo-scripts/sql
```

Run the scripts manually in PostgreSQL using IntelliJ IDEA Database tool, DBeaver, pgAdmin, or `psql`.
