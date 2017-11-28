package com.consolefire.orm.common;


import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditProperties {

    private String createdBy;

    private Date createdAt;

    private String updatedBy;

    private Date updatedAt;

}
