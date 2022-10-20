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
    public static final Scanner scanner = new Scanner(System.in);

    private Driver() {
    }

    public static void main(String[] args) throws IOException {

        System.out.println("this is a test class, use this only to test network classes");

        System.out.println("enter mode: \n\t'C'lient\n\t'S'erver");

        String in = scanner.nextLine();

        switch (in.toLowerCase(Locale.ROOT)) {
            case "c" -> {
                System.out.println("Client mode");
                NetworkPlayer player = new ConsolePlayer(Mode.CLIENT);
            }
            case "s" -> {
                System.out.println("Server mode");
                NetworkPlayer player = new ConsolePlayer(Mode.SERVER);
            }
        }
    }
}
