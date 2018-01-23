package com.consolefire.orm.mongo;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class EmployeeDaoTest {

    public static void main(String[] args) {
        //MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://10.33.81.28:27101,10.33.177.233:27101,10.32.241.195:27101/employee_database"));
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://10.85.133.84:27101,10.85.147.24:27101/employee_database"));
        MongoDatabase database = mongoClient.getDatabase("employee_database");
        MongoCollection<Document> collection = database.getCollection("employees");

        Employee<String> e = new Employee<>();
        e.setId("E002");
        e.setName("E 002");

        collection.insertOne(new Document().append("identity", e.getId()).append("name", e.getName()));
        
    }

}


