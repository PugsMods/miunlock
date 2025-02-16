package com.pugzarecute.miunlock.client;

import com.pugzarecute.miunlock.client.gui.UnlockScreen;
import com.pugzarecute.miunlock.networking.UnlockGUIPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MiUnlockClient implements ClientModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("MiUnlockClient");

    @Override
    public void onInitializeClient() {
        LOGGER.debug("Registering packet receiver");
        ClientPlayNetworking.registerGlobalReceiver(UnlockGUIPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                MinecraftClient.getInstance().setScreen(new UnlockScreen(payload.pos()));
            });
        });
    }
}
