package dev.vhx.client.module.combat;

import dev.vhx.client.module.ClientModule;
import java.util.Comparator;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;

/**
 * Automatically attacks nearby non-player mobs.
 *
 * The target type is intentionally Mob, not Player or a generic Entity. This
 * keeps player entities outside this module's targeting path.
 */
public final class MobAuraModule extends ClientModule {
    private double range = 3.5;
    private Mob target;

    public MobAuraModule() {
        super("Mob Aura", "Attacks nearby non-player mobs only.");
    }

    @Override
    public void onTick(Minecraft client) {
        if (client.player == null || client.level == null || client.gameMode == null) {
            target = null;
            return;
        }

        target = client.level.getEntitiesOfClass(
                        Mob.class,
                        client.player.getBoundingBox().inflate(range),
                        candidate -> isValidTarget(client, candidate))
                .stream()
                .min(Comparator.comparingDouble(candidate -> client.player.distanceToSqr(candidate)))
                .orElse(null);

        if (target == null || client.player.getAttackStrengthScale(0.5F) < 0.95F) {
            return;
        }

        client.gameMode.attack(client.player, target);
        client.player.swing(InteractionHand.MAIN_HAND);
    }

    private boolean isValidTarget(Minecraft client, Mob candidate) {
        if (candidate.isRemoved() || !candidate.isAlive() || candidate.isInvisible()) {
            return false;
        }

        // Mob is the non-player mob base class; player entities are never candidates.
        return client.player.distanceToSqr(candidate) <= range * range
                && client.player.hasLineOfSight(candidate);
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = Math.max(1.0, Math.min(6.0, range));
    }

    public Mob getTarget() {
        return target;
    }

    @Override
    protected void onDisable() {
        target = null;
    }
}
