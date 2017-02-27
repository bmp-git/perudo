package perudo.utility;

import java.time.Duration;

public class DiffTime {

    private static Duration diffTime = Duration.ZERO;

    /**
     * Gets the time difference from server and client. This Duration represent
     * (current client time) - (current server time)
     * 
     * @return the difference between client and server time
     */
    public static Duration getServerDiffTime() {
        return diffTime;
    }

    /**
     * Sets the time difference from server and client. This Duration represent
     * (current client time) - (current server time)
     * 
     * @param diff
     *            the difference between client and server time
     */
    public static void setServerDiffTime(final Duration diff) {
        diffTime = diff;
    }
}
