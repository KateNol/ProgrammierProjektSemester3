package gui.controllers;
import gui.GUIPlayer;

import gui.Util;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import logic.Logic;

import network.ServerMode;
import network.debug.Driver;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import static network.internal.Util.defaultAddress;
import static network.internal.Util.defaultPort;

/**
 * @author Stefan
 * Controller for Network Manager
 */
public class ControllerNetworkManager implements Initializable {

    //Background
    @FXML
    private StackPane background;

    //Multiplayer Connection TextField
    @FXML
    private VBox multiplayerConnectTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField portTextField;
    @FXML
    private ComboBox<String> comboBoxServerMode;
    @FXML
    private Label connectionFailed;

    //Information's to connect to Server
    private ServerMode serverMode = ServerMode.CLIENT;
    private String address;
    private int port;

    //ServerMode comparison
    private final String client = "CLIENT";
    private final String host = "HOST";

    //Loading to Join Lobby
    @FXML
    private VBox loadBox;
    @FXML
    private HBox multiplayerScene;
    private boolean pictureSet;

    Thread connectionWaitThread = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        background.setBackground(Settings.setBackgroundImage("file:src/gui/img/multiplayerImg.jpg"));
        setCombobox();
    }

    /**
     * Set options for combobox to choose between client or host
     */
    public void setCombobox(){
        comboBoxServerMode.getItems().addAll(client, host);
        comboBoxServerMode.getSelectionModel().selectedItemProperty().addListener((observableValue, s1, s2) -> {
            if(s2.equals(client)){
                serverMode = ServerMode.CLIENT;
                addressTextField.clear();
                addressTextField.setEditable(true);
            } else if (s2.equals(host)) {
                try {
                    addressTextField.setText(Inet4Address.getLocalHost().getHostAddress());
                    addressTextField.setEditable(false);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                serverMode = ServerMode.SERVER;
            }
        });
    }

    /**
     * check if user input is valid
     * @return boolean if input is valid
     */
    public boolean setConnectionInput(){
        Util.log_debug("set connection");
        if(serverMode.equals(ServerMode.CLIENT)){
            if (addressTextField.getText().isEmpty()) {
                address = defaultAddress;
            } else{
                address = addressTextField.getText();
            }
            if (portTextField.getText().isEmpty()) {
                port = defaultPort;
            } else {
                port = Integer.parseInt(portTextField.getText());
            }
            return true;
        } else if(serverMode.equals(ServerMode.SERVER)) {
            if (addressTextField.getText().isEmpty()) {
                address = defaultAddress;
            } else{
                address = addressTextField.getText();
            }
            if (portTextField.getText().isEmpty()) {
                port = defaultPort;
            } else {
                port = Integer.parseInt(portTextField.getText());
            }
            return true;
        }
        return false;
    }

    /**
     * establish connection
     * @throws IOException throws input/output Exception if failed establish connection
     */
    public void onConnect() throws IOException {
        AudioPlayer.playSFX(Audio.Click);
        Util.log_debug("click on connect");
        multiplayerConnectTextField.setVisible(false);
        multiplayerScene.setMouseTransparent(true);
        if(!pictureSet){
            Image image = new Image("file:src/gui/img/load.gif");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(150);
            imageView.setFitWidth(150);
            loadBox.getChildren().add(imageView);
            pictureSet = true;
        }
        loadBox.setVisible(true);

        if(setConnectionInput()) {
            multiplayerConnectTextField.setVisible(false);
            GUIPlayer.getInstance().establishConnection(serverMode, address, port);
            new Logic(GUIPlayer.getInstance());
            connectionWaitThread = new Thread(() -> {
                while (!connectionWaitThread.isInterrupted() && !GUIPlayer.getInstance().getIsConnectionEstablished()) ;
                ViewSwitcher.switchTo(View.Lobby);
                loadBox.setVisible(false);
            });
            connectionWaitThread.setName("GUI Conn wait");
            connectionWaitThread.setDaemon(true);
            connectionWaitThread.start();
        } else {
            System.err.println("Could not establish connection!	Try again");
            connectionFailed.setText("Could not establish connection!\tTry again!");
        }
    }

    /**
     * cancel connection when pressed
     */
    public void onCancelConnection() {
        AudioPlayer.playSFX(Audio.Click);
        multiplayerScene.setMouseTransparent(false);
        loadBox.setVisible(false);
        GUIPlayer.getInstance().abortEstablishConnection();
        connectionWaitThread.interrupt();
    }

    /**
     * Return to File Manager
     */
    public void onReturn() {
        AudioPlayer.playSFX(Audio.Click);
        addressTextField.clear();
        portTextField.clear();
        connectionFailed.setText("");
        multiplayerConnectTextField.setVisible(false);
        loadBox.setVisible(false);
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
        if(multiplayerConnectTextField.isVisible()){
            multiplayerConnectTextField.setVisible(false);
        }
        AudioPlayer.playSFX(Audio.Click);
        ViewSwitcher.switchTo(View.Lobby);
    }

    /**
     * Choose MultiPlayer Mode
     */
    public void onMultiPlayer() {
        AudioPlayer.playSFX(Audio.Click);
        multiplayerConnectTextField.setVisible(true);
    }
}
