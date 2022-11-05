package logic;

import network.NetworkPlayer;
import network.ServerMode;

import java.io.IOException;
import java.util.Random;

public class AIPlayer extends NetworkPlayer {


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

    /**
     * @return
     */
    @Override
    public Coordinate getShot() {
        Random random = new Random();
        return new Coordinate(random.nextInt(), random.nextInt());
    }
}
