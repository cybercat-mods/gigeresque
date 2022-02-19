package com.bvanseg.gigeresque.client.entity.animation;

import com.bvanseg.gigeresque.common.GigeresqueJava;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class EntityAnimationsJava {
    public static final Identifier ALIEN = new Identifier(GigeresqueJava.MOD_ID, "animations/alien.animation.json");
    public static final Identifier AQUATIC_ALIEN = new Identifier(GigeresqueJava.MOD_ID, "animations/aquatic_alien.animation.json");
    public static final Identifier AQUATIC_CHESTBURSTER = new Identifier(GigeresqueJava.MOD_ID, "animations/aquatic_chestburster.animation.json");
    public static final Identifier CHESTBURSTER = new Identifier(GigeresqueJava.MOD_ID, "animations/chestburster.animation.json");
    public static final Identifier EGG = new Identifier(GigeresqueJava.MOD_ID, "animations/egg.animation.json");
    public static final Identifier FACEHUGGER = new Identifier(GigeresqueJava.MOD_ID, "animations/facehugger.animation.json");
    public static final Identifier RUNNER_ALIEN = new Identifier(GigeresqueJava.MOD_ID, "animations/runner_alien.animation.json");
    public static final Identifier RUNNERBURSTER = new Identifier(GigeresqueJava.MOD_ID, "animations/runnerburster.animation.json");
}
