package gui.controllers;

public class Settings {


    /**
     * sets stage to full window size
     */

    public  static void setFullHDScreen() {

        ViewSwitcher.getStage().setWidth(1920);
        ViewSwitcher.getStage().setHeight(1080);

    }

    /**
     * sets stage to size  given as parameter
     */

    public static void setHDScreen() {
        ViewSwitcher.getStage().setWidth(1280);
        ViewSwitcher.getStage().setHeight(720);


    }


}
