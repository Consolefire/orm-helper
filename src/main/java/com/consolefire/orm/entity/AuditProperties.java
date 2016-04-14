package com.consolefire.orm.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter
@Setter
@Embeddable
public class AuditProperties {

    @Column(name = "created_by", columnDefinition = " VARCHAR(150) NOT NULL DEFAULT 'DB_HACKER' ")
    private String createdBy;

    @Column(name = "created_at",
            columnDefinition = " TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_by", length = 150)
    private String updatedBy;

    @Column(name = "updated_at", columnDefinition = " TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP ")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

}
