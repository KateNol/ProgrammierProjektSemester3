package gui.controllers;

import gui.GUIPlayer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import logic.PlayerConfig;
import logic.Util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerFileManager implements Initializable {

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

    /**
     * Initialize Names on Buttons and load if Name is available
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        background.setBackground(Settings.setBackgroundImage("file:src/gui/img/FileManager.jpg"));

        setPicture(deleteOne);
        setPicture(deleteTwo);
        setPicture(deleteThree);

        setFileNamesOnButton();
    }

    /**
     * Set FileName when Game is restarted and File exists
     */
    public void setFileNamesOnButton(){
        List<Integer> fileNumbers = new ArrayList<>();
        if (FileController.isFileOne())
            fileNumbers.add(0);
        if (FileController.isFileTwo())
            fileNumbers.add(1);
        if (FileController.isFileThree())
            fileNumbers.add(2);

        for (int fileNumber : fileNumbers) {
            Button file = fileNumber == 0 ? fileOne : fileNumber == 1 ? fileTwo : fileNumber == 2 ? fileThree : null;
            try {
                PlayerConfig player = FileController.loadFromFile(fileNumber);
                String semester = " (" + player.getMaxSemester() + ")";
                assert file != null;
                file.setText(FileController.getFileName(fileNumber) + semester);
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
        String deletePicturePath = "file:src/gui/img/RecBin.png";
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
        if(userInput.isVisible()){
            userInput.setVisible(false);
        }
        ViewSwitcher.switchTo(View.Menu);
    }

    /**
     * Create or load File 1
     */
    public void onFile1() {
        if (FileController.isFileOne()){
            loadPlayerFile(0);
            ViewSwitcher.switchTo(View.NetworkManager);
        } else {
            createPlayerFile(0);
            FileController.setFileOne(true);
        }
    }

    /**
     * Create or load File 2
     */
    public void onFile2() {
        if (FileController.isFileTwo()){
            loadPlayerFile(1);
            ViewSwitcher.switchTo(View.NetworkManager);
        } else {
            createPlayerFile(1);
            FileController.setFileTwo(true);
        }
    }

    /**
     * Create or load File 3
     */
    public void onFile3() {
        if (FileController.isFileThree()){
            loadPlayerFile(2);
            ViewSwitcher.switchTo(View.NetworkManager);
        } else {
            createPlayerFile(2);
            FileController.setFileThree(true);
        }
    }

    /**
     * Load Player File
     */
    public void loadPlayerFile(int i){
        try {
            this.playerConfig = FileController.loadFromFile(i);
        } catch (IOException | ClassNotFoundException e){
            Util.log_debug("Could not load PlayerConfig File");
        }
        new GUIPlayer(playerConfig);
    }

    /**
     * Create Player File
     * @param i Witch file chosen
     */
    public void createPlayerFile(int i) {
        userInput.setVisible(true);
        setUsername(i);
    }

    /**
     * Set Username for Player
     * @param i Witch file chosen
     */
    public void setUsername(int i){
        create.setOnMouseClicked(mouseEvent -> {
            if(isValidInput(nameInput.getText())){
                switch (i) {
                    case 0 -> fileOne.setText(nameInput.getText() + " (1)");
                    case 1 -> fileTwo.setText(nameInput.getText() + " (1)");
                    case 2 -> fileThree.setText(nameInput.getText() + " (1)");
                }
                playerConfig = new PlayerConfig(nameInput.getText());
                new GUIPlayer(playerConfig);
                try {
                    FileController.writeToFile(playerConfig, i);
                } catch (IOException e){
                    Util.log_debug("Could not create PlayerConfig File");
                }
                nameInput.clear();
                userInput.setVisible(false);
            } else {
                System.out.println("try again");
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
        deleteOne.setOnMouseClicked(e -> {
            if(FileController.isFileOne()){
                FileController.configDelete(0);
                deleteUserName(0);
                FileController.setFileOne(false);
            } else {
                System.out.println("Nothing to delete");
            }
        });
    }

    /**
     * Delete configFile
     */
    public void onDelete2(){
        deleteTwo.setOnMouseClicked(e -> {
            if(FileController.isFileTwo()){
                FileController.configDelete(0);
                deleteUserName(1);
                FileController.setFileTwo(false);
            } else {
                System.out.println("Nothing to delete");
            }
        });
    }

    /**
     * Delete configFile
     */
    public void onDelete3(){
        deleteThree.setOnMouseClicked(e -> {
            if(FileController.isFileThree()){
                FileController.configDelete(0);
                deleteUserName(2);
                FileController.setFileThree(false);
            } else {
                System.out.println("Nothing to delete");
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
}
