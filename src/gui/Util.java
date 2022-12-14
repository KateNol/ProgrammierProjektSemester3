package gui;


public class Util {

    // log
    private static final String logPrefix = "GUI";

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
