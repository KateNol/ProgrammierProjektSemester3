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
        Coordinate bullet;

        do {
            bullet = new Coordinate(random.nextInt(size), random.nextInt(size));
        } while (Moves.contains(bullet));

        Moves.add(bullet);
        return bullet;
    }
}
