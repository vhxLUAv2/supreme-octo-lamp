/*
 * Adapted from Meteor Client's Zoom module.
 * Copyright (c) Meteor Development.
 * Upstream: https://github.com/MeteorDevelopment/meteor-client
 * License: GNU GPL version 3. This file has been modified for Supreme Octo Lamp.
 */
package dev.vhx.client.module.render;

import dev.vhx.client.module.ClientModule;
import net.minecraft.client.Minecraft;

/**
 * Provides a simple zoom by temporarily reducing the vanilla FOV setting.
 * The original FOV is restored when the module is disabled.
 */
public final class ZoomModule extends ClientModule {
    private static final int ZOOM_FOV = 30;

    private int previousFov;
    private boolean capturedFov;

    public ZoomModule() {
        super("Zoom", "Reduces the field of view while enabled.");
    }

    @Override
    public void onTick(Minecraft client) {
        if (!capturedFov) {
            previousFov = client.options.fov().get();
            capturedFov = true;
        }

        client.options.fov().set(ZOOM_FOV);
    }

    @Override
    protected void onDisable() {
        if (capturedFov) {
            Minecraft.getInstance().options.fov().set(previousFov);
            capturedFov = false;
        }
    }
}
