package me.ialistannen.networktest.util;

import org.apache.commons.lang3.ArrayUtils;

/**
 * A util for {@link Runnable}s
 *
 * @author I Al Istannen
 */
public class RunnableUtil {

    /**
     * Executes an {@link UncheckedRunnable}
     *
     * @param runnable The runnable to execute
     */
    public static void doUnchecked(UncheckedRunnable runnable) {
        runnable.run();
    }

    /**
     * Executes the runnable, rethrowing all exceptions NOT in {@code swallowedClasses} as
     * {@link ErrorInRunnableException}s
     *
     * @param runnable The {@link UncheckedRunnable} to run
     * @param swallowedClasses The exceptions to swallow
     *
     * @throws ErrorInRunnableException if any other {@link Throwable} is thrown (wrapping the cause)
     */
    @SafeVarargs
    public static void doUncheckedAndSwallow(UncheckedRunnable runnable,
                                             Class<? extends Exception>... swallowedClasses) {
        try {
            runnable.execute();
        } catch (Throwable throwable) {
            if (!ArrayUtils.contains(swallowedClasses, throwable.getClass())) {
                throw new ErrorInRunnableException(throwable);
            }
        }
    }

    /**
     * A Runnable allowing for checked exceptions
     */
    @FunctionalInterface
    public interface UncheckedRunnable extends Runnable {

        /**
         * The run method of this runnable
         *
         * @throws Throwable Any exception thrown
         */
        void execute() throws Throwable;

        /**
         * Executes the {@link Runnable}
         *
         * @throws ErrorInRunnableException if an {@link Exception} occurs inside the Run method
         */
        @Override
        default void run() {
            try {
                execute();
            } catch (Throwable throwable) {
                throw new ErrorInRunnableException(throwable);
            }
        }
    }

    /**
     * A {@link RuntimeException} denoting that an Exception was raised inside an executed {@link Runnable}
     */
    public static class ErrorInRunnableException extends RuntimeException {
        /**
         * Constructs a new runtime exception with the specified cause and a
         * detail message of <tt>(cause==null ? null : cause.toString())</tt>
         * (which typically contains the class and detail message of
         * <tt>cause</tt>).  This constructor is useful for runtime exceptions
         * that are little more than wrappers for other throwables.
         *
         * @param cause the cause (which is saved for later retrieval by the
         * {@link #getCause()} method).  (A <tt>null</tt> value is
         * permitted, and indicates that the cause is nonexistent or
         * unknown.)
         *
         * @since 1.4
         */
        public ErrorInRunnableException(Throwable cause) {
            super(cause);
        }
    }
}
