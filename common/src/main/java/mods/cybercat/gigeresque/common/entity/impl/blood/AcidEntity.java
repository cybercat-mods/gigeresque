package mods.cybercat.gigeresque.common.entity.impl.blood;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.particle.GigParticles;
import mods.cybercat.gigeresque.common.source.GigDamageSources;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.BlockBreakProgressManager;
import mods.cybercat.gigeresque.common.util.DamageSourceUtils;

public class AcidEntity extends Entity {

    private int acidTicks = 0;

    public AcidEntity(EntityType<? extends Entity> entityType, Level level) {
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
        // Ensures it's always at the center of the block
        if (tickCount == 1)
            this.moveTo(this.blockPosition().offset(0, 0, 0), this.getYRot(), this.getXRot());
        this.applyCustomGravity();
        var canGrief = this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        if (this.level().isClientSide())
            this.applyParticle();
        if (!this.level().isClientSide()) {
            // Kill this after it's tickCount is higher
            if (this.tickCount >= this.random.nextIntBetweenInclusive(400, 800))
                this.kill();
            // Ensures it always plays a sound when first placed
            if (this.tickCount == 1)
                doParticleSounds(this.random);
            // Plays a sound every 2 seconds or so
            if (this.tickCount % 40 == 0)
                doParticleSounds(this.random);
            // Do things
            var blockStateBelow = this.level().getBlockState(this.blockPosition().below());
            acidTicks++;
            if (this.tickCount % 5 == 0 && canGrief && !blockStateBelow.is(GigTags.ACID_RESISTANT)) {
                this.doBlockBreaking(this.random, acidTicks * 2, blockStateBelow);
                acidTicks = 0;
            }
            if (this.tickCount % 40 == 0) {
                this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(1)).forEach(entity -> {
                    if (entity instanceof LivingEntity livingEntity) {
                        this.damageLivingEntities(livingEntity, this.random);
                        if (!CommonMod.config.enabledCreativeBootAcidProtection || Constants.isNotCreativeSpecPlayer.test(livingEntity)) {
                            DamageSourceUtils.damageArmor(livingEntity.getItemBySlot(EquipmentSlot.FEET), this.random, 1, 4);
                        }
                    }
                    if (entity instanceof ItemEntity itemEntity) {
                        this.damageItems(itemEntity, this.random);
                    }
                });
            }
            if (level().getBlockState(this.blockPosition()).is(Blocks.LAVA) && CommonMod.config.enableAcidLavaRemoval)
                this.remove(RemovalReason.KILLED);
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
                    GigParticles.ACID.get(),
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

    private void doBlockBreaking(RandomSource randomSource, int acidTicks, BlockState blockStateBelow) {
        var destorySpeed = blockStateBelow.getDestroySpeed(this.level(), this.blockPosition().below());
        BlockBreakProgressManager.damage(level(), this.blockPosition().below(), destorySpeed * acidTicks);
        this.level()
            .playSound(
                null,
                this.blockPosition().getX(),
                this.blockPosition().getY(),
                this.blockPosition().getZ(),
                SoundEvents.LAVA_EXTINGUISH,
                SoundSource.BLOCKS,
                0.2f + randomSource.nextFloat() * 0.2f,
                0.9f + randomSource.nextFloat() * 0.15f
            );
    }

    private void doParticleSounds(RandomSource randomSource) {
        this.level()
            .playSound(
                null,
                this.blockPosition().getX(),
                this.blockPosition().getY(),
                this.blockPosition().getZ(),
                SoundEvents.LAVA_EXTINGUISH,
                SoundSource.BLOCKS,
                0.2f + randomSource.nextFloat() * 0.2f,
                0.9f + randomSource.nextFloat() * 0.15f
            );
    }

    private void damageItems(ItemEntity itemEntity, RandomSource randomSource) {
        if (itemEntity.getItem().is(GigTags.ACID_IMMUNE_ITEMS))
            return;
        var itemStack = itemEntity.getItem();
        if (itemStack.getMaxDamage() < 2) {
            itemStack.shrink(1);
        } else {
            itemStack.setDamageValue(itemStack.getDamageValue() + randomSource.nextIntBetweenInclusive(0, 4));
        }
    }

    private void damageLivingEntities(LivingEntity livingEntity, RandomSource randomSource) {
        if (livingEntity.hasEffect(GigStatusEffects.ACID) || livingEntity.getType().is(GigTags.ACID_RESISTANT_ENTITY))
            return;
        if (Constants.notPlayer.test(livingEntity) || Constants.isNotCreativeSpecPlayer.test(livingEntity)) {
            livingEntity.addEffect(
                new MobEffectInstance(GigStatusEffects.ACID, 60, randomSource.nextIntBetweenInclusive(0, 4))
            );
        }
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
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

    @Override
    public @NotNull InteractionResult interact(Player player, @NotNull InteractionHand hand) {
        if (player.getItemInHand(hand).is(Items.GLASS_BOTTLE) && !player.level().isClientSide()) {
            player.getItemInHand(hand).hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            player.hurt(GigDamageSources.of(player.level(), GigDamageSources.ACID), CommonMod.config.acidDamage);

            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.hurt(GigDamageSources.of(player.level(), GigDamageSources.ACID), CommonMod.config.acidDamage);
                var advancement = serverPlayer.server.getAdvancements().get(Constants.modResource("dontacidbottle"));
                if (advancement != null && !serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone()) {
                    for (var s : serverPlayer.getAdvancements().getOrStartProgress(advancement).getRemainingCriteria()) {
                        serverPlayer.getAdvancements().award(advancement, s);
                    }
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.interact(player, hand);
    }
}
