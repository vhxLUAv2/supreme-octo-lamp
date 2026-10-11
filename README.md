# Supreme Octo Lamp

A client-side Fabric mod foundation for a configurable Minecraft client focused on single-player worlds.

## Current features

- Minecraft Java 26.2, Fabric Loader 0.19.5, Fabric API 0.161.0+26.2, and Java 25.
- Dark ClickGUI opened by the configurable **Right Shift** key binding.
- Module registry with click-to-toggle rows.
- **Persistent settings**: module enabled states and Mob Aura settings are saved to `config/supreme-octo-lamp.properties`.
- **Target Preview**: identifies the nearest eligible player; it does not aim or attack.
- **Mob Aura**: attacks non-player mobs with separate Passive and Hostile filters.
- **Fullbright**: raises gamma while enabled and restores the previous gamma when disabled.
- **Zoom**: reduces the field of view while enabled and restores the previous FOV when disabled.
- **AutoTool**: selects the hotbar item with the highest vanilla destroy speed for the targeted block while attack is held.
- **Attack Cooldown Bypass**: skips the client's vanilla attack-strength gate and sends attack attempts every client tick.
- **Range Bypass**: expands client-side target search to 16 blocks; it does not change vanilla's actual interaction reach.
- **Mob Aura range control**: click the range button to cycle from 1.0 to 6.0 blocks in 0.5-block increments.

The player entity is not eligible for Mob Aura. In the selector, vanilla `MobCategory.MONSTER` entities count as Hostile; other mob categories count as Passive. Range bypass affects target search only and cannot make vanilla accept attacks outside the game's valid reach.

Open-source client references and integration notes are documented in [UPSTREAMS.md](UPSTREAMS.md).

## Build

Install JDK 25 and Gradle 9.7.1, then run:

```sh
gradle build
```

The distributable JAR is written to `build/libs/`. GitHub Actions builds the project and uploads the JAR as an artifact.
