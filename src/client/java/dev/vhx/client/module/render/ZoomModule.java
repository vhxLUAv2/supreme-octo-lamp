package dev.vhx.client.module.render;

import dev.vhx.client.module.ClientModule;
import net.minecraft.client.Minecraft;

/**
 * A toggleable zoom mode. Stores and restores the player's configured FOV.
 * This implementation is independent of upstream client source.
 */
public final class ZoomModule extends ClientModule {
    private static final int ZOOM_FOV = 30;
    private Integer previousFov;

    public ZoomModule() {
        super("Zoom", "Reduces field of view while enabled.");
    }

    @Override
    public void onTick(Minecraft client) {
        if (client == null) {
            return;
        }

        if (previousFov == null) {
            previousFov = client.options.fov().get();
        }
        if (client.options.fov().get() != ZOOM_FOV) {
            client.options.fov().set(ZOOM_FOV);
        }
    }

    @Override
    protected void onDisable() {
        Minecraft client = Minecraft.getInstance();
        if (previousFov != null && client != null) {
            client.options.fov().set(previousFov);
        }
        previousFov = null;
    }
}
