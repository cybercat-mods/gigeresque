package mods.cybercat.gigeresque.common.entity.impl.extra;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.particle.Particles;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.tags.GigTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AcidEntity extends Entity {

    private long lastUpdateTime = 0L;

    public AcidEntity(EntityType<? extends Entity> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(false);
    }

    @Override
    public void tick() {
        super.tick();
        var canGrief = this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        if (this.level().isClientSide()) {
            for (int i = 0; i < this.random.nextIntBetweenInclusive(0, 4); i++) {
                this.level().addAlwaysVisibleParticle(Particles.ACID,
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
                var blockStateBelow = this.level().getBlockState(this.blockPosition().below());
                if (canGrief && !blockStateBelow.is(GigTags.ACID_RESISTANT)) {
                    var hardness = blockStateBelow.getDestroySpeed(this.level(), this.blockPosition().below());
                    var currentTime = System.currentTimeMillis();
                    // Check if enough time has elapsed since the last update
                    if (currentTime - lastUpdateTime >= (1000L * Math.max(1, (int) (hardness * 20)))) {
                        lastUpdateTime = currentTime;
                        this.doBlockBreaking(this.random);
                    }
                }
                this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox()).forEach(entity -> {
                    if (entity instanceof LivingEntity livingEntity) {
                        this.damageLivingEntities(livingEntity, this.random);
                        this.damageBoots(livingEntity.getItemBySlot(EquipmentSlot.FEET), this.random);
                    }
                    if (entity instanceof ItemEntity itemEntity) {
                        this.damageItems(itemEntity, this.random);
                    }
                });
            }
            level().getEntities(this, this.getBoundingBox()).forEach(e -> {
                if (e instanceof AcidEntity && e.tickCount < this.tickCount) e.remove(RemovalReason.KILLED);
            });
        }
    }

    private void doBlockBreaking(RandomSource randomSource) {
        this.level().setBlock(this.blockPosition().below(), Blocks.AIR.defaultBlockState(), 2);
        this.level().playSound(null, this.blockPosition().getX(), this.blockPosition().getY(),
                this.blockPosition().getZ(), SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS,
                0.2f + randomSource.nextFloat() * 0.2f, 0.9f + randomSource.nextFloat() * 0.15f);
    }

    private void doParticleSounds(RandomSource randomSource) {
        this.level().playSound(null, this.blockPosition().getX(), this.blockPosition().getY(),
                this.blockPosition().getZ(), SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS,
                0.2f + randomSource.nextFloat() * 0.2f, 0.9f + randomSource.nextFloat() * 0.15f);
    }

    private void damageItems(ItemEntity itemEntity, RandomSource randomSource) {
        if (itemEntity.getItem().is(GigTags.ACID_IMMUNE_ITEMS)) return;
        var itemStack = itemEntity.getItem();
        if (itemStack.getMaxDamage() < 2) {
            itemStack.shrink(1);
        } else {
            itemStack.setDamageValue(itemStack.getDamageValue() + randomSource.nextIntBetweenInclusive(0, 4));
        }
    }

    private void damageBoots(ItemStack itemStack, RandomSource randomSource) {
        if (!Objects.equals(itemStack, ItemStack.EMPTY) && !itemStack.is(GigTags.ACID_IMMUNE_ITEMS)) {
            itemStack.setDamageValue(itemStack.getDamageValue() + randomSource.nextIntBetweenInclusive(0, 4));
        }
    }

    private void damageLivingEntities(LivingEntity livingEntity, RandomSource randomSource) {
        if (livingEntity.hasEffect(GigStatusEffects.ACID) || livingEntity.getType().is(GigTags.ACID_RESISTANT_ENTITY))
            return;
        if (Constants.notPlayer.test(livingEntity) || Constants.isNotCreativeSpecPlayer.test(livingEntity)) {
            livingEntity.addEffect(
                    new MobEffectInstance(GigStatusEffects.ACID, 60, randomSource.nextIntBetweenInclusive(0, 4)));
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
    protected void readAdditionalSaveData(@NotNull CompoundTag compound) {

    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compound) {

    }
}
