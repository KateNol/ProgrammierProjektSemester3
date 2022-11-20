package gui.controllers;

import gui.GUIPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import logic.GlobalConfig;
import logic.PlayerConfig;
import network.ServerMode;


public class ControllerFileManager {

    @FXML
    private Button fileOne;

    @FXML
    private Button fileThree;

    @FXML
    private Button fileTwo;

    @FXML
    private TextField nameInput;

    @FXML
    private Button create;

    @FXML
    private VBox userInput;

    private boolean fileExist1 = false;
    private boolean fileExist2 = false;
    private boolean fileExist3 = false;

    private PlayerConfig playerConfig;
    private GlobalConfig globalConfig;
    private ServerMode serverMode;
    private String address;
    private int port;


    public void onReturn(){
        ViewSwitcher.switchTo(View.Menu);
    }

    public void onNext(){
        ViewSwitcher.switchTo(View.NetworkManager);
    }

    public void onFile1() {
        if (fileExist1){
            loadPlayerFile();
        } else {
            createPlayerFile(1);
            fileExist1 = true;
        }
    }

    public void onFile2() {
        if (fileExist2){
            loadPlayerFile();
        } else {
            createPlayerFile(2);
            fileExist2 = true;
        }
    }

    public void onFile3() {
        if (fileExist3){
            loadPlayerFile();
        } else {
            createPlayerFile(3);
            fileExist3 = true;
        }
    }

    public void loadPlayerFile(){

    }

    public void createPlayerFile(int i) {
        userInput.setVisible(true);
        setUsername(i);
    }

    public void setUsername(int i){
        create.setOnMouseClicked(mouseEvent -> {
            switch (i){
                case 1:
                    fileOne.setText(nameInput.getText());
                    break;
                case 2:
                    fileTwo.setText(nameInput.getText());
                    break;
                case 3:
                    fileThree.setText(nameInput.getText());
                    break;
            }
            playerConfig = new PlayerConfig(nameInput.getText());
            nameInput.clear();
            userInput.setVisible(false);
            //GUIPlayer guiPlayer = new GUIPlayer(playerConfig, globalConfig, serverMode, address, port);
        });
    }

    public boolean isValidInput(){
        return false;
    }
}
