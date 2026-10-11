# Supreme Octo Lamp

A client-side Fabric mod foundation for a configurable Minecraft PvP client.

## Current features

- Minecraft Java 26.2, Fabric Loader 0.19.5, Fabric API 0.161.0+26.2, and Java 25.
- Dark ClickGUI opened with **Right Shift**.
- Module registry with click-to-toggle rows.
- **Target Preview**: identifies the nearest eligible player in range; it does not aim or attack.
- **Mob Aura**: when enabled, automatically attacks the nearest visible non-player mob within 3.5 blocks, respecting the vanilla attack cooldown. Player entities are not valid targets. Range is clamped to 1–6 blocks in the module API.

Use automated combat only on servers where it is explicitly permitted.

## Build

Install JDK 25 and Gradle 9.7.1, then run:

```sh
gradle build
```

The distributable JAR is written to `build/libs/`. GitHub Actions also builds the project and uploads the JAR as an artifact.

## Next

Add a GUI for module settings and automated tests for target selection and attack eligibility.
