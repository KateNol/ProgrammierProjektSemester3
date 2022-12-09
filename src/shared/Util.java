package shared;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Util {

    public static final boolean debug = true;

    private static final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    private static String getDateTimePrefix() {
        return dtFormatter.format(LocalDateTime.now());
    }

    private static final String delimiter = " :: ";

    private static String buildLogString(String prefix, String msg) {
        String threadPadded = String.format("%-" + 25 + "s", Thread.currentThread().getName());
        String prefixPadded = String.format("%-" + 15 + "s", prefix);

        return getDateTimePrefix() + delimiter + threadPadded + delimiter + prefixPadded + delimiter + msg;
    }

    public static void log_stdio(String prefix, String msg) {
        System.out.println(buildLogString(prefix, msg));
    }

    public static void log_debug(String prefix, String msg) {
        if (!debug)
            return;
        log_stdio(prefix + " (debug)", msg);
    }

    public static void log_stderr(String prefix, String msg) {
        System.err.println(buildLogString(prefix, msg));
    }

}
