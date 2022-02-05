package com.bvanseg.gigeresque.client.config

import com.bvanseg.gigeresque.common.config.GigeresqueConfig
import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import me.shedaniel.autoconfig.AutoConfig

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

/**
 * @author Boston Vanseghi
 */
@Environment(EnvType.CLIENT)
class ModMenuGigeresque : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { parent ->
            AutoConfig.getConfigScreen(GigeresqueConfig::class.java, parent).get()
        }
    }
}