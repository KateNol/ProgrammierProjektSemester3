package logic;

import java.util.ArrayList;
import java.util.Observable;

import static logic.Util.mapStateToChar;
import static network.internal.Util.log_debug;


public abstract class Player extends Observable {
    private String username;
    private int maxSemester;

    private int[] shipSizes;
    private int mapSize;
    private boolean globalConfigLoaded;

    protected GlobalConfig globalConfig;

    private ArrayList<Ship> ships = null; // List of ships the player has
    protected Map myMap = null; // own map, that contains the state of the ships and the shots the enemy took
    protected Map enemyMap = null; // enemy map, contains information about whether the shot was a hit or miss


    public Player(PlayerConfig playerConfig, GlobalConfig globalConfig) {
        if (playerConfig != null && globalConfig != null) {
            maxSemester = playerConfig.getMaxSemester();
            username = playerConfig.getUsername();
        }

        this.globalConfig = globalConfig;
        loadGlobalConfig();
        globalConfigLoaded = false;
    }

    /**
     * Stored in GlobalConfig are baseinformations about the level the players play
     * this method loads them and initializes the maps
     */
    public void loadGlobalConfig() {
        shipSizes = globalConfig.getShipSizes(1 /*getCommonSemester()*/);
        mapSize = globalConfig.getMapSize(1 /*getCommonSemester()*/);
        ships = new ArrayList<Ship>(shipSizes.length);
        myMap = new Map(mapSize);
        enemyMap = new Map(mapSize);

        globalConfigLoaded = true;
    }

    /**
     * returns true when both players are connected
     * implemented by NetworkPlayer
     *
     * @return
     */
    public abstract boolean getIsConnected();

    /**
     * returns the highest semester both players can play in
     * a call to this is only valid if getIsConnected() returns true
     * @return
     */
    public abstract int getCommonSemester();

    /**
     * gets our username
     * implemented by NetworkPlayer
     * @return
     */
    public abstract String getUsername();

    protected int[] getShipSizes() {
        return shipSizes;
    }

    /**
     * method for setting ships on the map. Helpermethods for this method is the addShip(...)-Method
     * has to be implemented by ai/gui-player
     */
    protected abstract void setShips();

    /**
     * Creates a ship with check, if the position is legal nd adds it either to the ships-Array and to the Map
     * @param size int
     * @param pivot Coordinate
     * @param alignment Alignment
     */

    protected void addShip(int size, Coordinate pivot, Alignment alignment) {
        Coordinate[] position = createArray(size, pivot, alignment);
        //Has to be done once, otherwise it gets NullPointerException
        ships.add(new Ship(position));
        for(Coordinate c: position) {
            myMap.setState(c, MapState.S);
        }
        if(checkLegal(position)) {
            ships.add(new Ship(position));
            for(Coordinate c: position) {
                myMap.setState(c, MapState.S);
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
                    position[i] = new Coordinate(pivot.row()-i,pivot.col());
                    break;
                case VERT_DOWN:
                    position[i] = new Coordinate(pivot.row()+i,pivot.col());
                    break;
                case HOR_RIGHT:
                    position[i] = new Coordinate(pivot.row(),pivot.col()+i);
                    break;
                case HOR_LEFT:
                    position[i] = new Coordinate(pivot.row(),pivot.col()-i);
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
                    if(position[i].row() == s.getPoint(n).row()) {
                        if(position[i].col() == s.getPoint(n).col())
                            check = false;
                    }
                }
            }
        }
        if(check) {
            for (Coordinate c: position) {
                if(c.col() >= myMap.getMapSize() && c.row() >= myMap.getMapSize()) {
                    check = false;
                }
            }
        }
        return check;
    }

    /**
     * Updates Enemymap, getting the result of the shot from getInput.
     * @param c Coordinate the shot was set
     * @param res shotResult, the result of the shot (either hit or miss, or sunk)
     */
    public void updateMapState(Coordinate c, ShotResult res) {
        switch(res) {
            case HIT -> {enemyMap.setState(c, MapState.H);}
            case MISS -> {enemyMap.setState(c, MapState.M);}
            case SUNK -> {enemyMap.setState(c, MapState.H); shipSunk(c);}
        }
    }

    /**
     * helper for updateMapState(...) to handle the changes on enemymap in case the response is sunk
     * @param c
     */
    private void shipSunk(Coordinate c) {
        //TODO update every coordinate of this ship to MapState.D
        log_debug("Pathfinding shipSunk");
        if(enemyMap.getState(c) == MapState.H) {
            enemyMap.setState(c, MapState.D);
            shipSunk(new Coordinate(c.row()-1, c.col())); //look west
            shipSunk(new Coordinate(c.row(), c.col()-1)); //look north
            shipSunk(new Coordinate(c.row()+1, c.col())); //look east
            shipSunk(new Coordinate(c.row(), c.col()+1)); //look south
        }
    }


    /**
     * requests a coordinate from the player
     * has to be implemented by GUIPlayer/AIPlayer/ConsolePlayer
     * @return the point where your player wants to shoot
     */
    public abstract Coordinate getShot();

    /**
     * asks the player to send our shot to the enemy player
     * implemented by NetworkPlayer
     * @param coordinate
     */
    public abstract void sendShot(Coordinate coordinate);

    /**
     * call when we have evaluated a shot from the enemy (received by notify())
     * sends the result to the enemy shot back to the enemy
     * implemented by NetworkPlayer
     * @param shotResult
     */
    public abstract void sendShotResponse(ShotResult shotResult);

    /**
     * this method will be called by Logic when a shot was received via notify()
     * may be overwritten by GUIPlayer/AIPlayer/ConsolePlayer
     * @param shot
     * @return
     */
    public ShotResult receiveShot(Coordinate shot) {
        // TODO look up actual result in map
        ShotResult shotResult = ShotResult.MISS;
        myMap.setState(shot, MapState.M);
        Ship destroyedShip = null;
        for (Ship s : ships) {
            int shipHealth = -1;
            if (s.checkIfHit(shot)) {
                shipHealth = s.decreaseHealth();
                myMap.setState(shot, MapState.H);
                shotResult =  ShotResult.HIT;
            }
            if (shipHealth == 0) {
                for(Coordinate c: s.getPos()) {
                    myMap.setState(c, MapState.D);
                }
                destroyedShip = s;
                shotResult = ShotResult.SUNK;
            }
        }
        ships.remove(destroyedShip);
        return shotResult;
    }

    protected void printBothMaps() {
        int mapSize = globalConfig.getMapSize(getCommonSemester());
        System.out.print("My Map:");
        // right padding
        for (int i=0; i<mapSize*2-"My Map:".length(); i++) {
            System.out.print(" ");
        }
        System.out.println("\t  Enemy Map:");

        for (int i=0; i<mapSize; i++) {
            // our map
            for (int j=0; j<myMap.getMapSize(); j++) {
                System.out.print(mapStateToChar(myMap.getMap()[i][j]));
                System.out.print(" ");
            }

            // padding
            System.out.print(" |" + i%10 + "|  ");

            // enemy map
            for (int j=0; j<enemyMap.getMapSize(); j++) {
                System.out.print(mapStateToChar(enemyMap.getMap()[i][j]));
                System.out.print(" ");
            }

            System.out.println();
        }

        for (int i=0; i<myMap.getMapSize(); i++) {
            System.out.print(i%10 + "|");
        }
        System.out.print("  +   ");
        for (int i=0; i<myMap.getMapSize(); i++) {
            System.out.print(i%10 + "|");
        }
        System.out.println();
    }

}
