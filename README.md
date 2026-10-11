# Supreme Octo Lamp

A client-side Fabric mod foundation for a configurable Minecraft PvP client.

## Current foundation

- Fabric client mod targeting Minecraft Java 26.3 and Java 25.
- Dark ClickGUI opened with **Right Shift**.
- Module registry with click-to-toggle rows.
- **Target Preview**: when enabled, selects the nearest visible, alive, non-spectator player within range. It does not rotate the camera or send attack inputs.

## Build

Install JDK 25 and Gradle 9.7.1, then run:

```sh
gradle build
```

The distributable JAR is written to `build/libs/`.

## Next

Add configurable module settings and automated tests for target-selection rules before extending combat behavior. Use any automation only on servers where it is explicitly permitted.
