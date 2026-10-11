package dev.vhx.client.module.combat;

import dev.vhx.client.module.ClientModule;
import java.util.Comparator;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public final class TargetPreviewModule extends ClientModule {
    private double range = 4.5;
    private Player target;

    public TargetPreviewModule() {
        super("Target Preview", "Selects the nearest valid player in range; does not aim or attack.");
    }

    @Override
    public void onTick(Minecraft client) {
        if (client.player == null || client.level == null) {
            target = null;
            return;
        }

        target = client.level.players().stream()
                .filter(candidate -> isValidTarget(client, candidate))
                .min(Comparator.comparingDouble(candidate -> client.player.distanceToSqr(candidate)))
                .orElse(null);
    }

    private boolean isValidTarget(Minecraft client, Player candidate) {
        if (candidate == client.player || candidate.isRemoved() || !candidate.isAlive()) return false;
        if (candidate.isSpectator() || candidate.isInvisible()) return false;
        return client.player.distanceToSqr(candidate) <= range * range;
    }

    public Optional<Player> getTarget() { return Optional.ofNullable(target); }
    public double getRange() { return range; }

    public void setRange(double range) {
        this.range = Math.max(1.0, Math.min(6.0, range));
    }

    @Override
    protected void onDisable() { target = null; }
}
