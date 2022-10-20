package network.internal;

public final class Util {
    private Util() {}

    public static final String defaultAddress = "localhost";
    public static final int defaultPort = 13337;

    public static final Character messageDelimiter = ';';
    public static String constructMessage(String command, String[] args) {
        StringBuilder builder = new StringBuilder();

        builder.append(command);
        builder.append(messageDelimiter);

        for (String arg : args) {
            builder.append(arg);
            builder.append(messageDelimiter);
        }

        return builder.toString();
    }

    private static final String logPrefix = "Network: ";
    public static void log_stdio(String msg) {
        System.out.println(logPrefix + msg);
    }
    public static void log_stderr(String msg) {
        System.err.println(logPrefix + msg);
    }

    public static int implementedProtocolVersion = 1;
}
