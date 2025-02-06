package mods.cybercat.gigeresque.common.entity.helper.managers;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.PathfinderMob;

/**
 * Credit to Boston/AVP
 */
public class CrawlingManager {

    private static final String CRAWLING_TAG_KEY = "crawling";

    private final PathfinderMob entity;

    private final EntityDataAccessor<Boolean> isCrawlingEDA;

    public CrawlingManager(PathfinderMob entity, EntityDataAccessor<Boolean> isCrawlingEDA) {
        this.entity = entity;
        this.isCrawlingEDA = isCrawlingEDA;
    }

    public void tick() {
        if (entity.level().isClientSide) {
            return;
        }

        if (entity.isInWater()) {
            return;
        }

        if (!entity.isInWater())
            tryToCrawl();
    }

    public boolean isCrawling() {
        return entity.getEntityData().get(isCrawlingEDA);
    }

    private void tryToCrawl() {
        var blockPosition = entity.blockPosition();
        var level = entity.level();
        var navigation = entity.getNavigation();

        if (level.isClientSide) {
            return;
        }

        var path = navigation.getPath();

        var isTight = isTightSpace(blockPosition);

        if (path != null && path.getNextNodeIndex() < path.getNodeCount()) {
            var previousNode = path.getPreviousNode();
            isTight = isTight || previousNode != null && isTightSpace(previousNode.asBlockPos());
            var nextNode = path.getNextNode();
            isTight = isTight || isTightSpace(nextNode.asBlockPos());
        }
        if (entity.tickCount % 20 == 0)
            entity.getEntityData().set(isCrawlingEDA, isTight);
        if (entity.tickCount % 10 == 0)
            entity.refreshDimensions();
    }

    private boolean isTightSpace(BlockPos blockPos) {
        var level = entity.level();
        var above = blockPos.above();
        var aboveState = level.getBlockState(above);
        return !aboveState.isAir() && aboveState.entityCanStandOn(entity.level(), blockPos, entity);
    }

    public void load(CompoundTag compoundTag) {
        entity.getEntityData().set(isCrawlingEDA, compoundTag.getBoolean(CRAWLING_TAG_KEY));
    }

    public void save(CompoundTag compoundTag) {
        compoundTag.putBoolean(CRAWLING_TAG_KEY, entity.getEntityData().get(isCrawlingEDA));
    }
}
