package logic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
