package mods.cybercat.gigeresque.common.entity.impl.extra;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.particle.Particles;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.tags.GigTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;

public class GooEntity extends Entity {

    public GooEntity(EntityType<? extends Entity> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(false);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            for (int i = 0; i < this.random.nextIntBetweenInclusive(0, 4); i++) {
                this.level().addAlwaysVisibleParticle(Particles.GOO,
                        this.blockPosition().getX() + this.random.nextDouble(), this.blockPosition().getY() + 0.01,
                        this.blockPosition().getZ() + this.random.nextDouble(), 0.0, 0.0, 0.0);
            }
        }
        if (!this.level().isClientSide()) {
            // Ensures it's always at the center of the block
            this.moveTo(this.blockPosition().offset(0, 0, 0), this.getYRot(), this.getXRot());
            // Kill this after it's tickCount is higher
            if (this.tickCount >= this.random.nextIntBetweenInclusive(400, 800)) {
                this.kill();
            }
            // Ensures it always plays a sound when first placed
            if (this.tickCount == 1) {
                doParticleSounds(this.random);
            }
            // Plays a sound every 2 seconds or so
            if (this.tickCount % 40 == 0) {
                doParticleSounds(this.random);
            }
            // Sim Gravity Credit to Boston for this
            this.setDeltaMovement(0, this.getDeltaMovement().y - 0.03999999910593033D, 0);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(0, this.getDeltaMovement().y * 0.9800000190734863D, 0);
            // Do things
            if (this.tickCount % (this.random.nextInt(200 - 100 + 1) + 100) == 0) {
                this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox()).forEach(entity -> {
                    if (entity instanceof LivingEntity livingEntity) {
                        this.damageLivingEntities(livingEntity, this.random);
                    }
                });
            }
        }
    }

    private void doParticleSounds(RandomSource randomSource) {
        this.level().playSound(null, this.blockPosition().getX(), this.blockPosition().getY(),
                this.blockPosition().getZ(), SoundEvents.SCULK_BLOCK_SPREAD, SoundSource.BLOCKS,
                0.2f + randomSource.nextFloat() * 0.2f, 0.9f + randomSource.nextFloat() * 0.15f);
    }

    private void damageLivingEntities(LivingEntity livingEntity, RandomSource randomSource) {
        if (livingEntity.hasEffect(GigStatusEffects.DNA) || livingEntity.getType().is(GigTags.DNAIMMUNE)) return;
        if (Constants.notPlayer.test(livingEntity) || Constants.isNotCreativeSpecPlayer.test(livingEntity)) {
            livingEntity.addEffect(
                    new MobEffectInstance(GigStatusEffects.DNA, Gigeresque.config.gooEffectTickTimer / 2, 0));
        }
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public void checkDespawn() {
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {

    }
}
