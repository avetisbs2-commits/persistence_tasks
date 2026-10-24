package com.example.mongo.util;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Connects to MongoDB. The address and the database name come from the file src/main/resources/mongo.properties.
 *
 * PROVIDED CODE - students do not need to change it.
 */
public final class MongoUtil {

    private static final Properties PROPERTIES = loadProperties();
    private static MongoClient client;

    private MongoUtil() {
    }

    /** The address of the server, for example mongodb://localhost:27017 (a system property -Dmongo.uri=... wins). */
    public static String getUri() {
        return System.getProperty("mongo.uri", PROPERTIES.getProperty("mongo.uri", "mongodb://localhost:27017"));
    }

    public static String getDatabaseName() {
        return System.getProperty("mongo.database", PROPERTIES.getProperty("mongo.database", "nosql_crud"));
    }

    public static synchronized MongoClient getClient() {
        if (client == null) {
            client = createClient(getUri());
        }
        return client;
    }

    /** A client that gives up after 3 seconds when there is no server (the default is 30 seconds). */
    public static MongoClient createClient(String uri) {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .applyToClusterSettings(cluster -> cluster.serverSelectionTimeout(3, TimeUnit.SECONDS))
                .build();
        return MongoClients.create(settings);
    }

    public static MongoDatabase getDatabase() {
        return getClient().getDatabase(getDatabaseName());
    }

    /** True when the MongoDB server answers a "ping". */
    public static boolean isRunning() {
        return isRunning(getClient());
    }

    public static boolean isRunning(MongoClient anyClient) {
        try {
            anyClient.getDatabase("admin").runCommand(new Document("ping", 1));
            return true;
        } catch (MongoException e) {
            return false;
        }
    }

    public static synchronized void close() {
        if (client != null) {
            client.close();
            client = null;
        }
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream in = MongoUtil.class.getResourceAsStream("/mongo.properties")) {
            if (in != null) {
                properties.load(in);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read mongo.properties", e);
        }
        return properties;
    }
}
