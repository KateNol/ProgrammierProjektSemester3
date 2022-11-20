package gui.controllers;

import javafx.application.Platform;

public class ControllerMenu {

    public void onContinue(){
        ViewSwitcher.switchTo(View.Lobby);
    }

    public void onNewGame(){
        ViewSwitcher.switchTo(View.FileManager);
    }

    public void onLoadGame(){
        ViewSwitcher.switchTo(View.FileManager);
    }

    public void onRules(){
        ViewSwitcher.switchTo(View.Rules);
    }

    public void onSettings(){
        ViewSwitcher.switchTo(View.Settings);
    }

    public void onExit(){
        Platform.exit();
    }
}
