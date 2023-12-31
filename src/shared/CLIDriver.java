package shared;

import network.debug.ConsolePlayer;
import logic.*;
import network.NetworkPlayer;
import network.ServerMode;

import java.io.IOException;
import java.util.Scanner;

import static network.internal.Util.*;


/**
 * driver class for testing purposes only
 * only used as main entry point when debugging networking
 * to start as server, use args: player=human network=online server=host
 * to start as client, use args: player=human network=online server=client
 */
public final class CLIDriver {
    public static final Scanner scanner = new Scanner(System.in);

    private CLIDriver() {
    }

    public static void main(String[] args) throws IOException {
        log_debug("this is a test class, use this only to test classes");

        if (args.length == 0) {
            log_stderr("missing arguments");
            log_stderr("valid args are: player=[human,ai], server=[host,client], network=[online/offline], enemy=[human/ai], address=$address, port=$port, semester=[1..6]");
            System.exit(1);
        }

        PlayerMode playerMode = null;
        NetworkMode networkMode = null;
        LocalEnemyMode localEnemyMode = null;
        ServerMode serverMode = null;
        String addr = defaultAddress;
        int port = defaultPort;
        int semester = 1;

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
            } else if (arg.startsWith("port=")) {
                port = Integer.parseInt(arg.substring("port=".length()));
            } else if (arg.startsWith("server=")) {
                if (arg.endsWith("host")) {
                    serverMode = ServerMode.SERVER;
                } else if (arg.endsWith("client")) {
                    serverMode = ServerMode.CLIENT;
                }
            } else if (arg.startsWith("semester=")) {
                semester = Integer.parseInt(arg.substring("semester=".length()));
            } else if (arg.contains("debug")) {
                shared.Util.debug = true;
            }
        }

        NetworkPlayer player = null;
        if (playerMode == PlayerMode.HUMAN) {
            player = new ConsolePlayer("Console Player", semester);
            player.establishConnection(serverMode, addr, port);
        } else if (playerMode == PlayerMode.COMPUTER) {
            player = new AIPlayer(semester);
            player.establishConnection(serverMode, addr, port);
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
