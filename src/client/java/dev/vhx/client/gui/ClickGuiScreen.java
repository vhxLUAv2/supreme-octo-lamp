package dev.vhx.client.gui;

import dev.vhx.client.config.ConfigManager;
import dev.vhx.client.module.ClientModule;
import dev.vhx.client.module.ModuleManager;
import dev.vhx.client.module.combat.MobAuraModule;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class ClickGuiScreen extends Screen {
    private static final int PANEL_WIDTH = 370;
    private static final int MODULE_ROW_HEIGHT = 23;
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
    protected void init() {
        List<ClientModule> entries = modules.getModules();
        int panelHeight = 46 + entries.size() * MODULE_ROW_HEIGHT + 190;
        int left = (width - PANEL_WIDTH) / 2;
        int top = Math.max(12, (height - panelHeight) / 2);
        int settingsY = top + 46 + entries.size() * MODULE_ROW_HEIGHT + 6;
        int buttonX = left + 16;
        int buttonWidth = PANEL_WIDTH - 32;

        // Decorative panel is added first so interactive controls render above it.
        addRenderableWidget(new PanelBackgroundWidget(
                left, top, PANEL_WIDTH, panelHeight, settingsY - top, font));

        for (int i = 0; i < entries.size(); i++) {
            ClientModule module = entries.get(i);
            int y = top + 45 + i * MODULE_ROW_HEIGHT;
            addRenderableWidget(Button.builder(moduleLabel(module), button -> {
                module.toggle();
                button.setMessage(moduleLabel(module));
                ConfigManager.save(modules);
            }).bounds(buttonX, y, buttonWidth, 20).build());
        }

        MobAuraModule aura = entries.stream()
                .filter(MobAuraModule.class::isInstance)
                .map(MobAuraModule.class::cast)
                .findFirst()
                .orElse(null);

        if (aura != null) {
            int halfWidth = (buttonWidth - 8) / 2;
            addRenderableWidget(Button.builder(settingLabel("Passive mobs", aura.targetsPassive()), button -> {
                aura.setTargetPassive(!aura.targetsPassive());
                button.setMessage(settingLabel("Passive mobs", aura.targetsPassive()));
                ConfigManager.save(modules);
            }).bounds(buttonX, settingsY + 17, halfWidth, 20).build());

            addRenderableWidget(Button.builder(settingLabel("Hostile mobs", aura.targetsHostile()), button -> {
                aura.setTargetHostile(!aura.targetsHostile());
                button.setMessage(settingLabel("Hostile mobs", aura.targetsHostile()));
                ConfigManager.save(modules);
            }).bounds(buttonX + halfWidth + 8, settingsY + 17, halfWidth, 20).build());

            addRenderableWidget(Button.builder(settingLabel("Cooldown bypass", aura.isCooldownBypassEnabled()), button -> {
                aura.setCooldownBypass(!aura.isCooldownBypassEnabled());
                button.setMessage(settingLabel("Cooldown bypass", aura.isCooldownBypassEnabled()));
                ConfigManager.save(modules);
            }).bounds(buttonX, settingsY + 41, buttonWidth, 20).build());

            addRenderableWidget(Button.builder(settingLabel("Range bypass", aura.isRangeBypassEnabled()), button -> {
                aura.setRangeBypass(!aura.isRangeBypassEnabled());
                button.setMessage(settingLabel("Range bypass", aura.isRangeBypassEnabled()));
                ConfigManager.save(modules);
            }).bounds(buttonX, settingsY + 65, buttonWidth, 20).build());

            addRenderableWidget(Button.builder(rangeLabel(aura), button -> {
                aura.setRange(aura.getRange() >= 6.0 ? 1.0 : aura.getRange() + 0.5);
                button.setMessage(rangeLabel(aura));
                ConfigManager.save(modules);
            }).bounds(buttonX, settingsY + 89, buttonWidth, 20).build());
        }

        addRenderableWidget(Button.builder(Component.literal("Close  (Esc)"), button -> onClose())
                .bounds(buttonX, settingsY + 139, buttonWidth, 20).build());
    }

    private static Component moduleLabel(ClientModule module) {
        return Component.literal(module.getName() + (module.isEnabled() ? "  [ON]" : "  [OFF]"));
    }

    private static Component settingLabel(String name, boolean enabled) {
        return Component.literal(name + (enabled ? ": ON" : ": OFF"));
    }

    private static Component rangeLabel(MobAuraModule aura) {
        return Component.literal("Attack range: " + String.format(java.util.Locale.ROOT, "%.1f", aura.getRange())
                + " blocks  (click to cycle)");
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static final class PanelBackgroundWidget extends AbstractWidget {
        private final int settingsOffset;
        private final net.minecraft.client.gui.Font panelFont;

        private PanelBackgroundWidget(int x, int y, int width, int height,
                                      int settingsOffset, net.minecraft.client.gui.Font panelFont) {
            super(x, y, width, height, Component.empty());
            this.settingsOffset = settingsOffset;
            this.panelFont = panelFont;
            this.active = false;
        }

        @Override
        protected void extractWidgetRenderState(GuiGraphicsExtractor graphics,
                                                int mouseX, int mouseY, float delta) {
            int x = getX();
            int y = getY();
            graphics.fill(x, y, x + width, y + height, PANEL);
            graphics.fill(x, y, x + width, y + 3, ACCENT);
            graphics.text(panelFont, "SUPREME OCTO LAMP", x + 16, y + 13, TEXT, true);
            graphics.text(panelFont, "MODULES  /  CONFIGURATION", x + 16, y + 29, MUTED, true);
            graphics.fill(x + 12, y + 39, x + width - 12, y + 40, 0xFF2A2D36);
            graphics.text(panelFont, "Target filters", x + 16, y + settingsOffset + 2, TEXT, true);
            graphics.text(panelFont, "Settings save automatically to your Minecraft config folder.", x + 16,
                    y + settingsOffset + 116, MUTED, false);
            graphics.text(panelFont, "Range bypass expands search only; vanilla reach still applies.", x + 16,
                    y + settingsOffset + 128, MUTED, false);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput builder) {
            // Decorative panel only; controls have their own narration.
        }
    }
}
