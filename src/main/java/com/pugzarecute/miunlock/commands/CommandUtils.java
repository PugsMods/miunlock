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

package com.pugzarecute.miunlock.commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.util.math.ChunkPos;

public class CommandUtils {
    public static void register(){
        CommandRegistrationCallback.EVENT.register(((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
            commandDispatcher.register(CommandManager.literal("get_unlock_token")
                            .requires(source -> source.hasPermissionLevel(2))
                            .executes(commandContext -> {
                   if(commandContext.getSource().isExecutedByPlayer()){
                        PlayerEntity executor = commandContext.getSource().getPlayer();
                       assert executor != null;
                       ChunkPos chunk = executor.getChunkPos();
                        String token = "PUG-"+chunk.x+"-"+chunk.z+"-"+executor.getName().getString()+"-"+(Math.abs(chunk.x+chunk.z)%7);

                       commandContext.getSource().sendFeedback(() -> Text.literal(token),false);

                       return 0;
                   }else{
                       commandContext.getSource().sendFeedback(() -> Text.literal("MiUnlock can only unlock MiHuman!"),false);
                       return 1;
                   }
            }));
        }));
    }
}
