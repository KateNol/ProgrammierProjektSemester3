package gui.controllers;

import gui.GUIPlayer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import logic.GUIAIPlayer;
import logic.PlayerConfig;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Stefan
 * Controller for managing Files safe, create and delete
 */
public class ControllerFileManager implements Initializable {

    //AI
    @FXML
    private VBox AIDifficultBox;
    @FXML
    private Button aiButton;

    //File Buttons
    @FXML
    private Button fileOne;
    @FXML
    private Button fileThree;
    @FXML
    private Button fileTwo;
    @FXML
    private Button deleteOne;
    @FXML
    private Button deleteTwo;
    @FXML
    private Button deleteThree;

    //CreateName
    @FXML
    private TextField nameInput;
    @FXML
    private Button create;
    @FXML
    private VBox userInput;

    //Background
    @FXML
    private HBox background;

    private PlayerConfig playerConfig;
    private static ControllerFileManager instance = null;

    /**
     * Initialize Names on Buttons and load if Name is available
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        background.setBackground(Settings.setBackgroundImage("/img/hsh_mensa.png"));

        setPicture(deleteOne);
        setPicture(deleteTwo);
        setPicture(deleteThree);

        setFileNamesOnButton();
    }

    /**
     * Get instance from ControllerFileManager
     * @return instance
     */
    public static ControllerFileManager getInstance(){
        return instance;
    }

    /**
     * Set FileName when Game is restarted and File exists
     */
    public void setFileNamesOnButton(){
        List<Integer> fileNumbers = new ArrayList<>();
        if (FileController.isFileOne()) {
            fileNumbers.add(0);
        }
        if (FileController.isFileTwo()) {
            fileNumbers.add(1);
        }
        if (FileController.isFileThree()){
            fileNumbers.add(2);
        }


        for (int fileNumber : fileNumbers) {
            try {
                PlayerConfig player = FileController.loadFromFile(fileNumber);
                String semester = " (" + player.getMaxSemester() + ")";
                if(fileNumber == 0){
                    fileOne.setText(FileController.getFileName(fileNumber) + semester);
                } else if (fileNumber == 1) {
                    fileTwo.setText(FileController.getFileName(fileNumber) + semester);
                } else if (fileNumber == 2) {
                    fileThree.setText(FileController.getFileName(fileNumber) + semester);
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Set Picture on a Button
     * @param deleteButton button where to set Picture
     */
    public void setPicture(Button deleteButton){
        String deletePicturePath = "/img/RecBin.png";
        ImageView view = new ImageView(new Image(deletePicturePath));
        view.setFitHeight(40);
        view.setFitWidth(40);
        view.setPreserveRatio(true);
        deleteButton.setGraphic(view);
    }

    /**
     * Return to Screen Menu
     */
    public void onReturn(){
        if(userInput.isVisible() || AIDifficultBox.isVisible() || aiButton.isDisable()){
            nameInput.clear();
            userInput.setVisible(false);
            AIDifficultBox.setVisible(false);
            aiButton.setDisable(false);
            setFilesDisabled(false);
        }
        AudioPlayer.playSFX(Audio.Click);
        ViewSwitcher.switchTo(View.Menu);
    }

    /**
     * Create or load File 1
     */
    public void onFile1() {
        AudioPlayer.playSFX(Audio.Click);
        if (FileController.isFileOne()){
            loadPlayerFile(0);
            ViewSwitcher.switchTo(View.NetworkManager);
        } else {
            createPlayerFile(0);
        }
    }

    /**
     * Create or load File 2
     */
    public void onFile2() {
        AudioPlayer.playSFX(Audio.Click);
        if (FileController.isFileTwo()){
            loadPlayerFile(1);
            ViewSwitcher.switchTo(View.NetworkManager);
        } else {
            createPlayerFile(1);
        }
    }

    /**
     * Create or load File 3
     */
    public void onFile3() {
        AudioPlayer.playSFX(Audio.Click);
        if (FileController.isFileThree()){
            loadPlayerFile(2);
            ViewSwitcher.switchTo(View.NetworkManager);
        } else {
            createPlayerFile(2);
        }
    }

    /**
     * Load Player File
     */
    public void loadPlayerFile(int i){
        try {
            this.playerConfig = FileController.loadFromFile(i);
        } catch (IOException | ClassNotFoundException e){
            gui.Util.log_debug("Could not load playerConfig file from folder");
        }
        new GUIPlayer(playerConfig);
    }

    /**
     * Create Player File
     * @param i Witch file chosen
     */
    public void createPlayerFile(int i) {
        if(!userInput.isVisible()){
            userInput.setVisible(true);
            aiButton.setDisable(true);
        } else {
            userInput.setVisible(false);
            aiButton.setDisable(false);
        }
        setUsername(i);
    }

    /**
     * Set Username for Player
     * @param i Witch file chosen
     */
    public void setUsername(int i){
        create.setOnMouseClicked(mouseEvent -> {
            AudioPlayer.playSFX(Audio.Click);
            if(isValidInput(nameInput.getText())){
                switch (i) {
                    case 0 -> {
                        fileOne.setText(nameInput.getText() + " (1)");
                        FileController.setFileOne(true);
                    }
                    case 1 -> {
                        fileTwo.setText(nameInput.getText() + " (1)");
                        FileController.setFileTwo(true);
                    }
                    case 2 -> {
                        fileThree.setText(nameInput.getText() + " (1)");
                        FileController.setFileThree(true);
                    }
                }
                playerConfig = new PlayerConfig(nameInput.getText());
                new GUIPlayer(playerConfig);
                try {
                    FileController.writeToFile(playerConfig, i);
                    FileController.setNewFile(i);
                } catch (IOException e){
                    gui.Util.log_debug("Could not create PlayerConfig File");
                }
                nameInput.clear();
                userInput.setVisible(false);
                aiButton.setDisable(false);
            } else {
                gui.Util.log_debug("Invalid user input, try again!");
            }
        });
    }

    /**
     * Valid input for Player Name
     * @return validInput for Name
     */
    public boolean isValidInput(String nameInput){
        return !nameInput.equals("");
    }

    /**
     * Delete configFile
     */
    public void onDelete1(){
        AudioPlayer.playSFX(Audio.Click);
        deleteOne.setOnMouseClicked(e -> {
            if(FileController.isFileOne()){
                FileController.configDelete(0);
                deleteUserName(0);
                FileController.setFileOne(false);
            } else {
                gui.Util.log_debug("Nothing to delete on file 1");
            }
        });
    }

    /**
     * Delete configFile
     */
    public void onDelete2(){
        AudioPlayer.playSFX(Audio.Click);
        deleteTwo.setOnMouseClicked(e -> {
            if(FileController.isFileTwo()){
                FileController.configDelete(1);
                deleteUserName(1);
                FileController.setFileTwo(false);
            } else {
                gui.Util.log_debug("Nothing to delete on file 2");
            }
        });
    }

    /**
     * Delete configFile
     */
    public void onDelete3(){
        AudioPlayer.playSFX(Audio.Click);
        deleteThree.setOnMouseClicked(e -> {
            if(FileController.isFileThree()){
                FileController.configDelete(2);
                deleteUserName(2);
                FileController.setFileThree(false);
            } else {
                gui.Util.log_debug("Nothing to delete on file 3");
            }
        });
    }

    /**
     * Delete Name on Button where Name is set
     * @param fileSlot witch file to delete
     */
    private void deleteUserName(int fileSlot) {
        switch (fileSlot) {
            case 0 -> fileOne.setText("New File 1");
            case 1 -> fileTwo.setText("New File 2");
            case 2 -> fileThree.setText("New File 3");
        }
    }

    /**
     * PLay as AI
     */
    public void onAi(){
        if(!AIDifficultBox.isVisible()){
            AIDifficultBox.setVisible(true);
            setFilesDisabled(true);
        } else {
            AIDifficultBox.setVisible(false);
            setFilesDisabled(false);
        }
    }

    /**
     * AI with difficult 1
     */
    public void onAIOne(){
        PlayerConfig playerConfig = new PlayerConfig("AIOne");
        new GUIAIPlayer(playerConfig);
        AIDifficultBox.setVisible(false);
        setFilesDisabled(false);
        ViewSwitcher.switchTo(View.NetworkManager);
    }

    /**
     * AI with difficult 2
     */
    public void onAITwo(){
        PlayerConfig playerConfig = new PlayerConfig("AITwo");
        for(int i = 0; i < 1; i++){
            playerConfig.increaseMaxSemester();
        }
        new GUIAIPlayer(playerConfig);
        AIDifficultBox.setVisible(false);
        setFilesDisabled(false);
        ViewSwitcher.switchTo(View.NetworkManager);
    }

    /**
     * AI with difficult 3
     */
    public void onAIThree(){
        PlayerConfig playerConfig = new PlayerConfig("AIThree");
        for(int i = 0; i < 2; i++){
            playerConfig.increaseMaxSemester();
        }
        new GUIAIPlayer(playerConfig);
        AIDifficultBox.setVisible(false);
        setFilesDisabled(false);
        ViewSwitcher.switchTo(View.NetworkManager);
    }

    /**
     * AI with difficult 4
     */
    public void onAIFour(){
        PlayerConfig playerConfig = new PlayerConfig("AIFour");
        for(int i = 0; i < 3; i++){
            playerConfig.increaseMaxSemester();
        }
        new GUIAIPlayer(playerConfig);
        AIDifficultBox.setVisible(false);
        setFilesDisabled(false);
        ViewSwitcher.switchTo(View.NetworkManager);
    }

    /**
     * AI with difficult 5
     */
    public void onAIFive(){
        PlayerConfig playerConfig = new PlayerConfig("AIFive");
        for(int i = 0; i < 4; i++){
            playerConfig.increaseMaxSemester();
        }
        new GUIAIPlayer(playerConfig);
        AIDifficultBox.setVisible(false);
        setFilesDisabled(false);
        ViewSwitcher.switchTo(View.NetworkManager);
    }

    /**
     * AI with difficult 6
     */
    public void onAISix(){
        PlayerConfig playerConfig = new PlayerConfig("AISix");
        for(int i = 0; i < 5; i++){
            playerConfig.increaseMaxSemester();
        }
        new GUIAIPlayer(playerConfig);
        AIDifficultBox.setVisible(false);
        setFilesDisabled(false);
        ViewSwitcher.switchTo(View.NetworkManager);
    }

    /**
     * Disable Files when Ai is chosen
     * @param b true on AI
     */
    public void setFilesDisabled(boolean b){
        fileOne.setDisable(b);
        fileTwo.setDisable(b);
        fileThree.setDisable(b);
    }
}
