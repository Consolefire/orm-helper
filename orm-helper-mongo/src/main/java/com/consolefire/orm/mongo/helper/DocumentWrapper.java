package com.consolefire.orm.mongo.helper;

import com.consolefire.orm.mongo.WrappableDocument;

public class DocumentWrapper<T> implements WrappableDocument<T> {

    private T document;

    public T unwrap() {
        return document;
    }

    public void wrap(T document) {
        this.document = document;
    }


}
