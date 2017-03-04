package me.ialistannen.networktest.util;

import java.lang.reflect.Constructor;

/**
 * A util to help with reflection
 *
 * @author I Al Istannen
 */
public class ReflectionUtil {

    /**
     * Creates a new instance of a {@link Class}
     *
     * @param clazz The class
     * @param <T> The type of the class
     *
     * @return The new instance
     *
     * @throws IllegalArgumentException if the class has no nullary constructor
     * @throws RuntimeException         wrapping any Exception that might occur
     */
    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (IllegalAccessException e) {
            // has no nullary constructor or not accessible
            return newInstanceFromPrivate(clazz);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T newInstanceFromPrivate(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Class has no nullary constructor");
        } catch (ReflectiveOperationException newException) {
            throw new RuntimeException(newException);
        }
    }
}
