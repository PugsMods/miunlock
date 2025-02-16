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

import com.pugzarecute.miunlock.networking.NetworkingUtils;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MiUnlock implements ModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("MiUnlockMain");

    @Override
    public void onInitialize() {
        LOGGER.debug("Setting up networking");
        NetworkingUtils.registerPayloads();
        NetworkingUtils.registerServerNetworking();

        LOGGER.debug("Registering BlockBreakEvent");
        BlockBreakEvent.register();
    }
}
