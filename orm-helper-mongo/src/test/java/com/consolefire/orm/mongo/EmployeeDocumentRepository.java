package com.consolefire.orm.mongo;

import java.io.Serializable;

public interface EmployeeDocumentRepository<I extends Serializable> {

    Employee<I> findByEmployeeName(String name);

}
