package com.consolefire.orm.mongo;

import java.io.Serializable;

import com.consolefire.orm.common.GenericDao;

public interface WrappedDocumentRepository<E, W extends WrappableDocument<E>, I extends Serializable>
        extends GenericDao<E, I> {

    

}
