package amerebagatelle.github.io.afkpeace

import amerebagatelle.github.io.afkpeace.config.AFKPeaceConfigManager
import amerebagatelle.github.io.afkpeace.util.ReconnectThread
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.ConnectScreen
import net.minecraft.client.gui.screens.DisconnectedScreen
import net.minecraft.client.gui.screens.TitleScreen
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen
import net.minecraft.client.multiplayer.ServerData
import net.minecraft.client.multiplayer.resolver.ServerAddress
import net.minecraft.network.chat.Component

@JvmField
var isDisconnecting: Boolean = false

var reconnectThread: ReconnectThread? = null

fun startReconnect(target: ServerData) {
    val client = Minecraft.getInstance()
    client.disconnect()
    reconnectThread = ReconnectThread(target)
    reconnectThread!!.start()
    client.setScreen(
        DisconnectedScreen(
            JoinMultiplayerScreen(TitleScreen()),
            Component.literal("AFKPeaceReconnection"),
            Component.translatable("afkpeace.reconnect.reason")
        )
    )
}

fun finishReconnect() = connectToServer(AFKPeaceClient.currentServerEntry!!)

fun cancelReconnect() {
    try {
        reconnectThread!!.join()
    } catch (ignored: InterruptedException) {
    }
    AFKPeaceClient.LOGGER.debug("Reconnecting cancelled.")
    Minecraft.getInstance().setScreen(
        DisconnectedScreen(
            JoinMultiplayerScreen(TitleScreen()),
            Component.literal("AFKPeaceDisconnect"),
            Component.translatable("afkpeace.reconnectfail")
        )
    )
}

fun connectToServer(target: ServerData) =
    ConnectScreen.startConnecting(
        JoinMultiplayerScreen(TitleScreen()),
        Minecraft.getInstance(),
        ServerAddress.parseString(target.ip),
        target,
        false
    )

fun disconnectFromServer(reason: Component) {
    if (AFKPeaceConfigManager.RECONNECT_ON_DAMAGE_LOGOUT.value()) {
        if (AFKPeaceClient.currentServerEntry != null) startReconnect(AFKPeaceClient.currentServerEntry!!)
        return
    }

    isDisconnecting = true
    val client = Minecraft.getInstance()
    client.connection!!.connection.disconnect(reason)
    client.disconnect()
    client.setScreen(
        DisconnectedScreen(
            JoinMultiplayerScreen(TitleScreen()),
            Component.literal("AFKPeaceDisconnect"),
            reason
        )
    )
}