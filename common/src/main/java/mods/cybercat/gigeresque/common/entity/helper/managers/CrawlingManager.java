package mods.cybercat.gigeresque.common.entity.helper.managers;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.tags.BlockTags;

import mods.cybercat.gigeresque.common.entity.AlienEntity;

/**
 * Credit to Boston/AVP
 */
public class CrawlingManager {

    private static final String CRAWLING_TAG_KEY = "crawling";

    private final AlienEntity entity;

    private final EntityDataAccessor<Boolean> isCrawlingEDA;

    public CrawlingManager(AlienEntity entity, EntityDataAccessor<Boolean> isCrawlingEDA) {
        this.entity = entity;
        this.isCrawlingEDA = isCrawlingEDA;
    }

    public void tick() {
        if (entity.level().isClientSide) {
            return;
        }

        if (entity.isUnderWater()) {
            return;
        }

        if (!entity.isUnderWater())
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

        if (entity.isVehicle()) {
            return;
        }

        var path = navigation.getPath();

        var shouldCrawl = isTightSpace(blockPosition) || entity.canClimb;

        if (path != null && path.getNextNodeIndex() < path.getNodeCount()) {
            var previousNode = path.getPreviousNode();
            shouldCrawl = shouldCrawl || previousNode != null && isTightSpace(previousNode.asBlockPos());
            var nextNode = path.getNextNode();
            shouldCrawl = shouldCrawl || isTightSpace(nextNode.asBlockPos());
        }
        if (entity.tickCount % 20 == 0)
            entity.getEntityData().set(isCrawlingEDA, shouldCrawl);
        if (entity.tickCount % 10 == 0)
            entity.refreshDimensions();
    }

    private boolean isTightSpace(BlockPos blockPos) {
        var level = entity.level();
        var above = blockPos.above();
        var aboveState = level.getBlockState(above);
        return (!aboveState.isAir() && aboveState.entityCanStandOn(entity.level(), blockPos, entity)) || level.getBlockState(
            blockPos.below()
        ).is(BlockTags.STAIRS);
    }

    public void load(CompoundTag compoundTag) {
        entity.getEntityData().set(isCrawlingEDA, compoundTag.getBoolean(CRAWLING_TAG_KEY));
    }

    public void save(CompoundTag compoundTag) {
        compoundTag.putBoolean(CRAWLING_TAG_KEY, entity.getEntityData().get(isCrawlingEDA));
    }
}
