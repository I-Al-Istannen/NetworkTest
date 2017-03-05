package me.ialistannen.networktest.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A util to help with reflection
 *
 * @author I Al Istannen
 */
public class ReflectionUtil {

    private static final Logger LOGGER = Logger.getLogger(ReflectionUtil.class.getName());

    /**
     * Creates a new instance of a {@link Class}
     *
     * @param clazz The class
     * @param <T> The type of the class
     *
     * @return The new instance
     *
     * @throws IllegalArgumentException  if the class has no nullary constructor
     * @throws InvocationTargetException if an error occurred inside the constructor
     * @throws InstantiationException    if you passed me stuff like an interface or abstract class
     */
    public static <T> T newInstance(Class<T> clazz) throws InvocationTargetException, InstantiationException {
        try {
            return clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            // has no nullary constructor or not accessible
            return newInstanceFromPrivate(clazz);
        }
    }

    private static <T> T newInstanceFromPrivate(Class<T> clazz)
            throws InvocationTargetException, InstantiationException {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Class has no nullary constructor");
        } catch (IllegalAccessException e) {
            LOGGER.log(Level.SEVERE, "I have no rights to perfrom reflection", e);
            throw new IllegalStateException("No permission for reflection", e);
        }
    }
}
