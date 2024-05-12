package amerebagatelle.github.io.afkpeace

import amerebagatelle.github.io.afkpeace.AFKManager.isAfk
import amerebagatelle.github.io.afkpeace.AFKManager.tickAfkStatus
import amerebagatelle.github.io.afkpeace.config.AFKPeaceConfigManager
import amerebagatelle.github.io.afkpeace.config.AFKPeaceConfigScreen
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.client.multiplayer.ServerData
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents
import org.quiltmc.qsl.networking.api.PacketSender
import org.quiltmc.qsl.networking.api.client.ClientPlayConnectionEvents

class AFKPeaceClient : ClientModInitializer {
    override fun onInitializeClient(mod: ModContainer) {
        settingsKeybind = KeyBindingHelper.registerKeyBinding(KeyMapping("afkpeace.keybind.settingsMenu", -1, "AFKPeace"))
        ClientTickEvents.END.register(ClientTickEvents.End { client: Minecraft ->
            if (settingsKeybind.isDown) {
                client.setScreen(AFKPeaceConfigScreen(client.screen))
            }
            if (AFKPeaceConfigManager.AUTO_AFK.value()) {
                tickAfkStatus()
            }
        })
        HudRenderCallback.EVENT.register(HudRenderCallback { guiGraphics: GuiGraphics?, tickDelta: Float ->
            if ((AFKPeaceConfigManager.DAMAGE_LOGOUT_ENABLED.value() || isAfk) && AFKPeaceConfigManager.FEATURES_ENABLED_INDICATOR.value()) {
                val textRenderer = Minecraft.getInstance().font
                guiGraphics?.drawString(
                    textRenderer,
                    I18n.get("afkpeace.hud.featuresEnabled"),
                    10,
                    10,
                    0xFFFFFF,
                    false
                )
            }
        })
        ClientPlayConnectionEvents.JOIN.register(ClientPlayConnectionEvents.Join { _: ClientPacketListener?, _: PacketSender<CustomPacketPayload>?, client: Minecraft ->
            currentServerEntry = client.currentServer
        })
        LOGGER.info("AFKPeace " + mod.metadata().version().raw() + " Initialized")
    }

    companion object {
        val LOGGER: Logger = LogManager.getLogger("AFKPeace")!!

        @Suppress("unused")
        val MODID = "afkpeace"
        lateinit var settingsKeybind: KeyMapping

        @JvmField
        var currentServerEntry: ServerData? = null

        @JvmField
        var loginScreen: Screen? = null
    }
}