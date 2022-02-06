package com.bvanseg.gigeresque.common.entity.attribute;

import com.bvanseg.gigeresque.common.GigeresqueJava;
import com.bvanseg.gigeresque.common.util.GigeresqueInitializerJava;
import com.bvanseg.gigeresque.common.util.InitializationTimer;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AlienEntityAttributesJava implements GigeresqueInitializerJava {
    public static final double SABOTAGE_THRESHOLD = 0.85;
    public static final double SELF_PRESERVE_THRESHOLD = 0.35;

    public static final ClampedEntityAttribute INTELLIGENCE_ATTRIBUTE = new ClampedEntityAttribute("attribute.name.gigeresque.intelligence", 0.5, 0.0, 1.0);

    @Override
    public void initialize() {
        InitializationTimer.initializingBlock("EntityAttributes", this::initializeImpl);
    }

    private void initializeImpl() {
        Registry.register(Registry.ATTRIBUTE, new Identifier(GigeresqueJava.MOD_ID, "attribute.intelligence"), INTELLIGENCE_ATTRIBUTE);
    }
}
