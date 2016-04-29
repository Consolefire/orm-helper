/**
 * Created at 29-Apr-2016 by sabuj.das
 */
package com.consolefire.orm.entity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.consolefire.orm.ContextAutowireFactory;
import com.consolefire.orm.config.AuditPropertiesProvider;
import com.consolefire.orm.config.EntityListener;
import com.consolefire.orm.util.ObjectUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * @author sabuj.das
 *
 */
@Getter
@Setter
public class AuditableEntityListener implements EntityListener {

    private static Logger logger = LoggerFactory.getLogger(AuditableEntityListener.class);

    @Autowired
    private AuditPropertiesProvider auditPropertiesProvider;

    @PrePersist
    public void onPrePersist(Object entity) {
        updateAuditProperties(entity, false);
    }

    @PostPersist
    public void onPostPersist(Object entity) {

    }

    @PostLoad
    public void onPostLoad(Object entity) {

    }

    @PreUpdate
    public void onPreUpdate(Object entity) {
        updateAuditProperties(entity, true);
    }

    private void updateAuditProperties(Object entity, boolean update) {
        if (null == entity) {
            return;
        }
        ContextAutowireFactory.injectBean(this, this.auditPropertiesProvider);
        if (null == auditPropertiesProvider) {
            logger.warn("!!! AuditProperties not set.");
        }
        Annotation annotation = ObjectUtil.getAnnotation(entity.getClass(), Auditable.class, true);
        if (null != annotation) {
            AuditProperties sourceAuditProperties = auditPropertiesProvider.getAuditProperties();
            if (null == sourceAuditProperties) {
                logger.warn("Nothing to set in audit properties.");
                return;
            }
            AuditProperties targetAuditProperties = null;
            Auditable auditable = (Auditable) annotation;
            if (null != auditable) {
                Method getterMethod = ObjectUtil.getGetterMethod(entity.getClass(), auditable.value());
                try {
                    targetAuditProperties = (AuditProperties) getterMethod.invoke(entity, null);
                } catch (Exception e) {
                    targetAuditProperties = new AuditProperties();
                }
            }
            if (null == targetAuditProperties) {
                targetAuditProperties = new AuditProperties();
            }

            if (update) {
                targetAuditProperties.setUpdatedBy(sourceAuditProperties.getUpdatedBy());
            } else {
                targetAuditProperties.setCreatedBy(sourceAuditProperties.getCreatedBy());
            }

            try {
                Method setterMethod = entity.getClass().getMethod(ObjectUtil.createSetterMethod(auditable.value()),
                        new Class[] {AuditProperties.class});
                if (null == setterMethod) {
                    logger.error("Could not set Audit Properties as not set method");
                }
                setterMethod.invoke(entity, new Object[] {targetAuditProperties});
            } catch (Exception e) {
                logger.error("Could not set Audit Properties");
            }
        }
    }

    @PostUpdate
    public void onPostUpdate(Object entity) {

    }

    @PreRemove
    public void onPreRemove(Object entity) {

    }

    @PostRemove
    public void onPostRemove(Object entity) {

    }


}
