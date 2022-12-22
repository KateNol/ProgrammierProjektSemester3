package network.debug;


import logic.*;
import network.NetworkPlayer;

import java.util.ArrayList;
import java.util.Random;

import static logic.Util.log_debug;
import static shared.CLIDriver.scanner;

/**
 * console player class for testing purposes only
 * implements a player that gets all input/output from/to stdio
 * input is non-blocking
 */
public final class ConsolePlayer extends NetworkPlayer {

    public ConsolePlayer(PlayerConfig playerConfig) {
        super(playerConfig);
    }

    public ConsolePlayer(String name, int semester) {
        this(new PlayerConfig(""));
        setUsername(name);
        setMaxSemester(semester);
    }

    /**
     * gets userinput from console to set the ships
     */
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
        setBegin();
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

    @Override
    public void receiveChatMessage(String message) {
        log_debug("received chat msg: " + message);
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
