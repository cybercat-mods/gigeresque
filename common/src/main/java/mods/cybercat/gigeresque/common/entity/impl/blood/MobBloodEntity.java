package mods.cybercat.gigeresque.common.entity.impl.blood;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.client.particle.GigParticles;

public class MobBloodEntity extends Entity {

    public MobBloodEntity(EntityType<? extends Entity> entityType, Level level) {
        super(entityType, level);
        this.setDeltaMovement(Vec3.ZERO);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.04;
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount == 1) {
            this.moveTo(this.blockPosition().offset(0, 0, 0), this.getYRot(), this.getXRot());
        }
        this.applyCustomGravity();
        if (this.level().isClientSide()) {
            this.applyParticle();
        }
        if (!this.level().isClientSide()) {
            if (this.tickCount >= 10) {
                this.kill();
            }
            if (level().getBlockState(this.blockPosition()).is(Blocks.LAVA)) {
                this.remove(RemovalReason.KILLED);
            }
            level().getEntities(this, this.getBoundingBox()).forEach(e -> {
                if (e instanceof AcidEntity && e.tickCount < this.tickCount)
                    e.remove(RemovalReason.KILLED);
            });
        }
    }

    private void applyParticle() {
        for (var i = 0; i < this.random.nextIntBetweenInclusive(0, 4); i++) {
            this.level()
                .addAlwaysVisibleParticle(
                    GigParticles.BLOOD.get(),
                    this.blockPosition().getX() + this.random.nextDouble(),
                    this.blockPosition().getY() + 0.09,
                    this.blockPosition().getZ() + this.random.nextDouble(),
                    0.0,
                    0.0,
                    0.0
                );
        }
    }

    private void applyCustomGravity() {
        this.applyGravity();
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.38));
    }

    @Override
    public void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {}

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compound) {}

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compound) {}

    @Override
    public boolean dampensVibrations() {
        return true;
    }
}
