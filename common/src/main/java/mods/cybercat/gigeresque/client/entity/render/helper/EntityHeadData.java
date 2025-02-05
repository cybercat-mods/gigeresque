package mods.cybercat.gigeresque.client.entity.render.helper;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

/**
 * Credit to Boston for this code
 */
public record EntityHeadData(
    Vec3 size,
    Vec3 position,
    Vec3 pivot
) {

    private static final double MULTIPLIER = 1 / 16.0;

    private static final EntityHeadData COW = adjust(vec3(8, 8, 6), vec3(-4, 16, -14), vec3(0, 20, -8));

    private static final EntityHeadData HORSE = adjust(vec3(6, 5, 7), vec3(-3, 28, -11), vec3(0, 22, -9));

    private static final EntityHeadData LLAMA = adjust(vec3(4, 4, 9), vec3(-2, 27, -16), vec3(0, 17, -11));

    private static final EntityHeadData PIGLIN = adjust(vec3(10, 8, 8), vec3(-5, 24, -4), vec3(0, 24, 0));

    private static final EntityHeadData VILLAGER = adjust(vec3(8, 10, 8), vec3(-4, 24, -4), vec3(0, 24, 0));

    public static final Map<EntityType<?>, EntityHeadData> ENTITY_HEAD_DATA_BY_TYPE = new HashMap<>(
        Map.ofEntries(
            Map.entry(EntityType.CAMEL, adjust(vec3(7, 8, 19), vec3(-3.5, 22, -24), vec3(0, 23, -9))),
            Map.entry(EntityType.COW, COW),
            Map.entry(EntityType.DONKEY, HORSE),
            Map.entry(EntityType.EVOKER, VILLAGER),
            Map.entry(EntityType.FOX, adjust(vec3(8, 6, 6), vec3(-4, 3.5, -8), vec3(1, 7.5, -3))),
            Map.entry(EntityType.GOAT, adjust(vec3(5, 7, 10), vec3(-3, 16, -14), vec3(-0.5, 10, 0))),
            Map.entry(EntityType.HORSE, HORSE),
            Map.entry(EntityType.ILLUSIONER, VILLAGER),
            Map.entry(EntityType.LLAMA, LLAMA),
            Map.entry(EntityType.MOOSHROOM, COW),
            Map.entry(EntityType.MULE, HORSE),
            Map.entry(EntityType.PANDA, adjust(vec3(13, 10, 9), vec3(-6.5, 7.5, -21), vec3(0, 12.5, -17))),
            Map.entry(EntityType.PIG, adjust(vec3(8, 8, 8), vec3(-4, 8, -14), vec3(0, 12, -6))),
            Map.entry(EntityType.PIGLIN, PIGLIN),
            Map.entry(EntityType.PIGLIN_BRUTE, PIGLIN),
            Map.entry(EntityType.PILLAGER, VILLAGER),
            Map.entry(EntityType.PLAYER, adjust(vec3(8, 8, 8), vec3(-4, 24, -4), vec3(0, 24, 0))),
            Map.entry(EntityType.POLAR_BEAR, adjust(vec3(7, 7, 7), vec3(-3.5, 10, -19), vec3(0, 14, -16 - 3))),
            Map.entry(EntityType.RAVAGER, adjust(vec3(16, 20, 16), vec3(-8, 14, -24), vec3(0, 14, -10 - 2.5))),
            Map.entry(EntityType.SHEEP, adjust(vec3(6, 6, 8), vec3(-3, 16, -14), vec3(0, 18, -8))),
            Map.entry(EntityType.SNIFFER, adjust(vec3(13, 18, 11), vec3(-6.5, 5, -31), vec3(0, 12.5, -19.5))),
            Map.entry(EntityType.TRADER_LLAMA, LLAMA),
            Map.entry(EntityType.VILLAGER, VILLAGER),
            Map.entry(EntityType.VINDICATOR, VILLAGER),
            Map.entry(EntityType.WITCH, VILLAGER),
            Map.entry(EntityType.WANDERING_TRADER, VILLAGER),
            Map.entry(EntityType.WOLF, adjust(vec3(6, 6, 4), vec3(-3, 7.5, -9), vec3(1, 10.5, -7))),
            Map.entry(EntityType.DOLPHIN, adjust(vec3(2, 2, 4), vec3(-1, 0, -13), vec3(0, 0, -3)))
        )
    );

    private static EntityHeadData adjust(Vec3 size, Vec3 position, Vec3 pivot) {
        return new EntityHeadData(size.scale(MULTIPLIER), position.scale(MULTIPLIER), pivot.scale(MULTIPLIER));
    }

    private static Vec3 vec3(double x, double y, double z) {
        return new Vec3(x, y, z);
    }

}
