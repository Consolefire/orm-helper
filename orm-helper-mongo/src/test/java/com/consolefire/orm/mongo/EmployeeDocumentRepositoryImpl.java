package com.consolefire.orm.mongo;

import com.consolefire.orm.mongo.helper.GenericDocumentWrapperDao;

public class EmployeeDocumentRepositoryImpl extends GenericDocumentWrapperDao<Employee<String>, String>
        implements EmployeeDocumentRepository<String> {

    @Override
    public Employee<String> findByEmployeeName(String name) {
        
        return null;
    }

}
