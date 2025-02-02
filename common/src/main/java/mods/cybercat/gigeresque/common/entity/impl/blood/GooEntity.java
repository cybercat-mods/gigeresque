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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
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

public class GooEntity extends Entity {

    public GooEntity(EntityType<? extends Entity> entityType, Level level) {
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
        if (this.level().isClientSide()) {
            this.applyParticle();
        }
        if (!this.level().isClientSide()) {
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
            // Do things
            this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(1)).forEach(livingEntity -> {
                this.damageLivingEntities(livingEntity, this.random);
            });
            if (level().getBlockState(this.blockPosition()).is(Blocks.LAVA) && CommonMod.config.enableAcidLavaRemoval)
                this.remove(RemovalReason.KILLED);
            level().getEntities(this, this.getBoundingBox().inflate(1)).forEach(e -> {
                if (e instanceof GooEntity && e.tickCount < this.tickCount)
                    e.remove(RemovalReason.KILLED);
            });
        }
    }

    private void applyParticle() {
        for (var i = 0; i < this.random.nextIntBetweenInclusive(0, 4); i++) {
            this.level()
                .addAlwaysVisibleParticle(
                    GigParticles.GOO.get(),
                    this.blockPosition().getX() + this.random.nextDouble(),
                    this.blockPosition().getY() + 0.01,
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

    private void doParticleSounds(RandomSource randomSource) {
        this.level()
            .playSound(
                null,
                this.blockPosition().getX(),
                this.blockPosition().getY(),
                this.blockPosition().getZ(),
                SoundEvents.SCULK_BLOCK_SPREAD,
                SoundSource.BLOCKS,
                0.2f + randomSource.nextFloat() * 0.2f,
                0.9f + randomSource.nextFloat() * 0.15f
            );
    }

    private void damageLivingEntities(LivingEntity livingEntity, RandomSource randomSource) {
        if (livingEntity.hasEffect(GigStatusEffects.DNA) || livingEntity.getType().is(GigTags.DNAIMMUNE))
            return;
        if (Constants.notPlayer.test(livingEntity) || Constants.isNotCreativeSpecPlayer.test(livingEntity)) {
            livingEntity.addEffect(
                new MobEffectInstance(GigStatusEffects.DNA, CommonMod.config.gooEffectTickTimer / 2, 0)
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
            player.getItemInHand(hand).hurtAndBreak(1, player, Player.getSlotForHand(hand));
            player.addEffect(new MobEffectInstance(GigStatusEffects.DNA, 1000, 0));

            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.hurt(GigDamageSources.of(player.level(), GigDamageSources.ACID), CommonMod.config.acidDamage);
                var advancement = serverPlayer.server.getAdvancements().get(Constants.modResource("dontgoobottle"));
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
