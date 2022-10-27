package logic;

import network.NetworkMode;
import network.NetworkPlayer;

import java.io.IOException;
import java.util.ArrayList;

public class Player extends NetworkPlayer {

    public Logic logic = null;

    public int maxLevel = 1;
    public String name;
    private final ArrayList<Ship> ships = null;
    private final Map myMap = new Map();
    private final Map enemyMap = new Map();

    /**
     * Initialize player with name and empty ships for level 1 in an array
     *
     * @param name Name of the player
     */
    public Player(NetworkMode networkMode, String name) throws IOException {
        super(networkMode);
        this.name = name;
        //Initialize shipsizes for Level 1 fixed
        int[] shipSizes = {2, 2, 2, 2, 4, 6};
        for (int shipSize : shipSizes) {
            ships.add(new Ship(shipSize));
        }
    }

    public void setLogic(Logic logic) {
        if (this.logic == null) {
            this.logic = logic;
        }
    }

    /**
     * @param size size of the ship
     * @param c    coordinate of the pivotpoint
     * @param a    alignment of the ship
     * @see Alignment
     */
    public Ship createShip(int size, Coordinate c, Alignment a) {
        Ship s = new Ship(size, c, a);
        ships.add(s);
        return s;
    }

    public void setShip(Ship s) {
        ships.add(s);
    }

    //TODO check if useless
    public MapState getShot(Coordinate c) {
        switch (myMap.getState(c)) {
            case WATER:
                myMap.setState(c, MapState.MISS);
                break;
            case SHIP:
                myMap.setState(c, MapState.HIT);
                hitShip(c);
                break;
            default:
                break;
        }
        return MapState.WATER;
    }

    private MapState hitShip(Coordinate c) {
        for (Ship s : ships) {
            int shipHealth = -1;
            if (s.checkIfHit(c)) {
                shipHealth = s.decreaseHealth();
            }
            if (shipHealth == 0) {
                ships.remove(s);
                return MapState.SUNK;
            }
        }
        return MapState.HIT;
    }

    public String getName() {
        return name;
    }

    public Coordinate getInput() {
        //TODO read input
        return new Coordinate(5, 5);
    }

    public MapState updateMap(Coordinate c) {
        switch (myMap.getState(c)) {
            case WATER:
                myMap.setState(c, MapState.MISS);
                break;
            case SHIP:
                myMap.setState(c, hitShip(c));
                break;
            default:
                break;
        }
        if (ships.isEmpty()) {
            logic.gameOver(this);
        }

        return myMap.getState(c);
    }

    public void updateMapState(Coordinate c, MapState ms) {
        enemyMap.setState(c, ms);
    }
}
