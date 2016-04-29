/**
 * Created at 29-Apr-2016 by sabuj.das
 */
package com.consolefire.orm.config;

/**
 * @author sabuj.das
 *
 */
public interface EntityListener {

    void onPrePersist(Object entity);

    void onPostPersist(Object entity);

    void onPostLoad(Object entity);

    void onPreUpdate(Object entity);

    void onPostUpdate(Object entity);

    void onPreRemove(Object entity);

    void onPostRemove(Object entity);
}
