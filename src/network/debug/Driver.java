package network.debug;

import network.Mode;
import network.NetworkPlayer;


import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;


/**
 * driver class for testing purposes only
 */
public final class Driver {
    private Driver() {
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("this is a test class, use this only to test network classes");

        System.out.println("enter mode: \n\t'C'lient\n\t'S'erver\n\tconso'L'e");

        String in = scanner.nextLine();

        switch (in.toLowerCase(Locale.ROOT)) {
            case "c" -> {
                System.out.println("Client mode");
                NetworkPlayer player = new NetworkPlayer(Mode.CLIENT);

                break;
            }
            case "s" -> {
                System.out.println("Server mode");
                NetworkPlayer player = new NetworkPlayer(Mode.SERVER);

                break;
            }
            case "l" -> {
                System.out.println("Console mode");

                ConsolePlayer player = new ConsolePlayer();
                break;
            }
        }
    }
}
