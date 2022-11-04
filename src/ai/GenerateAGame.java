package ai;

import java.io.IOException;
import java.util.Random;

public class GenerateAGame extends logic.Player extends AI {
    /**
     * Initialize player with name and empty ships for level 1 in an array
     *
     * @param networkMode
     * @param name        Name of the player
     */
    public GenerateAGame(NetworkMode networkMode, String name) throws IOException {
        super(networkMode, name);
    }


    private static char[][

    placeShips(char[][] board, int shipNumber, char water, char ship) {
        int placedShips = 0;
        int boardLength = board.length;
        while (placedShips < shipNumber) {
            int[] location = generateShipCoordinates();
            char possiblePlacement = boardLength[0][location[1]];
            if (possiblePlacement == water) {
                board[location[0]][location[1]] = ship;
            }
            return board;
        }
    }


    int[] generateShipCoordinates(int boardLength) {
        int[] coordinates = new int[2];
        for (int i = 0; i < coordinates.length; i++) {
            coordinates[i] = new Random.nextInt(boardLength);

        }
        return coordinates;
    }


}
