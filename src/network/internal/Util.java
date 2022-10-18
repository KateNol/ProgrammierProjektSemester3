package network.internal;

public final class Util {
    private Util() {
    }

    public static final String defaultAddress = "localhost";

    public static final int defaultPort = 13337;

    private static final String logPrefix = "Network: ";

    public static void log_stdio(String msg) {
        System.out.println(logPrefix + msg);
    }

    public static void log_stderr(String msg) {
        System.err.println(logPrefix + msg);
    }
}
