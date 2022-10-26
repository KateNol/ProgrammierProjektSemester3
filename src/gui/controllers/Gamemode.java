package gui.controllers;

import gui.GUIMain;
import internal.LocalEnemyMode;
import internal.NetworkMode;
import internal.PlayerMode;
import internal.ServerMode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * controller class for "choose game parameters" gui
 * javafx controller class, reacts to gui input
 */
public class Gamemode {
    @FXML
    Button playerModeHuman;
    @FXML
    Button playerModeComputer;
    @FXML
    Button networkModeOnline;
    @FXML
    Button networkModeOffline;
    @FXML
    Button onlineModeHost;
    @FXML
    Button onlineModeJoin;
    @FXML
    TextField onlineAddrAddress;
    @FXML
    TextField onlineAddrPort;
    @FXML
    Button enemyModeHuman;
    @FXML
    Button enemyModeComputer;
    @FXML
    Button start;

    PlayerMode playerMode;
    NetworkMode networkMode;
    ServerMode serverMode;
    LocalEnemyMode localEnemyMode;

    public Gamemode() {
        playerMode = null;
        networkMode = null;
        serverMode = null;
        localEnemyMode = null;
    }

    @FXML
    private void initialize() {

    }

    public void onPlayerModeHuman(ActionEvent actionEvent) {
        playerMode = PlayerMode.HUMAN;
    }

    public void onPlayerModeComputer(ActionEvent actionEvent) {
        playerMode = PlayerMode.COMPUTER;
    }

    public void onNetworkModeOnline(ActionEvent actionEvent) {
        networkMode = NetworkMode.ONLINE;
    }

    public void onNetworkModeOffline(ActionEvent actionEvent) {
        networkMode = NetworkMode.OFFLINE;
    }

    public void onOnlineModeHost(ActionEvent actionEvent) {
        serverMode = ServerMode.HOST;
    }

    public void onOnlineModeJoin(ActionEvent actionEvent) {
        serverMode = ServerMode.HOST;
    }

    public void onEnemyModeHuman(ActionEvent actionEvent) {
        localEnemyMode = LocalEnemyMode.HUMAN;
    }

    public void onEnemyModeComputer(ActionEvent actionEvent) {
        localEnemyMode = LocalEnemyMode.COMPUTER;
    }

    public void onStart(ActionEvent event) throws IOException {
        GUIMain.getInstance().changeSceneToGame(playerMode, networkMode, serverMode, localEnemyMode, onlineAddrAddress.getText(), onlineAddrPort.getText());
    }
}
