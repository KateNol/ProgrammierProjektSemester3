package gui.controllers;

import javafx.application.Platform;

/**
 * Controller for Menu Screen
 */
public class ControllerMenu {

    /**
     * Switch to Screen Lobby
     */
    public void onContinue(){
        ViewSwitcher.switchTo(View.Lobby);
    }

    /**
     * Switch to Screen New Game File Manager
     */
    public void onNewGame(){
        ViewSwitcher.switchTo(View.FileManager);
    }

    /**
     * Switch to Screen Load Game File Manager
     */
    public void onLoadGame(){
        ViewSwitcher.switchTo(View.FileManager);
    }

    /**
     * Switch to Screen Rule
     */
    public void onRules(){
        ViewSwitcher.switchTo(View.Rules);
    }

    /**
     * Switch to Screen Settings
     */
    public void onSettings(){
        ViewSwitcher.switchTo(View.Settings);
    }

    /**
     * Close Game
     */
    public void onExit(){
        Platform.exit();
    }
}
