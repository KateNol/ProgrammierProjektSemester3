package gui.controllers;

import gui.GUIPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import logic.Logic;
import network.ServerMode;
import network.debug.Driver;

import java.io.IOException;

import static network.internal.Util.*;

/**
 * Controller for Network Manager
 */
public class ControllerNetworkManager {

    @FXML
    private VBox multiplayerConnectTextfield;
    @FXML
    private TextField addressTextfield;
    @FXML
    private TextField portTextfield;
    @FXML
    private TextField serverModeTextfield;
    @FXML
    private Label connectionFaild;

    private ServerMode serverMode;
    private String address;
    private int port;

    private boolean connectionEstablished = false;

    public boolean establishedConnecetion(){
        String client = "client";
        String host = "host";
        if(client.equals(serverModeTextfield.getText())){
            serverMode = ServerMode.CLIENT;
            address = addressTextfield.getText();
            port =  Integer.parseInt(portTextfield.getText());
            return true;
        } else if(host.equals(serverModeTextfield.getText())){
            serverMode = ServerMode.SERVER;
            address = addressTextfield.getText();
            port =  Integer.parseInt(portTextfield.getText());
            return true;
        }
        return false;
    }

    public void onConnect() throws IOException {
        if(establishedConnecetion()){
            multiplayerConnectTextfield.setVisible(false);
            GUIPlayer.getInstance().establishConnection(serverMode, address, port);
            new Logic(GUIPlayer.getInstance());
            ViewSwitcher.switchTo(View.Lobby);
        } else {
            connectionFaild.setText("Could not establish connection!\tTry again!");
        }
    }

    /**
     * Return to File Manager
     */
    public void onReturn(){
        addressTextfield.clear();
        portTextfield.clear();
        serverModeTextfield.clear();
        connectionFaild.setText("");
        connectionEstablished = false;
        multiplayerConnectTextfield.setVisible(false);
        ViewSwitcher.switchTo(View.FileManager);
    }

    /**
     * Choose SinglePlayer Mode
     */
    public void onSinglePlayer() throws IOException {
        Thread enemyThread = new Thread(() -> {
            String[] new_args = new String[]{"player=ai", "server=client", "network=offline"};
            try {
                Driver.main(new_args);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        enemyThread.setDaemon(false);
        enemyThread.setName("Enemy Thread");
        enemyThread.start();


        GUIPlayer.getInstance().establishConnection(ServerMode.SERVER);
        new Logic(GUIPlayer.getInstance());
        if(multiplayerConnectTextfield.isVisible()){
            multiplayerConnectTextfield.setVisible(false);
        }
        ViewSwitcher.switchTo(View.Lobby);
    }

    /**
     * Choose MultiPlayer Mode
     */
    public void onMultiPlayer() {
        multiplayerConnectTextfield.setVisible(true);
    }
}
