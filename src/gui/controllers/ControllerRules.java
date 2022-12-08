package gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * Controller for Rule Scene
 */
public class ControllerRules {

    @FXML
    private VBox ingame;
    @FXML
    private VBox prepare;
    @FXML
    private Button page;

    private boolean switchPage = true;

    /**
     * Return to Scene Menu
     */
    public void onReturn(){
        AudioPlayer.playSFX(Audio.Click);
        ViewSwitcher.switchTo(View.Menu);
    }

    public void changeRuleSite(){
        AudioPlayer.playSFX(Audio.Click);
        if (switchPage){
            prepare.setVisible(false);
            ingame.setVisible(true);
            switchPage = false;
            page.setText("Previous Page");
        } else {
            prepare.setVisible(true);
            ingame.setVisible(false);
            switchPage = true;
            page.setText("Next Page");
        }
    }
}
