package dev.vhx.client.module;

import net.minecraft.client.Minecraft;

public abstract class ClientModule {
    private final String name;
    private final String description;
    private boolean enabled;

    protected ClientModule(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public final String getName() { return name; }
    public final String getDescription() { return description; }
    public final boolean isEnabled() { return enabled; }

    public final void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        if (enabled) onEnable();
        else onDisable();
    }

    public final void toggle() { setEnabled(!enabled); }

    public void onTick(Minecraft client) { }
    protected void onEnable() { }
    protected void onDisable() { }
}
