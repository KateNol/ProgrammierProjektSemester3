package network.debug;


import network.NetworkMode;
import network.NetworkPlayer;

import java.io.IOException;

import static network.debug.Driver.scanner;

/**
 * console player class for testing purposes only
 * implements a player that gets all input/output from/to stdio
 * input is non-blocking
 */
public final class ConsolePlayer extends NetworkPlayer {

    public ConsolePlayer(NetworkMode networkMode) throws IOException {
        super(networkMode);

        inputLoop();
    }

    public ConsolePlayer(NetworkMode networkMode, String address) throws IOException {
        super(networkMode, address);

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
