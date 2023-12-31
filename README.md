# Gruppe07

## Install Requirements
1. java version >= 19 (getestet mit 19)
2. Linux: wget unzip <br> Windows: powershell curl.exe tar.exe

## Install
1. [Releases](https://lab.it.hs-hannover.de/f4-informatik/prgprj/programmier-projekte-ws22-23/gruppe07/-/releases) aufrufen.
2. Aktuellste Version von gruppe07.zip herunterladen.
3. Zip Archiv entpacken
4. run.sh/run.ps1 ausführen (läd javafx von gluonhq.com herunter und startet dann das Spiel)


## Links

- [Netzwerk Protokoll Repo](https://lab.it.hs-hannover.de/tcm-ss5-u1/progpr22-23-api)
- [Academic Cloud Ordner](https://sync.academiccloud.de/index.php/apps/files/?dir=/ProgProj2223)
- [ShareLatex poster ](https://sharelatex.gwdg.de/read/kjxszbbjyjxm) 


- [Spielregeln](org/abgaben/Spielregeln.pdf)

## notes
- run jar from pool-pc: <br>`/usr/lib/jvm/jdk-17.0.1/bin/java -jar --module-path lib/lib --add-modules javafx.controls,javafx.fxml,javafx.media -Dprism.dirtyopts=false out/artifacts/gruppe07_jar/gruppe07.jar`
- javafx params: <br>`--module-path lib/lib --add-modules javafx.controls,javafx.fxml,javafx.media -Dprism.dirtyopts=false`


### geordnet nach prioritaet
#### all - 03.01 deadline
- [ ] code kommentare aufraeumen/aktualisieren
- [x] poster in git
- [ ] mainAbgabe branch finishen und zurueck in main mergen (lukas => stefan)
- [x] anleitung spiel runterladen/install etc (jannik)
- [ ] (optional) Chat einbauen / refinen
- [x] spielregeln pdf dokument (alex)
- [x] protokolle/diagramme etc in git/org einfuegen (kate/jannik)
- [x] ki in gui spielen lassen (stefan, jannik, lukas) 14.
- [x] semester implementieren und testen (semester increase/decrease, testen) (team)
- [x] überraschung (zb semester ueberspringen, zb stefan ueberraschung) 
- [x] turn label falscher text (lukas)
- [x] sounds (philip)
- [x] gui einige tiles unresponsive (stefan)
- [x] shoot schlauer machen (kat)
- [x] ai delay (kat)
- [x] design / schriftart (kat)
- [x] (optional) Button für Random Schiffe platzieren
- [x] (optional) turnLabel anpassen ohne flimmern
- [x] (optional) mit begin warten bis gui go gibt
- [x] ai name auswaehlen (kat)
- [x] gui options entfernen
- [x] verbindungsabbruch zurueck ins menu (stefan jannik)
- [x] game_over: save player state (team)
- [x] mehrere spiele hintereinander spielen fixen (jannik) 12.
- [x] game over winner message ist falsch (jannik) 12.
- [X] höhere semester start button geht nicht (stefan) 12.
- [x] shoot input buffer bug (schiesst auf tile was man frueher gedrückt hat, nicht das letzte was man gedrückt hat) (stefan)
- [x] GameOver sceen wohin man kommt
- [x] schiffe versenken gui
- [x] buttons anpassen / removen
- [x] einige tiles sind dead?
- [x] alle tiles updaten, nicht nur shots bei versunkenem schiff

#### ai
- [x] setships mergen
- [x] shooten bauen

## Anforderungen

### Anforderungen an den Einzelnen

- [x] klar definierte Aufgabe im Team und absolute Ergebnisverantwortung
- [x] Mindestens 1000 Zeilen Code
- [ ] Sorgfältige Kommentierung und Dokumentation seines Codes
- [x] Fehlerfreiheit des Codes
- [x] Regelmäßige Teilnahme an allen Projekttreffen!
- [x] Vollständiges und pünktliches Einhalten von Terminen

### Anforderungen an die Gruppe
- [x] vollständig erfasste Anwendungsfälle+gui.GUIMain-Prototyp (abgenommen durch Betreuer!)
- [x] Schichtenarchitektur (min. 2 Schichten; gui.GUIMain und Logik getrennt!)
- [x] Sechs Semester (Spielstufen/Level)
- [x] Sound!
- [x] Eine „Überraschung” im Spiel
- [x] Projektplanung
- [ ] Sorgfältige Dokumentation
- [x] fehlerfrei laufendes, absturz- und anwendungssicheres Spiel!
- [x] Lauffähig im Fenster (JFrame)
- [x] Das Spiel muss gegen die Spiele der anderen Projektgruppen spielbar sein
- [ ] Binaries als JAR-Datei (ggfs. mit Startskript für Linux (spiel.sh) und Windows (spiel.bat))

### Anforderungen an die zu erstellende Software
- [x] Lehrveranstaltungen werden durch „Schiffe” repräsentiert
- [x] eine Lehrveranstaltung kann horizontal oder vertikal platziert werden.
- [x] die Matrix ist mindestens 14 x 14 Felder groß und vergrößert sich mit jedem weiteren Semester um eine Zeile/Spalte
    - 1. Semester: 14x14
    - 2. Semester: 15x15
    - 3. Semester: ...
- [x] das Spiel bildet alle sechs Semster des BIN-Studiengangs ab (d.h.: es gibt sechs Level)
- [x] Gegner in dem Spiel können sowohl menschlich als auch künstlich sein  
    Mögliche Paarungen:
        - Mensch gegen Mensch,
        - Mensch gegen Computer
        - oder Computer gegen Computer

### Organisatorische Anforderungen
- [ ] Alle erstellten Dokumente, Diagramme, Pläne, Sourcen und das lauffähige Programm müssen bis zum Projekt-Ende in dem dazugehörigen Git-Repository liegen



## Spielregeln

### Spielregeln Vorbereitung des Spiels:
- Zwei Spieler verbinden sich über ein Netzwerk zu einem Spiel.
- Jeder Spiele kann maximal in dem höchsten Semester starten, dass er je gewonnen hat.  
    Beispiel: Zwei Spieler treffen sich zu einem ersten Duell. D. h., dass sich beide
    im ersten Semester treffen müssen. Der Sieger aus dieser Partie kann
    anschließend beim nächsten Spiel im zweiten Semester starten, der Verlierer
    bleibt im ersten Semester.
- Jeder Spieler zeichnet seine „Schiffe” entsprechend der Vorlesungen im gespielten Semester in die Spielfeld-Matrix ein.
- Die Größe eiens „Schiffs” berechnet sich aus 8 - Anzahl_der_Credits_pro_Vorlesung
- Die Schiffe dürfen sich nicht berühren; es muss mindestens 1 Kästchen Abstand eingehalten werden
- Die Schiffe dürfen nicht über Eck gesetzt werden oder Ausbuchtungen enthalten
- Die Schiffe dürfen nicht diagonal gesetzt werden
- Nach dem Spielstart dürfen die Schiffe nicht mehr verschoben werden!

### Spielregeln Spielverlauf
- Über einen Zufallsgenerator wird ausgelost, wer das Spiel beginnen darf.
- Anschließend gibt Spieler 1 eine Koordinate an, auf die er feuert.  
    Die Koordinate besteht aus einer Zeile und einer Spalte, also zum Beispiel „B7”.
    Diese Koordinate markiert sich Spieler 1 auf dem Spielfeld „Gegnerische Schiffe”, um den Überblick zu behalten.
- Spieler 2 prüft nun, ob eines seiner Schiffe getroffen wurde.  
    Er antwortet mit „Wasser” (falls kein Schiff getroffen wurde), „Treffer” oder „Versenkt” (falls alle Kästchen des Schiffs getroffen sind).
    Der Spieler markiert sich die Koordinate ebenfalls im Spielfeld „Eigene Schiffe”, um zu verfolgen, ob
    bzw. wann seine Schiffe versenkt sind.
- Wenn Spieler 1 ein Schiff von Spieler 2 getroffen oder sogar versenkt hat, darf er noch einmal feuern.
- Wenn Spieler 1 das Wasser getroffen hat, ist Spieler 2 an der Reihe.
- Gewonnen hat am Ende der Spieler, der alle Schiffe seines Gegners versenkt hat.


# Wichtige Notizen

Semester/Schiffe
    8 - Anzahl_der_Credits_pro_Vorlesung

    1. 2, 2, 2, 2, 4, 6
    2. 2, 2, 2, 2, 2,
    3. 2, 2, 2, 2, 4, 6
    3. 2, 2, 2, 2, 4, 6
    4. 2, 2, 2, 3, 3, 6
    5. 2, 1, 1, 1, 6
