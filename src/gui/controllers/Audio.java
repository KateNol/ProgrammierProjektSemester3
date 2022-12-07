package gui.controllers;

public enum Audio {
    PlaceShip(""),
    Hit(""),
    Sink(""),
    Miss(""),
    Lose("" ),
    Win(""),
    Click("src/gui/audio/intro.mp3"),
    MenuScreen(""),
    GameScene("");


    private String pathName;
    Audio(String pathName) {
        this.pathName = pathName;
    }

    public String getPathName() {
        return pathName;
    }
}
