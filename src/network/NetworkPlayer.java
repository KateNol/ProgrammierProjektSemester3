package network;

import logic.Player;
import network.internal.Client;
import network.internal.Contact;
import network.internal.Server;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static network.internal.Util.*;

public abstract class NetworkPlayer extends Player {
    private Contact contact;

    /**
     * @param networkMode determines if we set up a Server and wait for a connection or try to connect as a client
     * @param address     is the target address for client mode, may be null in server mode
     * @throws IOException if an I/O error occurs when waiting for a connection.
     */
    public NetworkPlayer(@NotNull NetworkMode networkMode, String address, int port) throws IOException {
        switch (networkMode) {
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
        System.out.println("NetworkPlayer CTOR end");
    }

    public NetworkPlayer(NetworkMode networkMode, String address) throws IOException {
        this(networkMode, address, defaultPort);
    }

    public NetworkPlayer(NetworkMode networkMode, int port) throws IOException {
        this(networkMode, defaultAddress, port);
    }

    public NetworkPlayer(NetworkMode networkMode) throws IOException {
        this(networkMode, defaultAddress, defaultPort);
    }


    public void sendMessage(String msg) {
        contact.sendMessage(msg);
    }
}