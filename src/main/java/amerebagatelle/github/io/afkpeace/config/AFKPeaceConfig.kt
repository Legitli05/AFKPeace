package amerebagatelle.github.io.afkpeace.config

import kotlinx.serialization.Serializable

@Suppress("unused")
@Serializable
class AFKPeaceConfig {
    val toggles = Toggles()
    val configurations = Configurations()
    val afkMode = AFKMode()

    @Serializable
    data class Toggles(
        var reconnectEnabled: Boolean = false,
        var damageLogoutEnabled: Boolean = false,
        var featuresEnabledIndicator: Boolean = true
    )

    @Serializable
    data class Configurations (
        var reconnectOnDamageLogout: Boolean = false,
        var secondsBetweenReconnectAttempts: Int = 3,
        var reconnectAttemptNumber: Int = 10,
        var damageLogoutTolerance: Int = 20
    )

    @Serializable
    data class AFKMode (
        var autoAfk: Boolean = false,
        var autoAfkReconnectEnabled: Boolean = false,
        var autoAfkDamageLogoutEnabled: Boolean = false,
        var autoAfkTimerSeconds: Int = 300
    )
}