# Upstream source and licensing

This project is licensed under GNU GPL version 3. The full license text is in [LICENSE](LICENSE).

The following source projects were inspected and used as implementation references. Adapted source files retain upstream copyright attribution in their headers.

## Wurst7

- Repository: https://github.com/Wurst-Imperium/Wurst7
- License: GNU GPL-3.0 (see upstream `LICENSE.txt`)
- Source references:
  - `src/main/java/net/wurstclient/hacks/AutoToolHack.java`
  - `src/main/java/net/wurstclient/hacks/FullbrightHack.java`
- Source snapshot inspected: `f98551a3bfab97a1e70c340f91b334976c7fc4c2`
- Adapted files in this project:
  - `src/client/java/dev/vhx/client/module/player/AutoToolModule.java`
  - `src/client/java/dev/vhx/client/module/render/FullbrightModule.java`

## Meteor Client

- Repository: https://github.com/MeteorDevelopment/meteor-client
- License: GNU GPL-3.0 (see upstream `LICENSE`)
- Source reference:
  - `src/main/java/meteordevelopment/meteorclient/systems/modules/render/Zoom.java`
- Source snapshot inspected: `2385f7b4bb9de1a698b4503254bd32f30fd7a87d`
- Adapted file in this project:
  - `src/client/java/dev/vhx/client/module/render/ZoomModule.java`

These are focused ports/adaptations of module behavior, not complete copies of either upstream client. Upstream projects retain their original copyrights. Adaptations are modified for this project's existing module API and Minecraft 26.2 target. The original upstream repositories and complete source histories remain available at the links above.
