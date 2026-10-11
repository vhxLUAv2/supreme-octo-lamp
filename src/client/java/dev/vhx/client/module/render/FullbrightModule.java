package dev.vhx.client.module.render;

import dev.vhx.client.module.ClientModule;
import net.minecraft.client.Minecraft;

/**
 * Raises client-side gamma while enabled and restores the user's previous value.
 * This implementation is independent of upstream client source.
 */
public final class FullbrightModule extends ClientModule {
    private Double previousGamma;

    public FullbrightModule() {
        super("Fullbright", "Raises brightness while enabled.");
    }

    @Override
    public void onTick(Minecraft client) {
        if (client == null) {
            return;
        }

        if (previousGamma == null) {
            previousGamma = client.options.gamma().get();
        }
        if (client.options.gamma().get() < 16.0) {
            client.options.gamma().set(16.0);
        }
    }

    @Override
    protected void onDisable() {
        Minecraft client = Minecraft.getInstance();
        if (previousGamma != null && client != null) {
            client.options.gamma().set(previousGamma);
        }
        previousGamma = null;
    }
}
