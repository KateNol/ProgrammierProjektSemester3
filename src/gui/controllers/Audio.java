package gui.controllers;

public enum Audio {
    PlaceShip(""),
    Shot("src/gui/audio/GunShot.wav"),
    Hit("src/gui/audio/HitExplosion.wav"),
    Sink("src/gui/audio/Sunk.wav"),
    Miss("src/gui/audio/water.wav"),
    Lose("src/gui/audio/Lose.wav" ),
    Win("src/gui/audio/Victory.wav"),
    Click("src/gui/audio/mouseclick.wav"),
    BattleMusic1("src/gui/audio/BattleTheme1.mp3"),
    BattleMusic2("src/gui/audio/BattleTheme2.mp3"),
    BattleMusic3("src/gui/audio/BattleTheme3.mp3"),
    BattleMusic4("src/gui/audio/BattleTheme4.mp3"),
    BattleMusic5("src/gui/audio/BattleTheme5.mp3"),
    MenuScreen("src/gui/audio/Background.wav");


    private String pathName;
    Audio(String pathName) {
        this.pathName = pathName;
    }

    public String getPathName() {
        return pathName;
    }
}
