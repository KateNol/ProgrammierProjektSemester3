package logic;

import network.NetworkPlayer;
import network.ServerMode;

import java.io.IOException;
import java.util.Random;

import static logic.Util.getCoordinateInBounds;
import static logic.Util.getSurroundingCoordinates;

public class AIPlayer2 extends NetworkPlayer {
    public AIPlayer2(PlayerConfig playerConfig, GlobalConfig globalConfig, ServerMode serverMode, String address, int port) throws IOException {
        super(playerConfig, globalConfig, serverMode, address, port);
    }

    public AIPlayer2(PlayerConfig playerConfig, GlobalConfig globalConfig, ServerMode serverMode, String address) throws IOException {
        super(playerConfig, globalConfig, serverMode, address);
    }

    public AIPlayer2(PlayerConfig playerConfig, GlobalConfig globalConfig, ServerMode serverMode, int port) throws IOException {
        super(playerConfig, globalConfig, serverMode, port);
    }

    public AIPlayer2(PlayerConfig playerConfig, GlobalConfig globalConfig, ServerMode serverMode) throws IOException {
        super(playerConfig, globalConfig, serverMode);
    }

    @Override
    protected void setShips() {
        Random random = new Random();
        int mapSize = globalConfig.getMapSize(getCommonSemester());

        for (int shipSize : globalConfig.getShipSizes(getCommonSemester())) {
            boolean placed = true;
            Coordinate randomCoordinate = getRandomCoordinate();
            Alignment randomAlignment = getRandomAlignment();
            do {
                randomCoordinate = getRandomCoordinate();
                randomAlignment = getRandomAlignment();
                int deltax = randomAlignment == Alignment.HOR_RIGHT ? 1 : randomAlignment == Alignment.HOR_LEFT ? -1 : 0;
                int deltay = randomAlignment == Alignment.VERT_DOWN ? 1 : randomAlignment == Alignment.VERT_UP ? -1 : 0;

                for (int k = 0; k < shipSize; k++) {
                    Coordinate shipTile = new Coordinate(randomCoordinate.row() + deltax*k, randomCoordinate.col() + deltay*k);
                    if (!getCoordinateInBounds(shipTile, mapSize))
                        placed = false;
                    for (Coordinate surrounding : getSurroundingCoordinates(shipTile, mapSize)) {
                        if (myMap.getState(surrounding) != MapState.W) {
                            placed = false;
                        }
                    }
                }
            } while (!placed);
            addShip(shipSize, randomCoordinate, randomAlignment);
        }
    }

    @Override
    public Coordinate getShot() {
        return null;
    }

    private Coordinate getRandomCoordinate() {
        Random random = new Random();
        return new Coordinate(random.nextInt(0, globalConfig.getMapSize(getCommonSemester())), random.nextInt(0, globalConfig.getMapSize(getCommonSemester())));
    }

    private Alignment getRandomAlignment() {
        Random random = new Random();
        int rand = random.nextInt(0, 4);

        if (rand == 0)
            return Alignment.HOR_LEFT;
        if (rand == 1)
            return Alignment.HOR_RIGHT;
        if (rand == 2)
            return Alignment.VERT_UP;
        return Alignment.VERT_DOWN;
    }
}
