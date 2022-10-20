package network.internal;

import network.Mode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static network.internal.Util.*;

public final class Contact {
    private Socket socket;
    private Mode mode;

    private PrintWriter outWriter;
    private BufferedReader inReader;

    private String username;

    Contact(Socket socket, Mode mode, String username) throws IOException {
        this.socket = socket;
        this.mode = mode;
        this.username = username;

        this.outWriter = new PrintWriter(this.socket.getOutputStream(), true);
        this.inReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

        new Thread(this::receiveLoop).start();

        HELLO();
    }

    Contact(Socket socket, Mode mode) throws IOException {
        this(socket, mode, "undefined");
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

    public void HELLO() {
        outWriter.println(constructMessage("HELLO", new String[]{username, "1"}));
    }

    public void sendMessage(String msg) {
        outWriter.println(msg);
    }
}
