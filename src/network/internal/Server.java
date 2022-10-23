package network.internal;


import network.NetworkMode;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static network.internal.Util.log_stdio;


/**
 *
 */
public final class Server {

    // disable instantiation of this class
    private Server() {
    }

    /**
     * @param port port on which to listen
     * @return a Contact to a connected network target
     * @throws IOException if an I/O error occurs when waiting for a connection.
     */
    public static Contact getContact(int port) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log_stdio("Server starting to listen on port " + port);
            Socket clientSocket = serverSocket.accept();
            log_stdio("Server accepted client");

            return new Contact(clientSocket, NetworkMode.SERVER);
        }
    }
}
