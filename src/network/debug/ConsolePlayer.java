package network.debug;


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

    public ConsolePlayer(ServerMode serverMode) throws IOException {
        super(serverMode);

        inputLoop();
    }

    public ConsolePlayer(ServerMode serverMode, String address) throws IOException {
        super(serverMode, address);

        inputLoop();
    }

    private void inputLoop() {
        new Thread(() -> {
            while (true) {
                String input;

                input = scanner.nextLine();
                sendMessage(input);
            }
        }).start();
    }
}
