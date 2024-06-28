package mods.cybercat.gigeresque.common.entity.ai.sensors;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mods.cybercat.gigeresque.common.entity.ai.GigMemoryTypes;
import mods.cybercat.gigeresque.common.entity.ai.GigSensors;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.PredicateSensor;
import net.tslat.smartbrainlib.util.BrainUtils;
import net.tslat.smartbrainlib.util.EntityRetrievalUtil;

import java.util.Comparator;
import java.util.List;

public class ItemEntitySensor<E extends LivingEntity> extends PredicateSensor<ItemEntity, E> {
    private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(GigMemoryTypes.FOOD_ITEMS.get());

    public ItemEntitySensor() {
        setScanRate(entity -> 15);
        setPredicate((item, entity) -> {
            ItemStack itemStack = item.getItem();
            return (itemStack.is(ItemTags.MEAT) || itemStack.is(Items.WHEAT) || itemStack.is(Items.WHEAT_SEEDS) || itemStack.is(Items.BEETROOT_SEEDS) || itemStack.is(Items.MELON_SEEDS) || itemStack.is(Items.PUMPKIN_SEEDS)) && item.isAlive() && !item.hasPickUpDelay();
        });
    }

    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return MEMORIES;
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return GigSensors.FOOD_ITEMS.get();
    }

    @Override
    protected void doTick(ServerLevel level, E entity) {
        List<ItemEntity> projectiles = EntityRetrievalUtil.getEntities(level, entity.getBoundingBox().inflate(7), target -> target instanceof ItemEntity projectile && predicate().test(projectile, entity));

        if (!projectiles.isEmpty()) {
            projectiles.sort(Comparator.comparingDouble(entity::distanceToSqr));
            BrainUtils.setMemory(entity, GigMemoryTypes.FOOD_ITEMS.get(), projectiles);
        } else {
            BrainUtils.clearMemory(entity, GigMemoryTypes.FOOD_ITEMS.get());
        }
    }
}