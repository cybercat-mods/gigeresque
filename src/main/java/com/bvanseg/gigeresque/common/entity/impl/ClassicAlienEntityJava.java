package com.bvanseg.gigeresque.common.entity.impl;

import com.bvanseg.gigeresque.common.entity.AlienEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class ClassicAlienEntityJava extends AdultAlienEntityJava {
    public ClassicAlienEntityJava(@NotNull EntityType<? extends AlienEntity> type, @NotNull World world) {
        super(type, world);
    }
}
