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

package com.pugzarecute.miunlock.unlocking;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.ChunkPos;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenVerifier {
    public static boolean verify(String token, ChunkPos pos, PlayerEntity player) {
        Pattern verificationPattern = Pattern.compile("[A-Z]{3}-(-?\\d+)-(-?\\d+)-([a-zA-Z0-9]+)-(-?\\d+)"); //"[A-Z]{3}-(-?\d+)-(-?\d+)-([a-zA-Z0-9]+)-(-?\d+)"gm
        Matcher patternMatcher = verificationPattern.matcher(token);
        boolean matches = patternMatcher.matches();

        if (!matches) return false;

        int chunkX = Integer.parseInt(patternMatcher.group(1));
        int chunkZ = Integer.parseInt(patternMatcher.group(2));
        String playerName = patternMatcher.group(3);
        int checkDigit = Integer.parseInt(patternMatcher.group(4));

        if (chunkX != pos.x) return false;

        if (chunkZ != pos.z) return false;

        if (!playerName.equals(player.getName().getString())) return false;

        return chunkX + chunkZ + checkDigit % 7 == 0;
    }

}
