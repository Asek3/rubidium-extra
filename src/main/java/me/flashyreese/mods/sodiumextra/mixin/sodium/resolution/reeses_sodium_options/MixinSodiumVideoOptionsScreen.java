/*package me.flashyreese.mods.sodiumextra.mixin.sodium.resolution.reeses_sodium_options;

import me.flashyreese.mods.reeses_sodium_options.client.gui.SodiumVideoOptionsScreen;
import me.jellysquid.mods.sodium.client.gui.options.OptionFlag;
import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.EnumSet;
import java.util.HashSet;

@Pseudo
@Mixin(SodiumVideoOptionsScreen.class)
public class MixinSodiumVideoOptionsScreen {

    @Inject(method = "applyChanges",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/MinecraftClient;getInstance()Lnet/minecraft/client/MinecraftClient;",
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    public void applyChanges(CallbackInfo ci, HashSet<OptionStorage<?>> dirtyStorages, EnumSet<OptionFlag> flags) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (flags.contains(OptionFlag.REQUIRES_GAME_RESTART)) {
            client.getWindow().applyVideoMode();
        }
    }
}*/