package gui.controllers;

import logic.PlayerConfig;

import java.io.*;


import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class FileController {

    private static final File folderPath = new File("playerConfig/");
    private static ArrayList<File> listOfFiles = new ArrayList<>();

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
     * @throws IOException
     */
    public static void createFolder() throws IOException {
        Files.createDirectories(Paths.get("playerConfig/"));
    }

    /**
     * Checks if configFile exists an if save in ArrayList
     */
    public static void checkIfFileExists(){
        File[] loadedFiles = folderPath.listFiles();
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
     * @param fileNumber
     * @return playerName
     */
    public static String getFileName(int fileNumber){
        String s = listOfFiles.get(fileNumber).getName();
        String playerName = s.substring(1, s.length() - 4);
        return playerName;
    }

    /**
     * writes the current Player state and saves it in a Binary file
     * @param playerConfig
     * @param count
     * @throws IOException In case there is an input/output exception
     */
    public static void writeToFile(PlayerConfig playerConfig, int count) throws IOException {
        String absolutePath = "playerConfig/" + count + "" +playerConfig.getUsername() + ".bin";
        listOfFiles.add(new File(absolutePath));
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(listOfFiles.get(listOfFiles.size() -1)));
        oos.writeObject(playerConfig);
        oos.close();
    }

    /**
     * Return the deserialized object from the binary file
     * @param count
     * @return playerConfig
     * @throws IOException
     * @throws ClassNotFoundException
     */
     public static PlayerConfig loadFromFile(int count) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(listOfFiles.get(count)));
        PlayerConfig playerConfig = (PlayerConfig) ois.readObject();
        ois.close();
        return playerConfig;
     }

    /**
     * Deletes configFiles if no longer needed
     * @param fileNumber
     */
    public static void configDelete(int fileNumber){
        listOfFiles.get(fileNumber).delete();
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
     * @param fileOne
     */
    public static void setFileOne(boolean fileOne) {
        FileController.fileOne = fileOne;
    }

    /**
     * Set existence of fileTwo
     * @param fileTwo
     */
    public static void setFileTwo(boolean fileTwo) {
        FileController.fileTwo = fileTwo;
    }

    /**
     * Set existence of fileThree
     * @param fileThree
     */
    public static void setFileThree(boolean fileThree) {
        FileController.fileThree = fileThree;
    }
}
