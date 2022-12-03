package network.internal;


import network.ServerMode;

import java.io.IOException;
import java.net.Socket;

import static network.internal.Util.log_debug;
import static network.internal.Util.log_stdio;


/**
 * client class
 * assumes a server is present, fails otherwise
 */
public final class Client {

    private static Thread connectionThread;

    private Client() {
    }

    /**
     * @param address the network address or hostname of the network target
     * @param port    the port of the network target
     * @return IOException if an I/O error occurs when creating the socket.
     */
    public static Contact getContact(String address, int port, String username, int semester) throws IOException {
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
            } while (!success);

        });
        connectionThread.start();
        return contact;
    }

    public static void abort() {
        if (connectionThread != null) {
            log_debug("interrupting connection thread");
            connectionThread.interrupt();
            connectionThread = null;
        }
    }
}
