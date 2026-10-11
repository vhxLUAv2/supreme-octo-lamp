package dev.vhx.client.config;

import dev.vhx.client.module.ClientModule;
import dev.vhx.client.module.ModuleManager;
import dev.vhx.client.module.combat.MobAuraModule;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import net.fabricmc.loader.api.FabricLoader;

/**
 * Stores user-facing module settings in the normal Minecraft config directory.
 * Missing or malformed values are ignored so a damaged config cannot prevent startup.
 */
public final class ConfigManager {
    private static final Path CONFIG_PATH =
            FabricLoader.getInstance().getConfigDir().resolve("supreme-octo-lamp.properties");

    private ConfigManager() { }

    public static void load(ModuleManager modules) {
        if (!Files.isRegularFile(CONFIG_PATH)) {
            save(modules);
            return;
        }

        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(CONFIG_PATH)) {
            properties.load(input);
        } catch (IOException exception) {
            System.err.println("[Supreme Octo Lamp] Could not read config: " + exception.getMessage());
            return;
        }

        for (ClientModule module : modules.getModules()) {
            String enabled = properties.getProperty(moduleKey(module, "enabled"));
            if (enabled != null) {
                module.setEnabled(Boolean.parseBoolean(enabled));
            }
        }

        for (ClientModule module : modules.getModules()) {
            if (module instanceof MobAuraModule aura) {
                aura.setRange(readDouble(properties, "mobAura.range", aura.getRange(), 1.0, 6.0));
                aura.setTargetPassive(readBoolean(properties, "mobAura.passive", aura.targetsPassive()));
                aura.setTargetHostile(readBoolean(properties, "mobAura.hostile", aura.targetsHostile()));
                aura.setCooldownBypass(readBoolean(properties, "mobAura.cooldownBypass", aura.isCooldownBypassEnabled()));
                aura.setRangeBypass(readBoolean(properties, "mobAura.rangeBypass", aura.isRangeBypassEnabled()));
            }
        }
    }

    public static void save(ModuleManager modules) {
        Properties properties = new Properties();
        for (ClientModule module : modules.getModules()) {
            properties.setProperty(moduleKey(module, "enabled"), Boolean.toString(module.isEnabled()));
        }

        for (ClientModule module : modules.getModules()) {
            if (module instanceof MobAuraModule aura) {
                properties.setProperty("mobAura.range", Double.toString(aura.getRange()));
                properties.setProperty("mobAura.passive", Boolean.toString(aura.targetsPassive()));
                properties.setProperty("mobAura.hostile", Boolean.toString(aura.targetsHostile()));
                properties.setProperty("mobAura.cooldownBypass", Boolean.toString(aura.isCooldownBypassEnabled()));
                properties.setProperty("mobAura.rangeBypass", Boolean.toString(aura.isRangeBypassEnabled()));
            }
        }

        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (OutputStream output = Files.newOutputStream(CONFIG_PATH)) {
                properties.store(output, "Supreme Octo Lamp client settings");
            }
        } catch (IOException exception) {
            System.err.println("[Supreme Octo Lamp] Could not save config: " + exception.getMessage());
        }
    }

    private static String moduleKey(ClientModule module, String setting) {
        return "module." + module.getName() + "." + setting;
    }

    private static boolean readBoolean(Properties properties, String key, boolean fallback) {
        String value = properties.getProperty(key);
        return value == null ? fallback : Boolean.parseBoolean(value);
    }

    private static double readDouble(Properties properties, String key, double fallback,
                                     double minimum, double maximum) {
        String value = properties.getProperty(key);
        if (value == null) {
            return fallback;
        }
        try {
            double parsed = Double.parseDouble(value);
            if (!Double.isFinite(parsed)) {
                return fallback;
            }
            return Math.max(minimum, Math.min(maximum, parsed));
        } catch (NumberFormatException exception) {
            return fallback;
        }
    }
}
