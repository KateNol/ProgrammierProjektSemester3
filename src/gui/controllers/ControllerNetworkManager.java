package gui.controllers;

import gui.GUIPlayer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
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
import java.net.URL;
import java.util.ResourceBundle;

import static gui.Util.log_debug;
import static network.internal.Util.defaultAddress;
import static network.internal.Util.defaultPort;

/**
 * Controller for Network Manager
 */
public class ControllerNetworkManager implements Initializable {

    //Background
    @FXML
    private StackPane background;

    //Multiplayer Connection Textfield
    @FXML
    private VBox multiplayerConnectTextfield;
    @FXML
    private TextField addressTextfield;
    @FXML
    private TextField portTextfield;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        background.setBackground(Settings.setBackgroundImage("file:src/gui/img/multiplayerImg.jpg"));
        setCombobox();
    }

    public void onCancelConnection(){
        multiplayerScene.setMouseTransparent(false);
        loadBox.setVisible(false);
    }

    /**
     * Set options for combobox to choose between client or host
     */
    public void setCombobox(){
        comboBoxServerMode.getItems().addAll(client, host);
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

    public boolean setConnectionInput(){
        log_debug("set connection");
        if(serverMode.equals(ServerMode.CLIENT)){
            address = addressTextfield.getText();
            port =  Integer.parseInt(portTextfield.getText());
            return true;
        } else if(serverMode.equals(ServerMode.SERVER)) {
            if (addressTextfield.getText().isEmpty()) {
                address = defaultAddress;
            } else{
                address = addressTextfield.getText();
            }
            if (portTextfield.getText().isEmpty()) {
                port = defaultPort;
            } else {
                port = Integer.parseInt(portTextfield.getText());
            }
            return true;
        }
        return false;
    }

    public void onConnect() throws IOException {
        log_debug("on connect");
        multiplayerConnectTextfield.setVisible(false);
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

        if(setConnectionInput()){
            multiplayerConnectTextfield.setVisible(false);
            GUIPlayer.getInstance().establishConnection(serverMode, address, port);
            new Logic(GUIPlayer.getInstance());
            new Thread(() -> {
                while (!GUIPlayer.getInstance().getIsConnectionEstablished()) ;
                ViewSwitcher.switchTo(View.Lobby);
            }).start();
        } else {
            System.err.println("Could not establish connection!	Try again");
            connectionFailed.setText("Could not establish connection!\tTry again!");
        }
    }

    /**
     * Return to File Manager
     */
    public void onReturn(){
        addressTextfield.clear();
        portTextfield.clear();
        connectionFailed.setText("");
        multiplayerConnectTextfield.setVisible(false);
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
