# Gruppe07

Links

- [Netzwerk Protokoll Repo](https://lab.it.hs-hannover.de/tcm-ss5-u1/progpr22-23-api)
- [Academic Cloud Ordner](https://sync.academiccloud.de/index.php/apps/files/?dir=/ProgProj2223)

## ToDo - bis 07.12

### geordnet nach prioritaet
#### all
- [ ] semester implementieren und testen (semester increase/decrease, testen) (lukas) 12.
- [ ] überraschung (zb semester ueberspringen, zb stefan ueberraschung) 
- [ ] mehrere spiele hintereinander spielen fixen (jannik) 12.
- [ ] game over winner message ist falsch (jannik) 12.

#### logic/player
- [ ] game_over: save player state (lukas) 12.

#### gui
- [ ] ki in gui spielen lassen (stefan, jannik, lukas) 14.
- [X] höhere semester start button geht nicht (stefan) 12.
- [ ] shoot input buffer bug (schiesst auf tile was man frueher gedrückt hat, nicht das letzte was man gedrückt hat) (stefan)
- [ ] design / schriftart (kat) 14.
- [ ] (optional) Chat einbauen
- [ ] (optional) Button für Random Schiffe plazieren
- [ ] (optional) turnLabel anpassen ohne flimmern- 
- [x] sounds
- [x] GameOver sceen wohin man kommt
- [x] schiffe versenken gui
- [x] buttons anpassen / removen
- [x] einige tiles sind dead?
- [x] alle tiles updaten, nicht nur shots bei versunkenem schiff


#### network
- [ ] verbindungsabbruch was tun (stefan, jannik) 12.
- [ ] (optional) mit begin warten bis gui go gibt

#### ai
- [ ] shoot schlauer machen (kat)
- [ ] delay (kat)
- [x] setships mergen
- [x] shooten bauen

## Anforderungen

### Anforderungen an den Einzelnen

- [x] klar definierte Aufgabe im Team und absolute Ergebnisverantwortung
- [ ] Mindestens 1000 Zeilen Code
- [ ] Sorgfältige Kommentierung und Dokumentation seines Codes
- [ ] Fehlerfreiheit des Codes
- [ ] Regelmäßige Teilnahme an allen Projekttreffen!
- [x] Vollständiges und pünktliches Einhalten von Terminen

### Anforderungen an die Gruppe
- [ ] vollständig erfasste Anwendungsfälle+gui.GUIMain-Prototyp (abgenommen durch Betreuer!)
- [x] Schichtenarchitektur (min. 2 Schichten; gui.GUIMain und Logik getrennt!)
- [ ] Sechs Semester (Spielstufen/Level)
- [ ] Sound!
- [ ] Eine „Überraschung” im Spiel
- [x] Projektplanung
- [ ] Sorgfältige Dokumentation
- [ ] fehlerfrei laufendes, absturz- und anwendungssicheres Spiel!
- [x] Lauffähig im Fenster (JFrame)
- [ ] Das Spiel muss gegen die Spiele der anderen Projektgruppen spielbar sein
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
