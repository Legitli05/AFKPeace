package amerebagatelle.github.io.afkpeace

import amerebagatelle.github.io.afkpeace.config.AFKPeaceConfigManager
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.toasts.SystemToast
import net.minecraft.network.chat.Component

object AFKManager {
    private var wasAfk = false

    @JvmField
    var afkTime: Long = 0
    private var lastUpdate: Long = 0

    @JvmStatic
    var isAfk = false

    @JvmStatic
    fun tickAfkStatus() {
        if (System.nanoTime() - lastUpdate > 1e+9) {
            afkTime++
            val afk = afkTime > AFKPeaceConfigManager.config.afkMode.autoAfkTimerSeconds
            if (afk && !wasAfk) {
                AFKPeaceClient.LOGGER.info("AutoAFK on.")
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().toasts.addToast(
                        SystemToast(
                            SystemToast.SystemToastId.NARRATOR_TOGGLE,
                            Component.translatable("afkpeace.afkmode.on"),
                            Component.translatable("")
                        )
                    )
                }
                isAfk = true
            } else if (!afk && wasAfk) {
                AFKPeaceClient.LOGGER.info("AutoAFK off.")
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().toasts.addToast(
                        SystemToast(
                            SystemToast.SystemToastId.NARRATOR_TOGGLE,
                            Component.translatable("afkpeace.afkmode.off"),
                            Component.translatable("")
                        )
                    )
                }
                isAfk = false
            }
            wasAfk = afk && AFKPeaceConfigManager.config.afkMode.autoAfk
            lastUpdate = System.nanoTime()
        }
    }

    @JvmStatic
    fun damageLogoutOverride(): Boolean {
        return isAfk && AFKPeaceConfigManager.config.afkMode.autoAfkDamageLogoutEnabled
    }

    @JvmStatic
    fun reconnectOverride(): Boolean {
        return isAfk && AFKPeaceConfigManager.config.afkMode.autoAfkReconnectEnabled
    }
}