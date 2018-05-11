package de.flxplzk.vaadin.mvvm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Felix Plazek
 */
class ReflectionUtil {

    /**
     * Private constructor to prevent instantiation.
     */
    private ReflectionUtil() {}

    /**
     * this method returns the the wanted instance which is represented
     * by the field param.
     *
     * @param field  that shall be extracted
     * @param object from which the field shall be extracted
     * @param type   expected type that should be retrieved
     * @param <T>    matching the provided class of param type
     * @return the extracted instance from the object.
     */
    public static <T> T get(Field field, Object object, Class<T> type) {
        try {
            field.setAccessible(true);
            return type.cast(field.get(object));
        } catch (IllegalAccessException | ClassCastException e) {
            throw new BindingException(e);
        }
    }

    /**
     * this method returns the the wanted instance which is represented
     * by the field name.
     *
     * @param name   of the target field in the target object
     * @param object from which the field shall be extracted
     * @param type   expected type that should be retrieved
     * @param <T>    matching the provided class of param type
     * @return the extracted instance from the object.
     */
    public static <T> T get(String name, Object object, Class<T> type) {
        try {
            Field field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return get(field, object, type);
        } catch (NoSuchFieldException e) {
            throw new BindingException(e);
        }
    }

    /**
     * retrieves the wantedt method out of the provided object.
     *
     * @param methodName name of the target method
     * @param object     instance of which the method should be extracted
     * @return the wanted method
     */
    public static Method getMethod(String methodName, Object object) {
        try {
            return object.getClass().getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            throw new BindingException("no method called: '"
                    + methodName
                    + "' found; unable binding that method reference"
                    , e);
        }
    }

    /**
     * retrieves the wantedt method out of the provided object.
     *
     * @param methodName    name of the target method
     * @param object        instance of which the method should be extracted
     * @param parameterType the parameter type of the target method
     * @return the wanted method
     */
    public static Method getMethod(String methodName, Object object, Class<?> parameterType) {
        try {
            return object.getClass().getDeclaredMethod(methodName, parameterType);
        } catch (NoSuchMethodException e) {
            throw new BindingException("no method called: '"
                    + methodName
                    + "' found; unable binding that method reference"
                    , e);
        }
    }
}
