package amerebagatelle.github.io.afkpeace.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import net.fabricmc.loader.api.FabricLoader
import java.nio.file.Path
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream

@OptIn(ExperimentalSerializationApi::class)
object AFKPeaceConfigManager {
    private val settingsFile = FabricLoader.getInstance().configDir.resolve("settings.json")
    var config = AFKPeaceConfig()

    fun loadSettings() {
       config = Json.decodeFromStream<AFKPeaceConfig>(settingsFile.inputStream())
    }

    fun saveSettings() {
        Json.encodeToStream(config, settingsFile.outputStream())
    }
}