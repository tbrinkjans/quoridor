**Domain/Core:**

- Turn-Logik vom Game abtrennen? Game wie Board?
- Testing mit Gradle einrichten und UnitTests anlegen
- GitHub-Pipeline zum Ausführen der Tests konfigurieren
- Spiellogik von `app` in neues Projekt `domain` verschieben
- Spiel konfigurierbar machen (bspw. Feldgröße, Wände, KOTM)

**Application:**

- `application`-Projekt für `GameService`, `GameManager` und Views anlegen
- `GameEvents` für Benachrichtigung der Darstellung implementieren
- `GameClient`-Interface für Infrastructure-Projekte anlegen
- Notation für Züge bzw. Im-/Export vom Game einführen

**Infrastructure/Adapter:**

- `local`-Projekt für Offline-Multiplayer (als reine Bridge) anlegen
- `remote`-Projekt für Online-Multiplayer (HTTP/WebSockets) anlegen

**Presentation/UI:**

- `terminal`-Projekt für Terminal-UI anlegen
- `swing`-Projekt für Swing-UI anlegen
