package net.xalcon.torchmaster.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.xalcon.torchmaster.Constants;
import net.xalcon.torchmaster.TorchmasterRuntime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MixinTitleScreen {

    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo info) {

        TorchmasterRuntime.LOG.info("This line is printed by an example mod mixin from NeoForge!");
        TorchmasterRuntime.LOG.info("MC Version: {}", MinecraftClient.getInstance().getVersionType());
    }
}
