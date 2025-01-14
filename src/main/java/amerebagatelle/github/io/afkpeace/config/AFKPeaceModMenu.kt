package amerebagatelle.github.io.afkpeace.config

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import net.minecraft.client.gui.screens.Screen

class AFKPeaceModMenu : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory<Screen> { parent: Screen? -> AFKPeaceConfigScreen(parent) }
    }
}