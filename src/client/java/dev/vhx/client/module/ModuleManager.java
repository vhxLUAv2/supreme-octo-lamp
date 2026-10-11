package dev.vhx.client.module;

import dev.vhx.client.module.combat.MobAuraModule;
import dev.vhx.client.module.combat.TargetPreviewModule;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;

public final class ModuleManager {
    private final List<ClientModule> modules = new ArrayList<>();

    public void registerDefaults() {
        if (modules.isEmpty()) {
            register(new TargetPreviewModule());
            register(new MobAuraModule());
        }
    }

    public void register(ClientModule module) {
        modules.add(module);
    }

    public List<ClientModule> getModules() {
        return Collections.unmodifiableList(modules);
    }

    public void tick(Minecraft client) {
        for (ClientModule module : modules) {
            if (module.isEnabled()) {
                module.onTick(client);
            }
        }
    }
}
