package network.debug;

import internal.LocalEnemyMode;
import internal.NetworkMode;
import internal.PlayerMode;
import logic.*;
import network.NetworkPlayer;
import network.ServerMode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import static network.internal.Util.log_debug;
import static network.internal.Util.log_stderr;


/**
 * driver class for testing purposes only
 * only used as main entry point when debugging networking
 * to start as server, use args: player=human network=online server=host
 * to start as client, use args: player=human network=online server=client
 */
public final class Driver {
    public static final Scanner scanner = new Scanner(System.in);

    private Driver() {
    }

    public static void main(String[] args) throws IOException {
        log_debug("this is a test class, use this only to test classes");

        String mode = "";

        if (args.length == 0) {
            log_stderr("missing arguments");
            System.exit(1);
        }

        PlayerMode playerMode = null;
        NetworkMode networkMode = null;
        LocalEnemyMode localEnemyMode = null;
        ServerMode serverMode = null;
        String addr = null;

        for (String arg : args) {
            arg = arg.toLowerCase();
            if (arg.startsWith("player=")) {
                if (arg.endsWith("human")) {
                    playerMode = PlayerMode.HUMAN;
                } else if (arg.endsWith("ai")) {
                    playerMode = PlayerMode.COMPUTER;
                }
            } else if (arg.startsWith("network=")) {
                if (arg.endsWith("online")) {
                    networkMode = NetworkMode.ONLINE;
                } else if (arg.endsWith("offline")) {
                    networkMode = NetworkMode.OFFLINE;
                    addr = "localhost";
                }
            } else if (arg.startsWith("enemy=")) {
                if (arg.endsWith("human")) {
                    localEnemyMode = LocalEnemyMode.HUMAN;
                } else if (arg.endsWith("ai")) {
                    localEnemyMode = LocalEnemyMode.COMPUTER;
                }
            } else if (arg.startsWith("address=")) {
                addr = arg.substring("address=".length());
            } else if (arg.startsWith("server=")) {
                if (arg.endsWith("host")) {
                    serverMode = ServerMode.SERVER;
                } else if (arg.endsWith("client")) {
                    serverMode = ServerMode.CLIENT;
                }
            }
        }

        NetworkPlayer player = null;
        // TODO this info will come from the gui later on
        String username = serverMode == ServerMode.SERVER ? "Server" : "Client";

        if (playerMode == PlayerMode.HUMAN) {
            player = new ConsolePlayer(new PlayerConfig(username));
            player.establishConnection(serverMode, addr);
        } else if (playerMode == PlayerMode.COMPUTER) {
            player = new AIPlayer(new PlayerConfig(username));
            player.establishConnection(serverMode, addr);
        }
        assert player != null;
        Logic logic = new Logic(player);
        // if we are server and local play is enabled, spawn enemy player ourselves
        // note: if local play is disabled, e.g. networkMode==ONLINE, then the other player has to connect from another process/pc/network
        if (networkMode == NetworkMode.OFFLINE && serverMode == ServerMode.SERVER) {
            LocalEnemyMode finalLocalEnemyMode = localEnemyMode;
            Thread enemyThread = new Thread(() -> {
                log_debug("starting new player thread");
                String enemy;
                if (finalLocalEnemyMode == LocalEnemyMode.HUMAN) {
                    enemy = "human";
                } else {
                    enemy = "ai";
                }
                String[] new_args = new String[]{"player=" + enemy, "server=client", "network=offline"};
                try {
                    main(new_args);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            enemyThread.setDaemon(true);
            enemyThread.setName("Enemy Thread");
            enemyThread.start();
        }

    }
}
