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

package com.pugzarecute.miunlock.client.gui;

import com.pugzarecute.miunlock.networking.SendUnlockCodePayload;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class SystemDestroyedScreen extends Screen {
    private static final Identifier BACKGROUND_TEXTURE = Identifier.of("miunlock", "textures/gui/unlock_gui.png");

    private TextFieldWidget codeInput;
    private ButtonWidget confirmButton;
    private final BlockPos pos;
    private final String action;

    public SystemDestroyedScreen(BlockPos pos, String action) {
        super(Text.translatable("screen.miunlock.unlock.title"));
        this.pos = pos;
        this.action = action;
    }

    @Override
    protected void init() {
        this.codeInput = new TextFieldWidget(textRenderer, 200, 20, Text.translatable("screen.miunlock.unlock.placeholder"));
        this.addSelectableChild(this.codeInput);

        this.setInitialFocus(this.codeInput);

        this.confirmButton = ButtonWidget.builder(Text.translatable("screen.miunlock.unlock.confirm"), button -> {
                    String enteredCode = codeInput.getText();
                    validateCode(enteredCode);
                }).dimensions(width / 2 - 50, height / 2 + 10, 100, 20)
                .build();

        this.addDrawableChild(this.confirmButton);
    }

    private void validateCode(String code) {
        ClientPlayNetworking.send(new SendUnlockCodePayload(pos, code, action));

        assert client != null;
        Objects.requireNonNull(client.currentScreen).close();
    }

    @Override
    public void render(DrawContext matrices, int mouseX, int mouseY, float delta) {
        // We don't call super.drawBackground() since it causes the entire thing to darken
        super.render(matrices, mouseX, mouseY, delta);

        matrices.drawTexture(RenderLayer::getGuiTextured, BACKGROUND_TEXTURE, 0, 0, 0, 0, width, height, width, height);

        //renderBackgroundTexture();
        // Render the inputs
        int inputWidth = codeInput.getWidth();
        int inputHeight = codeInput.getHeight();

        int buttonWidth = confirmButton.getWidth();

        this.codeInput.setX((width - inputWidth - buttonWidth) / 2);
        this.codeInput.setY(height - inputHeight);
        this.codeInput.render(matrices, mouseX, mouseY, delta);

        this.confirmButton.setX((width + inputWidth - buttonWidth) / 2);
        this.confirmButton.setY(height - inputHeight);
        this.confirmButton.render(matrices, mouseX, mouseY, delta);


    }

}
