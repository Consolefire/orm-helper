package com.consolefire.orm.mongo;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;

import org.bson.Document;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import lombok.Getter;
import lombok.Setter;

public class EmployeeDaoTest {

    public static void main(String[] args) {
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        MongoDatabase database = mongoClient.getDatabase("employee_database");
        MongoCollection<Document> collection = database.getCollection("employees");

        MongoPersistenceContext persistenceContext =
                new MongoPersistenceContext("localhost", (short) 27017, "employee_database");

        Employee<String> e = new Employee<>();
        e.setId("E001");
        e.setName("E 001");

        collection.insertOne(new Document().append("identity", e.getId()).append("name", e.getName()));
    }

}


@Getter
@Setter
@Component
class MongoPersistenceContext {
    private final String host;
    private final Short port;
    private final String databaseName;


    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoEntityManager entityManager;


    public MongoPersistenceContext(String host, Short port, String databaseName) {
        this.host = host;
        this.port = port;
        this.databaseName = databaseName;
        mongoClient = new MongoClient(new MongoClientURI(String.format("mongodb://%s:%d", host, port)));
        database = mongoClient.getDatabase("employee_database");
        entityManager = new MongoEntityManager();
    }


}


class MongoEntityManager implements EntityManager {

    @Override
    public void persist(Object entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> T merge(T entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void remove(Object entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T getReference(Class<T> entityClass, Object primaryKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void flush() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setFlushMode(FlushModeType flushMode) {
        // TODO Auto-generated method stub

    }

    @Override
    public FlushModeType getFlushMode() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void lock(Object entity, LockModeType lockMode) {
        // TODO Auto-generated method stub

    }

    @Override
    public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
        // TODO Auto-generated method stub

    }

    @Override
    public void refresh(Object entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public void refresh(Object entity, Map<String, Object> properties) {
        // TODO Auto-generated method stub

    }

    @Override
    public void refresh(Object entity, LockModeType lockMode) {
        // TODO Auto-generated method stub

    }

    @Override
    public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
        // TODO Auto-generated method stub

    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub

    }

    @Override
    public void detach(Object entity) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean contains(Object entity) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public LockModeType getLockMode(Object entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setProperty(String propertyName, Object value) {
        // TODO Auto-generated method stub

    }

    @Override
    public Map<String, Object> getProperties() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Query createQuery(String qlString) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Query createNamedQuery(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Query createNativeQuery(String sqlString) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Query createNativeQuery(String sqlString, Class resultClass) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Query createNativeQuery(String sqlString, String resultSetMapping) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void joinTransaction() {
        // TODO Auto-generated method stub

    }

    @Override
    public <T> T unwrap(Class<T> cls) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getDelegate() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isOpen() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public EntityTransaction getTransaction() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Metamodel getMetamodel() {
        // TODO Auto-generated method stub
        return null;
    }

}
