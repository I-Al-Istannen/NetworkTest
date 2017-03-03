package me.ialistannen.testchat.shared.util;

/**
 * A TriFunction
 *
 * @author I Al Istannen
 */
@FunctionalInterface
public interface TriFunction <T, U, V, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @param v the third function argument
     *
     * @return the function result
     */
    R apply(T t, U u, V v);
}
