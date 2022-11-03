package logic;

import network.NetworkPlayer;
import network.ServerMode;

import java.io.IOException;
import java.util.Random;

public class AIPlayer extends NetworkPlayer {

    public AIPlayer(ServerMode serverMode, String address, int port) throws IOException {
        super(serverMode, address, port);
    }

    public AIPlayer(ServerMode serverMode, String address) throws IOException {
        super(serverMode, address);
    }

    public AIPlayer(ServerMode serverMode, int port) throws IOException {
        super(serverMode, port);
    }

    public AIPlayer(ServerMode serverMode) throws IOException {
        super(serverMode);
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
