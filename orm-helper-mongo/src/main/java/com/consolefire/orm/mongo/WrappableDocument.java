package com.consolefire.orm.mongo;

import org.bson.Document;

public interface WrappableDocument<T> {

    T unwrap();

    Document wrap();

}
