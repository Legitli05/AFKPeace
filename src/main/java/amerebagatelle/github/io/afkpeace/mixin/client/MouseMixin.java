package amerebagatelle.github.io.afkpeace.mixin.client;

import amerebagatelle.github.io.afkpeace.AFKManager;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseMixin {
    @Inject(method = "onPress", at = @At("HEAD"))
    public void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        AFKManager.afkTime = 0;
    }

    @Inject(method = "onScroll", at = @At("HEAD"))
    public void onScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        AFKManager.afkTime = 0;
    }

    @Inject(method = "onMove", at = @At("HEAD"))
    public void onCursorPos(long window, double x, double y, CallbackInfo ci) {
        AFKManager.afkTime = 0;
    }
}
