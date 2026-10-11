# Supreme Octo Lamp

A client-side Fabric mod foundation for a configurable Minecraft PvP client.

## Current features

- Minecraft Java 26.2, Fabric Loader 0.19.5, Fabric API 0.161.0+26.2, and Java 25.
- Dark ClickGUI opened by the configurable **Right Shift** key binding.
- Module registry with click-to-toggle rows.
- **Target Preview**: identifies the nearest eligible player; it does not aim or attack.
- **Mob Aura**: attacks non-player mobs with separate Passive and Hostile filters.
- **Attack Cooldown Bypass**: skips the client's vanilla attack-strength gate and sends attack attempts every client tick.
- **Range Bypass**: expands client-side target search to 16 blocks; without server cooperation, hits outside server-approved reach can still be rejected.

The player entity is not eligible for Mob Aura. In the selector, vanilla `MobCategory.MONSTER` entities count as Hostile; other mob categories count as Passive. Automated combat should only be used where the server explicitly permits it.

## Build

Install JDK 25 and Gradle 9.7.1, then run:

```sh
gradle build
```

The distributable JAR is written to `build/libs/`. GitHub Actions builds the project and uploads the JAR as an artifact.
