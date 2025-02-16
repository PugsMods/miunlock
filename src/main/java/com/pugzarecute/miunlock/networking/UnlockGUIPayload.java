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

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record UnlockGUIPayload(BlockPos pos) implements CustomPayload {
    public static final CustomPayload.Id<UnlockGUIPayload> ID = new CustomPayload.Id<>(NetworkingConstants.OPEN_UNLOCK_GUI);
    public static final PacketCodec<RegistryByteBuf, UnlockGUIPayload> CODEC = PacketCodec.tuple(BlockPos.PACKET_CODEC, UnlockGUIPayload::pos, UnlockGUIPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
