package logic;

import java.util.ArrayList;

/**
 * This class contains all possible sizes of ships and also the map size
 */
public class GlobalConfig {
    private static final int[][] ships = {{2, 2, 2, 2, 4, 6}, {2, 2, 2, 2, 2}, {2, 2, 2, 2, 4, 6}, {2, 2, 2, 2, 4, 6}, {2, 2, 2, 3, 3, 6}, {2, 1, 1, 1, 6}};
    private static final int mapSize = 14;

    /**
     *  Constructor for the GlobalConfig class
     */
    private GlobalConfig() {
    }

    /**
     *
     * @param commonSemester the semester in which both players are
     * @return the mapSize according to the semester
     */
    public static int getMapSize(int commonSemester) {
        return mapSize + commonSemester - 1;
    }

    /**
     * create Array of ships by commonSemester
     * @param commonSemester the semester in which both players are
     * @return neuShip the size of ships the player needs
     */
    public static int[] getShipSizes(int commonSemester) {
        return ships[commonSemester-1];
    }

    /**
     *
     * @param commonSemester the semester in which both players are
     * @return a list of ships sizes which the players need
     */
    public static ArrayList<Ship> getShips(int commonSemester) {
        ArrayList<Ship> shipHarbour = new ArrayList<>();
        for (int size : getShipSizes(commonSemester)) {
            shipHarbour.add(new Ship(size));
        }
        return shipHarbour;
    }
}
