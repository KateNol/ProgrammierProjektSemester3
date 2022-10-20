package network.internal;

import network.Mode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static network.internal.Util.*;

/**
 * this class communicates with the other player via a socket
 * network protocol methods are named in CAPS
 * 
 */
public final class Contact {
    private Socket socket;
    private Mode mode;

    private PrintWriter outWriter;
    private BufferedReader inReader;

    private String username;
    private String peerUsername;

    private int protocolVersion;
    private boolean protocolVersionNegotiated = false;

    private int semester;
    private boolean semesterNegotiated = false;


    Contact(Socket socket, Mode mode, String username, int semester) throws IOException {
        this.socket = socket;
        this.mode = mode;
        this.username = username;
        this.semester = semester;

        this.outWriter = new PrintWriter(this.socket.getOutputStream(), true);
        this.inReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

        new Thread(this::receiveLoop).start();

        HELLO();
        VERSION_SEND();
        SEMESTER_SEND();
    }

    Contact(Socket socket, Mode mode) throws IOException {
        this(socket, mode, "undefined", 1);
    }

    private void receiveLoop() {
        try {
            while (true) {
                String input = inReader.readLine();
                if (input == null) {
                    throw new IOException("Network received null input, peer seems to have disconnected");
                }
                log_stdio("received '" + input + "'");
            }
        } catch (IOException e) {
            log_stderr(e.getMessage());
        }
    }

    /**
     * evaluates received messages
     * @param msg
     */
    private void eval(String msg) {

    }

    private void HELLO() {
        outWriter.println(constructMessage("HELLO", new String[]{username}));
    }
    private void VERSION_SEND() {
        outWriter.println(constructMessage("VERSION_SEND", new String[] {String.valueOf(implementedProtocolVersion)}));
    }
    private void VERSION_ACK() {

    }
    private void SEMESTER_SEND() {
        outWriter.println(constructMessage("SEMESTER_SEND", new String[] {String.valueOf(semester)}));
    }
    private void SEMESTER_ACK() {
        
    }
    private void READY_CHECK() {

    }
    private void READY_RESPONSE() {

    }
    private void START() {

    }
    private void START_ACK() {

    }
    private void FIRE() {

    }
    private void FIRE_ACK() {

    }
    private void ERROR() {
        
    }


    public void sendMessage(String msg) {
        outWriter.println(msg);
    }
}
