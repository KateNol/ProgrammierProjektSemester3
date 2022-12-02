package gui.controllers;

public enum Audio {
    Intro("src/gui/audio/intro.mp3");

    private String pathName;
    Audio(String pathName) {
        this.pathName = pathName;
    }

    public String getPathName() {
        return pathName;
    }
}
