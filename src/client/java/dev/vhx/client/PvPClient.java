package dev.vhx.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.vhx.client.gui.ClickGuiScreen;
import dev.vhx.client.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public final class PvPClient implements ClientModInitializer {
    public static final ModuleManager MODULES = new ModuleManager();

    private KeyMapping clickGuiKey;

    @Override
    public void onInitializeClient() {
        KeyMapping.Category category = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath("supreme_octo_lamp", "client"));

        clickGuiKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.supreme_octo_lamp.click_gui",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                category));

        MODULES.registerDefaults();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (clickGuiKey.consumeClick()) {
                if (client.player == null) {
                    continue;
                }

                if (client.gui.screen() == null) {
                    client.gui.setScreen(new ClickGuiScreen(MODULES));
                } else if (client.gui.screen() instanceof ClickGuiScreen) {
                    client.gui.setScreen(null);
                }
            }

            MODULES.tick(client);
        });
    }
}
