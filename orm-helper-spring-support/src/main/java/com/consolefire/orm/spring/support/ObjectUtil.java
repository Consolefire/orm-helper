package com.consolefire.orm.spring.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.SerializationUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility to manipulate Class Meta data.
 *
 * @author Sabuj.das
 */
public class ObjectUtil {
    final static private Logger logger = LoggerFactory.getLogger(ObjectUtil.class);

    /**
     * This method performs the deep copy of given object and returns the Serializable
     * @param obj
     * @return Serializable
     */
    public static Serializable copy(Object obj) {
        try {
            return (Serializable) SerializationUtils.deserialize(SerializationUtils.serialize(obj));
        } catch (Exception e) {
            logger.error("Deep copy of a object is failed - {},{}",e.getMessage(),e.getCause());
            e.printStackTrace();
        }
        return null;
    }

	/**
	 * Creates getter method name from the field name.
	 * @param fieldName
	 * @return
	 */
	public static String createGetterMethod(String fieldName){
		if(StringUtils.hasText(fieldName)){
			if(fieldName.length() > 1)
				return "get" + (""+fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);
			return "get" + (""+fieldName.charAt(0)).toUpperCase();
		}
		return "";
	}

	/**
     * Creates getter method name from the field name.
     * @param fieldName
     * @return
     */
    public static String createSetterMethod(String fieldName){
        if(StringUtils.hasText(fieldName)){
            if(fieldName.length() > 1)
                return "set" + (""+fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);
            return "set" + (""+fieldName.charAt(0)).toUpperCase();
        }
        return "";
    }


	/**
	 * Create a copy of the goven object. This method is only to create
	 * a copy of data contained in the object and create a new instance
	 * with those data.
	 *
	 * @param from T
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static <T> T copyOf(T from) throws InstantiationException,
			IllegalAccessException, NoSuchMethodException,
			IllegalArgumentException, InvocationTargetException {
		if (null == from)
			return null;

		Class<T> clazz = (Class<T>) from.getClass();
		T newObject = clazz.newInstance();
		if (null == newObject)
			return null;

		Method[] methods = clazz.getDeclaredMethods();
		if (null != methods) {
			for (int i = 0; i < methods.length; i++) {
				Method getterMethod = methods[i];
				final String name = getterMethod.getName();

				if (name.startsWith("get")) {
					String setter = "set"
							+ name.substring(name.indexOf("get")
									+ "get".length());
					Method setterMethod = clazz.getMethod(setter,
							getterMethod.getReturnType());
					setterMethod.invoke(newObject,
							getterMethod.invoke(from, null));
				}
			}
		}

		return newObject;
	}

	public static boolean isNumberType(Class clazz) {
		if (clazz == null)
			return false;
		if (clazz.getCanonicalName().equals(Integer.class.getCanonicalName())
				|| clazz.getCanonicalName().equals(
						Float.class.getCanonicalName())
				|| clazz.getCanonicalName().equals(
						Double.class.getCanonicalName())
				|| clazz.getCanonicalName().equals(
						Long.class.getCanonicalName()))
			return true;
		return false;
	}

	/**
	 * By default this will return only declared fields.
	 * if <code>inherited == true</code>, all the fields, before <code>java.lang.Object</code> will be returned.
	 * @param type
	 * @param inherited
	 * @return
	 */
	public static List<Field> getFields(Class type, boolean inherited){
		if(null == type)
			return null;
		List<Field> fields = new ArrayList<Field>();
		fields.addAll(Arrays.asList(type.getDeclaredFields()));
		if( inherited ){
			Class c = type.getSuperclass();
			while(null != c
					&& !c.getCanonicalName().equals(Object.class.getCanonicalName())){
				fields.addAll(Arrays.asList(c.getDeclaredFields()));
				c = c.getSuperclass();
			}
		}
		return fields;
	}

	public static Method getGetterMethod(Class type, String fieldName) {
		if(null == type)
			return null;
		String methodName = createGetterMethod(fieldName);
		if(StringUtils.hasText(methodName)){
			try {
				return type.getMethod(methodName, null);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	public static Method getSetterMethod(Class type, String fieldName) {
        if(null == type)
            return null;
        String methodName = createSetterMethod(fieldName);
        if(StringUtils.hasText(methodName)){
            try {
                return type.getMethod(methodName, null);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

	public static Annotation getAnnotation(Class type, Class annotation, boolean inherited){
        if(null == type)
            return null;

        if( inherited && !type.isAnnotationPresent(annotation) ){
            Class c = type.getSuperclass();
            while(null != c
                    && !c.getCanonicalName().equals(Object.class.getCanonicalName())){
                if(!c.isAnnotationPresent(annotation) ){
                    c = c.getSuperclass();
                } else {
                    type = c;
                    break;
                }
            }
        }
        return type.getAnnotation(annotation);
    }
}


