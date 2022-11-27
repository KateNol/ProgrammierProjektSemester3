package gui.controllers;

import logic.PlayerConfig;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class FileController {

    private static final File folder = new File("playerConfig/");
    private static ArrayList<File> listOfFiles = new ArrayList<>();

    private static boolean fileOne = false;
    private static boolean fileTwo = false;
    private static boolean fileThree = false;

    public static void checkIfFileExists(){
        File[] loadedFiles = folder.listFiles();
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

    public static String getFileName(int fileNumber){
        String s = listOfFiles.get(fileNumber).getName();
        String playerName = s.substring(1, s.length() - 4);
        return playerName;
    }

    public static void writeToFile(PlayerConfig playerConfig, int count) throws IOException {
        String absolutePath = "playerConfig/" + count + "" +playerConfig.getUsername() + ".bin";
        listOfFiles.add(new File(absolutePath));
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(listOfFiles.get(listOfFiles.size() -1)));
        oos.writeObject(playerConfig);
        oos.close();
    }

     public static PlayerConfig loadFromFile(int count) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(listOfFiles.get(count)));
        PlayerConfig playerConfig = (PlayerConfig) ois.readObject();
        ois.close();
        return playerConfig;
     }

    public static void configDelete(int fileNumber){
        listOfFiles.get(fileNumber).delete();
        listOfFiles.remove(fileNumber);
    }

    public static boolean isFileOne() {
        return fileOne;
    }

    public static boolean isFileTwo() {
        return fileTwo;
    }

    public static boolean isFileThree() {
        return fileThree;
    }

    public static void setFileOne(boolean fileOne) {
        FileController.fileOne = fileOne;
    }

    public static void setFileTwo(boolean fileTwo) {
        FileController.fileTwo = fileTwo;
    }

    public static void setFileThree(boolean fileThree) {
        FileController.fileThree = fileThree;
    }
}
