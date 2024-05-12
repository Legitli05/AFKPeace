package amerebagatelle.github.io.afkpeace.mixin.client;

import amerebagatelle.github.io.afkpeace.AFKManager;
import amerebagatelle.github.io.afkpeace.AFKPeaceClient;
import amerebagatelle.github.io.afkpeace.ConnectionManagerKt;
import amerebagatelle.github.io.afkpeace.config.AFKPeaceConfigManager;
import com.mojang.realmsclient.RealmsMainScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientCommonPacketListenerImpl.class)
public class DisconnectMixin {
    /**
     * Checks if we should try to automatically reconnect, and if not opens a custom screen with a reconnect button
     */
    @Inject(method = "onDisconnect", at = @At("HEAD"), cancellable = true)
    public void tryReconnect(Component reason, CallbackInfo ci) {
        if(AFKPeaceClient.loginScreen instanceof RealmsScreen) {
            Minecraft.getInstance().setScreen(new RealmsMainScreen(new TitleScreen()));
            return;
        }

        if (!(AFKPeaceConfigManager.RECONNECT_ENABLED.value() || AFKManager.reconnectOverride())) return;
        if (reason.toString().contains("multiplayer.disconnect.kicked")) return;

        ServerData target = AFKPeaceClient.currentServerEntry;
        if (!ConnectionManagerKt.isDisconnecting) {
            if (target != null) {
                ConnectionManagerKt.startReconnect(target);
                ci.cancel();
            }
        } else {
            ConnectionManagerKt.isDisconnecting = false;
        }
    }
}
