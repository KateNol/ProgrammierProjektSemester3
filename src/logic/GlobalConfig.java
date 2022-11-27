package logic;

import java.util.ArrayList;

/**
 * This class contains all possible sizes of ships and also the map size
 */
public class GlobalConfig {
    private final int ships[][] = {{2, 2, 2, 2, 4, 6}, {2, 2, 2, 2, 2}, {2, 2, 2, 2, 4, 6}, {2, 2, 2, 2, 4, 6}, {2, 2, 2, 3, 3, 6}, {2, 1, 1, 1, 6}};
    private int  mapSize = 14;

    /**
     *  Constructor for the GlobalConfig class
     */
    public GlobalConfig() {
    }

    /**
     *
     * @param commonSemester the semester in which both players are
     * @return the mapSize according to the semester
     */
    public int getMapSize(int commonSemester) {
        mapSize += commonSemester - 1;
        return mapSize;
    }

    /**
     * create Array of ships by commonSemester
     * @param commonSemester the semester in which both players are
     * @return neuShip the size of ships the player needs
     */
    public int[] getShipSizes(int commonSemester) {
        int neuShip[] = new int[ships[commonSemester - 1].length];
        for (int i = 0; i < ships[commonSemester - 1].length; i++) {
            neuShip[i] = ships[commonSemester - 1][i];
        }
        return neuShip;
    }

    /**
     *
     * @param commonSemester the semester in which both players are
     * @return a list of ships sizes which the players need
     */
    public ArrayList<Ship> getShips(int commonSemester) {
        ArrayList<Ship> shipHarbour = new ArrayList<>();
        for (int i = 0;i < getShipSizes(commonSemester).length; i++) {
            shipHarbour.add(new Ship(ships[commonSemester - 1][i]));
        }
        return shipHarbour;
    }
}
