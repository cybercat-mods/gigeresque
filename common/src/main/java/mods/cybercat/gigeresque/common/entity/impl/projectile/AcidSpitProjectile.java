package mods.cybercat.gigeresque.common.entity.impl.projectile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.client.particle.GigParticles;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.source.GigDamageSources;

public class AcidSpitProjectile extends Projectile implements ItemSupplier {

    public double accelerationPower = 0.1;

    public AcidSpitProjectile(EntityType<? extends AcidSpitProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public AcidSpitProjectile(
        EntityType<? extends AcidSpitProjectile> entityType,
        double x,
        double y,
        double z,
        Vec3 movement,
        Level level
    ) {
        this(entityType, level);
        this.moveTo(x, y, z, this.getYRot(), this.getXRot());
        this.reapplyPosition();
        this.assignDirectionalMovement(movement, this.accelerationPower);
    }

    public AcidSpitProjectile(Level level, LivingEntity shooter, Vec3 movement) {
        this(GigEntities.ACID_PROJECTILE.get(), shooter.getX(), shooter.getY(), shooter.getZ(), movement, level);
        this.setOwner(shooter);
        this.setRot(shooter.getYRot(), shooter.getXRot());
    }

    private void assignDirectionalMovement(Vec3 movement, double accelerationPower) {
        this.setDeltaMovement(movement.normalize().scale(accelerationPower));
        this.hasImpulse = true;
    }

    @Override
    public @NotNull ItemStack getItem() {
        return Items.AIR.getDefaultInstance();
    }

    @Override
    protected void onHit(HitResult result) {
        HitResult.Type type = result.getType();
        if (type == HitResult.Type.BLOCK)
            handleBlockHit(result);
        else if (type == HitResult.Type.ENTITY)
            handleEntityHit((EntityHitResult) result);
    }

    private void handleEntityHit(EntityHitResult result) {
        var entity = result.getEntity();
        var speed = this.getDeltaMovement().length();
        if (speed > 0.1 && entity instanceof LivingEntity livingEntity && !livingEntity.getUseItem().is(Items.SHIELD)) {
            livingEntity.hurt(GigDamageSources.of(this.level(), GigDamageSources.ACID), 4.0f);
            if (livingEntity instanceof Player player)
                player.getUseItem().hurtAndBreak(10, player, player.getEquipmentSlotForItem(player.getUseItem()));
        }
        this.setDeltaMovement(this.getDeltaMovement().multiply(0.25, 0.25, 0.25));
        this.kill();
    }

    private void handleBlockHit(HitResult result) {
        var blockResult = (BlockHitResult) result;
        var level = level();
        var resultPos = blockResult.getBlockPos();
        if (!this.level().isClientSide()) {
            var acidEntity = GigEntities.ACID.get().create(level);
            if (acidEntity != null) {
                acidEntity.moveTo(resultPos, 0, 0);
                level.addFreshEntity(acidEntity);
            }
        }
        this.kill();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {}

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putDouble("acceleration_power", this.accelerationPower);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("acceleration_power", 6)) {
            this.accelerationPower = compound.getDouble("acceleration_power");
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount >= 300)
            this.remove(Entity.RemovalReason.DISCARDED);
        if (this.level().isClientSide) {
            var x = this.getX() + (this.random.nextDouble()) * this.getBbWidth() * 0.5D;
            var y = this.getZ() + (this.random.nextDouble()) * this.getBbWidth() * 0.5D;
            var initialVelocityY = 0.1;
            var gravityEffect = -0.02;
            this.level().addParticle(GigParticles.ACID.get(), true, x, this.getY(0.5), y, 0, initialVelocityY, gravityEffect);
            this.playSound(SoundEvents.LAVA_EXTINGUISH, 3.0f, 1.0f);
        }
        if ((this.getOwner() == null || !this.getOwner().isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            this.checkInsideBlocks();
            var currentVelocity = this.getDeltaMovement();
            var newX = this.getX() + currentVelocity.x;
            var newY = this.getY() + currentVelocity.y;
            var newZ = this.getZ() + currentVelocity.z;
            ProjectileUtil.rotateTowardsMovement(this, 0.2F);
            float dragFactor = this.isInWater() ? 0.8F : 0.95F;
            var adjustedVelocity = currentVelocity.add(currentVelocity.normalize().scale(this.accelerationPower)).scale(dragFactor);
            this.setDeltaMovement(adjustedVelocity);
            this.setPos(newX, newY, newZ);
        }
        if (!this.level().isClientSide) {
            this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(1)).forEach(livingEntity -> {
                if (livingEntity.getUseItem().is(Items.SHIELD)) {
                    livingEntity.getUseItem()
                        .hurtAndBreak(10, livingEntity, livingEntity.getEquipmentSlotForItem(livingEntity.getUseItem()));
                } else {
                    livingEntity.hurt(GigDamageSources.of(this.level(), GigDamageSources.ACID), 4.0f);
                }
            });
        }
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }
}
