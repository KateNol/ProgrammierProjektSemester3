package network.debug;


import network.Mode;
import network.NetworkPlayer;

import java.io.IOException;

import static network.debug.Driver.scanner;

/**
 * console player class for testing purposes only
 */
final class ConsolePlayer extends NetworkPlayer {

    public ConsolePlayer(Mode mode) throws IOException {
        super(mode);

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
