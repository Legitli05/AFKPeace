package amerebagatelle.github.io.afkpeace.config

import dev.lambdaurora.spruceui.Position
import dev.lambdaurora.spruceui.background.Background
import dev.lambdaurora.spruceui.background.SimpleColorBackground
import dev.lambdaurora.spruceui.border.Border
import dev.lambdaurora.spruceui.border.EmptyBorder
import dev.lambdaurora.spruceui.option.SpruceIntegerInputOption
import dev.lambdaurora.spruceui.option.SpruceOption
import dev.lambdaurora.spruceui.option.SpruceToggleBooleanOption
import dev.lambdaurora.spruceui.screen.SpruceScreen
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget
import dev.lambdaurora.spruceui.widget.SpruceSeparatorWidget
import dev.lambdaurora.spruceui.widget.container.SpruceOptionListWidget
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

class AFKPeaceConfigScreen(private val parent: Screen?) : SpruceScreen(Component.translatable("afkpeace.config.title")) {
    private val reconnectEnabled: SpruceOption = SpruceToggleBooleanOption(
        "afkpeace.option.reconnectEnabled", { AFKPeaceConfigManager.config.toggles.reconnectEnabled },
        { newValue: Boolean -> AFKPeaceConfigManager.config.toggles.reconnectEnabled = newValue; AFKPeaceConfigManager.saveSettings() },
        Component.translatable("afkpeace.option.reconnectEnabled.tooltip")
    )
    private val damageLogoutEnabled: SpruceOption = SpruceToggleBooleanOption(
        "afkpeace.option.damageLogoutEnabled", { AFKPeaceConfigManager.config.toggles.damageLogoutEnabled },
        { newValue: Boolean -> AFKPeaceConfigManager.config.toggles.damageLogoutEnabled = newValue; AFKPeaceConfigManager.saveSettings() },
        Component.translatable("afkpeace.option.damageLogoutEnabled.tooltip")
    )
    private val featuresEnabledIndicator: SpruceOption = SpruceToggleBooleanOption(
        "afkpeace.option.featuresEnabledIndicator", { AFKPeaceConfigManager.config.toggles.featuresEnabledIndicator },
        { newValue: Boolean -> AFKPeaceConfigManager.config.toggles.featuresEnabledIndicator = newValue; AFKPeaceConfigManager.saveSettings() },
        Component.translatable("afkpeace.option.featuresEnabledIndicator.tooltip")
    )

    private val reconnectOnDamageLogout: SpruceOption = SpruceToggleBooleanOption(
        "afkpeace.option.reconnectOnDamageLogout", { AFKPeaceConfigManager.config.configurations.reconnectOnDamageLogout },
        { newValue: Boolean -> AFKPeaceConfigManager.config.configurations.reconnectOnDamageLogout = newValue; AFKPeaceConfigManager.saveSettings() },
        Component.translatable("afkpeace.option.reconnectOnDamageLogout.tooltip")
    )
    private val secondsBetweenReconnectAttempts: SpruceOption = SpruceIntegerInputOption(
        "afkpeace.option.secondsBetweenReconnectAttempts",
        { AFKPeaceConfigManager.config.configurations.secondsBetweenReconnectAttempts },
        { newValue: Int -> AFKPeaceConfigManager.config.configurations.secondsBetweenReconnectAttempts = newValue; AFKPeaceConfigManager.saveSettings() },
        Component.translatable("afkpeace.option.secondsBetweenReconnectAttempts.tooltip")
    )
    private val reconnectAttemptNumber: SpruceOption = SpruceIntegerInputOption(
        "afkpeace.option.reconnectAttemptNumber", { AFKPeaceConfigManager.config.configurations.reconnectAttemptNumber },
        { newValue: Int -> AFKPeaceConfigManager.config.configurations.reconnectAttemptNumber = newValue; AFKPeaceConfigManager.saveSettings() },
        Component.translatable("afkpeace.option.reconnectAttemptNumber.tooltip")
    )
    private val damageLogoutTolerance: SpruceOption = SpruceIntegerInputOption(
        "afkpeace.option.damageLogoutTolerance", { AFKPeaceConfigManager.config.configurations.damageLogoutTolerance },
        { newValue: Int -> AFKPeaceConfigManager.config.configurations.damageLogoutTolerance = newValue; AFKPeaceConfigManager.saveSettings() },
        Component.translatable("afkpeace.option.damageLogoutTolerance.tooltip")
    )

    private val autoAfk: SpruceOption = SpruceToggleBooleanOption(
        "afkpeace.option.autoAfk", { AFKPeaceConfigManager.config.afkMode.autoAfk },
        { newValue: Boolean -> AFKPeaceConfigManager.config.afkMode.autoAfk = newValue; AFKPeaceConfigManager.saveSettings() },
        Component.translatable("afkpeace.option.autoAfk.tooltip")
    )
    private val afkModeReconnectEnabled: SpruceOption = SpruceToggleBooleanOption(
        "afkpeace.option.reconnectEnabled", { AFKPeaceConfigManager.config.afkMode.autoAfkReconnectEnabled },
        { newValue: Boolean -> AFKPeaceConfigManager.config.afkMode.autoAfkReconnectEnabled = newValue; AFKPeaceConfigManager.saveSettings() },
        Component.translatable("afkpeace.option.reconnectEnabled.tooltip")
    )
    private val afkModeDamageLogoutEnabled: SpruceOption = SpruceToggleBooleanOption(
        "afkpeace.option.damageLogoutEnabled", { AFKPeaceConfigManager.config.afkMode.autoAfkDamageLogoutEnabled },
        { newValue: Boolean -> AFKPeaceConfigManager.config.afkMode.autoAfkDamageLogoutEnabled = newValue; AFKPeaceConfigManager.saveSettings() },
        Component.translatable("afkpeace.option.damageLogoutEnabled.tooltip")
    )
    private val autoAfkTimerSeconds: SpruceOption = SpruceIntegerInputOption(
        "afkpeace.option.autoAfkTimer", { AFKPeaceConfigManager.config.afkMode.autoAfkTimerSeconds },
        { newValue: Int -> AFKPeaceConfigManager.config.afkMode.autoAfkTimerSeconds = newValue; AFKPeaceConfigManager.saveSettings() },
        Component.translatable("afkpeace.option.autoAfkTimer.tooltip")
    )


    override fun init() {
        super.init()
        addRenderableWidget(
            SpruceButtonWidget(
                Position.of(2, 0),
                100,
                20,
                Component.translatable("afkpeace.config.back")
            ) {
                minecraft!!.setScreen(
                    parent
                )
            })

        val generalOptionTitle =
            SpruceSeparatorWidget(Position.of(0, 20), width, Component.translatable("afkpeace.option.generalOptions"))
        addRenderableWidget(generalOptionTitle)

        val options =
            SpruceOptionListWidget(Position.of(0, generalOptionTitle.y + generalOptionTitle.height), width, 300)
        options.addSingleOptionEntry(reconnectEnabled)
        options.addSingleOptionEntry(damageLogoutEnabled)
        options.addSingleOptionEntry(featuresEnabledIndicator)
        options.addSingleOptionEntry(reconnectOnDamageLogout)
        options.addSingleOptionEntry(secondsBetweenReconnectAttempts)
        options.addSingleOptionEntry(reconnectAttemptNumber)
        options.addSingleOptionEntry(damageLogoutTolerance)
        options.background = BACKGROUND
        options.border = WIDGET_BORDER
        addRenderableWidget(options)

        val afkModeTitle = SpruceSeparatorWidget(
            Position.of(0, options.y + options.height),
            width,
            Component.translatable("afkpeace.option.autoAfk")
        )
        addRenderableWidget(afkModeTitle)

        val afkMode = SpruceOptionListWidget(Position.of(0, afkModeTitle.y + afkModeTitle.height), width, height - 20)
        afkMode.addSingleOptionEntry(autoAfk)
        afkMode.addSingleOptionEntry(afkModeReconnectEnabled)
        afkMode.addSingleOptionEntry(afkModeDamageLogoutEnabled)
        afkMode.addSingleOptionEntry(autoAfkTimerSeconds)
        afkMode.background = BACKGROUND
        afkMode.border = WIDGET_BORDER
        addRenderableWidget(afkMode)
    }

    override fun renderTitle(graphics: GuiGraphics?, mouseX: Int, mouseY: Int, delta: Float) {
        graphics?.drawCenteredString(font, title, width / 2, 8, 16777215)
    }

    companion object {
        private val BACKGROUND: Background = SimpleColorBackground(0, 0, 0, 135)
        private val WIDGET_BORDER: Border = EmptyBorder.EMPTY_BORDER
    }
}