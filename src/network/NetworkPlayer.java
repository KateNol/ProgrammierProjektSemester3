package network;

import javafx.util.Pair;
import logic.*;
import network.internal.ChatMsg;
import network.internal.Client;
import network.internal.Contact;
import shared.Notification;
import network.internal.Server;
//import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Observer;

import static network.internal.Util.*;

public abstract class NetworkPlayer extends Player {
    private Contact contact;

    public NetworkPlayer(PlayerConfig playerConfig) {
        super(playerConfig);
        contact = null;
    }

    /**
     *
     * @param serverMode {Server or Client}, both modes will start a new thread and wait indefinitely for a connection
     * @param address
     * @param port
     * @throws IOException
     */
    public void establishConnection(ServerMode serverMode, String address, int port) throws IOException {
        log_debug("establishing connection as " + serverMode);
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

    /**
     *
     * @param serverMode {Server or Client}, both modes will start a new thread and wait indefinitely for a connection
     * @throws IOException
     */
    public void establishConnection(ServerMode serverMode) throws IOException {
        establishConnection(serverMode, defaultAddress, defaultPort);
    }

    public void abortEstablishConnection() {
        if (getServerMode() != null) {
            if (getServerMode() == ServerMode.SERVER)
                Server.abort();
            else if (getServerMode() == ServerMode.CLIENT)
                Client.abort();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        if (contact != null) {

            contact.endConnection();
            contact = null;
        }
        abortEstablishConnection();

        notifyObservers(Notification.SelfDestruct);
    }

    /* getters ********************************************************************************************************/

    @Override
    public boolean getIsConnectionEstablished() {
        if (contact == null)
            return false;
        return contact.getIsConnectionEstablished();
    }

    @Override
    public int getNegotiatedSemester() {
        if (contact == null)
            return -1;
        return contact.getNegotiatedSemester();
    }

    public String getEnemyUsername() {
        return contact.getPeerUsername();
    }

    @Override
    public boolean getStart() {
        if (contact == null)
            return false;
        return contact.getStart();
    }

    public boolean getReady() {
        if (contact == null)
            return false;
        return contact.getEnemyReady();
    }

    public boolean getBegin() {
        if (contact == null)
            return false;
        return contact.getBegin();
    }


    @Override
    public boolean getWeBegin() {
        return contact.getWeBeginGame();
    }

    /* setters ********************************************************************************************************/

    public void setReadyToBegin() {
        contact.setReady();
        contact.setBegin();
    }

    public void setReady() {
        contact.setReady();
    }

    public void setBegin() {
        contact.setBegin();
    }

    /* communication methods ******************************************************************************************/

    public void sendMessage(String msg) {
        contact.sendRawMessage(msg);
    }

    @Override
    public void sendShot(Coordinate coordinate) {
        sendMessage("FIRE;" + coordinate.row() + ";" + coordinate.col());
    }

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

    public void onGameOver(String winner) {
        if (contact.getConnectionTerminated())
            return;
        sendEnd(winner);
        sendBye();
        contact.endConnection();
    }

    @Override
    public void sendChatMessage(String message) {
        contact.sendChatMessage(message);
    }


    /* observable *****************************************************************************************************/

    @Override
    public void addObserver(Observer o) {
        super.addObserver(o);
        contact.addObserver(o);
    }

    @Override
    public void notifyObservers(Object arg) {
        Thread notifyThread = new Thread(() -> {
            log_debug("network notify " + arg);
            if (arg instanceof ChatMsg argMsg) {
                log_debug("network notify chatmsg");
                receiveChatMessage(argMsg.msg);
            }
            super.notifyObservers(arg);

        });
        notifyThread.setName("NetworkNotify");
        notifyThread.start();
    }

}