package dev.vhx.client.gui;

import dev.vhx.client.module.ClientModule;
import dev.vhx.client.module.ModuleManager;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public final class ClickGuiScreen extends Screen {
    private static final int PANEL_WIDTH = 300;
    private static final int ROW_HEIGHT = 32;
    private static final int PANEL = 0xF0181A21;
    private static final int ACCENT = 0xFF54C8FF;
    private static final int TEXT = 0xFFF0F2F7;
    private static final int MUTED = 0xFF9299A8;

    private final ModuleManager modules;

    public ClickGuiScreen(ModuleManager modules) {
        super(Component.literal("Supreme Octo Lamp"));
        this.modules = modules;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        int left = (width - PANEL_WIDTH) / 2;
        int top = Math.max(24, (height - 220) / 2);
        int panelHeight = 94 + modules.getModules().size() * ROW_HEIGHT;

        graphics.fill(left, top, left + PANEL_WIDTH, top + panelHeight, PANEL);
        graphics.fill(left, top, left + PANEL_WIDTH, top + 3, ACCENT);
        graphics.drawString(font, "SUPREME OCTO LAMP", left + 16, top + 15, TEXT);
        graphics.drawString(font, "CLIENT MODULES", left + 16, top + 33, MUTED);
        graphics.fill(left + 12, top + 51, left + PANEL_WIDTH - 12, top + 52, 0xFF2A2D36);

        List<ClientModule> entries = modules.getModules();
        for (int i = 0; i < entries.size(); i++) {
            ClientModule module = entries.get(i);
            int rowTop = top + 59 + i * ROW_HEIGHT;
            boolean hovered = mouseX >= left + 8 && mouseX <= left + PANEL_WIDTH - 8
                    && mouseY >= rowTop && mouseY < rowTop + ROW_HEIGHT;
            if (hovered) {
                graphics.fill(left + 8, rowTop, left + PANEL_WIDTH - 8, rowTop + ROW_HEIGHT, 0xFF242833);
            }
            graphics.drawString(font, module.getName(), left + 16, rowTop + 5, TEXT);
            graphics.drawString(font, module.isEnabled() ? "ON" : "OFF",
                    left + PANEL_WIDTH - 42, rowTop + 5, module.isEnabled() ? ACCENT : MUTED);
            graphics.drawString(font, module.getDescription(), left + 16, rowTop + 18, MUTED);
        }

        graphics.drawString(font, "Right Shift: close  |  Click a row: toggle",
                left + 12, top + panelHeight - 20, MUTED);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int left = (width - PANEL_WIDTH) / 2;
            int top = Math.max(24, (height - 220) / 2);
            int index = (int) ((mouseY - (top + 59)) / ROW_HEIGHT);
            if (mouseX >= left + 8 && mouseX <= left + PANEL_WIDTH - 8
                    && mouseY >= top + 59 && index >= 0
                    && index < modules.getModules().size()) {
                modules.getModules().get(index).toggle();
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}
