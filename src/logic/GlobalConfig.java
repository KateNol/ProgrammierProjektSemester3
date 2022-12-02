package logic;

import java.util.ArrayList;
import java.util.Arrays;

import static logic.Util.log_debug;

/**
 * This class contains all possible sizes of ships and also the map size
 */
public class GlobalConfig {
    private final int[][] ships = {{2, 2, 2, 2, 4, 6}, {2, 2, 2, 2, 2}, {2, 2, 2, 2, 4, 6}, {2, 2, 2, 2, 4, 6}, {2, 2, 2, 3, 3, 6}, {2, 1, 1, 1, 6}};
    private final int mapSize = 14;

    /**
     * Constructor for the GlobalConfig class
     */
    public GlobalConfig() {
    }

    /**
     *
     * @param commonSemester the semester in which both players are
     * @return the mapSize according to the semester
     */
    public int getMapSize(int commonSemester) {
        return mapSize + commonSemester - 1;
    }

    /**
     * create Array of ships by commonSemester
     * @param commonSemester the semester in which both players are
     * @return neuShip the size of ships the player needs
     */
    public int[] getShipSizes(int commonSemester) {
        return ships[commonSemester - 1];
    }

    /**
     *
     * @param commonSemester the semester in which both players are
     * @return a list of ships sizes which the players need
     */
    public ArrayList<Ship> getShips(int commonSemester) {
        ArrayList<Ship> shipHarbour = new ArrayList<>();

        for (int size : getShipSizes(commonSemester)) {
            shipHarbour.add(new Ship(size));
        }

        log_debug("getShips() " + commonSemester + " -> " + (shipHarbour));
        return shipHarbour;
    }
}
