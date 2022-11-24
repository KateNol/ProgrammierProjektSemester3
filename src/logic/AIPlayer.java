package logic;

import network.NetworkPlayer;
import network.ServerMode;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIPlayer extends NetworkPlayer {


    int size = globalConfig.getMapSize(1);
    List<Coordinate> Moves = new ArrayList<Coordinate>(); //Stores the moves which were already used
    List<Coordinate> HitPoints = new ArrayList<Coordinate>(); //Stores the Points which hit a ship




    public AIPlayer(PlayerConfig playerConfig, GlobalConfig globalConfig, ServerMode serverMode, String address, int port) throws IOException {
        super(playerConfig, globalConfig, serverMode, address, port);
    }

    public AIPlayer(PlayerConfig playerConfig, GlobalConfig globalConfig, ServerMode serverMode, String address) throws IOException {
        super(playerConfig, globalConfig, serverMode, address);
    }

    public AIPlayer(PlayerConfig playerConfig, GlobalConfig globalConfig, ServerMode serverMode, int port) throws IOException {
        super(playerConfig, globalConfig, serverMode, port);
    }

    public AIPlayer(PlayerConfig playerConfig, GlobalConfig globalConfig, ServerMode serverMode) throws IOException {
        super(playerConfig, globalConfig, serverMode);
    }

    @Override
    protected void setShips() {

    }

    /**
     * @return
     */
    @Override
    public Coordinate getShot() {
        Random random = new Random();
        Coordinate bullet = null; //return this coordinate
        boolean randomShot = true; //if no ship found, shoot randomly
        for (int row = 0; row < size; row++) {  //iterate through enemy map: rows
            for (int col = 0; col < size; col++) {  //iterate through enemy map: columns
                if(enemyMap.getState(new Coordinate(row, col))== MapState.S) { //search for ships
                    randomShot = false; //Ship was found, so no random shot
                    bullet = cleverShot((new Coordinate(row, col))); //let algorithm decide the shot
                    break;
                }
            }
        }
        if(randomShot) { //if true, then no ship is found and the shot is placed randomly
            do {
                bullet = new Coordinate(random.nextInt(size), random.nextInt(size));
            } while (Moves.contains(bullet));
        }
        if (bullet != null) { //no idea why there is an error without the if statement :D
            Moves.add(bullet);
        }
        return bullet;
    }

    Coordinate cleverShot(Coordinate orientation) {
        /** todo: implement algorithm for precise shooting */
        return getShot();
    }


}
