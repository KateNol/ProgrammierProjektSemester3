package gui.controllers;

import gui.Util;
import logic.PlayerConfig;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static gui.Util.log_debug;

/**
 * @author Stefan
 * This class is responsible for saving, updating, deleting or loading playerConfigs
 */
public class FileController {

    private static final File folder = new File("playerConfig/");
    private static final File folderPath = new File(folder.getPath());
    private static final ArrayList<File> listOfFiles = new ArrayList<>();

    private static boolean fileOne = false;
    private static boolean fileTwo = false;
    private static boolean fileThree = false;

    /**
     * update listOfFiles when a new file is created
     * @param i slot
     */
    public static void setNewFile(int i){
        File[] loadedFiles = folderPath.listFiles();
        assert loadedFiles != null;
        for (File file : loadedFiles) {
            int slot = Character.getNumericValue(file.getName().charAt(0));
            if(slot == i){
                listOfFiles.remove(i);
                listOfFiles.add(i, file);
            }
        }
    }

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
        for (int i = 0; i < 3; i++){
            listOfFiles.add(null);
        }

        File[] loadedFiles = folderPath.listFiles();
        assert loadedFiles != null;
        for (File file : loadedFiles) {
            int slot = Character.getNumericValue(file.getName().charAt(0));
            listOfFiles.remove(slot);
            listOfFiles.add(slot, file);
        }

        for (int i = 0; i < listOfFiles.size(); i++) {
            if (listOfFiles.get(i) != null){
                if(i == 0){
                    fileOne = true;
                } else if (i == 1) {
                    fileTwo = true;
                } else if (i == 2) {
                    fileThree = true;
                }
            }
        }
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

    /**
     * find the slot to given username
     * @param name username from player
     * @return slot saved in ControllerFileManager and null if username doesn't exist
     */
    public static int getSlot(String name){
        for (File filename: listOfFiles) {
            if(filename != null){
                String fileName = filename.getName();
                String tempName = fileName.substring(1, fileName.length() - 4);
                if(name.equals(tempName)){
                    return Character.getNumericValue(fileName.charAt(0));
                }
            }
        }
        return -1;
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

    /**
     * update the playerConfig and write the updated Player in a Binary file
     * @param playerConfig playerConfig from Player
     * @throws IOException In case there is an input/output exception
     */
    public static void updateFile(PlayerConfig playerConfig) throws IOException {
        if(getSlot(playerConfig.getUsername()) != -1){
            configDelete(getSlot(playerConfig.getUsername()));
            String absolutePath = "playerConfig/" + getSlot(playerConfig.getUsername()) + "" +playerConfig.getUsername() + ".bin";
            File file = new File(absolutePath);
            log_debug("saved file to: " + file.getAbsolutePath());
            listOfFiles.add(getSlot(playerConfig.getUsername()), file);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(listOfFiles.get(listOfFiles.size() -1)));
            oos.writeObject(playerConfig);
            oos.close();
        } else {
            log_debug("update file failed");
        }

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
        listOfFiles.add(fileNumber, null);
        if(!delete){
            log_debug("delete file failed");
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
     * @return fileThree
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
