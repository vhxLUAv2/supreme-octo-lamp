package dev.vhx.client;

import dev.vhx.client.gui.ClickGuiScreen;
import dev.vhx.client.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.lwjgl.glfw.GLFW;

public final class PvPClient implements ClientModInitializer {
    public static final ModuleManager MODULES = new ModuleManager();
    private boolean guiKeyWasDown;

    @Override
    public void onInitializeClient() {
        MODULES.registerDefaults();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            boolean keyDown = GLFW.glfwGetKey(
                    client.getWindow().getWindow(), GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
            if (keyDown && !guiKeyWasDown && client.screen == null) {
                client.setScreen(new ClickGuiScreen(MODULES));
            }
            guiKeyWasDown = keyDown;
            MODULES.tick(client);
        });
    }
}
