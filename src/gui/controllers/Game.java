package gui.controllers;

import internal.LocalEnemyMode;
import internal.NetworkMode;
import internal.PlayerMode;
import internal.ServerMode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Game {
    private static Game instance;


    @FXML
    private void initialize() {
        instance = this;
    }

    public Game getInstance() {
        return instance;
    }

    public void setParameters() {

    }

    public void onFire(ActionEvent actionEvent) {
    }

}
