package com.bvanseg.gigeresque.common.item;

import com.bvanseg.gigeresque.common.GigeresqueJava;
import com.bvanseg.gigeresque.common.entity.EntitiesJava;
import com.bvanseg.gigeresque.common.fluid.FluidsJava;
import com.bvanseg.gigeresque.common.item.group.ItemGroupsJava;
import com.bvanseg.gigeresque.common.util.GigeresqueInitializerJava;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemsJava implements GigeresqueInitializerJava {

    public static final BucketItem BLACK_FLUID_BUCKET = new BucketItem(FluidsJava.BLACK_FLUID_STILL, new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1).group(ItemGroupsJava.GENERAL));

    public static final SurgeryKitItemJava SURGERY_KIT = new SurgeryKitItemJava(new Item.Settings().maxDamage(4).group(ItemGroupsJava.GENERAL));

    @Override
    public void initialize() {

    }

    private void initializeImpl() {
        Registry.register(Registry.ITEM, new Identifier(GigeresqueJava.MOD_ID, "black_fluid_bucket"), BLACK_FLUID_BUCKET);

        if (GigeresqueJava.config.features.surgeryKit) {
            Registry.register(Registry.ITEM, new Identifier(GigeresqueJava.MOD_ID, "surgery_kit"), SURGERY_KIT);
        }

        registerSpawnEgg("alien", EntitiesJava.ALIEN, 0x2C2B27, 0x4D4B3F);
        registerSpawnEgg("aquatic_alien", EntitiesJava.AQUATIC_ALIEN, 0x404345, 0x949597);
        registerSpawnEgg("aquatic_chestburster", EntitiesJava.AQUATIC_CHESTBURSTER, 0xDED29D, 0x2C2B27);
        registerSpawnEgg("chestburster", EntitiesJava.CHESTBURSTER, 0xDED29D, 0x2C2B27);
        registerSpawnEgg("egg", EntitiesJava.EGG, 0x554E45, 0x4D4932);
        registerSpawnEgg("facehugger", EntitiesJava.FACEHUGGER, 0xC7B986, 0x516B21);
        registerSpawnEgg("runner_alien", EntitiesJava.RUNNER_ALIEN, 0x3E230B, 0x623C25);
        registerSpawnEgg("runnerburster", EntitiesJava.RUNNERBURSTER, 0xDED29D, 0x2C2B27);
        DispenserBehavior.registerDefaults();
    }

    private void registerSpawnEgg(
            String path,
            EntityType<? extends MobEntity> entityType,
            int primaryColor,
            int secondaryColor
    ) {
        Registry.register(
                Registry.ITEM,
                new Identifier(GigeresqueJava.MOD_ID, "%s_spawn_egg".formatted(path)),
                new SpawnEggItem(entityType, primaryColor, secondaryColor, new Item.Settings().group(ItemGroupsJava.GENERAL))
        );
    }
}
