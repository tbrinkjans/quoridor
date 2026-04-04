**Domain:**

- Parameter testen (bspw. ob `Player` in `players`)
- Testing mit Gradle einrichten und UnitTests anlegen
- GitHub-Pipeline zum Ausführen der Tests konfigurieren
- Spiellogik von `app` in neues Projekt `domain` verschieben
- Spiel konfigurierbar machen (bspw. Spielfeldgröße, Wände, ...)

**Application:**

- `application`-Projekt für `GameService`, `GameManager` und Views anlegen
- `GameEvents` für Benachrichtigung der Darstellung implementieren
- Notation für Züge bzw. Im-/Export vom Game einführen

**Transport:**

- `local`-Projekt für Offline-Multiplayer (als reine Bridge) anlegen
- `remote`-Projekt für Online-Multiplayer (HTTP/WebSockets) anlegen

**Presentation:**

- `terminal`-Projekt für Terminal-UI anlegen
- `swing`-Projekt für Swing-UI anlegen
