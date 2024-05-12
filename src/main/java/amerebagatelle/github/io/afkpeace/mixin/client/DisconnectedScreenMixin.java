package amerebagatelle.github.io.afkpeace.mixin.client;

import amerebagatelle.github.io.afkpeace.AFKManager;
import amerebagatelle.github.io.afkpeace.AFKPeaceClient;
import amerebagatelle.github.io.afkpeace.ConnectionManagerKt;
import amerebagatelle.github.io.afkpeace.config.AFKPeaceConfigManager;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Adds a button for reconnecting to the server
 */
@Mixin(DisconnectedScreen.class)
public abstract class DisconnectedScreenMixin extends Screen {
    @Shadow @Final private LinearLayout layout;

    protected DisconnectedScreenMixin() {
        super(Component.empty());
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/LinearLayout;arrangeElements()V", shift = At.Shift.BEFORE))
    public void onInit(CallbackInfo ci) {
        String timeOfDisconnect = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.layout.addChild(new StringWidget(Component.translatable("afkpeace.disconnectscreen.time", timeOfDisconnect), this.font));
        if (AFKPeaceClient.currentServerEntry != null) {
            var button = new Button.Builder(Component.translatable("afkpeace.reconnect"), (buttonWidget) -> ConnectionManagerKt.connectToServer(AFKPeaceClient.currentServerEntry))
                    .pos(width / 2 - 100, this.height - 30)
                    .size(200, 20)
                    .build();
            this.layout.addChild(button);
        }
    }
}