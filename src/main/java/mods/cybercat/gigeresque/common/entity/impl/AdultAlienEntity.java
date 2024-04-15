package mods.cybercat.gigeresque.common.entity.impl;

import mod.azure.azurelib.ai.pathing.AzureNavigation;
import mod.azure.azurelib.animatable.GeoEntity;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.client.particle.Particles;
import mods.cybercat.gigeresque.common.block.AcidBlock;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.ai.pathing.AmphibiousNavigation;
import mods.cybercat.gigeresque.common.entity.helper.AzureVibrationUser;
import mods.cybercat.gigeresque.common.entity.helper.Growable;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import mods.cybercat.gigeresque.common.tags.GigTags;
import mods.cybercat.gigeresque.common.util.DamageSourceUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public abstract class AdultAlienEntity extends AlienEntity {

    public int holdingCounter = 0;
    public int biteCounter = 0;
    public int passoutCounter = 0;
    public int wakeupCounter = 0;
    protected long searchingProgress = 0L;

    protected AdultAlienEntity(@NotNull EntityType<? extends AlienEntity> type, @NotNull Level world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();
        // Passing and waking up logic
        var velocityLength = this.getDeltaMovement().horizontalDistance();
        if (!this.getTypeName().getString().equalsIgnoreCase(
                "neomorph") && (velocityLength == 0 && !this.isVehicle() && this.isAlive() && !this.isSearching() && !this.isHissing() && !this.isPassedOut())) {
            if (!this.level().isClientSide) this.passoutCounter++;
            if (this.passoutCounter >= 6000) {
                this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 100, false, false));
                this.triggerAnim("attackController", "passout");
                this.passoutCounter = -6000;
                this.setPassedOutStatus(true);
            }
        }
        if (this.isPassedOut()) {
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 100, false, false));
            if (this.isAggressive()) {
                this.triggerAnim("attackController", "wakeup");
                this.setPassedOutStatus(false);
                if (!this.level().isClientSide) this.passoutCounter = -6000;
                this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 160, 100, false, false));
            }
        }
        if (this.isAggressive() && !this.level().isClientSide) this.passoutCounter = 0;

        if (this.isInWater()) {
            this.searchingProgress = 0;
        }

        // Searching Logic
        if ((this.level().getBlockState(
                this.blockPosition().below()).isSolid() && velocityLength == 0 && !this.isInWater() && !this.isAggressive() && !this.isVehicle() && !this.isHissing() && this.isAlive() && !this.isPassedOut() && !this.isCrawling())) {
            if (!this.level().isClientSide) this.searchingProgress++;

            if (this.searchingProgress == 80) {
                this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 100, false, false));
                this.setIsSearching(true);
            }

            if (this.searchingProgress > 160) {
                this.setIsSearching(false);
                this.searchingProgress = -500;
            }
        }
    }
}
