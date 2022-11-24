package logic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Util {

    private static final boolean debug = true;

    private static final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss :: ");

    private static String getDateTimePrefix() {
        return dtFormatter.format(LocalDateTime.now());
    }

    private static final String logPrefix = "Logic :: ";

    public static void log_stdio(String msg) {
        System.out.println(getDateTimePrefix() + logPrefix + msg);
    }

    public static void log_debug(String msg) {
        if (!debug)
            return;
        log_stdio("debug :: " + msg);
    }

    public static void log_stderr(String msg) {
        System.err.println(getDateTimePrefix() + logPrefix + msg);
    }

    /**
     * returns list of all (max 8) Coordinates surrounding c
     * @param c target Coordinate
     * @param max max allowed value, set to mapsize -1
     * @return
     */
    public static List<Coordinate> getSurroundingCoordinates(Coordinate c, int mapSize) {
        List<Coordinate> result = new ArrayList<>();

        int []arr = { -1, 0, 1 };

        for (int deltax : arr) {
            for (int deltay : arr) {
                if (deltax == 0 && deltay == 0)
                    continue;

                Coordinate current = new Coordinate(c.row() + deltax, c.col() + deltay);

                if (current.row() < 0 || current.row() >= mapSize)
                    continue;
                if (current.col() < 0 || current.col() >= mapSize)
                    continue;

                result.add(current);
            }
        }

        return result;
    }

    public static boolean getCoordinateInBounds(Coordinate c, int mapSize) {
        return c.row() > 0 && c.col() > 0 && c.row() < mapSize-1 && c.col() < mapSize-1;
    }
}
