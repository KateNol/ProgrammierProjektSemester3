package network.internal;


import network.Mode;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


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
            Socket clientSocket = serverSocket.accept();

            return new Contact(clientSocket, Mode.SERVER);
        }
    }
}
