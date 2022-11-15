package network;

import logic.*;
import network.internal.Client;
import network.internal.Contact;
import network.internal.Server;
//import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Observer;

import static network.internal.Util.*;

public abstract class NetworkPlayer extends Player {
    private Contact contact;
    private ShotResult lastShotResult = null;

    /**
     * @param serverMode determines if we set up a Server and wait for a connection or try to connect as a client
     * @param address    is the target address for client mode, may be null in server mode
     * @throws IOException if an I/O error occurs when waiting for a connection.
     */
    public NetworkPlayer(PlayerConfig playerConfig, GlobalConfig globalConfig, ServerMode serverMode, String address, int port) throws IOException {
        super(playerConfig, globalConfig);
        switch (serverMode) {
            case SERVER -> {
                this.contact = Server.getContact(port);
            }
            case CLIENT -> {
                this.contact = Client.getContact(address, port);
            }
            default -> {
                log_stderr("invalid network mode");
                System.exit(1);
            }
        }
    }

    public NetworkPlayer(PlayerConfig playerConfig, GlobalConfig globalConfig, ServerMode serverMode, String address) throws IOException {
        this(playerConfig, globalConfig, serverMode, address, defaultPort);
    }

    public NetworkPlayer(PlayerConfig playerConfig, GlobalConfig globalConfig, ServerMode serverMode, int port) throws IOException {
        this(playerConfig, globalConfig, serverMode, defaultAddress, port);
    }

    public NetworkPlayer(PlayerConfig playerConfig, GlobalConfig globalConfig, ServerMode serverMode) throws IOException {
        this(playerConfig, globalConfig, serverMode, defaultAddress, defaultPort);
    }


    public void sendMessage(String msg) {
        contact.sendRawMessage(msg);
    }

    @Override
    public boolean getIsConnectionEstablished() {
        return contact.getIsConnectionEstablished();
    }

    @Override
    public int getNegotiatedSemester() {
        return contact.getNegotiatedSemester();
    }

    @Override
    public String getUsername() {
        return contact.getUsername();
    }

    public String getEnemyUsername() {
        return contact.getPeerUsername();
    }

    public void setReadyToBegin(boolean b) {
        // FIXME: replace with semester sensitive info
        contact.setShipsPlaced(globalConfig.getShipSizes(getNegotiatedSemester()).length);
    }

    public boolean getEnemyReadyToBegin() {
        return contact.getBegin();
    }

    /**
     * @param coordinate
     */
    @Override
    public void sendShot(Coordinate coordinate) {
        sendMessage("FIRE;" + coordinate.row() + ";" + coordinate.col());
    }

    /**
     *
     */
    @Override
    public void sendShotResponse(ShotResult shotResult) {
        boolean gameOver = true;
        for (int i = 0; i < myMap.getMapSize(); i++) {
            for (int j = 0; j < myMap.getMapSize(); j++) {
                if (myMap.getMap()[i][j] == MapState.S)
                    gameOver = false;
            }
        }
        if (!gameOver)
            sendMessage("FIRE_ACK;" + shotResult);
        else
            sendMessage("FIRE_ACK;" + shotResult + ";" + "true");
    }

    /**
     * Adds an observer to the set of observers for this object, provided
     * that it is not the same as some observer already in the set.
     * The order in which notifications will be delivered to multiple
     * observers is not specified. See the class comment.
     *
     * @param o an observer to be added.
     * @throws NullPointerException if the parameter o is null.
     */
    @Override
    public void addObserver(Observer o) {
        super.addObserver(o);
        contact.addObserver(o);
    }

    @Override
    public void notifyObservers(Object arg) {
        new Thread(() -> {
            super.notifyObservers(arg);
        }).start();
    }
}