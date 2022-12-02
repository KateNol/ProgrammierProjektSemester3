package gui.controllers;

import gui.GUIPlayer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import logic.Logic;
import network.ServerMode;
import network.debug.Driver;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for Network Manager
 */
public class ControllerNetworkManager implements Initializable {

    @FXML
    private VBox multiplayerConnectTextfield;
    @FXML
    private TextField addressTextfield;
    @FXML
    private TextField portTextfield;
    @FXML
    private ComboBox<String> comboBoxServerMode;
    @FXML
    private Label connectionFaild;

    private boolean connectionEstablished = false;
    private ServerMode serverMode = ServerMode.CLIENT;
    private String address;
    private int port;
    private String client = "client";
    private String host = "host";
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBoxServerMode.getItems().addAll("client", "host");
        comboBoxServerMode.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s1, String s2) {
                if(s2.equals(client)){
                    serverMode = ServerMode.CLIENT;
                } else if (s2.equals(host)) {
                    serverMode = ServerMode.SERVER;
                }
            }
        });
    }

    public boolean setConnection(){
        if(serverMode.equals(ServerMode.CLIENT)){
            address = addressTextfield.getText();
            port =  Integer.parseInt(portTextfield.getText());
            return true;
        } else if(serverMode.equals(ServerMode.SERVER)){
            address = addressTextfield.getText();
            port =  Integer.parseInt(portTextfield.getText());
            return true;
        }
        return false;
    }

    public void onConnect() throws IOException {
        if(setConnection()){
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
        enemyThread.setDaemon(true);
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
