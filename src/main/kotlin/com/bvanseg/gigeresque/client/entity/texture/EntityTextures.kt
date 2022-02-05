package com.bvanseg.gigeresque.client.entity.texture

import com.bvanseg.gigeresque.common.Gigeresque
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.util.Identifier

/**
 * @author Boston Vanseghi
 */
@Environment(EnvType.CLIENT)
object EntityTextures {

    private const val BASE_URL = "textures"
    private const val BASE_ENTITY_URL = "$BASE_URL/entity"

    val ALIEN = Identifier(Gigeresque.MOD_ID, "$BASE_ENTITY_URL/alien/alien.png")
    val ALIEN_YOUNG = Identifier(Gigeresque.MOD_ID, "$BASE_ENTITY_URL/alien/alien_young.png")
    val AQUATIC_ALIEN = Identifier(Gigeresque.MOD_ID, "$BASE_ENTITY_URL/aquatic_alien/aquatic_alien.png")
    val AQUATIC_ALIEN_CONSTANT =
        Identifier(Gigeresque.MOD_ID, "$BASE_ENTITY_URL/aquatic_alien/aquatic_alien_constant.png")
    val AQUATIC_ALIEN_TINTABLE =
        Identifier(Gigeresque.MOD_ID, "$BASE_ENTITY_URL/aquatic_alien/aquatic_alien_tintable.png")
    val AQUATIC_ALIEN_YOUNG = Identifier(Gigeresque.MOD_ID, "$BASE_ENTITY_URL/aquatic_alien/aquatic_alien_young.png")
    val AQUATIC_CHESTBURSTER =
        Identifier(Gigeresque.MOD_ID, "$BASE_ENTITY_URL/aquatic_chestburster/aquatic_chestburster.png")
    val CHESTBURSTER = Identifier(Gigeresque.MOD_ID, "$BASE_ENTITY_URL/chestburster/chestburster.png")
    val EGG = Identifier(Gigeresque.MOD_ID, "$BASE_ENTITY_URL/egg/egg.png")
    val FACEHUGGER = Identifier(Gigeresque.MOD_ID, "$BASE_ENTITY_URL/facehugger/facehugger.png")
    val RUNNER_ALIEN = Identifier(Gigeresque.MOD_ID, "$BASE_ENTITY_URL/runner_alien/runner_alien.png")
    val RUNNER_ALIEN_YOUNG = Identifier(Gigeresque.MOD_ID, "$BASE_ENTITY_URL/runner_alien/runner_alien_young.png")
    val RUNNERBURSTER = Identifier(Gigeresque.MOD_ID, "$BASE_ENTITY_URL/runnerburster/runnerburster.png")
}