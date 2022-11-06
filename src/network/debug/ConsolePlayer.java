package network.debug;


import logic.Coordinate;
import logic.GlobalConfig;
import logic.PlayerConfig;
import logic.ShotResult;
import network.ServerMode;
import network.NetworkPlayer;

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


    /**
     * @return
     */
    @Override
    public Coordinate getShot() {
        System.out.println("Enter a Move: ");
        int col = scanner.nextInt();
        int row = scanner.nextInt();
        return new Coordinate(row, col);
    }
}
