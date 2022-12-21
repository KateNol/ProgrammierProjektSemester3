package logic;

import gui.GUIPlayer;
import gui.controllers.ControllerLobby;
import gui.controllers.View;
import gui.controllers.ViewSwitcher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static logic.Util.log_debug;
import static logic.Util.log_stderr;

public class GUIAIPlayer extends GUIPlayer {

    private final Set<Coordinate> previousShots = new HashSet<>();

    /**
     * Create a GuiPlayer
     *
     * @param playerConfig Save file from the player
     */
    public GUIAIPlayer(PlayerConfig playerConfig) {
        super(playerConfig);
    }

    @Override
    public Coordinate getShot() {
        System.out.println("our turn! state of the game:");
        printBothMaps();
        int mapSize = myMap.getMapSize();
        Coordinate shot = null;

        // search for hit
        Coordinate c = null;
        for (int i=0; i<mapSize; i++) {
            for (int j=0; j<mapSize; j++) {
                if (enemyMap.getState(i, j) == MapState.H) {
                    c = new Coordinate(i, j);
                    break;
                }
            }
        }

        // if hit found
        if (c != null) {
            ArrayList<Coordinate> possibleShots = new ArrayList<>();

            boolean horizontal = false;
            boolean vertical = false;
            // check if direction known
            if (enemyMap.inBounds(c.row()+1, c.col()) && enemyMap.getState(c.row()+1, c.col()) == MapState.H) {
                vertical = true;
            } else if (enemyMap.inBounds(c.row()-1, c.col()) && enemyMap.getState(c.row()-1, c.col()) == MapState.H) {
                vertical = true;
            } else if (enemyMap.inBounds(c.row(), c.col()+1) && enemyMap.getState(c.row(), c.col()+1) == MapState.H) {
                horizontal = true;
            } else if (enemyMap.inBounds(c.row(), c.col()-1) && enemyMap.getState(c.row(), c.col()-1) == MapState.H) {
                horizontal = true;
            }

            // if direction know (2 possible ways)
            if (horizontal || vertical) {
                if (horizontal) {
                    int i = c.row();
                    int j = c.col();
                    int k = 0;
                    while (enemyMap.inBounds(i, j+k) && enemyMap.getState(i, j + k) == MapState.H) {
                        k++;
                    }
                    if (enemyMap.inBounds(i, j+k) && enemyMap.getState(i, j + k) == MapState.W) {
                        possibleShots.add(new Coordinate(i, j + k));
                    } else {
                        log_debug("" + i + " " + (j+k) + " not a viable shot");
                    }
                    k = 0;
                    while (enemyMap.inBounds(i, j+k) && enemyMap.getState(i, j+k) == MapState.H) {
                        k--;
                    }
                    if (enemyMap.inBounds(i, j+k) && enemyMap.getState(i, j+k) == MapState.W) {
                        possibleShots.add(new Coordinate(i, j+k));
                    } else {
                        log_debug("" + i + " " + (j+k) + " not a viable shot");
                    }
                }
                else {
                    int i = c.row();
                    int j = c.col();
                    int k = 0;
                    while (enemyMap.inBounds(i+k, j) && enemyMap.getState(i + k, j) == MapState.H) {
                        k++;
                    }
                    if (enemyMap.inBounds(i+k, j) && enemyMap.getState(i + k, j) == MapState.W) {
                        possibleShots.add(new Coordinate(i + k, j));
                    } else {
                        log_debug("" + (i+k) + " " + j + " not a viable shot");
                    }
                    k = 0;
                    while (enemyMap.inBounds(i+k, j) && enemyMap.getState(i + k, j) == MapState.H) {
                        k--;
                    }
                    if (enemyMap.inBounds(i+k, j) && enemyMap.getState(i + k, j) == MapState.W) {
                        possibleShots.add(new Coordinate(i+k, j));
                    } else {
                        log_debug("" + (i+k) + " " + j + " not a viable shot");
                    }
                }
            }
            // else (4 possible ways)
            else {
                int i = c.row();
                int j = c.col();

                if (enemyMap.inBounds(i+1, j) && enemyMap.getState(i+1, j) == MapState.W)
                    possibleShots.add(new Coordinate(i+1, j));
                if (enemyMap.inBounds(i-1, j) && enemyMap.getState(i-1, j) == MapState.W)
                    possibleShots.add(new Coordinate(i-1, j));
                if (enemyMap.inBounds(i, j+1) && enemyMap.getState(i, j+1) == MapState.W)
                    possibleShots.add(new Coordinate(i, j+1));
                if (enemyMap.inBounds(i, j-1) && enemyMap.getState(i, j-1) == MapState.W)
                    possibleShots.add(new Coordinate(i, j-1));
            }

            if (possibleShots.size() == 0) {
                log_stderr("could not find a shot for " + c);
            }

            log_debug("found " + possibleShots.size() + " possible locations");
            log_debug("vert: " + vertical + ", hor: " + horizontal);
            log_debug(String.valueOf(possibleShots));

            Random random = new Random();
            shot = possibleShots.get(random.nextInt(possibleShots.size()));
        } else {
            // else shoot randomly
            Random random = new Random();
            shot = new Coordinate(random.nextInt(0, GlobalConfig.getMapSize(getNegotiatedSemester())), random.nextInt(0, GlobalConfig.getMapSize(getNegotiatedSemester())));
            while (previousShots.contains(shot)) {
                shot = new Coordinate(random.nextInt(0, GlobalConfig.getMapSize(getNegotiatedSemester())), random.nextInt(0, GlobalConfig.getMapSize(getNegotiatedSemester())));
            }
        }

        previousShots.add(shot);

        return shot;
    }

    @Override
    protected void setShips() {
        Random coord = new Random();
        ArrayList<Ship> ships = getArrayListShips();
        for (Ship ship : ships) {
            boolean placed = true;
            do {
                int x = coord.nextInt(myMap.getMapSize() - 1);
                int y = coord.nextInt(myMap.getMapSize() - 1);
                Coordinate coordinate = new Coordinate(x, y);
                Alignment alignment = coord.nextBoolean() ? Alignment.VERT_DOWN : Alignment.HOR_RIGHT;
                try {
                    placed = addShip(ship, coordinate, alignment);
                } catch (IllegalArgumentException e) {
                    placed = false;
                }
                if (placed) {
                    log_debug("successfully placed ship at " + coordinate + " aligned " + alignment);
                }
            } while (!placed);
        }
        ControllerLobby.getInstance().enableStartButton();
        GUIPlayer.getInstance().setBegin();
        ViewSwitcher.switchTo(View.Game);

        getGuiBoard().updateBoard();
    }
}
