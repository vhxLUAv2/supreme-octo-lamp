package dev.vhx.client.module.player;

import dev.vhx.client.module.ClientModule;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Selects the hotbar tool with the highest vanilla destroy speed for the
 * block currently targeted while the attack key is held.
 * This implementation is independent of upstream client source.
 */
public final class AutoToolModule extends ClientModule {
    public AutoToolModule() {
        super("AutoTool", "Selects the best hotbar tool for the targeted block.");
    }

    @Override
    public void onTick(Minecraft client) {
        if (client == null || client.player == null || client.level == null
                || client.gui.screen() != null
                || !client.options.keyAttack.isDown()
                || !(client.hitResult instanceof BlockHitResult blockHit)) {
            return;
        }

        BlockState state = client.level.getBlockState(blockHit.getBlockPos());
        int currentSlot = client.player.getInventory().selected;
        int bestSlot = currentSlot;
        float bestSpeed = client.player.getInventory().getItem(currentSlot).getDestroySpeed(state);

        for (int slot = 0; slot < 9; slot++) {
            float speed = client.player.getInventory().getItem(slot).getDestroySpeed(state);
            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = slot;
            }
        }

        if (bestSlot != currentSlot) {
            client.player.getInventory().selected = bestSlot;
        }
    }
}
