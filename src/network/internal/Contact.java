package network.internal;

import network.NetworkMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static network.internal.MessageMode.SEND;
import static network.internal.MessageMode.RECEIVE;
import static network.internal.Util.*;

/**
 * this class communicates with the other player via a socket
 * network protocol methods are named in CAPS
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
        this.socket = socket;
        this.networkMode = networkMode;
        this.username = username;
        this.semester = semester;

        this.outWriter = new PrintWriter(this.socket.getOutputStream(), true);
        this.inReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

        // fallback as per spec
        this.protocolVersion = 1;

        new Thread(this::receiveLoop).start();

        HELLO(SEND, this.username);
        VERSION_SEND(SEND, implementedProtocolVersion);
        SEMESTER_SEND(SEND, semester);
    }

    Contact(Socket socket, NetworkMode networkMode) throws IOException {
        this(socket, networkMode, "undefined", 1);
    }

    private void receiveLoop() {
        try {
            while (true) {
                String input = inReader.readLine();
                if (input == null) {
                    throw new IOException("Network received null input, peer seems to have disconnected");
                }
                log_stdio("received '" + input + "'");
                input = input.toUpperCase();

                ArrayList<String> args = new ArrayList<>(List.of(input.split(String.valueOf(messageDelimiter))));
                String command = args.get(0);
                args.remove(0);

                eval(command, args);
            }
        } catch (IOException e) {
            log_stderr(e.getMessage());
        }
    }

    /**
     * evaluates received messages
     */
    private void eval(String command, ArrayList<String> args) {
        //log_stdio("eval got {" + command + "} {" + args + "}");
        if (command.equals("HELLO")) {
            // HELLO;<USERNAME>;[VERSION]
        } else if (command.equals("VERSION_SEND")) {
            // VERSION_SEND;<VERSION>
            int peerVersion = Integer.parseInt(args.get(0));
            VERSION_SEND(RECEIVE, peerVersion);
        } else if (command.equals("VERSION_ACK")) {
            // VERSION_ACK;<VERSION>
            int peerVersion = Integer.parseInt(args.get(0));
            VERSION_ACK(RECEIVE, peerVersion);
        } else if (command.equals("SEMESTER_SEND")) {
            // SEMESTER_SEND;<SEMESTER>
            int peerSemester = Integer.parseInt(args.get(0));
            SEMESTER_SEND(RECEIVE, peerSemester);
        } else if (command.equals("SEMESTER_ACK")) {
            // SEMESTER_ACK;<SEMESTER>
            int peerSemester = Integer.parseInt(args.get(0));
            SEMESTER_ACK(RECEIVE, peerSemester);
        }


    }

    private void HELLO(MessageMode mode, String username) {
        switch (mode) {
            case SEND -> {
                outWriter.println(constructMessage("HELLO", new String[]{username}));
            }
            case RECEIVE -> {
                peerUsername = username;
            }
        }
    }

    private void VERSION_SEND(MessageMode mode, int protocolVersion) {
        switch (mode) {
            case SEND -> {
                outWriter.println(constructMessage("VERSION_SEND", new String[]{String.valueOf(implementedProtocolVersion)}));
            }
            case RECEIVE -> {
                // if this is the first time receiving this message, calc protocol version and send ack
                // if protocol version was already negotiated before, return without action
                // TODO: decide whether to allow re-negotiation
                if (protocolVersionNegotiated) {
                    log_stderr("Peer trying to re-negotiate protocol version, no action taken");
                    return;
                }
                this.protocolVersion = Math.min(implementedProtocolVersion, protocolVersion);
                VERSION_ACK(SEND, this.protocolVersion);
            }
        }
    }

    private void VERSION_ACK(MessageMode mode, int protocolVersion) {
        switch (mode) {
            case SEND -> {
                outWriter.println(constructMessage("VERSION_ACK", new String[]{String.valueOf(protocolVersion)}));
            }
            case RECEIVE -> {
                if (!protocolVersionNegotiated && this.protocolVersion == protocolVersion) {
                    protocolVersionNegotiated = true;
                } else if (protocolVersionNegotiated) {
                    log_stderr("Peer trying to re-negotiate protocol version, no action taken");
                } else {
                    log_stderr("Failed to negotiate protocol version");
                }
            }
        }
    }

    private void SEMESTER_SEND(MessageMode mode, int semester) {
        switch (mode) {
            case SEND -> {
                outWriter.println(constructMessage("SEMESTER_SEND", new String[]{String.valueOf(semester)}));
            }
            case RECEIVE -> {
                // if this is the first time receiving this message, calc common semester and send ack
                // if semester was already negotiated before, return without action
                // TODO: decide whether to allow re-negotiation
                if (semesterNegotiated) {
                    log_stderr("Peer trying to re-negotiate semester, no action taken");
                    return;
                }
                this.semester = Math.min(this.semester, semester);
                SEMESTER_ACK(SEND, this.semester);
            }
        }
    }

    private void SEMESTER_ACK(MessageMode mode, int semester) {
        switch (mode) {
            case SEND -> {
                outWriter.println(constructMessage("SEMESTER_ACK", new String[]{String.valueOf(semester)}));
            }
            case RECEIVE -> {
                if (!semesterNegotiated && this.semester == semester) {
                    semesterNegotiated = true;
                } else if (semesterNegotiated) {
                    log_stderr("Peer trying to re-negotiate semester, no action taken");
                } else {
                    log_stderr("Failed to negotiate semester version");
                }
            }
        }
    }

    private void READY_CHECK(MessageMode mode) {
        switch (mode) {
            case SEND -> {
            }
            case RECEIVE -> {
            }
        }
    }

    private void READY_RESPONSE(MessageMode mode) {
        switch (mode) {
            case SEND -> {
            }
            case RECEIVE -> {
            }
        }
    }

    private void START(MessageMode mode) {
        switch (mode) {
            case SEND -> {
            }
            case RECEIVE -> {
            }
        }
    }

    private void START_ACK(MessageMode mode) {
        switch (mode) {
            case SEND -> {
            }
            case RECEIVE -> {
            }
        }
    }

    private void FIRE(MessageMode mode) {
        switch (mode) {
            case SEND -> {
            }
            case RECEIVE -> {
            }
        }
    }

    private void FIRE_ACK(MessageMode mode) {
        switch (mode) {
            case SEND -> {
            }
            case RECEIVE -> {
            }
        }
    }

    private void ERROR(MessageMode mode) {
        switch (mode) {
            case SEND -> {
            }
            case RECEIVE -> {
            }
        }
    }


    public void sendMessage(String msg) {
        outWriter.println(msg);
    }
}
