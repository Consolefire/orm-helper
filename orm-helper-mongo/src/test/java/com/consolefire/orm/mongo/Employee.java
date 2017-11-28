package com.consolefire.orm.mongo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Employee<I extends Serializable> {

    private I id;
    private String name;


}
