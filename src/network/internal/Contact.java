package network.internal;

import network.NetworkMode;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static network.internal.MessageMode.RECEIVE;
import static network.internal.MessageMode.SEND;
import static network.internal.Util.*;

/**
 * this class communicates with the other player via a socket
 * contains a thread which is permanently listening for new messages
 * this class is not to be used by anyone outside the network package
 * player classes will use this class to send info to the other player
 * network protocol methods are named in CAPSLOCK
 */
public final class Contact {
    private Socket socket;
    private NetworkMode networkMode;

    private PrintWriter outWriter;
    private BufferedReader inReader;

    private String username;
    private String peerUsername;

    private int protocolVersion;
    private boolean protocolVersionNegotiated = false;

    private int semester;
    private boolean semesterNegotiated = false;


    Contact(Socket socket, NetworkMode networkMode, String username, int semester) throws IOException {
        this.networkMode = networkMode;
        this.username = username;
        this.semester = semester;

        // fallback as per spec
        this.protocolVersion = 1;

        if (socket != null) {
            setSocket(socket);
            init_communication();
        } else {
            this.socket = null;
        }

        System.out.println("Contact CTOR end");
    }

    Contact(Socket socket, NetworkMode networkMode) throws IOException {
        this(socket, networkMode, networkMode.name(), 1);
    }

    Contact(NetworkMode networkMode) throws IOException {
        this(null, networkMode);
    }

    private void init_communication() {
        new Thread(this::receiveLoop).start();

        // if hosted, send HELLO
        if (networkMode == NetworkMode.SERVER) {
            HELLO(SEND, implementedProtocolVersion, semester, username);
        }
    }

    private void receiveLoop() {
        try {
            while (true) {
                String input = inReader.readLine();
                if (input == null) {
                    throw new IOException("Network received null input, peer seems to have disconnected");
                }
                log_debug("received: '" + input + "'");
                input = input.toUpperCase();

                ArrayList<String> args = new ArrayList<>(List.of(input.split(String.valueOf(messageDelimiter))));
                String command = args.get(0);
                args.remove(0);

                eval(command, args);
            }
        } catch (IOException e) {
            log_stderr(e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            log_stderr(e.getMessage());
        }
    }

    /**
     * parses incoming messages and sends them to their according methods for handling
     */
    private void eval(String command, ArrayList<String> args) throws IndexOutOfBoundsException {
        switch (command) {
            case "HELLO" -> {
                // HELLO;<VERSION>;<MAX_SEMESTER>;<USERNAME>
                // Die erste Nachricht der Verbindung und wird nach Herstellung der SocketVerbindung von dem Host an den
                // Client gesendet
                int peerVersion = Integer.parseInt(args.get(0));
                int peerSemester = Integer.parseInt(args.get(1));
                String peerUsername = args.get(2);
                HELLO(RECEIVE, peerVersion, peerSemester, peerUsername);
            }
            case "HELLO_ACK" -> {
                // HELLO_ACK;<VERSION>;<SEMESTER>;<USERNAME>
                // Diese Nachricht wird als Antwort auf eine HELLO-Nachricht gesendet.
                int peerVersion = Integer.parseInt(args.get(0));
                int peerSemester = Integer.parseInt(args.get(1));
                String peerUsername = args.get(2);
                HELLO_ACK(RECEIVE, peerVersion, peerSemester, peerUsername);
            }
            case "START" -> {
                // START
                START(RECEIVE);
            }
            case "START_ACK" -> {
                // START_ACK
                START_ACK(RECEIVE);
            }
            case "READY_PING" -> {
                // READY_PING;<SHIPS_PLACED>
                int peerShipsPlaced = Integer.parseInt(args.get(0));
                READY_PING(RECEIVE, peerShipsPlaced);
            }
            case "READY_CHK" -> {
                // READY_CHK
                READY_CHK(RECEIVE);
            }
            case "BEGIN" -> {
                // BEGIN;<WHO>
                String who = args.get(0);
                BEGIN(RECEIVE, who);
            }
            case "BEGIN_ACK" -> {
                // BEGIN_ACK;<WHO>
                String who = args.get(0);
                BEGIN_ACK(RECEIVE, who);
            }
            case "FIRE" -> {
                // FIRE;<ROW>;<COLUMN>
                int row = Integer.parseInt(args.get(0));
                int col = Integer.parseInt(args.get(1));
                FIRE(RECEIVE, row, col);
            }
            case "FIRE_ACK" -> {
                // FIRE_ACK;<TYPE>[;<GAME_OVER>]
                String type = args.get(0);
                if (args.size() >= 2) {
                    boolean gameOver = Boolean.parseBoolean(args.get(1));
                    FIRE_ACK(RECEIVE, type, gameOver);
                } else {
                    FIRE_ACK(RECEIVE, type);
                }
            }
            case "END" -> {
                // END;<WINNER>
                String winner = args.get(0);
                END(RECEIVE, winner);
            }
            case "END_ACK" -> {
                // END_ACK
                END_ACK(RECEIVE);
            }
            case "BYE" -> {
                // BYE;[<CODE>;<REASON>]
                // TODO handle optionals better
                int code = 0;
                String reason = "None";
                if (args.size() >= 2) {
                    code = Integer.parseInt(args.get(0));
                    reason = args.get(1);
                }
                BYE(RECEIVE, code, reason);
            }
            case "ERR" -> {
                // ERR;<CODE>;<REASON>
                // TODO handle optionals better
                int code = 0;
                String reason = "None";
                if (args.size() >= 2) {
                    code = Integer.parseInt(args.get(0));
                    reason = args.get(1);
                }
                ERR(RECEIVE, code, reason);
            }

        }


    }

    private void HELLO(MessageMode mode, int VERSION, int MAX_SEMESTER, String USERNAME) {
        switch (mode) {
            case SEND -> {
                sendMessage("HELLO", String.valueOf(VERSION), String.valueOf(MAX_SEMESTER), USERNAME);
            }
            case RECEIVE -> {
                if (protocolVersionNegotiated || semesterNegotiated) {
                    log_stderr("HELLO: attempted re-negotiation of protocol or semester");
                    System.exit(1);
                }
                protocolVersion = Math.min(implementedProtocolVersion, VERSION);
                protocolVersionNegotiated = true;
                semester = Math.min(semester, MAX_SEMESTER);
                semesterNegotiated = true;
                peerUsername = USERNAME;
                log_debug("HELLO: protocol version: " + protocolVersion + ", semester: " + semester + ", peerUsername: " + peerUsername);
                HELLO_ACK(SEND, protocolVersion, semester, username);
            }
        }
    }

    private void HELLO_ACK(MessageMode mode, int VERSION, int SEMESTER, String USERNAME) {
        switch (mode) {
            case SEND -> {
                sendMessage("HELLO_ACK", String.valueOf(VERSION), String.valueOf(SEMESTER), USERNAME);
            }
            case RECEIVE -> {
                if (VERSION > implementedProtocolVersion) {
                    log_stderr("HELLO_ACK: VERSION > implementedProtocolVersion (" + VERSION + ">" + implementedProtocolVersion + ")");
                    System.exit(1);
                }
                if (SEMESTER > semester) {
                    log_stderr("HELLO_ACK: SEMESTER > this.semester (" + SEMESTER + ">" + semester + ")");
                    System.exit(1);
                }
                if (protocolVersionNegotiated || semesterNegotiated) {
                    log_stderr("HELLO_ACK: attempted re-negotiation of protocol or semester");
                    System.exit(1);
                }

                protocolVersion = VERSION;
                protocolVersionNegotiated = true;
                semester = SEMESTER;
                semesterNegotiated = true;
                peerUsername = USERNAME;
                log_debug("HELLO_ACK: protocol version: " + protocolVersion + ", semester: " + semester + ", peerUsername: " + peerUsername);
                START(SEND);
            }
        }
    }

    private void START(MessageMode mode) {
        // TODO set a boolean here?
        switch (mode) {
            case SEND -> {
                sendMessage("START");
            }
            case RECEIVE -> {
                START_ACK(SEND);
            }
        }
    }

    private void START_ACK(MessageMode mode) {
        // TODO set a boolean here?
        switch (mode) {
            case SEND -> {
                sendMessage("START_ACK");
            }
            case RECEIVE -> {
            }
        }
    }

    private void READY_PING(MessageMode mode, int SHIPS_PLACED) {
        switch (mode) {
            case SEND -> {
                // TODO expose this to logic
                sendMessage("READY_PING", String.valueOf(SHIPS_PLACED));
            }
            case RECEIVE -> {
                // TODO forward SHIPS_PLACED to logic
                // TODO get info from logic on how many ships we have placed to be able to respond
                int playerShipsPlaced = 0;      // dummy
                // TODO get info what maxPlacedShips for current semester is (from logic?)
                int maxPlacedShips = 5;         // dummy

                // TODO does this not cause an infinite loop / spam until all ships are placed?
                if (playerShipsPlaced == maxPlacedShips) {
                    READY_CHK(SEND);
                } else {
                    // FIXME maybe the api gets changed?
                    // if we received READY_PING, wait before sending response to avoid spam
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    READY_PING(SEND, playerShipsPlaced);
                                }
                            }, 500
                    );
                }
            }
        }
    }

    private void READY_CHK(MessageMode mode) {
        switch (mode) {
            case SEND -> {
                // TODO expose this to logic
                sendMessage("READY_CHK");
            }
            case RECEIVE -> {
                // TODO forward this info to logic
            }
        }
    }

    private void BEGIN(MessageMode mode, String WHO) {
        // TODO set a boolean here?
        switch (mode) {
            case SEND -> {
                // TODO expose this to logic
                // TODO decide who rolls the random value
                sendMessage("BEGIN", WHO);
            }
            case RECEIVE -> {
                // TODO forward this info to logic
                String whoInverse = WHO.equalsIgnoreCase("host") ? "client" : "host";
                BEGIN_ACK(SEND, whoInverse);
            }
        }
    }

    private void BEGIN_ACK(MessageMode mode, String WHO) {
        switch (mode) {
            case SEND -> {
                sendMessage("BEGIN_ACK", WHO);
            }
            case RECEIVE -> {
                // TODO do we need to do anything here?
            }
        }
    }

    private void FIRE(MessageMode mode, int ROW, int COL) {
        switch (mode) {
            case SEND -> {
                // TODO expose this to logic
                sendMessage("FIRE", String.valueOf(ROW), String.valueOf(COL));
            }
            case RECEIVE -> {
                // TODO forward this info to logic
            }
        }
    }

    private void FIRE_ACK(MessageMode mode, String TYPE, boolean GAME_OVER) {
        switch (mode) {
            case SEND -> {
                // TODO expose this to logic
                sendMessage("FIRE_ACK", TYPE, String.valueOf(GAME_OVER));
            }
            case RECEIVE -> {
                // TODO forward this info to logic
                // TODO special handling for GAME_OVER?
            }
        }
    }

    private void FIRE_ACK(MessageMode mode, String TYPE) {
        // TODO is this the correct handling of GAME_OVER?
        FIRE_ACK(mode, TYPE, false);
    }

    private void END(MessageMode mode, String WINNER) {
        switch (mode) {
            case SEND -> {
                // TODO expose this to logic
                sendMessage("END", WINNER);
            }
            case RECEIVE -> {
                // TODO forward this info to logic
                boolean accept = true;      // dummy
                if (accept)
                    END_ACK(SEND);
            }
        }
    }

    private void END_ACK(MessageMode mode) {
        switch (mode) {
            case SEND -> {
                sendMessage("END_ACK");
            }
            case RECEIVE -> {
                // TODO forward this info to logic
            }
        }
    }

    private void BYE(MessageMode mode, int CODE, String REASON) {
        // TODO CODE and REASON are optional, add overloads without them?
        switch (mode) {
            case SEND -> {
                sendMessage("BYE", String.valueOf(CODE), REASON);
            }
            case RECEIVE -> {
                // TODO how do we handle this?
                log_debug("BYE: " + CODE + ", " + REASON);
            }
        }
    }

    private void ERR(MessageMode mode, int CODE, String REASON) {
        // TODO CODE and REASON are optional, add overloads without them?
        switch (mode) {
            case SEND -> {
                sendMessage("ERR", String.valueOf(CODE), REASON);
            }
            case RECEIVE -> {
                // TODO how do we handle this?
                log_stderr("ERR: " + CODE + ", " + REASON);
            }
        }
    }

    /**
     * send an arbitrary message to the other player
     * use for debug purposes only
     *
     * @deprecated
     */
    public void sendRawMessage(String msg) {
        log_debug("sending: '" + msg + "'");
        outWriter.println(msg);
    }

    /**
     * constructs a command message as per protocol and sends it
     *
     * @param command
     * @param args
     */
    private void sendMessage(String command, String... args) {
        String msg = constructMessage(command, args);
        log_debug("sending: '" + msg + "'");
        outWriter.println(msg);
    }

    public void setSocket(@NotNull Socket clientSocket) throws IOException {
        if (socket != null) {
            log_stderr("Attempting to overwrite socket");
            System.exit(1);
        }

        this.socket = clientSocket;

        this.outWriter = new PrintWriter(this.socket.getOutputStream(), true);
        this.inReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

        init_communication();
    }
}
