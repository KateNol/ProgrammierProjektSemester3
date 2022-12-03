package network.internal;


import network.ServerMode;

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
        Contact contact = new Contact(null, ServerMode.SERVER, username, semester);

        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                log_stdio("Server starting to listen on port " + port);
                Socket clientSocket = serverSocket.accept();
                log_stdio("Server accepted client with address " + clientSocket.getInetAddress() + " " + clientSocket.getInetAddress().getHostName());

                contact.setSocket(clientSocket);
            } catch (IOException ignored) {

            }
        }).start();
        return contact;
    }
}
