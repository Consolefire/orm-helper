package com.consolefire.orm.mongo;

public interface WrappableDocument<T> {

    T unwrap();

    void wrap(T document);

}
