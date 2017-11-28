package com.consolefire.orm.common.util;

import lombok.Getter;

/**
 * Created by sabuj.das on 13/04/16.
 * @author sabuj.das
 */
@Getter
public final class ModelEntry<V> {
    private final String fieldName;
    private final V value;

    public ModelEntry(String fieldName, V value) {
        this.fieldName = fieldName;
        this.value = value;
    }

}