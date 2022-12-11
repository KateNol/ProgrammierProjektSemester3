package logic;

import java.io.*;

/**
 * This class creates the Configuration file needed to be implemented
 * at the start of the Game
 */
public class PlayerConfig implements Serializable {

    private final String userName;
    private int maxSemester;


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
        if (maxSemester < 6) {
            maxSemester++;
        }
        Util.log_debug("increaseMaxSemester");
    }

    public void decreaseMaxSemester(){
        if (maxSemester > 1) {
            maxSemester--;
        }
        Util.log_debug("decreaseMaxSemester");
    }
}
