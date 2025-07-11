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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenVerifier {
    private static final Logger LOGGER = LoggerFactory.getLogger("TokenVerifier");

    public static boolean verify(String token, ChunkPos pos, PlayerEntity player) {
        Pattern verificationPattern = Pattern.compile("[A-Z]{3}-(-?\\d+)-(-?\\d+)-([a-zA-Z0-9]+)-(-?\\d+)"); //"[A-Z]{3}-(-?\d+)-(-?\d+)-([a-zA-Z0-9]+)-(-?\d+)"gm
        Matcher patternMatcher = verificationPattern.matcher(token);
        boolean matches = patternMatcher.matches();

        if (!matches) return false;
        LOGGER.debug("Pattern match check succeed");

        int chunkX = Integer.parseInt(patternMatcher.group(1));
        int chunkZ = Integer.parseInt(patternMatcher.group(2));
        String playerName = patternMatcher.group(3);
        int checkDigit = Integer.parseInt(patternMatcher.group(4));

        if (chunkX != pos.x) return false;
        LOGGER.debug("X check succeed");

        if (chunkZ != pos.z) return false;
        LOGGER.debug("Z check succeed");

        if (!playerName.equals(player.getName().getString())) return false;
        LOGGER.debug("Name check succeed");

        int modulo = (Math.abs(chunkX + chunkZ) - checkDigit) % 7;
        LOGGER.debug("Modulo = {} ", modulo);
        return modulo == 0;
    }

}
