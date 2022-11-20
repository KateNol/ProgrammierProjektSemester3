package logic;

import java.util.ArrayList;

/**
 * return values are just for debug
 */


public class GlobalConfig {
    private final int ships[][] = {{2, 2, 2, 2, 4, 6}, {2, 2, 2, 2, 2}, {2, 2, 2, 2, 4, 6}, {2, 2, 2, 2, 4, 6}, {2, 2, 2, 3, 3, 6}, {2, 1, 1, 1, 6}};
    private int  mapSize = 14;


    public GlobalConfig() {
    }


    public int getMapSize(int commonSemester) {
        mapSize  += commonSemester - 1;
        return mapSize;
    }

    public int[] getShipSizes(int commonSemester) {
        int neuShip[] = new int[ships[commonSemester - 1].length];
        for (int i = 0; i < ships[commonSemester - 1].length; i++) {
            neuShip[i] = ships[commonSemester - 1][i];
        }
        return neuShip;
    }

    public ArrayList<Ship> getShips(int commonSemester){
        ArrayList<Ship> shipsArray = new ArrayList<>();

        for(int i = 0; i < getShipSizes(commonSemester).length; i++){
            shipsArray.add(new Ship(ships[commonSemester - 1][i]));
        }
        return shipsArray;
    }
}
