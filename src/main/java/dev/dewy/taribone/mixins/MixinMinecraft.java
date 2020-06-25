package dev.dewy.taribone.mixins;

import dev.dewy.taribone.Taribone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft
{
    @Inject(
            method = "updateDisplay",
            at = @At("HEAD")
    )
    private void updateDisplay(CallbackInfo ci)
    {
        if (Minecraft.getMinecraft().ingameGUI != null && Taribone.shouldExec)
        {
            Taribone.logger.info("Executing a shouldExec call...");

            Taribone.shouldExec = false;
            Minecraft.getMinecraft().displayGuiScreen(new GuiConnecting(new GuiMainMenu(), Minecraft.getMinecraft(), new ServerData("Taribonal Proxy", Taribone.CONFIG.taribone.serverIp, false)));
        }
    }
}
