package network.debug;

import network.NetworkMode;
import network.NetworkPlayer;
import network.internal.Util;


import java.io.IOException;
import java.net.InetAddress;
import java.util.Locale;
import java.util.Scanner;


/**
 * driver class for testing purposes only
 * only used as main entry point when debugging networking
 */
public final class Driver {
    public static final Scanner scanner = new Scanner(System.in);

    private Driver() {
    }

    public static void main(String[] args) throws IOException {

        System.out.println("this is a test class, use this only to test network classes");

        System.out.println("enter mode: \n\t'C'lient\n\t'S'erver");

        String in;
        if (args.length > 0) {
            in = args[0];
        } else {
            in = scanner.nextLine();
        }

        switch (in.toLowerCase(Locale.ROOT)) {
            case "c" -> {
                System.out.println("Client mode");

                //NetworkPlayer player = new ConsolePlayer(NetworkMode.CLIENT, "www.jannik-rosendahl.com");
                NetworkPlayer player = new ConsolePlayer(NetworkMode.CLIENT);
            }
            case "s" -> {
                System.out.println("Server mode");
                Util.implementedProtocolVersion = 1;
                NetworkPlayer player = new ConsolePlayer(NetworkMode.SERVER);
            }
        }
    }
}
