package logic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Util {

    // log
    private static final String logPrefix = "Logic";

    public static void log_stdio(String msg) {
        shared.Util.log_stdio(logPrefix, msg);
    }

    public static void log_debug(String msg) {
        shared.Util.log_debug(logPrefix, msg);
    }

    public static void log_stderr(String msg) {
        shared.Util.log_stderr(logPrefix, msg);
    }
    public static char mapStateToChar(MapState mapState) {
        switch (mapState) {
            case W -> {
                return '~';
            }
            case S -> {
                return 'S';
            }
            case H -> {
                return 'x';
            }
            case M -> {
                return 'o';
            }
            case D -> {
                return 'X';
            }
        }
        return '.';
    }

}
