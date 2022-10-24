package logic_demo;

public class Player {
    int maxLevel = 1;
    String name = null;
    Ship[] ships = null; //TODO change to ArrayList
    Map map = new Map();

    /**
     * Initialize player with name and empty ships for level 1 in an array
     * @param name Name of the player
     */
    public Player(String name) {
        this.name = name;
        //Initialize shipsizes for Level 1 fixed
        int[] shipSizes = {2,2,2,2,4,6};
        for (int i = 0; i < shipSizes.length; i++) {
            ships[i] = new Ship(shipSizes[i]);
        }
    }

    /**
     *
     * @param shipNumber index of the ship in the ships array
     * @param c coordinate of the pivotpoint
     * @param a alignment of the ship
     * @see Alignment
     */
    public void setShip(int shipNumber, Coordinate c, Alignment a) {
        if(shipNumber < 6 && shipNumber >= 0) {
            ships[shipNumber].setPos(c, a);
        }
    }

    public MapState getShot(Coordinate c) {
        switch (map.getState(c)) {
            case WATER:
                map.setState(c, MapState.MISS);
                break;
            case SHIP:
                map.setState(c, MapState.HIT);
                hitShip(c);
                break;
            default:
                break;
        }
        return MapState.WATER;
    }

    private void hitShip(Coordinate c) {
        for(Ship s: ships) {
            int shipHealth = -1;
            if(s.gotHit(c)) {
                shipHealth = s.decreaseHealth();
            }
            if(shipHealth == 0) {
                //TODO delete ship from shiparray
            }
        }
    }
}
