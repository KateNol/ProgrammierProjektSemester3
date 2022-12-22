package gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Stefan
 * Controller for Rule Scene
 */
public class ControllerRules implements Initializable {

    @FXML
    private VBox inGame;
    @FXML
    private VBox prepare;
    @FXML
    private Button page;

    @FXML
    private AnchorPane background;

    private boolean switchPage = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        background.setBackground(Settings.setBackgroundImage("file:src/gui/img/battleship-bismarck.jpg"));
    }

    /**
     * Return to Scene Menu
     */
    public void onReturn(){
        AudioPlayer.playSFX(Audio.Click);
        ViewSwitcher.switchTo(View.Menu);
    }

    /**
     * Switches rule sites
     */
    public void changeRuleSite(){
        AudioPlayer.playSFX(Audio.Click);
        if (switchPage){
            prepare.setVisible(false);
            inGame.setVisible(true);
            switchPage = false;
            page.setText("Previous Page");
        } else {
            prepare.setVisible(true);
            inGame.setVisible(false);
            switchPage = true;
            page.setText("Next Page");
        }
    }
}
