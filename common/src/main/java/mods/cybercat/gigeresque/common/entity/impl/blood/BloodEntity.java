package mods.cybercat.gigeresque.common.entity.impl.blood;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
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

public class BloodEntity extends Entity {

    public enum Type {
        BLOOD,
        ACID,
        GOO,
    }

    final Type type;

    public static BloodEntity place(EntityType<?> type, Level level, BlockPos pos) {
        var entity = type.create(level);
        assert entity != null;
        assert entity instanceof BloodEntity;
        entity.moveTo(pos, 0, 0);
        level.addFreshEntity(entity);
        return (BloodEntity) entity;
    }

    public BloodEntity(EntityType<? extends Entity> entityType, Level level, Type type) {
        super(entityType, level);
        this.setDeltaMovement(Vec3.ZERO);
        this.type = type;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.04;
    }

    @Override
    public void tick() {
        super.tick();

        // particles
        if (level().isClientSide()) {
            var particleType = switch (type) {
                case BLOOD -> GigParticles.BLOOD.get();
                case ACID -> GigParticles.ACID.get();
                case GOO -> GigParticles.GOO.get();
            };
            for (var i = 0; i < random.nextIntBetweenInclusive(0, 4); i++) {
                level().addAlwaysVisibleParticle(
                    particleType,
                    blockPosition().getX() + random.nextDouble(),
                    blockPosition().getY() + 0.09,
                    blockPosition().getZ() + random.nextDouble(),
                    0.0,
                    0.0,
                    0.0
                );
            }
            return;
        }

        // movement stuff
        if (tickCount == 1) {
            moveTo(blockPosition().offset(0, 0, 0), getYRot(), getXRot());
        }
        applyCustomGravity();

        // griefing
        if (
            type == Type.ACID && tickCount % 5 == 0 &&
                level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)
        ) {
            var blockStateBelow = level().getBlockState(blockPosition().below());
            if (!blockStateBelow.is(GigTags.ACID_RESISTANT)) {
                var blockHardness = blockStateBelow.getDestroySpeed(
                    level(),
                    blockPosition().below()
                );
                var destroySpeedMultiplier = 10;
                BlockBreakProgressManager.damage(
                    level(),
                    blockPosition().below(),
                    blockHardness * destroySpeedMultiplier
                );
                level().playSound(
                    null,
                    blockPosition().getX(),
                    blockPosition().getY(),
                    blockPosition().getZ(),
                    SoundEvents.LAVA_EXTINGUISH,
                    SoundSource.BLOCKS,
                    0.2f + random.nextFloat() * 0.2f,
                    0.9f + random.nextFloat() * 0.15f
                );
            }
        }

        applyContactEffects();

        // removal
        if (
            (type == Type.BLOOD && tickCount >= 10) ||
                tickCount >= random.nextIntBetweenInclusive(400, 800) ||
                (CommonMod.config.alienblockConfigs.enableAcidLavaRemoval &&
                    level().getBlockState(blockPosition()).is(Blocks.LAVA))
        ) {
            kill();
        }

        for (var e : level().getEntities(this, getBoundingBox())) {
            if (
                e instanceof BloodEntity blood
                    && blood.type == type
                    && e.tickCount < tickCount
            ) {
                e.kill();
            }
        }

        // sounds
        var soundInterval = 40;
        if (tickCount == 1 || tickCount % soundInterval == 0) {
            SoundEvent sound = switch (type) {
                case BLOOD -> null;
                case ACID -> SoundEvents.SCULK_BLOCK_SPREAD;
                case GOO -> SoundEvents.LAVA_EXTINGUISH;
            };
            if (sound != null) {
                level().playSound(
                    null,
                    blockPosition().getX(),
                    blockPosition().getY(),
                    blockPosition().getZ(),
                    sound,
                    SoundSource.BLOCKS,
                    0.2f + random.nextFloat() * 0.2f,
                    0.9f + random.nextFloat() * 0.15f
                );
            }
        }
    }

    private void applyContactEffects() {
        var entities = level().getEntitiesOfClass(Entity.class, getBoundingBox().inflate(1));
        Holder<MobEffect> effect = null;
        TagKey<EntityType<?>> resistanceTag = null;
        int durationTicks = 0;
        switch (type) {
            case BLOOD -> {
                return;
            }
            case ACID -> {
                effect = GigStatusEffects.ACID;
                resistanceTag = GigTags.ACID_RESISTANT_ENTITY;
                durationTicks = 60;
            }
            case GOO -> {
                effect = GigStatusEffects.DNA;
                resistanceTag = GigTags.DNAIMMUNE;
                durationTicks = CommonMod.config.alienblockConfigs.gooEffectTickTimer / 2;
            }
            case null -> {
                assert false; // null case to enforce exhaustiveness
            }
        }

        assert effect != null;
        assert resistanceTag != null;

        for (var e : entities) {
            if (e instanceof LivingEntity living) {
                if (living.hasEffect(effect) || e.getType().is(resistanceTag)) {
                    continue;
                }
                if (Constants.notPlayer.test(e) || Constants.isNotCreativeSpecPlayer.test(e)) {
                    living.addEffect(
                        new MobEffectInstance(effect, durationTicks, random.nextIntBetweenInclusive(0, 4))
                    );
                }
                if (type == Type.ACID) {
                    DamageSourceUtils.damageArmor(living.getItemBySlot(EquipmentSlot.FEET), this.random, 1, 4);
                }
            } else if (
                tickCount % 40 == 0 &&
                    type == Type.ACID &&
                    e instanceof ItemEntity item &&
                    !item.getItem().is(GigTags.ACID_IMMUNE_ITEMS)
            ) {
                var itemStack = item.getItem();
                if (itemStack.getMaxDamage() < 2) {
                    itemStack.shrink(1);
                } else {
                    itemStack.setDamageValue(itemStack.getDamageValue() + random.nextIntBetweenInclusive(0, 4));
                }
            }
        }
    }

    private void applyCustomGravity() {
        applyGravity();
        move(MoverType.SELF, getDeltaMovement());
        setDeltaMovement(getDeltaMovement().scale(0.38));
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

    // TODO figure out why this is never called - it seems like you cannot interact with blood entities?
    // note this code is untested
    @Override
    public @NotNull InteractionResult interact(@NotNull Player player, @NotNull InteractionHand hand) {
        if (type == Type.BLOOD) {
            return super.interact(player, hand);
        }

        if (player.getItemInHand(hand).is(Items.GLASS_BOTTLE) && player instanceof ServerPlayer serverPlayer) {
            player.getItemInHand(hand).hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));

            String advancementID = null;
            switch (type) {
                case BLOOD -> {
                    assert false;
                }
                case ACID -> {
                    player.hurt(GigDamageSources.of(player.level(), GigDamageSources.ACID), CommonMod.config.alienblockConfigs.acidDamage);
                    advancementID = "dontacidbottle";
                }
                case GOO -> {
                    player.addEffect(new MobEffectInstance(GigStatusEffects.DNA, 1000, 0));
                    advancementID = "dontgoobottle";
                }
                case null -> {
                    assert false; // null case to enforce exhaustiveness
                }
            }
            assert advancementID != null;

            var advancement = serverPlayer.server.getAdvancements().get(Constants.modResource(advancementID));
            assert advancement != null;

            if (!serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone()) {
                for (var s : serverPlayer.getAdvancements().getOrStartProgress(advancement).getRemainingCriteria()) {
                    serverPlayer.getAdvancements().award(advancement, s);
                }
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }
        return super.interact(player, hand);
    }
}
