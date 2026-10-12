/*
 * Adapted from Wurst7's AutoToolHack.
 * Original: Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 * Upstream: https://github.com/Wurst-Imperium/Wurst7
 * License: GNU GPL version 3. This file has been modified for Supreme Octo Lamp.
 */
package dev.vhx.client.module.player;

import dev.vhx.client.module.ClientModule;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Selects the hotbar item with the best destroy speed for the block under the
 * crosshair. It only changes the selected slot; it does not break blocks.
 */
public final class AutoToolModule extends ClientModule {
    private int previousSlot = -1;
    private int lastAutoSlot = -1;

    public AutoToolModule() {
        super("Auto Tool", "Selects the most effective hotbar tool for the targeted block.");
    }

    @Override
    public void onTick(Minecraft client) {
        if (client.player == null || client.level == null) {
            restoreSlot(client);
            return;
        }

        if (!(client.hitResult instanceof BlockHitResult hit)
                || hit.getType() != HitResult.Type.BLOCK) {
            restoreSlot(client);
            return;
        }

        BlockState state = client.level.getBlockState(hit.getBlockPos());
        var inventory = client.player.getInventory();
        int selectedSlot = inventory.getSelectedSlot();
        ItemStack heldStack = inventory.getItem(selectedSlot);
        float bestSpeed = heldStack.getDestroySpeed(state);
        int bestSlot = selectedSlot;

        for (int slot = 0; slot < 9; slot++) {
            if (slot == selectedSlot) {
                continue;
            }

            ItemStack stack = inventory.getItem(slot);
            float speed = stack.getDestroySpeed(state);
            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = slot;
            }
        }

        if (bestSlot != selectedSlot) {
            if (previousSlot == -1) {
                previousSlot = selectedSlot;
            }
            inventory.setSelectedSlot(bestSlot);
            lastAutoSlot = bestSlot;
        }
    }

    private void restoreSlot(Minecraft client) {
        if (client.player == null || previousSlot == -1) {
            previousSlot = -1;
            lastAutoSlot = -1;
            return;
        }

        var inventory = client.player.getInventory();
        // Do not overwrite a slot change made manually by the player.
        if (inventory.getSelectedSlot() == lastAutoSlot) {
            inventory.setSelectedSlot(previousSlot);
        }

        previousSlot = -1;
        lastAutoSlot = -1;
    }

    @Override
    protected void onDisable() {
        restoreSlot(Minecraft.getInstance());
    }
}
