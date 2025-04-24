package mods.cybercat.gigeresque.client.entity.render.helper;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Credit to Boston for this code
 */
public record EntityHeadOffsetData(
    BiFunction<EntityHeadData, Entity, Double> verticalOffsetSupplier,
    BiFunction<EntityHeadData, Entity, Double> faceOffsetSupplier

) {

    private static final EntityHeadOffsetData COW = new EntityHeadOffsetData(
        EntityHeadOffsetData::cowVerticalOffset,
        EntityHeadOffsetData::cowFaceOffset
    );

    private static final EntityHeadOffsetData LLAMA = new EntityHeadOffsetData(
        EntityHeadOffsetData::llamaVerticalOffset,
        EntityHeadOffsetData::llamaFaceOffset
    );

    private static final EntityHeadOffsetData VILLAGER = new EntityHeadOffsetData(
        EntityHeadOffsetData::villagerVerticalOffset,
        EntityHeadOffsetData::villagerFaceOffset
    );

    public static final Map<EntityType<?>, EntityHeadOffsetData> ENTITY_HEAD_OFFSET_DATA_BY_TYPE = new HashMap<>(
        Map.ofEntries(
            Map.entry(
                EntityType.CAMEL,
                new EntityHeadOffsetData(EntityHeadOffsetData::camelVerticalOffset, EntityHeadOffsetData::camelFaceOffset)
            ),
            Map.entry(EntityType.COW, COW),
            Map.entry(
                EntityType.DONKEY,
                new EntityHeadOffsetData(EntityHeadOffsetData::donkeyVerticalOffset, EntityHeadOffsetData::donkeyFaceOffset)
            ),
            Map.entry(EntityType.EVOKER, VILLAGER),
            Map.entry(
                EntityType.FOX,
                new EntityHeadOffsetData(EntityHeadOffsetData::foxVerticalOffset, EntityHeadOffsetData::foxFaceOffset)
            ),
            Map.entry(
                EntityType.GOAT,
                new EntityHeadOffsetData(EntityHeadOffsetData::goatVerticalOffset, EntityHeadOffsetData::goatFaceOffset)
            ),
            Map.entry(
                EntityType.HOGLIN,
                new EntityHeadOffsetData(EntityHeadOffsetData::hoglinVerticalOffset, EntityHeadOffsetData::hoglinFaceOffset)
            ),
            Map.entry(
                EntityType.HORSE,
                new EntityHeadOffsetData(EntityHeadOffsetData::horseVerticalOffset, EntityHeadOffsetData::horseFaceOffset)
            ),
            Map.entry(EntityType.ILLUSIONER, VILLAGER),
            Map.entry(EntityType.LLAMA, LLAMA),
            Map.entry(EntityType.MOOSHROOM, COW),
            Map.entry(
                EntityType.MULE,
                new EntityHeadOffsetData(EntityHeadOffsetData::muleVerticalOffset, EntityHeadOffsetData::muleFaceOffset)
            ),
            Map.entry(
                EntityType.PANDA,
                new EntityHeadOffsetData(EntityHeadOffsetData::pandaVerticalOffset, EntityHeadOffsetData::pandaFaceOffset)
            ),
            Map.entry(
                EntityType.PIG,
                new EntityHeadOffsetData(EntityHeadOffsetData::pigVerticalOffset, EntityHeadOffsetData::pigFaceOffset)
            ),
            Map.entry(EntityType.PIGLIN, VILLAGER),
            Map.entry(EntityType.PIGLIN_BRUTE, VILLAGER),
            Map.entry(EntityType.PILLAGER, VILLAGER),
            Map.entry(
                EntityType.PLAYER,
                new EntityHeadOffsetData(EntityHeadOffsetData::playerVerticalOffset, EntityHeadOffsetData::playerFaceOffset)
            ),
            Map.entry(
                EntityType.POLAR_BEAR,
                new EntityHeadOffsetData(EntityHeadOffsetData::polarBearVerticalOffset, EntityHeadOffsetData::polarBearFaceOffset)
            ),
            Map.entry(
                EntityType.RAVAGER,
                new EntityHeadOffsetData(EntityHeadOffsetData::ravagerVerticalOffset, EntityHeadOffsetData::ravagerFaceOffset)
            ),
            Map.entry(
                EntityType.SHEEP,
                new EntityHeadOffsetData(EntityHeadOffsetData::sheepVerticalOffset, EntityHeadOffsetData::sheepFaceOffset)
            ),
            Map.entry(
                EntityType.SNIFFER,
                new EntityHeadOffsetData(EntityHeadOffsetData::snifferVerticalOffset, EntityHeadOffsetData::snifferFaceOffset)
            ),
            Map.entry(EntityType.TRADER_LLAMA, LLAMA),
            Map.entry(EntityType.VILLAGER, VILLAGER),
            Map.entry(EntityType.WANDERING_TRADER, VILLAGER),
            Map.entry(EntityType.VINDICATOR, VILLAGER),
            Map.entry(
                EntityType.WITCH,
                new EntityHeadOffsetData(EntityHeadOffsetData::witchVerticalOffset, EntityHeadOffsetData::villagerFaceOffset)
            ),
            Map.entry(
                EntityType.WOLF,
                new EntityHeadOffsetData(EntityHeadOffsetData::wolfVerticalOffset, EntityHeadOffsetData::wolfFaceOffset)
            ),
            Map.entry(
                EntityType.DOLPHIN,
                new EntityHeadOffsetData(EntityHeadOffsetData::dolphinVerticalOffset, EntityHeadOffsetData::dolphinFaceOffset)
            )
        )
    );

    private static double camelVerticalOffset(EntityHeadData data, Entity parasite) {
        return data.size().y / 1.5;
    }

    private static double camelFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z + parasite.getBbHeight();
    }

    private static double cowVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y;
    }

    private static double cowFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z + (data.size().z / 2) + parasite.getBbHeight();
    }

    private static double donkeyVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y / 4;
    }

    private static double donkeyFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z * 2 + data.size().z / 1.4;
    }

    private static double foxVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y - (data.size().y / 1.3);
    }

    private static double foxFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z * 1.7 + parasite.getBbHeight();
    }

    private static double goatVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y - (data.size().y / 4);
    }

    private static double goatFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z + (data.size().z / 3) + parasite.getBbHeight() / 4;
    }

    private static double hoglinVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y - data.size().y * 2.3;
    }

    private static double hoglinFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z + (data.size().z / 10) + parasite.getBbHeight();
    }

    private static double horseVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y / 4;
    }

    private static double horseFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z * 3 + data.size().z / 2;
    }

    private static double llamaVerticalOffset(EntityHeadData data, Entity parasite) {
        return data.size().y / 2.5;
    }

    private static double llamaFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z + (data.size().z / 1.5) + parasite.getBbHeight();
    }

    private static double muleVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y / 4;
    }

    private static double muleFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z * 2 + data.size().z / 1.2;
    }

    private static double pigVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y;
    }

    private static double pigFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z + (data.size().z / 2) + parasite.getBbHeight() / 2;
    }

    private static double pandaVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y - (data.size().y / 2);
    }

    private static double pandaFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z * 2 + parasite.getBbHeight();
    }

    private static double playerVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y - (data.size().y / 4);
    }

    private static double playerFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z - (data.size().z / 2);
    }

    private static double polarBearVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y - (data.size().y / 1.15);
    }

    private static double polarBearFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z * 3 + parasite.getBbHeight();
    }

    private static double ravagerVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y - data.size().y / 4;
    }

    private static double ravagerFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z + (data.size().z / 1.75) + parasite.getBbHeight();
    }

    private static double sheepVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y - (data.size().y / 4);
    }

    private static double sheepFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z + (data.size().z / 3) + parasite.getBbHeight() / 2;
    }

    private static double snifferVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y - (data.size().y / 2);
    }

    private static double snifferFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z * 3 + (data.size().z / 2);
    }

    private static double villagerVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y - (data.size().y / 4);
    }

    private static double villagerFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z - (data.size().z / 1) + 0.3;
    }

    private static double witchVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y - (data.size().y / 1.5);
    }

    private static double wolfVerticalOffset(EntityHeadData data, Entity parasite) {
        return -data.size().y - (data.size().y / 1.9);
    }

    private static double wolfFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z * 1.9 + parasite.getBbHeight();
    }

    private static double dolphinVerticalOffset(EntityHeadData data, Entity parasite) {
        return data.size().z - data.size().z - 0.7;
    }

    private static double dolphinFaceOffset(EntityHeadData data, Entity parasite) {
        return data.size().z * 2 + data.size().z + 0.1;
    }
}
