package gui.controllers;

public enum Audio {
    PlaceShip("src/gui/audio/Splash.wav"),
    Shot("src/gui/audio/GunShot.wav"),
    Hit("src/gui/audio/HitExplosion.wav"),
    Sink("src/gui/audio/Sunk.wav"),
    Miss("src/gui/audio/water.wav"),
    Lose("src/gui/audio/Lose.wav" ),
    Win("src/gui/audio/Victory.wav"),
    Click("src/gui/audio/mouseclick.wav"),
    BattleMusic1("src/gui/audio/background/BattleTheme1.mp3"),
    BattleMusic2("src/gui/audio/background/BattleTheme2.mp3"),
    BattleMusic3("src/gui/audio/background/BattleTheme3.mp3"),
    BattleMusic4("src/gui/audio/background/BattleTheme4.mp3"),
    BattleMusic5("src/gui/audio/background/BattleTheme5.mp3"),
    MenuScreen("src/gui/audio/Background.wav");


    private String pathName;

    /**
     * Manage audio handling
     * @param pathName audio Path
     */
    Audio(String pathName) {
        this.pathName = pathName;
    }

    /**
     * Get Path
     * @return audio Path
     */
    public String getPathName() {
        return pathName;
    }
}
