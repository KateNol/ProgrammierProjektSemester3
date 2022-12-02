package logic;

import network.ServerMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import static logic.Util.mapStateToChar;
import static network.internal.Util.log_debug;


public abstract class Player extends Observable {
    private String username;
    private int maxSemester;

    private int mapSize;
    private boolean globalConfigLoaded;

    protected GlobalConfig globalConfig = new GlobalConfig();

    private ArrayList<Ship> ships = null; // List of ships the player has
    protected Map myMap = null; // own map, that contains the state of the ships and the shots the enemy took
    protected Map enemyMap = null; // enemy map, contains information about whether the shot was a hit or miss

    private ServerMode serverMode = null;

    public Player(PlayerConfig playerConfig) {
        if (playerConfig != null) {
            maxSemester = playerConfig.getMaxSemester();
            username = playerConfig.getUsername();
        }
        loadGlobalConfig();
        globalConfigLoaded = false;
    }

    /**
     * Stored in GlobalConfig are baseinformations about the level the players play
     * this method loads them and initializes the maps
     */
    public void loadGlobalConfig() {
        mapSize = globalConfig.getMapSize(1 /*getCommonSemester()*/);
        //ships = new ArrayList<Ship>(shipSizes.length);
        ships = globalConfig.getShips(1 /*getCommomSemester()*/);
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
    public abstract boolean getIsConnectionEstablished();

    /**
     * returns the highest semester both players can play in
     * a call to this is only valid if getIsConnected() returns true
     *
     * @return
     */
    public abstract int getNegotiatedSemester();

    /**
     * gets our username
     * implemented by NetworkPlayer
     * @return
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getMaxSemester() {
        return maxSemester;
    }

    public Map getEnemyMap() {
        return enemyMap;
    }

    public void setMaxSemester(int semester) {
        this.maxSemester = semester;
    }

    public ServerMode getServerMode() {
        return serverMode;
    }

    public void setServerMode(ServerMode serverMode) {
        this.serverMode = serverMode;
    }

    public abstract boolean getWeBegin();

    /**
     * method for setting ships on the map. Helpermethods for this method is the addShip(...)-Method
     * has to be implemented by ai/gui-player
     */
    protected abstract void setShips();


    /**
     * Sets the position of the ship handed over with check if position is legal
     *
     * @param s         Ship from ArrayList ships
     * @param pivot     Coordinate of the pivotpoint
     * @param alignment Alignment the ship has
     * @return boolean true if ship successfully set on the map
     */
    public boolean addShip(Ship s, Coordinate pivot, Alignment alignment) {
        boolean check = false;
        Coordinate[] position = createArray(s.getSize(), pivot, alignment);
        if (checkLegal(position)) {
            s.setPos(position);
            for (Coordinate c : position) {
                myMap.setState(c, MapState.S);
            }
            check = true;
        }
        return check;
    }

    /**
     *
     * @return
     */
    public boolean noShipsLeft() {
        return ships.isEmpty();
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
    private boolean checkLegal(Coordinate[] position) {
        boolean check = true;
        //Check if off map
        if(check) {
            for (Coordinate c: position) {
                if(c.col() >= myMap.getMapSize() || c.row() >= myMap.getMapSize() || c.col() < 0 || c.row() < 0) {
                    check = false;
                }
            }
        }
        //Check if overlapping ship
        for(Coordinate c:position) {
            if(check) {
                check = checkSurroundings(c);
            }
        }


        return check;
    }

    /**
     * Checks if in all directions, even diagonally, there is no ship set on the map, otherwise it returns false
     * @param coordinate Coordinate on the map to check surrounding tiles for water
     * @return true if every tile surrounding the coordinate, including the coordinate itself, is water
     */
    private boolean checkSurroundings(Coordinate coordinate) {
        // only gets true if every surrounding is Water
        return checkIfWater(coordinate)
                && checkIfWater(new Coordinate(coordinate.row() - 1, coordinate.col())) // up
                && checkIfWater(new Coordinate(coordinate.row() + 1, coordinate.col())) // down
                && checkIfWater(new Coordinate(coordinate.row(), coordinate.col() - 1)) // left
                && checkIfWater(new Coordinate(coordinate.row(), coordinate.col() + 1)) // right
                && checkIfWater(new Coordinate(coordinate.row() - 1, coordinate.col() - 1)) // up-left
                && checkIfWater(new Coordinate(coordinate.row() - 1, coordinate.col() + 1)) // up-right
                && checkIfWater(new Coordinate(coordinate.row() + 1, coordinate.col() - 1)) // down-left
                && checkIfWater(new Coordinate(coordinate.row() + 1, coordinate.col() + 1)); // down-right
    }

    /**
     *
     * @param c Coordinate to check MapState
     * @return true if mapState == Water
     */
    private boolean checkIfWater(Coordinate c) {
        if(c.row() >= mapSize || c.col() >= mapSize || c.row() < 0 || c.col() < 0) return true;
        return myMap.getState(c) == MapState.W;
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
        log_debug("Pathfinding shipSunk");
        if(enemyMap.getState(c) == MapState.H) {
            enemyMap.setState(c, MapState.D);
            if (c.row() - 1 > 0)
                shipSunk(new Coordinate(c.row() - 1, c.col())); //look west
            if (c.col() - 1 > 0)
                shipSunk(new Coordinate(c.row(), c.col() - 1)); //look north
            if (c.row() + 1 < mapSize)
                shipSunk(new Coordinate(c.row() + 1, c.col())); //look east
            if (c.col() + 1 < mapSize)
                shipSunk(new Coordinate(c.row(), c.col() + 1)); //look south

            // mark all surrounding tiles as miss
            int i = c.row();
            int j = c.col();
            int mapMax = enemyMap.getMapSize() - 1;
            Coordinate coordinate;
            // up
            if (enemyMap.getState(new Coordinate(i, Math.min(j + 1, mapMax))) == MapState.W)
                enemyMap.setState(new Coordinate(i, Math.min(j + 1, mapMax)), MapState.M);
            // up right
            if (enemyMap.getState(new Coordinate(Math.min(i + 1, mapMax), Math.min(j + 1, mapMax))) == MapState.W)
                enemyMap.setState(new Coordinate(Math.min(i + 1, mapMax), Math.min(j + 1, mapMax)), MapState.M);
            // up left
            if (enemyMap.getState(new Coordinate(Math.max(i - 1, 0), Math.min(j + 1, mapMax))) == MapState.W)
                enemyMap.setState(new Coordinate(Math.max(i - 1, 0), Math.min(j + 1, mapMax)), MapState.M);
            // down
            if (enemyMap.getState(new Coordinate(i, Math.max(j - 1, 0))) == MapState.W)
                enemyMap.setState(new Coordinate(i, Math.max(j - 1, 0)), MapState.M);
            // down right
            if (enemyMap.getState(new Coordinate(Math.min(i + 1, mapMax), Math.max(j - 1, 0))) == MapState.W)
                enemyMap.setState(new Coordinate(Math.min(i + 1, mapMax), Math.max(j - 1, 0)), MapState.M);
            // down left
            if (enemyMap.getState(new Coordinate(Math.max(i - 1, 0), Math.max(j - 1, 0))) == MapState.W)
                enemyMap.setState(new Coordinate(Math.max(i - 1, 0), Math.max(j - 1, 0)), MapState.M);
            // right
            if (enemyMap.getState(new Coordinate(Math.min(i + 1, mapMax), j)) == MapState.W)
                enemyMap.setState(new Coordinate(Math.min(i + 1, mapMax), j), MapState.M);
            // left
            if (enemyMap.getState(new Coordinate(Math.max(i - 1, 0), j)) == MapState.W)
                enemyMap.setState(new Coordinate(Math.max(i - 1, 0), j), MapState.M);

        }
    }

    protected ArrayList<Ship> getArrayListShips() {return ships;}


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
    protected ShotResult receiveShot(Coordinate shot) {
        ShotResult shotResult = null;
        Ship destroyedShip = null;

        switch (myMap.getState(shot)) {
            case W -> {
                shotResult = ShotResult.MISS;
                myMap.setState(shot, MapState.M);
            }
            case S -> {
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
            }
            case H -> shotResult = ShotResult.HIT;
            case D -> shotResult = ShotResult.SUNK;
            case M -> shotResult = ShotResult.MISS;
        }

        return shotResult;
    }

    public ArrayList<Ship> getShips() {
        return ships;
    }

    public int getMapSize() {
        return myMap.getMapSize();
    }


    //FIXME delete if not needed anymore
    public void printBothMaps() {
        int mapSize = globalConfig.getMapSize(getNegotiatedSemester());
        System.out.print("My Map:");
        // right padding
        for (int i = 0; i < mapSize * 2 - "My Map:".length(); i++) {
            System.out.print(" ");
        }
        System.out.println("\t  Enemy Map:");

        for (int i = 0; i < mapSize; i++) {
            // our map
            for (int j=0; j<myMap.getMapSize(); j++) {
                System.out.print(mapStateToChar(myMap.getMap()[i][j]));
                System.out.print(" ");
            }

            // padding
            System.out.print(" |" + String.format("%02d", i) + "|  ");

            // enemy map
            for (int j=0; j<enemyMap.getMapSize(); j++) {
                System.out.print(mapStateToChar(enemyMap.getMap()[i][j]));
                System.out.print(" ");
            }

            System.out.println();
        }

        for (int i=0; i<myMap.getMapSize(); i++) {
            System.out.print(i/10 + "|");
        }
        System.out.print(" ++++  ");
        for (int i=0; i<myMap.getMapSize(); i++) {
            System.out.print(i/10 + "|");
        }
        System.out.println();
        for (int i=0; i<myMap.getMapSize(); i++) {
            System.out.print(i%10 + "|");
        }
        System.out.print(" ++++  ");
        for (int i=0; i<myMap.getMapSize(); i++) {
            System.out.print(i%10 + "|");
        }
        System.out.println();
    }

}
