package gui.controllers;

import gui.Util;
import logic.PlayerConfig;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class FileController {

    private static final File folder = new File("playerConfig/");
    private static final File folderPath = new File(folder.getPath());
    private static final ArrayList<File> listOfFiles = new ArrayList<>();

    private static boolean fileOne = false;
    private static boolean fileTwo = false;
    private static boolean fileThree = false;

    /**
     * Check if playerConfig Folder exists
     * @return folderPath exists
     */
    public static boolean checkIfFolderExists(){
        return folderPath.exists();
    }

    /**
     * Create new playerConfig Folder
     * @throws IOException throws when path don't exist
     */
    public static void createFolder() throws IOException {
        Files.createDirectories(Paths.get("playerConfig/"));
    }

    /**
     * Checks if configFile exists an if save in ArrayList
     */
    public static void checkIfFileExists(){
        File[] loadedFiles = folderPath.listFiles();
        assert loadedFiles != null;
        for (int i = 0; i < loadedFiles.length; i++) {
            listOfFiles.add(loadedFiles[i]);
            if(i == 0){
                fileOne = true;
            }
            if(i == 1){
                fileTwo = true;
            }
            if(i == 2){
                fileThree = true;
            }

        }
        Collections.sort(listOfFiles);
    }

    /**
     * get FileName
     * @param fileNumber witch File Slot is meant
     * @return playerName Name of the Player
     */
    public static String getFileName(int fileNumber){
        String s = listOfFiles.get(fileNumber).getName();
        return s.substring(1, s.length() - 4);
    }

    public static Character getSlot(String name){
        for (File filename: listOfFiles) {
            String fileName = filename.getName();
            String tempName = fileName.substring(1, fileName.length() - 4);
            if(name.equals(tempName)){
                return fileName.charAt(0);
            }
        }
        return null;
    }

    /**
     * writes the current Player state and saves it in a Binary file
     * @param playerConfig playerConfig from Player
     * @param slot Witch Slot to write in File
     * @throws IOException In case there is an input/output exception
     */
    public static void writeToFile(PlayerConfig playerConfig, int slot) throws IOException {
        String absolutePath = "playerConfig/" + slot + "" +playerConfig.getUsername() + ".bin";
        listOfFiles.add(new File(absolutePath));
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(listOfFiles.get(listOfFiles.size() -1)));
        oos.writeObject(playerConfig);
        oos.close();
    }

    public static void updateFile(PlayerConfig playerConfig) throws IOException {
        String absolutePath = "playerConfig/" + getSlot(playerConfig.getUsername()) + "" +playerConfig.getUsername() + ".bin";
        listOfFiles.add(new File(absolutePath));
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(listOfFiles.get(listOfFiles.size() -1)));
        oos.writeObject(playerConfig);
        oos.close();
    }

    /**
     * Return the deserialized object from the binary file
     * @param slot witch slot to load from Files
     * @return playerConfig
     * @throws IOException throws input/output Exception if fail to read/load the File
     * @throws ClassNotFoundException wrong classpath given
     */
     public static PlayerConfig loadFromFile(int slot) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(listOfFiles.get(slot)));
        PlayerConfig playerConfig = (PlayerConfig) ois.readObject();
        ois.close();
        return playerConfig;
     }

    /**
     * Deletes configFiles if no longer needed
     * @param fileNumber witch File to delete 0 to 2
     */
    public static void configDelete(int fileNumber){
        boolean delete = listOfFiles.get(fileNumber).delete();
        if(!delete){
            Util.log_debug("delete File failed");
        }
        listOfFiles.remove(fileNumber);
    }

    /**
     * Get boolean if FileOne exists or not
     * @return fileOne
     */
    public static boolean isFileOne() {
        return fileOne;
    }

    /**
     * Get boolean if FileTwo exists or not
     * @return fileTwo
     */
    public static boolean isFileTwo() {
        return fileTwo;
    }

    /**
     * Get boolean if FileThree exists or not
     * @return filethree
     */
    public static boolean isFileThree() {
        return fileThree;
    }

    /**
     * Set existence of fileOne
     * @param fileOne first File Slot
     */
    public static void setFileOne(boolean fileOne) {
        FileController.fileOne = fileOne;
    }

    /**
     * Set existence of fileTwo
     * @param fileTwo second File Slot
     */
    public static void setFileTwo(boolean fileTwo) {
        FileController.fileTwo = fileTwo;
    }

    /**
     * Set existence of fileThree
     * @param fileThree third File Slot
     */
    public static void setFileThree(boolean fileThree) {
        FileController.fileThree = fileThree;
    }
}
