package gui.controllers;

public enum View {
    Menu("/gui/fxml/Menu.fxml"),
    Rules("/gui/fxml/Rules.fxml"),
    Settings("/gui/fxml/Settings.fxml"),
    FileManager("/gui/fxml/FileManager.fxml"),
    NetworkManager("/gui/fxml/NetworkManager.fxml"),
    Lobby("/gui/fxml/Lobby.fxml"),
    Game("/gui/fxml/Game.fxml");

    private  String fileName;

    View(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
