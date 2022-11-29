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

    public ConsolePlayer(PlayerConfig playerConfig) {
        super(playerConfig);
    }

    /**
     * gets userinput from console to set the ships
     */
    @Override
    protected void setShips() {
        // FIXME fixed ships for debugging
        addShip(2, new Coordinate(5, 4), Alignment.VERT_DOWN);
        addShip(2, new Coordinate(12, 2), Alignment.VERT_DOWN);
        addShip(2, new Coordinate(8,6), Alignment.HOR_LEFT);
        addShip(2, new Coordinate(4,8), Alignment.VERT_UP);
        addShip(4, new Coordinate(9,9), Alignment.VERT_DOWN);
        addShip(6, new Coordinate(4,13), Alignment.VERT_DOWN);
    }

    /**
     * prints the players map and the enemy map in the console and request a shotcoordinate
     * @return returns the shotcoordinate
     */
    @Override
    public Coordinate getShot() {
        printBothMaps();
        return validInput("Enter a Move: ");
    }

    /**
     * Checks if the entered Coordinate is in range of the mapsize
     * @param output
     * @return
     */
    private Coordinate validInput(String output) {
        System.out.println(output);
        System.out.println("Row: ");
        int row = scanner.nextInt();
        System.out.println("Column: ");
        int col = scanner.nextInt();
        boolean notValid = row >= enemyMap.getMapSize() || col >= enemyMap.getMapSize();
        if(notValid) {
            do {
                System.out.println("Coordinates are not valid, try again!\n");
                System.out.println(output);
                System.out.println("Row: ");
                row = scanner.nextInt();
                System.out.println("Column: ");
                col = scanner.nextInt();
            } while(notValid);
        }
        return new Coordinate(row, col);
    }
}
