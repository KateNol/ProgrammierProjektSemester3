package logic;

import network.NetworkMode;
import network.NetworkPlayer;

import java.io.IOException;
import java.util.ArrayList;

public abstract class Player {

    private Logic logic = null; //for inform the logic about certain actions

    public int maxLevel = 1; //max progress the player made
    public String name; // name of the player
    private final ArrayList<Ship> ships = null; // List of ships the player has
    private final Map myMap = new Map(); // own map, that contains the state of the ships and the shots the enemy took
    private final Map enemyMap = new Map(); // enemy map, contains information about whether the shot was a hit or miss

    /**
     * empty constructor
     */
    public Player() {
    }

    /**
     * Initialize player with name and empty ships for level 1 in an array
     *
     * @param name Name of the player
     */
    public Player(String name) throws IOException {
        this.name = name;
        //TODO shipSizes maybe attribute so one can iterate over it to set ships, or iterate over ships-Array?
        //Initialize shipsizes for Level 1 fixed
        int[] shipSizes = {2, 2, 2, 2, 4, 6};
        for (int shipSize : shipSizes) {
            ships.add(new Ship(shipSize));
        }
    }

    public String getName() {
        return name;
    }

    /**
     * sets Logic to communicate with it in special situations
     * @param logic Gamelogic
     */
    public void setLogic(Logic logic) {
        if (this.logic == null) {
            this.logic = logic;
        }
    }

    /**
     * has to be implemented by inheriting class
     * routine to set the ships on the map
     */
    abstract void setShips();

    /**
     * Creates a ship with check, if the position is legal nd adds it either to the ships-Array and to the Map
     * @param size int
     * @param pivot Coordinate
     * @param alignment Alignment
     */

    private void addShip(int size, Coordinate pivot, Alignment alignment) {
        Coordinate[] position = createArray(size, pivot, alignment);
        if(checkLegal(position)) {
            ships.add(new Ship(position));
            for(Coordinate c: position) {
                myMap.setState(c, MapState.SHIP);
            }
        }
    }

    /**
     * creates an array
     * @param pivot Coordinate
     * @param alignment Alignment
     * @return created array of type Coordinate[]
     */
    private Coordinate[] createArray(int size, Coordinate pivot, Alignment alignment) {
        Coordinate[] position = new Coordinate[size];
        for(int i = 0; i < size; i++) {
            switch (alignment) {
                case VERT_UP:
                    position[i].setRow(pivot.getRow());
                    position[i].setCol(pivot.getCol()-i);
                    break;
                case VERT_DOWN:
                    position[i].setRow(pivot.getRow());
                    position[i].setCol(pivot.getCol()+i);
                    break;
                case HOR_RIGHT:
                    position[i].setRow(pivot.getRow()+i);
                    position[i].setCol(pivot.getCol());
                    break;
                case HOR_LEFT:
                    position[i].setRow(pivot.getRow()-i);
                    position[i].setCol(pivot.getCol());
                    break;
            }
        }
        return position;
    }

    /**
     *  iterates over all ships set and checks, if there's an overlapping ship
     *  and checks if any point of the ship is off map
     * @param position
     * @return result of the check as boolean
     */
    //TODO check if neighbor is also empty
    private boolean checkLegal(Coordinate[] position) {
        boolean check = true;
        for(int i = 0; i < position.length; i++) {
            for(Ship s: ships) {
               for(int n = 0; n < s.getSize(); n++) {
                   if(position[i].isEqual(s.getPoint(n))) {
                       check = false;
                   }
               }
            }
        }
        if(check) {
            for (Coordinate c: position) {
                if(c.getCol() >= myMap.getMapSize() && c.getRow() >= myMap.getMapSize()) {
                    check = false;
                }
            }
        }

        return check;
    }


    /**
     * Inheriting class has to implement this.
     * this method gets called by the logic when it's the players turn.
     * Expects a Coordinate. After returning the Coordinate the procedure of the turn goes automatically
     * @return the coordinate of the shot the Player wants to shoot at.
     */
    public abstract Coordinate getInput();

    /**
     * triggered by the logic gets the shot of the other player and returns if shot was successful or not
     * triggers gameOver, if all ships sunk
     * @param c Shotcoordinate from the other player, handled by the logic
     * @return MapState whether the shot was a hit or miss.
     */
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

    /**
     * at the moment helpermethod for updateMap(c) in case a ship got hit.
     * @param c Shotcoordinate from the other player, handled by the logic
     * @return MapState whether the ship got hit but has health left or sunk.
     */
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

    /**
     * Updates Enemymap, getting the result of the shot from getInput.
     * @param c Coordinate the shot was set
     * @param ms MapState, the result of the shot (either hit or miss, or sunk)
     */
    public void updateMapState(Coordinate c, MapState ms) {
        enemyMap.setState(c, ms);
    }
}
