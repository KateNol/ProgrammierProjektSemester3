package network.internal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Util {
    private Util() {
    }

    public static int implementedProtocolVersion = 1;

    public static final String defaultAddress = "localhost";
    public static final int defaultPort = 42069;

    public static final Character messageDelimiter = ';';

    public static String constructMessage(String command, String... args) {
        StringBuilder builder = new StringBuilder();

        builder.append(command);
        builder.append(messageDelimiter);

        for (String arg : args) {
            builder.append(arg);
            builder.append(messageDelimiter);
        }

        return builder.toString();
    }

    // log
    private static final String logPrefix = "Network";

    public static void log_stdio(String msg) {
        shared.Util.log_stdio(logPrefix, msg);
    }

    public static void log_debug(String msg) {
        shared.Util.log_debug(logPrefix, msg);
    }

    public static void log_stderr(String msg) {
        shared.Util.log_stderr(logPrefix, msg);
    }

}
