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

package com.pugzarecute.miunlock.mixin;

import com.pugzarecute.miunlock.networking.UnlockGUIPayload;
import com.pugzarecute.miunlock.unlocking.ChunkAttachments;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at=@At("HEAD"),  cancellable = true)
    private void onBlockPlaced(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir){
        if(context.getPlayer() instanceof ServerPlayerEntity){
                BlockPos pos = context.getBlockPos();

                @Nullable
                Integer existing_timestamp = context.getWorld().getChunk(pos).getAttached(ChunkAttachments.UNLOCK_TIMESTAMP);

                if (existing_timestamp == null) {
                    ServerPlayNetworking.send((ServerPlayerEntity) context.getPlayer(), new UnlockGUIPayload(pos, "open"));
                    cir.setReturnValue(ActionResult.FAIL);
                    return;
                }

                boolean status = (int) (System.currentTimeMillis() / 1000L) > existing_timestamp;

                if (!status) {
                    context.getPlayer().sendMessage(Text.literal("Please try placing in " + (existing_timestamp - (int) (System.currentTimeMillis() / 1000L)) + "seconds!"), true);
                    cir.setReturnValue(ActionResult.FAIL);
                    return;
                }
                //Don't return, continue!
        }
    }
}
