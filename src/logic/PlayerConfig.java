package logic;

import java.io.*;


/**
 * This class creates the Configuration file needed to be implemented
 * at the start of the Game
 */
public class PlayerConfig implements Serializable {

    private String userName;
    private int maxSemester;
    private File file;


    /**
     * @param userName This is the name of the player and is also used as the name of the  Config file to be saved
     */
    public PlayerConfig(String userName) {
        this.userName = userName;
        maxSemester = 1;
    }


    /**
     * @return the maximum semester in which the player currently is
     */


    public int getMaxSemester() {
        return maxSemester;
    }


    /**
     * @return username of player
     */

    public String getUsername() {
        return userName;
    }


    /**
     * Increases the Semester in the case where the player has won a game around.
     */
    public void increaseMaxSemester() {
        maxSemester++;

    }


    public void decreaseMaxSemester(){


    }

    /**
     * this method deletes cofigFiles no longer needed
     */


    public void configDelete(){
        file.delete();
    }

    /**
     * writes the current Player state and save it in a Binary file
     *
     * @throws IOException Incase there is an input/output exception
     */

    public void writeToFile() throws IOException {
        String absolutePath = "src/configfiles" +userName + ".bin";
        file = new File(absolutePath);
        ObjectOutputStream as = new ObjectOutputStream(new FileOutputStream(file));
        as.writeObject(this); // write object
        as.close();

    }

    /**
     * @return the derealized object from the binary file
     * @throws IOException
     * @throws ClassNotFoundException
     */

    public PlayerConfig loadFromFile() throws IOException, ClassNotFoundException {
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(userName + ".bin"));
        PlayerConfig p = (PlayerConfig) is.readObject();
        is.close();
        return p;
    }
}

