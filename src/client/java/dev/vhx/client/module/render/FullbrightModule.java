/*
 * Adapted from Wurst7's FullbrightHack.
 * Original: Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 * Upstream: https://github.com/Wurst-Imperium/Wurst7
 * License: GNU GPL version 3. This file has been modified for Supreme Octo Lamp.
 */
package dev.vhx.client.module.render;

import dev.vhx.client.module.ClientModule;
import net.minecraft.client.Minecraft;

/**
 * Uses the vanilla brightness option at its maximum supported value and
 * restores the user's prior value when disabled.
 */
public final class FullbrightModule extends ClientModule {
    private double previousGamma;
    private boolean capturedGamma;

    public FullbrightModule() {
        super("Fullbright", "Sets vanilla brightness to its maximum supported value.");
    }

    @Override
    public void onTick(Minecraft client) {
        if (client.player == null) {
            return;
        }

        if (!capturedGamma) {
            previousGamma = client.options.gamma().get();
            capturedGamma = true;
        }

        client.options.gamma().set(1.0);
    }

    @Override
    protected void onDisable() {
        if (capturedGamma) {
            Minecraft.getInstance().options.gamma().set(previousGamma);
            capturedGamma = false;
        }
    }
}
