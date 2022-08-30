package me.flashyreese.mods.sodiumextra.mixin.gui;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import me.flashyreese.mods.sodiumextra.client.gui.SodiumExtraGameOptions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ForgeIngameGui.class)
public abstract class MixinInGameHud extends InGameHud {

    public MixinInGameHud(MinecraftClient client) {
        super(client);
    }

    @Inject(method = "renderHUDText", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;defaultBlendFunc()V", shift = At.Shift.AFTER))
    public void render(int width, int height, MatrixStack matrices, CallbackInfo callbackInfo) {
        if (!this.client.options.debugEnabled) {
            //Gotta love hardcoding
            if (SodiumExtraClientMod.options().extraSettings.showFps && SodiumExtraClientMod.options().extraSettings.showCoords) {
                this.renderFPS(matrices);
                this.renderCoords(matrices);
            } else if (SodiumExtraClientMod.options().extraSettings.showFps) {
                this.renderFPS(matrices);
            } else if (SodiumExtraClientMod.options().extraSettings.showCoords) {
                this.renderCoords(matrices);
            }
            if (!SodiumExtraClientMod.options().renderSettings.lightUpdates) {
                this.renderLightUpdatesWarning(matrices);
            }
        }
    }

    //Should I make this OOP or just leave as it :> I don't think I will be adding any more than these 2.
    private void renderFPS(MatrixStack matrices) {
        int currentFPS = MinecraftClientAccessor.getCurrentFPS();

        Text text = new TranslatableText("sodium-extra.overlay.fps", currentFPS);

        if (SodiumExtraClientMod.options().extraSettings.showFPSExtended)
            text = new LiteralText(String.format("%s %s", text.getString(), new TranslatableText("sodium-extra.overlay.fps_extended", SodiumExtraClientMod.getClientTickHandler().getHighestFps(), SodiumExtraClientMod.getClientTickHandler().getAverageFps(),
                    SodiumExtraClientMod.getClientTickHandler().getLowestFps()).getString()));

        int x, y;
        switch (SodiumExtraClientMod.options().extraSettings.overlayCorner) {
            case TOP_LEFT -> {
                x = 2;
                y = 2;
            }
            case TOP_RIGHT -> {
                x = this.scaledWidth - this.client.textRenderer.getWidth(text) - 2;
                y = 2;
            }
            case BOTTOM_LEFT -> {
                x = 2;
                y = this.scaledHeight - this.client.textRenderer.fontHeight - 2;
            }
            case BOTTOM_RIGHT -> {
                x = this.scaledWidth - this.client.textRenderer.getWidth(text) - 2;
                y = this.scaledHeight - this.client.textRenderer.fontHeight - 2;
            }
            default ->
                    throw new IllegalStateException("Unexpected value: " + SodiumExtraClientMod.options().extraSettings.overlayCorner);
        }

        this.drawString(matrices, text, x, y);
    }

    private void renderCoords(MatrixStack matrices) {
        if (this.client.player == null) return;
        if (this.client.hasReducedDebugInfo()) return;
        Vec3d pos = this.client.player.getPos();

        Text text = new TranslatableText("sodium-extra.overlay.coordinates", String.format("%.2f", pos.x), String.format("%.2f", pos.y), String.format("%.2f", pos.z));

        int x, y;
        switch (SodiumExtraClientMod.options().extraSettings.overlayCorner) {
            case TOP_LEFT -> {
                x = 2;
                y = 12;
            }
            case TOP_RIGHT -> {
                x = this.scaledWidth - this.client.textRenderer.getWidth(text) - 2;
                y = 12;
            }
            case BOTTOM_LEFT -> {
                x = 2;
                y = this.scaledHeight - this.client.textRenderer.fontHeight - 12;
            }
            case BOTTOM_RIGHT -> {
                x = this.scaledWidth - this.client.textRenderer.getWidth(text) - 2;
                y = this.scaledHeight - this.client.textRenderer.fontHeight - 12;
            }
            default ->
                    throw new IllegalStateException("Unexpected value: " + SodiumExtraClientMod.options().extraSettings.overlayCorner);
        }

        this.drawString(matrices, text, x, y);
    }

    private void renderLightUpdatesWarning(MatrixStack matrices) {
        Text text = new TranslatableText("sodium-extra.overlay.light_updates");

        int x, y;
        switch (SodiumExtraClientMod.options().extraSettings.overlayCorner) {
            case TOP_LEFT -> {
                x = 2;
                y = 22;
            }
            case TOP_RIGHT -> {
                x = this.scaledWidth - this.client.textRenderer.getWidth(text) - 2;
                y = 22;
            }
            case BOTTOM_LEFT -> {
                x = 2;
                y = this.scaledHeight - this.client.textRenderer.fontHeight - 22;
            }
            case BOTTOM_RIGHT -> {
                x = this.scaledWidth - this.client.textRenderer.getWidth(text) - 2;
                y = this.scaledHeight - this.client.textRenderer.fontHeight - 22;
            }
            default ->
                    throw new IllegalStateException("Unexpected value: " + SodiumExtraClientMod.options().extraSettings.overlayCorner);
        }

        this.drawString(matrices, text, x, y);
    }

    private void drawString(MatrixStack matrices, Text text, int x, int y) {
        if (SodiumExtraClientMod.options().extraSettings.textContrast == SodiumExtraGameOptions.TextContrast.NONE) {
            this.client.textRenderer.draw(matrices, text, x, y, 0xffffffff);
        } else if (SodiumExtraClientMod.options().extraSettings.textContrast == SodiumExtraGameOptions.TextContrast.BACKGROUND) {
            DrawableHelper.fill(matrices, x - 1, y - 1, x + this.client.textRenderer.getWidth(text) + 1, y + this.client.textRenderer.fontHeight, -1873784752);
            this.client.textRenderer.draw(matrices, text, x, y, 0xffffffff);
        } else if (SodiumExtraClientMod.options().extraSettings.textContrast == SodiumExtraGameOptions.TextContrast.SHADOW) {
            this.client.textRenderer.drawWithShadow(matrices, text, x, y, 0xffffffff);
        }
    }
}
