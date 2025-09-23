package mods.cybercat.gigeresque.common.entity.impl.misc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.common.entity.helper.AnimationDispatcher;
import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;

public class HologramEntity extends Entity {

    public static final EntityDataAccessor<Integer> DISTANCE_STATE = SynchedEntityData.defineId(
        HologramEntity.class,
        EntityDataSerializers.INT
    );

    public static final EntityDataAccessor<Integer> DISTANCE_FROM_STRUCTURE = SynchedEntityData.defineId(
        HologramEntity.class,
        EntityDataSerializers.INT
    );

    public AnimationDispatcher animationDispatcher;

    public HologramEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.animationDispatcher = new AnimationDispatcher(this);
    }

    public int getDistanceState() {
        return this.entityData.get(DISTANCE_STATE);
    }

    public void setDistanceState(int distanceState) {
        this.entityData.set(DISTANCE_STATE, distanceState);
    }

    public int getDistanceFromStructure() {
        return this.entityData.get(DISTANCE_FROM_STRUCTURE);
    }

    public void setDistanceFromStructure(int distanceState) {
        this.entityData.set(DISTANCE_FROM_STRUCTURE, distanceState);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        builder.define(DISTANCE_STATE, 0);
        builder.define(DISTANCE_FROM_STRUCTURE, 0);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compound) {
        this.setDistanceState(compound.getInt("distance_state"));
        this.setDistanceFromStructure(compound.getInt("distance_from_structure"));
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compound) {
        compound.putInt("distance_state", this.getDistanceState());
        compound.putInt("distance_from_structure", this.getDistanceFromStructure());
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    @Override
    public boolean dampensVibrations() {
        return true;
    }

    @Override
    public boolean ignoreExplosion(@NotNull Explosion explosion) {
        return true;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    public void tick() {
        // Ensures it's always at the center of the block
        if (tickCount == 1)
            this.moveTo(this.blockPosition().offset(0, 0, 0), this.getYRot(), this.getXRot());
        this.applyGravity();
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98));
        super.tick();
        if (!this.level().isClientSide() && this.tickCount >= 125) {
            this.kill();
        }
    }
}
