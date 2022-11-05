package network.debug;


import logic.*;
import network.ServerMode;
import network.NetworkPlayer;

import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

import static network.debug.Driver.scanner;

/**
 * console player class for testing purposes only
 * implements a player that gets all input/output from/to stdio
 * input is non-blocking
 */
public final class ConsolePlayer extends NetworkPlayer {

    public ConsolePlayer(PlayerConfig playerConfig, GlobalConfig globalConfig, ServerMode serverMode) throws IOException {
        super(playerConfig, globalConfig, serverMode);

        inputLoop();
    }

    public ConsolePlayer(PlayerConfig playerConfig, GlobalConfig globalConfig, ServerMode serverMode, String address) throws IOException {
        super(playerConfig, globalConfig, serverMode, address);
        inputLoop();
    }

    private void inputLoop() {
        /*
        new Thread(() -> {
            while (true) {
                String input;

                input = scanner.nextLine();
                sendMessage(input);
            }
        }).start();
        */
    }


    @Override
    protected void setShips() {
        //TODO set via consoleinput
        addShip(2, new Coordinate(5,4), Alignment.VERT_DOWN);
        addShip(2, new Coordinate(12,2), Alignment.VERT_DOWN);
        addShip(2, new Coordinate(8,6), Alignment.HOR_LEFT);
        addShip(2, new Coordinate(4,8), Alignment.VERT_UP);
        addShip(4, new Coordinate(9,9), Alignment.VERT_DOWN);
        addShip(6, new Coordinate(4,13), Alignment.VERT_DOWN);
    }

    /**
     * @return
     */
    @Override
    public Coordinate getShot() {
        System.out.println("My Map:");
        for(int i = 0; i < myMap.getMapSize(); i++) {
            for(int n = 0; n < myMap.getMapSize(); n++) {
                System.out.print(myMap.getMap()[i][n]);
            }
            System.out.println();
        }
        System.out.println("Enemy Map:");
        for(int i = 0; i < enemyMap.getMapSize(); i++) {
            for(int n = 0; n < enemyMap.getMapSize(); n++) {
                System.out.print(enemyMap.getMap()[i][n]);
            }
            System.out.println();
        }
        System.out.println("Enter a Move: ");
        int col = scanner.nextInt();
        int row = scanner.nextInt();
        return new Coordinate(row, col);
    }
}
