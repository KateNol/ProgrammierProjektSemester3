package gui;


import logic.MapState;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Util {

    private static final boolean debug = true;

    private static final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    private static String getDateTimePrefix() {
        return dtFormatter.format(LocalDateTime.now());
    }

    private static final String logPrefix = "GUI";

    public static void log_stdio(String msg) {
        System.out.println(getDateTimePrefix() + " :: " + Thread.currentThread().getName() + " :: " + logPrefix + " :: " + msg);
    }

    public static void log_debug(String msg) {
        if (!debug)
            return;
        log_stdio("debug :: " + msg);
    }

    public static void log_stderr(String msg) {
        System.err.println(getDateTimePrefix() + logPrefix + msg);
    }


}
