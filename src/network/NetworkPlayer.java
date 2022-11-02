package network;

import logic.Player;
import network.internal.Client;
import network.internal.Contact;
import network.internal.Server;
//import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static network.internal.Util.*;

public abstract class NetworkPlayer extends Player {
    private Contact contact;

    /**
     * @param serverMode determines if we set up a Server and wait for a connection or try to connect as a client
     * @param address    is the target address for client mode, may be null in server mode
     * @throws IOException if an I/O error occurs when waiting for a connection.
     */
    public NetworkPlayer(/*@NotNull*/ ServerMode serverMode, String address, int port) throws IOException {
        switch (serverMode) {
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

    public NetworkPlayer(ServerMode serverMode, String address) throws IOException {
        this(serverMode, address, defaultPort);
    }

    public NetworkPlayer(ServerMode serverMode, int port) throws IOException {
        this(serverMode, defaultAddress, port);
    }

    public NetworkPlayer(ServerMode serverMode) throws IOException {
        this(serverMode, defaultAddress, defaultPort);
    }


    public void sendMessage(String msg) {
        contact.sendRawMessage(msg);
    }

    @Override
    public boolean getIsConnected() {
        return contact.getIsConnected();
    }

    @Override
    public int getCommonSemester() {
        return contact.getCommonSemester();
    }

}