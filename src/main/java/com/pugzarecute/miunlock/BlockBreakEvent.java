/*
 * Copyright (c) 2025 PugzAreCute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.pugzarecute.miunlock;

import com.pugzarecute.miunlock.networking.UnlockGUIPayload;
import com.pugzarecute.miunlock.unlocking.ChunkAttachments;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class BlockBreakEvent {
    public static void register() {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            @Nullable
            Integer existing_timestamp = world.getChunk(pos).getAttached(ChunkAttachments.UNLOCK_TIMESTAMP);

            if (player instanceof ServerPlayerEntity && existing_timestamp == null) {
                ServerPlayNetworking.send((ServerPlayerEntity) player, new UnlockGUIPayload(pos));
                return false;
            }

            if (existing_timestamp == null) return false;

            boolean status = (int) (System.currentTimeMillis() / 1000L) > existing_timestamp;

            if (!status) {
                player.sendMessage(Text.literal("Please try breaking in " + (existing_timestamp - (int) (System.currentTimeMillis() / 1000L)) + "!"), true);
                return false;
            }

            return true;
        });
    }
}
