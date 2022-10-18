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

        System.out.println("enter mode: \n\t'C'lient\n\t'S'erver");

        String in = scanner.nextLine();

        if (in.toLowerCase(Locale.ROOT).equals("c")) {
            System.out.println("Client mode");
            System.out.println("enter target address");
            in = scanner.nextLine();
            String ip = in.equals("") ? "localhost" : in;
            System.out.println("enter target port");
            in = scanner.nextLine();
            int port = in.equals("") ? 21033 : Integer.parseInt(in);

            System.out.println("Starting client with " + ip + ":" + port);
            NetworkPlayer player = new NetworkPlayer(Mode.CLIENT, "localhost");

        } else if (in.toLowerCase(Locale.ROOT).equals("s")) {
            System.out.println("Server mode");
            System.out.println("enter port");
            in = scanner.nextLine();
            int port = in.equals("") ? 21033 : Integer.parseInt(in);

            System.out.println("Starting server with port " + port);
            NetworkPlayer player = new NetworkPlayer(Mode.SERVER, null);

        }
    }
}
