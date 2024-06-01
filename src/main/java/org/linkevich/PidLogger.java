package org.linkevich;

/**
 * Helper class to log messages with PID as a prefix.
 */
public final class PidLogger {

    /**
     * Logs given string with current PID as a prefix.
     * @param text string to log
     */
    public static void log(String text) {
        System.out.println(ProcessHandle.current().pid() + ": " + text);
    }
}
