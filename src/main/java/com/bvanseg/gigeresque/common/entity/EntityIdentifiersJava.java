package com.bvanseg.gigeresque.common.entity;

import com.bvanseg.gigeresque.common.GigeresqueJava;
import com.bvanseg.gigeresque.common.entity.impl.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import java.util.Map;

public class EntityIdentifiersJava {
    private EntityIdentifiersJava() {
    }

    public static final Identifier ALIEN = new Identifier(GigeresqueJava.MOD_ID, "alien");
    public static final Identifier AQUATIC_ALIEN = new Identifier(GigeresqueJava.MOD_ID, "aquatic_alien");
    public static final Identifier AQUATIC_CHESTBURSTER = new Identifier(GigeresqueJava.MOD_ID, "aquatic_chestburster");
    public static final Identifier CHESTBURSTER = new Identifier(GigeresqueJava.MOD_ID, "chestburster");
    public static final Identifier EGG = new Identifier(GigeresqueJava.MOD_ID, "egg");
    public static final Identifier FACEHUGGER = new Identifier(GigeresqueJava.MOD_ID, "facehugger");
    public static final Identifier RUNNER_ALIEN = new Identifier(GigeresqueJava.MOD_ID, "runner_alien");
    public static final Identifier RUNNERBURSTER = new Identifier(GigeresqueJava.MOD_ID, "runnerburster");

    public static final Map<Class<? extends Entity>, Identifier> typeMappings = Map.of(
            ClassicAlienEntityJava.class, ALIEN,
            AquaticAlienEntityJava.class, AQUATIC_ALIEN,
            AquaticChestbursterEntityJava.class, AQUATIC_CHESTBURSTER,
            ChestbursterEntityJava.class, CHESTBURSTER,
            AlienEggEntityJava.class, EGG,
            FacehuggerEntityJava.class, FACEHUGGER,
            RunnerAlienEntityJava.class, RUNNER_ALIEN,
            RunnerbursterEntityJava.class, RUNNERBURSTER
    );
}
