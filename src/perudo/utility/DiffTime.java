package perudo.utility;

import java.time.Duration;

/**
 * This static class represent the difference in terms of time with the server.
 * This class should be used only by client when using a controller over
 * network.
 */
public final class DiffTime {

    private static Duration deltaTime = Duration.ZERO;

    private DiffTime() {

    }

    /**
     * Gets the time difference from server and client. This Duration represent
     * (current client time) - (current server time)
     * 
     * @return the difference between client and server time
     */
    public static Duration getServerDiffTime() {
        return deltaTime;
    }

    /**
     * Sets the time difference from server and client. This Duration represent
     * (current client time) - (current server time)
     * 
     * @param diff
     *            the difference between client and server time
     */
    public static synchronized void setServerDiffTime(final Duration diff) {
        deltaTime = diff;
    }
}
