package network.internal;


import network.ServerMode;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.InterruptibleChannel;
import java.nio.channels.Selector;

import static network.internal.Util.*;


/**
 *
 */
public final class Server {

    private static Thread connectionThread;

    // disable instantiation of this class
    private Server() {
    }

    /**
     * this class creates a server and waits for a connection,
     * then it returns a contact.
     * an exception is made for local play. the server cant block the main thread
     * because then the gui would freeze. because of that we use a thread and only
     * setup the contact with a real socket after a connection has been established
     *
     * @param port port on which to listen
     * @return a Contact to a connected network target
     * @throws IOException if an I/O error occurs when waiting for a connection.
     */
    public static Contact getContact(int port, String username, int semester) throws IOException {
        if (connectionThread != null) {
            log_stderr("there is another connectionThread already running");
            return null;
        }

        Contact contact = new Contact(null, ServerMode.SERVER, username, semester);

        connectionThread = new Thread() {
            ServerSocket serverSocket = null;

            @Override
            public void run() {
                super.run();
                try {
                    serverSocket = new ServerSocket(port);
                    log_stdio("Server starting to listen on port " + port);
                    Socket clientSocket = serverSocket.accept();
                    log_stdio("Server accepted client with address " + clientSocket.getInetAddress() + " " + clientSocket.getInetAddress().getHostName());

                    contact.setSocket(clientSocket);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void interrupt() {
                super.interrupt();
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        connectionThread.setName("Server Connection");
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
