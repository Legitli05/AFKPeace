package amerebagatelle.github.io.afkpeace.config

import org.quiltmc.config.api.ReflectiveConfig
import org.quiltmc.config.api.values.TrackedValue

@Suppress("unused")
class AFKPeaceConfig : ReflectiveConfig() {
    val toggles = Toggles()
    val configurations = Configurations()
    val afkMode = AFKMode()

    inner class Toggles : Section() {
        val reconnectEnabled: TrackedValue<Boolean> = this.value(false)
        val damageLogoutEnabled: TrackedValue<Boolean> = this.value(false)
        val featuresEnabledIndicator: TrackedValue<Boolean> = this.value(true)
    }

    inner class Configurations : Section() {
        val autoAfkTimerSeconds: TrackedValue<Int> = this.value(300)
        val reconnectOnDamageLogout: TrackedValue<Boolean> = this.value(false)
        val secondsBetweenReconnectAttempts: TrackedValue<Int> = this.value(3)
        val reconnectAttemptNumber: TrackedValue<Int> = this.value(10)
        val damageLogoutTolerance: TrackedValue<Int> = this.value(20)
    }

    inner class AFKMode : Section() {
        val autoAfk: TrackedValue<Boolean> = this.value(false)
        val autoAfkReconnectEnabled: TrackedValue<Boolean> = this.value(false)
        val autoAfkDamageLogoutEnabled: TrackedValue<Boolean> = this.value(false)
    }
}