package gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * @author Stefan
 * Controller for Rule Scene
 */
public class ControllerRules {

    @FXML
    private VBox inGame;
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
