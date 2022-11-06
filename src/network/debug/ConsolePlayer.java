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
    }

    public ConsolePlayer(PlayerConfig playerConfig, GlobalConfig globalConfig, ServerMode serverMode, String address) throws IOException {
        super(playerConfig, globalConfig, serverMode, address);
    }

    /**
     * gets userinput from console to set the ships
     */
    @Override
    protected void setShips() {
        for(int size: getShipSizes()) {
            Coordinate pivot = validInput("Enter coordinates for ship size " + size + ":");
            boolean valid = true;
            Alignment alignment = null;
            do {
                System.out.println("Enter alignment, can be either hr (horizontal right), hl (horizontal left), vd (vertical down), vu (vertical up) ");
                System.out.println("Alignment:");
                String alignmentString = scanner.next();
                switch (alignmentString) {
                    case "hl" -> {alignment = Alignment.HOR_LEFT;}
                    case "hr" -> {alignment = Alignment.HOR_RIGHT;}
                    case "vd" -> {alignment = Alignment.VERT_DOWN;}
                    case "vu" -> {alignment = Alignment.VERT_UP;}
                    default -> {valid = false;}
                }
            } while(!valid);
            addShip(size, pivot, alignment);
            System.out.println("Your ship has been set to" + pivot.row() + " ," + pivot.col()
                    + "with the alignment" + alignment);
        }
    }

    /**
     * prints the players map and the enemy map in the console and request a shotcoordinate
     * @return returns the shotcoordinate
     */
    @Override
    public Coordinate getShot() {
        System.out.println("My Map:");
        for(int i = 0; i < myMap.getMapSize(); i++) {
            for(int n = 0; n < myMap.getMapSize(); n++) {
                System.out.print(myMap.getMap()[i][n]);
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println("Enemy Map:");
        for(int i = 0; i < enemyMap.getMapSize(); i++) {
            for(int n = 0; n < enemyMap.getMapSize(); n++) {
                System.out.print(enemyMap.getMap()[i][n]);
                System.out.print(" ");
            }
            System.out.println();
        }
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
