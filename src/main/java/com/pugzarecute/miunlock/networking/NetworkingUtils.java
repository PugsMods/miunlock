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

package com.pugzarecute.miunlock.networking;

import com.pugzarecute.miunlock.unlocking.ChunkAttachments;
import com.pugzarecute.miunlock.unlocking.TokenVerifier;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.text.Text;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
public class NetworkingUtils {
    public static void registerServerNetworking() {

        ServerPlayNetworking.registerGlobalReceiver(SendUnlockCodePayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                Chunk chunk = context.player().getServerWorld().getChunk(payload.pos());

                if (!TokenVerifier.verify(payload.code(), chunk.getPos(), context.player())) {
                    if(payload.action().equals("break")) {
                        context.player().sendMessage(Text.literal("Token verify fail! Rebooting device"), true);
                    }else {
                        context.player().sendMessage(Text.literal("Flashing is not allowed in locked state!"), true);
                    }
                    context.player().kill(context.player().getServerWorld());
                    return;
                }

                @Nullable
                Integer existing_timestamp = chunk.getAttached(ChunkAttachments.UNLOCK_TIMESTAMP);


                if (existing_timestamp == null) {
                    chunk.setAttached(ChunkAttachments.UNLOCK_TIMESTAMP, (int) (System.currentTimeMillis() / 1000L + 30L));
                    context.player().sendMessage(Text.literal("Account bound successfully!"), true);
                }
            });
        });
    }

    public static void registerPayloads(){
        PayloadTypeRegistry.playS2C().register(UnlockGUIPayload.ID, UnlockGUIPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SendUnlockCodePayload.ID, SendUnlockCodePayload.CODEC);
    }
}
