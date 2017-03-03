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
     * Executes the runnable, rethrowing all exceptions NOT in {@code swallowedClasses} as {@link RuntimeException}s
     *
     * @param runnable The {@link UncheckedRunnable} to run
     * @param swallowedClasses The exceptions to swallow
     */
    @SafeVarargs
    public static void doUncheckedAndSwallow(UncheckedRunnable runnable,
                                             Class<? extends Exception>... swallowedClasses) {
        try {
            runnable.execute();
        } catch (Throwable throwable) {
            if (!ArrayUtils.contains(swallowedClasses, throwable.getClass())) {
                throw new RuntimeException(throwable);
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

        @Override
        default void run() {
            try {
                execute();
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }
    }
}
