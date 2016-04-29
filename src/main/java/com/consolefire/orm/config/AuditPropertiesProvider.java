/**
 * Created at 29-Apr-2016 by sabuj.das
 */
package com.consolefire.orm.config;

import com.consolefire.orm.entity.AuditProperties;

/**
 * @author sabuj.das
 *
 */
public interface AuditPropertiesProvider {

    AuditProperties getAuditProperties();
    
}
