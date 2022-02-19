package com.bvanseg.gigeresque.client.entity.model;

import com.bvanseg.gigeresque.common.GigeresqueJava;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class EntityModelsJava {
    public static final Identifier ALIEN = new Identifier(GigeresqueJava.MOD_ID, "geo/alien.geo.json");
    public static final Identifier AQUATIC_ALIEN = new Identifier(GigeresqueJava.MOD_ID, "geo/aquatic_alien.geo.json");
    public static final Identifier AQUATIC_CHESTBURSTER = new Identifier(GigeresqueJava.MOD_ID, "geo/aquatic_chestburster.geo.json");
    public static final Identifier CHESTBURSTER = new Identifier(GigeresqueJava.MOD_ID, "geo/chestburster.geo.json");
    public static final Identifier EGG = new Identifier(GigeresqueJava.MOD_ID, "geo/egg.geo.json");
    public static final Identifier FACEHUGGER = new Identifier(GigeresqueJava.MOD_ID, "geo/facehugger.geo.json");
    public static final Identifier RUNNER_ALIEN = new Identifier(GigeresqueJava.MOD_ID, "geo/runner_alien.geo.json");
    public static final Identifier RUNNERBURSTER = new Identifier(GigeresqueJava.MOD_ID, "geo/runnerburster.geo.json");
}
