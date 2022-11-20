package logic;

import network.NetworkPlayer;
import network.ServerMode;

import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class AIPlayer extends NetworkPlayer {

    private final Set<Coordinate> previousShots = new HashSet<>();

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
        addShip(2, new Coordinate(5, 4), Alignment.VERT_DOWN);
        addShip(2, new Coordinate(12, 2), Alignment.VERT_DOWN);
        addShip(2, new Coordinate(8, 6), Alignment.HOR_LEFT);
        addShip(2, new Coordinate(4, 8), Alignment.VERT_UP);
        addShip(4, new Coordinate(9, 9), Alignment.VERT_DOWN);
        addShip(6, new Coordinate(4, 13), Alignment.VERT_DOWN);
    }

    /**
     * @return
     */
    @Override
    public Coordinate getShot() {
        System.out.println("our turn! state of the game:");
        printBothMaps();
        Random random = new Random();

        Coordinate shot = new Coordinate(random.nextInt(0, globalConfig.getMapSize(getNegotiatedSemester())), random.nextInt(0, globalConfig.getMapSize(getNegotiatedSemester())));

        while (previousShots.contains(shot)) {
            shot = new Coordinate(random.nextInt(0, globalConfig.getMapSize(getNegotiatedSemester())), random.nextInt(0, globalConfig.getMapSize(getNegotiatedSemester())));
        }

        previousShots.add(shot);

        return shot;
    }
}
