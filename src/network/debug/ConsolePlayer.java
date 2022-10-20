package network.debug;


import network.NetworkMode;
import network.NetworkPlayer;

import java.io.IOException;

import static network.debug.Driver.scanner;

/**
 * console player class for testing purposes only
 */
final class ConsolePlayer extends NetworkPlayer {

    public ConsolePlayer(NetworkMode networkMode) throws IOException {
        super(networkMode);

        inputLoop();
    }

    private void inputLoop() {
        String input;
        while (true) {
            input = scanner.nextLine();
            sendMessage(input);
        }
    }
}
