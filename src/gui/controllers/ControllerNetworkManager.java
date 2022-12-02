package gui.controllers;

import gui.GUIPlayer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import logic.Logic;
import network.ServerMode;
import network.debug.Driver;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static gui.Util.log_debug;
import static network.internal.Util.defaultAddress;
import static network.internal.Util.defaultPort;

/**
 * Controller for Network Manager
 */
public class ControllerNetworkManager implements Initializable {

    @FXML
    private HBox background;
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
        BackgroundSize backgroundSize = new BackgroundSize(1, 1, true, true, false, false);
        BackgroundImage backgroundImage = new BackgroundImage((new Image("file:src/gui/img/multiplayerImg.jpg")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,backgroundSize);
        background.setBackground(new Background(backgroundImage));

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
        log_debug("set connection");
        if(serverMode.equals(ServerMode.CLIENT)){
            address = addressTextfield.getText();
            port =  Integer.parseInt(portTextfield.getText());
            return true;
        } else if(serverMode.equals(ServerMode.SERVER)) {
            if (addressTextfield.getText().isEmpty())
                address = defaultAddress;
            else
                address = addressTextfield.getText();
            if (portTextfield.getText().isEmpty())
                port = defaultPort;
            else
                port = Integer.parseInt(portTextfield.getText());
            return true;
        }
        return false;
    }

    public void onConnect() throws IOException {
        log_debug("on connect");
        if(setConnection()){
            multiplayerConnectTextfield.setVisible(false);
            GUIPlayer.getInstance().establishConnection(serverMode, address, port);
            new Logic(GUIPlayer.getInstance());
            while (!GUIPlayer.getInstance().getIsConnectionEstablished()) ;
            ViewSwitcher.switchTo(View.Lobby);
        } else {
            System.err.println("Could not establish connection!	Try again");
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
            String[] new_args = new String[]{"player=ai", "server=client", "network=offline", "semester=6"};
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
        GUIPlayer.getInstance().loadGlobalConfig();
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
