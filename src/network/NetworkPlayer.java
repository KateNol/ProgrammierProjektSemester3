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

    /**
     * @throws IOException if an I/O error occurs when waiting for a connection.
     */
    public NetworkPlayer(PlayerConfig playerConfig) {
        super(playerConfig);
    }

    public void establishConnection(ServerMode serverMode, String address, int port) throws IOException {
        log_debug("establishing connection with " + serverMode);
        setServerMode(serverMode);
        switch (serverMode) {
            case SERVER -> {
                this.contact = Server.getContact(port, getUsername(), getMaxSemester());
            }
            case CLIENT -> {
                this.contact = Client.getContact(address, port, getUsername(), getMaxSemester());
            }
        }
    }

    public void establishConnection(ServerMode serverMode, String address) throws IOException {
        establishConnection(serverMode, address, defaultPort);
    }

    public void establishConnection(ServerMode serverMode, int port) throws IOException {
        establishConnection(serverMode, defaultAddress, port);
    }

    public void establishConnection(ServerMode serverMode) throws IOException {
        establishConnection(serverMode, defaultAddress, defaultPort);
    }

    public void establishConnection() throws IOException {
        establishConnection(ServerMode.SERVER, defaultAddress, defaultPort);
    }

    public void abortEstablishConnection() {
        switch (getServerMode()) {
            case SERVER -> {
                Server.abort();
            }
            case CLIENT -> {
                Client.abort();
            }
        }
        this.contact = null;
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

    public String getEnemyUsername() {
        return contact.getPeerUsername();
    }

    public void setReadyToBegin() {
        contact.setReady();
        contact.setBegin();
    }

    public boolean getEnemyReadyToBegin() {
        return contact.getBegin() && contact.getEnemyReady() && contact.getEnemyBegin();
    }

    @Override
    public boolean getWeBegin() {
        return contact.getWeBeginGame();
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
        sendShotResponse(shotResult, false);
    }

    public void sendShotResponse(ShotResult shotResult, boolean gameOver) {
        if (!gameOver)
            sendMessage("FIRE_ACK;" + shotResult);
        else
            sendMessage("FIRE_ACK;" + shotResult + ";" + "true");
    }

    public void sendEnd(String winner) {
        sendMessage("END;" + winner);
    }

    public void sendBye() {
        sendMessage("BYE");
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