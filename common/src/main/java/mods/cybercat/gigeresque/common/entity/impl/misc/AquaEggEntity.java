package mods.cybercat.gigeresque.common.entity.impl.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import mods.cybercat.gigeresque.common.entity.helper.Growable;

public class AquaEggEntity extends Entity implements Growable {

    private static final EntityDataAccessor<Float> GROWTH = SynchedEntityData.defineId(AquaEggEntity.class, EntityDataSerializers.FLOAT);

    public float growthCounter = 0;

    public AquaEggEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.setYRot(this.random.nextFloat() * 360.0F);
    }

    @Override
    public void tick() {
        super.tick();
        if (level() instanceof ServerLevel && this.isAlive()) {
            if (this.getGrowth() <= this.getMaxGrowth() && this.tickCount % Constants.TPS == 0) {
                if (CommonMod.config.generalConfigs.enableLogging && this.getGrowth() > 0) {
                    CommonMod.LOGGER.warn(
                        "Current Growth: {} of {} located at {}",
                        this.getGrowth(),
                        this.getDisplayName().getString(),
                        this.blockPosition()
                    );
                }
                this.growthCounter++;
                this.setGrowth((this.getGrowth() + 1) * getGrowthMultiplier());
            } else if (this.getGrowth() >= this.getMaxGrowth()) {
                this.growUp(this);
            }
        }

        GigCommonMethods.handleFloatingPhysics(this);
        GigCommonMethods.handleCollisionPhysics(this);
        GigCommonMethods.handleMovement(this);
    }

    @Override
    public @NotNull BlockPos getBlockPosBelowThatAffectsMyMovement() {
        return this.getOnPos(0.999999F);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.04;
    }

    @Override
    public float getMaxGrowth() {
        return 600;
    }

    @Override
    public LivingEntity growInto() {
        return GigEntities.FACEHUGGER.get().create(level());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        builder.define(GROWTH, 0.0f);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compound) {
        this.setGrowth(compound.getFloat("growth"));
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compound) {
        compound.putFloat("growth", this.getGrowth());
    }

    @Override
    public float getGrowth() {
        return entityData.get(GROWTH);
    }

    @Override
    public void setGrowth(float growth) {
        this.growthCounter = growth;
        entityData.set(GROWTH, growth);
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public @NotNull SoundSource getSoundSource() {
        return SoundSource.AMBIENT;
    }

    @Override
    public float getVisualRotationYInDegrees() {
        return 180.0F - ((this.tickCount + 0.5F) / 20.0F + this.random.nextFloat() * (float) Math.PI * 2.0F) / (float) (Math.PI * 2)
            * 360.0F;
    }
}
