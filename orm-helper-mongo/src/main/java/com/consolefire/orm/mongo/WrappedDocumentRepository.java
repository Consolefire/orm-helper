package com.consolefire.orm.mongo;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

public interface WrappedDocumentRepository<W extends WrappableDocument<?>, I extends Serializable>
        extends CrudRepository<W, I> {



}
