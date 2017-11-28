package com.consolefire.orm.spring.support;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.consolefire.orm.common.util.ModelEntry;

/**
 * Created by sabuj.das on 13/04/16.
 *
 * @author sabuj.das
 */
public abstract class FactoryGirl {

    private static Logger logger = LoggerFactory.getLogger(FactoryGirl.class);
    private static final int DEFAULT_RANDOM_SEED = 10000;
    private static final int DEFAULT_RANDOM_STRING_LENGTH = 10;

    /**
     * Creates the object with the field specified in the ModelEntity
     * @param clazz
     * @param entries
     * @return
     */
    public static <T> T create(Class<T> clazz, ModelEntry<?>... entries) {
        if (null == clazz) {
            return null;
        }
        try {
            T data = clazz.newInstance();
            if (null != entries && entries.length > 0) {
                for (ModelEntry e : entries) {
                    if (null == e.getValue()) {
                        continue;
                    }
                    String setterMethodName = "set" + StringUtils.capitalize(e.getFieldName());
                    Method fieldToSet = ReflectionUtils.findMethod(clazz, setterMethodName, e.getValue().getClass());
                    if (null != fieldToSet) {
                        ReflectionUtils.invokeMethod(fieldToSet, data, e.getValue());
                    }
                }
            }
            return data;
        } catch (InstantiationException e) {
            logger.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * Sets the fields from the ModelEntity and generates random values only for the primitive types.
     * @param clazz
     * @param entries
     * @return
     */
    public static <T> T generate(Class<T> clazz, ModelEntry<?>... entries) {
        if (null == clazz) {
            return null;
        }
        try {
            T data = clazz.newInstance();
            Set<Method> excludeFieldSet = new HashSet<>();
            if (null != entries && entries.length > 0) {
                for (ModelEntry e : entries) {
                    if (null == e.getValue()) {
                        continue;
                    }
                    String setterMethodName = "set" + StringUtils.capitalize(e.getFieldName());
                    Method fieldToSet = ReflectionUtils.findMethod(clazz, setterMethodName, e.getValue().getClass());
                    if (null != fieldToSet) {
                        excludeFieldSet.add(fieldToSet);
                        ReflectionUtils.invokeMethod(fieldToSet, data, e.getValue());
                    }
                }
            }
            generateRandomData(data, excludeFieldSet);
            return data;
        } catch (InstantiationException e) {
            logger.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    private static <T> void generateRandomData(final T data, final Set<Method> excludeFieldSet) {
        List<Field> fields = ObjectUtil.getFields(data.getClass(), true);
        if (null != fields) {
            for (Field field : fields) {
                Class type = field.getType();
                String setterMethodName = "set" + StringUtils.capitalize(field.getName());
                Method setterMethod = ReflectionUtils.findMethod(data.getClass(), setterMethodName, type);
                if (null == setterMethod) {
                    continue;
                }
                if (excludeFieldSet.contains(setterMethod)) {
                    continue;
                }

                Random random = new Random(DEFAULT_RANDOM_SEED);
                if (type.getTypeName().equals(String.class.getCanonicalName())) {
                    String stringValue = new RandomString(DEFAULT_RANDOM_STRING_LENGTH).nextString();
                    ReflectionUtils.invokeMethod(setterMethod, data, stringValue);
                }
                if (type.getTypeName().equals(Long.class.getCanonicalName())) {
                    ReflectionUtils.invokeMethod(setterMethod, data, random.nextLong());
                }
                if (type.getTypeName().equals(Integer.class.getCanonicalName())) {
                    ReflectionUtils.invokeMethod(setterMethod, data, random.nextInt());
                }
                if (type.getTypeName().equals(Float.class.getCanonicalName())) {
                    ReflectionUtils.invokeMethod(setterMethod, data, random.nextFloat());
                }
                if (type.getTypeName().equals(Double.class.getCanonicalName())) {
                    ReflectionUtils.invokeMethod(setterMethod, data, random.nextDouble());
                }
            }
        }
    }

    static class RandomString {

        private static final char[] SYMBOLS;

        static {
            StringBuilder tmp = new StringBuilder();
            for (char ch = '0'; ch <= '9'; ++ch) {
                tmp.append(ch);
            }
            for (char ch = 'a'; ch <= 'z'; ++ch) {
                tmp.append(ch);
            }
            SYMBOLS = tmp.toString().toCharArray();
        }

        private final Random random = new Random();

        private final char[] buf;

        public RandomString(int length) {
            if (length < 1) {
                throw new IllegalArgumentException("length < 1: " + length);
            }
            buf = new char[length];
        }

        public String nextString() {
            for (int idx = 0; idx < buf.length; ++idx) {
                buf[idx] = SYMBOLS[random.nextInt(SYMBOLS.length)];
            }
            return new String(buf);
        }
    }

}
