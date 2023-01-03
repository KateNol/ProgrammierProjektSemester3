package gui.controllers;

public enum Audio {
    PlaceShip("/audio/Splash.wav"),
    Shot("/audio/GunShot.wav"),
    Hit("/audio/HitExplosion.wav"),
    Sink("/audio/Sunk.wav"),
    Miss("/audio/water.wav"),
    Lose("/audio/Lose.wav"),
    Win("/audio/Victory.wav"),
    Click("/audio/mouseclick.wav"),
    BattleMusic1("/audio/background/BattleTheme1.mp3"),
    BattleMusic2("/audio/background/BattleTheme2.mp3"),
    BattleMusic3("/audio/background/BattleTheme3.mp3"),
    BattleMusic4("/audio/background/BattleTheme4.mp3"),
    BattleMusic5("/audio/background/BattleTheme5.mp3"),
    MenuScreen("/audio/Background.wav");


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
