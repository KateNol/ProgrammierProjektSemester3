package network.internal;

import javafx.util.Pair;
import logic.Coordinate;
import logic.ShotResult;
import network.ServerMode;
import shared.Notification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

import static network.internal.MessageMode.RECEIVE;
import static network.internal.MessageMode.SEND;
import static network.internal.Util.*;
import static shared.Util.debug;

/**
 * this class communicates with the other player via a socket <br>
 * contains a thread which is permanently listening for new messages <br>
 * this class is not to be used by anyone outside the network package <br>
 * player classes will use this class to send info to the other player <br>
 * network protocol methods are named in CAPSLOCK <br>
 * state order is : hello -> start -> ready -> begin -> (fire)* -> end
 */
public final class Contact extends Observable {
    private Socket socket = null;
    private ServerMode serverMode;

    private PrintWriter outWriter;
    private BufferedReader inReader;

    Thread commThread = null;

    private final Object semesterLock = new Object();
    private final Object beginLock = new Object();

    // general info
    private int shipsPlaced = 0;
    boolean connectionTerminated = false;

    // state hello
    private String username;
    private String peerUsername;
    private boolean protocolVersionNegotiated = false;
    private int protocolVersion;
    private boolean semesterNegotiated = false;
    private int semester;

    // state start
    boolean start = false;

    // state ready
    private boolean ready = false;
    private boolean peerIsReady = false;

    // state begin
    private boolean begin = false;
    private boolean peerIsBegin = false;
    private boolean weBeginGame = false;

    // state end
    // ?

    /* construction, deletion and general communication methods *******************************************************/

    Contact(Socket socket, ServerMode serverMode, String username , int semester) throws IOException {
        this.serverMode = serverMode;
        this.username = username;
        this.semester = semester;

        // fallback as per spec
        this.protocolVersion = 1;

        setSocket(socket);
    }

    private void init_communication() {
        commThread = new Thread(this::receiveLoop);
        commThread.setName("Contact");
        commThread.setDaemon(true);
        commThread.start();

        // if hosted, send HELLO
        if (serverMode == ServerMode.SERVER) {
            HELLO(SEND, implementedProtocolVersion, semester, username);
            if (debug) {
                sendRawMessage("to kill and restart this bot send ERR with code 9");
            }
        }
    }

    private void receiveLoop() {
        while (!connectionTerminated && !commThread.isInterrupted()) {
            try {
                String input = inReader.readLine();
                if (input == null) {
                    commThread.interrupt();
                    throw new IOException("Network received null input, peer seems to have disconnected");
                }

                if (input.equals("")) {
                    continue;
                }
                log_debug("received: '" + input + "'");
                input = input.startsWith("CHAT_MSG") ? input : input.toUpperCase();

                ArrayList<String> args = new ArrayList<>(List.of(input.split(String.valueOf(messageDelimiter))));
                String command = args.get(0);
                args.remove(0);

                eval(command, args);
            } catch (IOException e) {
                log_stderr(e.getMessage());
                connectionTerminated = true;
                notifyObservers(Notification.PeerDisconnected);
            }
        }

        log_debug("contact receive thread ending");
    }

    /**
     * send an arbitrary message to the other player <br>
     * use for debug purposes only, use sendMessage() instead
     * @deprecated
     */
    public void sendRawMessage(String msg) {
        if (outWriter == null)
            return;
        log_debug("sending: '" + msg + "'");
        outWriter.println(msg);
    }

    /**
     * constructs a command message as per protocol and sends it
     * @param command
     * @param args
     */
    public void sendMessage(String command, String... args) {
        String msg = constructMessage(command, args);

        log_debug("sending: '" + msg + "'");
        outWriter.println(msg);
    }

    public void endConnection() {
        log_debug("contact endConnection");
        BYE(SEND, 0, "got ordered to end connection");
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        commThread.interrupt();

        assert connectionTerminated;
    }

    private void exitWithError(int error, String msg) {
        log_stderr(msg);
        ERR(SEND, error, msg);
        System.exit(error);
    }

    /* getters ********************************************************************************************************/

    public boolean getIsConnectionEstablished() {
        synchronized (semesterLock) {
            return semesterNegotiated;
        }
    }

    public boolean getIsSemesterNegotiated() {
        synchronized (semesterLock) {
            return semesterNegotiated;
        }
    }

    public String getPeerUsername() {
        return peerUsername;
    }

    public synchronized int getNegotiatedSemester() {
        synchronized (semesterLock) {
            if (!semesterNegotiated) {
                log_stderr("error, trying to get negotiated semester before negotiation took place");
            }
        }
        return semester;
    }

    public boolean getStart() {
        return start;
    }

    public boolean getEnemyReady() {
        return peerIsReady;
    }


    public boolean getBegin() {
        synchronized (beginLock) {
            return peerIsBegin;
        }
    }

    public boolean getWeBeginGame() {
        synchronized (beginLock) {
            return weBeginGame;
        }
    }

    public boolean getConnectionTerminated() {
        return connectionTerminated;
    }


    /* setters ********************************************************************************************************/

    public void setSocket(Socket clientSocket) throws IOException {
        if (clientSocket == null) {
            log_debug("Contact got null socket, not initiating comm yet");
            return;
        }
        if (socket != null) {
            log_stderr("Attempting to overwrite non-null socket, fatal error! " + serverMode);
            System.exit(1);
        }

        this.socket = clientSocket;
        log_debug("Set socket");

        this.outWriter = new PrintWriter(this.socket.getOutputStream(), true);
        this.inReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

        init_communication();
    }

    public void setReady() {
        ready = true;
        READY_CHK(SEND);
    }

    public void setBegin() {
        begin = true;

        if (serverMode == ServerMode.SERVER) {
            Random random = new Random();
            boolean beginnerBoolean = random.nextBoolean();
            String beginner = beginnerBoolean ? "host" : "client";
            synchronized (beginLock) {
                weBeginGame = beginnerBoolean;
            }
            BEGIN(SEND, beginner);
        }
    }

    /* network protocol methods ***************************************************************************************/

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
            case "CHAT_MSG" -> {
                log_debug("eval command CHAT_MSG");
                String msg = args.get(0);
                CHAT_MSG(RECEIVE, msg);
            }
            default -> {
                ERR(SEND, 0, "unknown message command");
            }
        }
    }

    private void HELLO(MessageMode mode, int VERSION, int MAX_SEMESTER, String USERNAME) {
        switch (mode) {
            case SEND -> {
                sendMessage("HELLO", String.valueOf(VERSION), String.valueOf(MAX_SEMESTER), USERNAME);
            }
            case RECEIVE -> {
                synchronized (semesterLock) {
                    if (protocolVersionNegotiated || semesterNegotiated) {
                        exitWithError(1, "HELLO: attempted re-negotiation of protocol or semester, exiting...");
                    }
                    protocolVersion = Math.min(implementedProtocolVersion, VERSION);
                    protocolVersionNegotiated = true;
                    semester = Math.min(semester, MAX_SEMESTER);
                    peerUsername = USERNAME;
                    log_debug("HELLO: protocol version: " + protocolVersion + ", semester: " + semester + ", peerUsername: " + peerUsername);
                    HELLO_ACK(SEND, protocolVersion, semester, username);
                }
            }
        }
    }

    private void HELLO_ACK(MessageMode mode, int VERSION, int SEMESTER, String USERNAME) {
        switch (mode) {
            case SEND -> {
                sendMessage("HELLO_ACK", String.valueOf(VERSION), String.valueOf(SEMESTER), USERNAME);
            }
            case RECEIVE -> {
                synchronized (semesterLock) {
                    if (VERSION > implementedProtocolVersion) {
                        exitWithError(2, "HELLO_ACK: VERSION > implementedProtocolVersion (" + VERSION + ">" + implementedProtocolVersion + ")");
                    }
                    if (SEMESTER > semester) {
                        exitWithError(3, "HELLO_ACK: SEMESTER > this.semester (" + SEMESTER + ">" + semester + ")");
                    }
                    if (protocolVersionNegotiated || semesterNegotiated) {
                        exitWithError(4, "HELLO_ACK: attempted re-negotiation of protocol or semester");
                    }

                    protocolVersion = VERSION;
                    protocolVersionNegotiated = true;
                    semester = SEMESTER;
                    peerUsername = USERNAME;
                    log_debug("HELLO_ACK: protocol version: " + protocolVersion + ", semester: " + semester + ", peerUsername: " + peerUsername);
                    START(SEND);
                }
            }
        }
        synchronized (semesterLock) {
            semesterNegotiated = true;
            notifyObservers(Notification.ConnectionEstablished);
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
        start = true;
        notifyObservers(Notification.GameStart);
    }

    private void READY_PING(MessageMode mode, int SHIPS_PLACED) {
        switch (mode) {
            case SEND -> {
                // TODO expose this to logic
                sendMessage("READY_PING", String.valueOf(SHIPS_PLACED));
            }
            case RECEIVE -> {
                int enemyShipsPlaced = SHIPS_PLACED;      // dummy
                // TODO get info what maxPlacedShips for current semester is (from logic?)
                int maxPlacedShips = 5;         // dummy

                if (shipsPlaced == maxPlacedShips) {
                    READY_CHK(SEND);
                } else {
                    // if we received READY_PING, wait before sending response to avoid spam
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    READY_PING(SEND, shipsPlaced);
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
                peerIsReady = true;
                notifyObservers(Notification.GameReady);
            }
        }
    }

    private void BEGIN(MessageMode mode, String WHO) {
        switch (mode) {
            case SEND -> {
                sendMessage("BEGIN", WHO);
            }
            case RECEIVE -> {
                synchronized (beginLock) {
                    log_debug("begin: WHO: " + WHO + " servermode: " + serverMode);
                    if (WHO.equalsIgnoreCase("host") && serverMode == ServerMode.SERVER)
                        weBeginGame = true;
                    else if (WHO.equalsIgnoreCase("host") && serverMode == ServerMode.CLIENT)
                        weBeginGame = false;
                    else if (WHO.equalsIgnoreCase("client") && serverMode == ServerMode.SERVER)
                        weBeginGame = false;
                    else if (WHO.equalsIgnoreCase("client") && serverMode == ServerMode.CLIENT)
                        weBeginGame = true;
                }
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
            }
        }
        synchronized (beginLock) {
            peerIsBegin = true;
            notifyObservers(Notification.GameBegin);
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
                notifyObservers(new Coordinate(ROW, COL));
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
                ShotResult shotResult = switch (TYPE.toLowerCase(Locale.ROOT)) {
                    case "hit" -> ShotResult.HIT;
                    case "miss" -> ShotResult.MISS;
                    case "sunk" -> ShotResult.SUNK;
                    default -> null;
                };
                if (GAME_OVER)
                    notifyObservers(Notification.GameOver);
                notifyObservers(shotResult);
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
                if (CODE == 9) {
                    exitWithError(9, "received command to kill self, quitting...");
                    System.exit(9);
                }
            }
        }
    }

    private void CHAT_MSG(MessageMode mode, String MESSAGE) {
        switch (mode) {
            case SEND -> {
                if (!(protocolVersion >= 2)) {
                    log_debug("peer does not implement protocol version 2+");
                    notifyObservers(new Pair<>(Notification.ChatMsg, "peer does not implement protocol version 2+"));
                } else {
                    if (!MESSAGE.isEmpty())
                        sendMessage("CHAT_MSG", MESSAGE);
                }
            }
            case RECEIVE -> {
                log_debug("notifying of chatmsg");
                notifyObservers(new ChatMsg(MESSAGE));
            }
        }
    }

    /* observable *****************************************************************************************************/

    @Override
    public void notifyObservers(Object arg) {
        setChanged();
        super.notifyObservers(arg);
        clearChanged();
    }

    public void sendChatMessage(String message) {
        CHAT_MSG(SEND, message);
    }
}
