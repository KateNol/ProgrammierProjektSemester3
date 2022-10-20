package network;

import network.internal.Client;
import network.internal.Contact;
import network.internal.Server;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static network.internal.Util.*;

public abstract class NetworkPlayer {
    private Contact contact;

    /**
     * @param mode    determines if we set up a Server and wait for a connection or try to connect as a client
     * @param address is the target address for client mode, may be null in server mode
     * @throws IOException if an I/O error occurs when waiting for a connection.
     */
    public NetworkPlayer(@NotNull Mode mode, String address, int port) throws IOException {
        switch (mode) {
            case SERVER -> {
                log_stdio("server");
                this.contact = Server.getContact(port);
            }
            case CLIENT -> {
                log_stdio("client");
                this.contact = Client.getContact(address, port);
            }
            default -> {
                log_stderr("invalid network mode");
                System.exit(1);
            }
        }
    }

    public NetworkPlayer(Mode mode, String address) throws IOException {
        this(mode, address, defaultPort);
    }

    public NetworkPlayer(Mode mode, int port) throws IOException {
        this(mode, defaultAddress, port);
    }

    public NetworkPlayer(Mode mode) throws IOException {
        this(mode, defaultAddress, defaultPort);
    }


    public void sendMessage(String msg) {
        contact.sendMessage(msg);
    }
}