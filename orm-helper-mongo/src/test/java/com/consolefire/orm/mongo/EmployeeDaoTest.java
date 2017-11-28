package com.consolefire.orm.mongo;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class EmployeeDaoTest {

    public static void main(String[] args) {
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        MongoDatabase database = mongoClient.getDatabase("employee_database");
        MongoCollection<Document> collection = database.getCollection("employees");

        Employee<String> e = new Employee<>();
        e.setId("E001");
        e.setName("E 001");

        collection.insertOne(new Document().append("identity", e.getId()).append("name", e.getName()));
    }

}

class MongoPersistenceContext {
    private String host;
    private Short port;
    private String databaseName;
    
    
    
}


class MongoEntityManager {
    
}