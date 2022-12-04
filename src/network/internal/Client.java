package network.internal;


import network.ServerMode;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.Selector;

import static network.internal.Util.*;


/**
 * client class
 * assumes a server is present, fails otherwise
 */
public final class Client {

    private static Thread connectionThread = null;

    private Client() {
    }

    /**
     * @param address the network address or hostname of the network target
     * @param port    the port of the network target
     * @return IOException if an I/O error occurs when creating the socket.
     */
    public static Contact getContact(String address, int port, String username, int semester) throws IOException {
        if (connectionThread != null) {
            log_stderr("there is another connectionThread already running");
            return null;
        }

        Contact contact = new Contact(null, ServerMode.CLIENT, username, semester);

        log_debug("client trying to connect to " + address + ":" + port);

        connectionThread = new Thread(() -> {
            Socket socket = null;
            boolean success;

            do {
                success = true;
                try {
                    socket = new Socket(address, port);
                    contact.setSocket(socket);
                    log_stdio("Client successfully connected");
                } catch (IOException ignored) {
                    success = false;
                }
            } while (!success && !connectionThread.isInterrupted());

        });
        connectionThread.setName("Server connection");
        connectionThread.start();
        return contact;
    }

    public static void abort() {
        if (connectionThread == null) {
            log_stderr("no connection to interrupt");
        } else {
            log_debug("interrupting connection thread");
            connectionThread.interrupt();
            connectionThread = null;
        }
    }
}
