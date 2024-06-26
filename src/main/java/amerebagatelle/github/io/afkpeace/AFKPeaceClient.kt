package amerebagatelle.github.io.afkpeace

import amerebagatelle.github.io.afkpeace.AFKManager.isAfk
import amerebagatelle.github.io.afkpeace.AFKManager.tickAfkStatus
import amerebagatelle.github.io.afkpeace.config.AFKPeaceConfigManager
import amerebagatelle.github.io.afkpeace.config.AFKPeaceConfigScreen
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.DeltaTracker
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.client.multiplayer.ServerData
import net.minecraft.client.resources.language.I18n
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class AFKPeaceClient : ClientModInitializer {
    override fun onInitializeClient() {
        settingsKeybind = KeyBindingHelper.registerKeyBinding(KeyMapping("afkpeace.keybind.settingsMenu", -1, "AFKPeace"))
        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick { client: Minecraft ->
            if (settingsKeybind.isDown) {
                client.setScreen(AFKPeaceConfigScreen(client.screen))
            }
            if (AFKPeaceConfigManager.config.afkMode.autoAfk) {
                tickAfkStatus()
            }
        })
        HudRenderCallback.EVENT.register(HudRenderCallback { guiGraphics: GuiGraphics?, _: DeltaTracker ->
            if ((AFKPeaceConfigManager.config.toggles.damageLogoutEnabled || isAfk) && AFKPeaceConfigManager.config.toggles.featuresEnabledIndicator) {
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
        ClientPlayConnectionEvents.JOIN.register(ClientPlayConnectionEvents.Join { _: ClientPacketListener?, _: PacketSender?, client: Minecraft ->
            currentServerEntry = client.currentServer
        })
        LOGGER.info("AFKPeace " + FabricLoader.getInstance().getModContainer("afkpeace").get().metadata.version.friendlyString + " Initialized")
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