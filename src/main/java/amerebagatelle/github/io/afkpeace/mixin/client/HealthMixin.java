package amerebagatelle.github.io.afkpeace.mixin.client;

import amerebagatelle.github.io.afkpeace.AFKManager;
import amerebagatelle.github.io.afkpeace.ConnectionManagerKt;
import amerebagatelle.github.io.afkpeace.config.AFKPeaceConfigManager;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class HealthMixin {
    @Unique
    private float lastHealth;

    /**
     * Gets when the player's health changes, and logs the player out if it has taken damage
     */
    @Inject(method = "handleSetHealth", at = @At("TAIL"))
    public void onPlayerHealthUpdate(ClientboundSetHealthPacket packet, CallbackInfo cbi) {
        if (AFKPeaceConfigManager.DAMAGE_LOGOUT_ENABLED.value() || AFKManager.damageLogoutOverride()) {
            try {
                if (packet.getHealth() < lastHealth && packet.getHealth() < AFKPeaceConfigManager.DAMAGE_LOGOUT_TOLERANCE.value()) {
                    ConnectionManagerKt.disconnectFromServer(Component.translatable("afkpeace.reason.damagelogout"));
                }
            } catch (NullPointerException ignored) {
            }
        }
        lastHealth = packet.getHealth();
    }
}