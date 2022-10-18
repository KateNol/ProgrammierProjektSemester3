package network.internal;


import network.Mode;

import java.io.IOException;
import java.net.Socket;

import static network.internal.Util.log_stdio;


/**
 *
 */
public final class Client {
    private Client() {
    }

    /**
     * @param address the network address or hostname of the network target
     * @param port    the port of the network target
     * @return IOException if an I/O error occurs when creating the socket.
     */
    public static Contact getContact(String address, int port) throws IOException {
        Socket socket = new Socket(address, port);
        log_stdio("Client connected on port " + port);

        return new Contact(socket, Mode.CLIENT);
    }
}
