package gui.controllers;

public enum View {
    Menu("/gui/fxml/Menu.fxml"), //Menu Screen
    Rules("/gui/fxml/Rules.fxml"), //Rule Screen
    Settings("/gui/fxml/Settings.fxml"), // Setting Screen
    FileManager("/gui/fxml/FileManager.fxml"), //File manager Screen
    NetworkManager("/gui/fxml/NetworkManager.fxml"), // Network Manager Screen
    Lobby("/gui/fxml/Lobby.fxml"), //Lobby Screen
    Game("/gui/fxml/Game.fxml"); //Game Screen

    private String fileName;

    /**
     * Manage Screen handling
     * @param fileName Screen Path
     */
    View(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Get fileName
     * @return fileName
     */
    public String getFileName() {
        return fileName;
    }
}
