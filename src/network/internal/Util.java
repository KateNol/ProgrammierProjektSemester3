package network.internal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Util {
    private Util() {
    }

    public static final boolean debug = true;

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
    private static final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    private static String getDateTimePrefix() {
        return dtFormatter.format(LocalDateTime.now());
    }

    private static final String logPrefix = "Network";

    public static void log_stdio(String msg) {
        System.out.println(getDateTimePrefix() + " :: " + Thread.currentThread().getName() + " :: " + logPrefix + " :: " + msg);
    }

    public static void log_debug(String msg) {
        if (!debug)
            return;
        log_stdio("debug :: " + msg);
    }

    public static void log_stderr(String msg) {
        System.err.println(getDateTimePrefix() + " :: " + Thread.currentThread().getName() + " :: " + logPrefix + " :: " + msg);
    }

}
