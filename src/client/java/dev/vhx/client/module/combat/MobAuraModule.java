package dev.vhx.client.module.combat;

import dev.vhx.client.module.ClientModule;
import java.util.Comparator;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;

/**
 * Attacks selected non-player mobs. Client-side options do not override
 * reach or attack validation performed by the connected server.
 */
public final class MobAuraModule extends ClientModule {
    private static final double DEFAULT_RANGE = 3.5;
    private static final double EXTENDED_SEARCH_RANGE = 16.0;

    private double range = DEFAULT_RANGE;
    private boolean targetPassive = true;
    private boolean targetHostile = true;
    private boolean cooldownBypass = true;
    private boolean rangeBypass = true;
    private Mob target;

    public MobAuraModule() {
        super("Mob Aura", "Automatically attacks selected non-player mobs.");
    }

    @Override
    public void onTick(Minecraft client) {
        if (client.player == null || client.level == null || client.gameMode == null) {
            target = null;
            return;
        }

        if (!targetPassive && !targetHostile) {
            target = null;
            return;
        }

        double effectiveRange = rangeBypass ? EXTENDED_SEARCH_RANGE : range;
        target = client.level.getEntitiesOfClass(
                        Mob.class,
                        client.player.getBoundingBox().inflate(effectiveRange),
                        candidate -> isValidTarget(client, candidate, effectiveRange))
                .stream()
                .min(Comparator.comparingDouble(candidate -> client.player.distanceToSqr(candidate)))
                .orElse(null);

        if (target == null) {
            return;
        }

        // This bypasses only the client's attack-strength gate. Server rules still apply.
        if (!cooldownBypass && client.player.getAttackStrengthScale(0.5F) < 0.95F) {
            return;
        }

        client.gameMode.attack(client.player, target);
        client.player.swing(InteractionHand.MAIN_HAND);
    }

    private boolean isValidTarget(Minecraft client, Mob candidate, double effectiveRange) {
        if (candidate.isRemoved() || !candidate.isAlive() || candidate.isInvisible()) {
            return false;
        }

        // Mob excludes Player. Vanilla MONSTER category defines the hostile selection.
        boolean hostile = candidate.getType().getCategory() == MobCategory.MONSTER;
        if (hostile && !targetHostile) {
            return false;
        }
        if (!hostile && !targetPassive) {
            return false;
        }

        return client.player.distanceToSqr(candidate) <= effectiveRange * effectiveRange
                && client.player.hasLineOfSight(candidate);
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = Math.max(1.0, Math.min(6.0, range));
    }

    public boolean targetsPassive() {
        return targetPassive;
    }

    public void setTargetPassive(boolean targetPassive) {
        this.targetPassive = targetPassive;
    }

    public boolean targetsHostile() {
        return targetHostile;
    }

    public void setTargetHostile(boolean targetHostile) {
        this.targetHostile = targetHostile;
    }

    public boolean isCooldownBypassEnabled() {
        return cooldownBypass;
    }

    public void setCooldownBypass(boolean cooldownBypass) {
        this.cooldownBypass = cooldownBypass;
    }

    public boolean isRangeBypassEnabled() {
        return rangeBypass;
    }

    public void setRangeBypass(boolean rangeBypass) {
        this.rangeBypass = rangeBypass;
    }

    public double getEffectiveRange() {
        return rangeBypass ? EXTENDED_SEARCH_RANGE : range;
    }

    public Mob getTarget() {
        return target;
    }

    @Override
    protected void onDisable() {
        target = null;
    }
}
