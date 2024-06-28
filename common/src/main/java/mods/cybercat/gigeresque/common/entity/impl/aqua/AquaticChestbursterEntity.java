package mods.cybercat.gigeresque.common.entity.impl.aqua;

import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.object.PlayState;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import mods.cybercat.gigeresque.common.entity.helper.Growable;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;
import mods.cybercat.gigeresque.common.sound.GigSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.navigation.SmoothGroundNavigation;
import net.tslat.smartbrainlib.api.core.navigation.SmoothWaterBoundPathNavigation;
import org.jetbrains.annotations.NotNull;

public class AquaticChestbursterEntity extends ChestbursterEntity implements Growable {

    private final SmoothGroundNavigation landNavigation = new SmoothGroundNavigation(this, level());
    private final SmoothWaterBoundPathNavigation swimNavigation = new SmoothWaterBoundPathNavigation(this, level());
    private final MoveControl landMoveControl = new MoveControl(this);
    private final LookControl landLookControl = new LookControl(this);
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
    private final SmoothSwimmingMoveControl swimMoveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.7f, 1.0f,
            false);
    private final SmoothSwimmingLookControl swimLookControl = new SmoothSwimmingLookControl(this, 10);

    public AquaticChestbursterEntity(EntityType<? extends AquaticChestbursterEntity> type, Level world) {
        super(type, world);
        navigation = swimNavigation;
        moveControl = swimMoveControl;
        lookControl = swimLookControl;
    }

    @Override
    public void travel(@NotNull Vec3 movementInput) {
        this.navigation = (this.isUnderWater() || (this.level().getFluidState(this.blockPosition()).is(
                Fluids.WATER) && this.level().getFluidState(
                this.blockPosition()).getAmount() >= 8)) ? swimNavigation : landNavigation;
        this.moveControl = (this.wasEyeInWater || (this.level().getFluidState(this.blockPosition()).is(
                Fluids.WATER) && this.level().getFluidState(
                this.blockPosition()).getAmount() >= 8)) ? swimMoveControl : landMoveControl;
        this.lookControl = (this.wasEyeInWater || (this.level().getFluidState(this.blockPosition()).is(
                Fluids.WATER) && this.level().getFluidState(
                this.blockPosition()).getAmount() >= 8)) ? swimLookControl : landLookControl;

        if (this.tickCount % 10 == 0) this.refreshDimensions();

        super.travel(movementInput);
    }

    @Override
    public LivingEntity growInto() {
        var entity = GigEntities.AQUATIC_ALIEN.get().create(level());

        if (hasCustomName()) entity.setCustomName(this.getCustomName());

        return entity;
    }

    @Override
    public @NotNull PathNavigation createNavigation(@NotNull Level world) {
        return swimNavigation;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    protected void jumpInLiquid(@NotNull TagKey<Fluid> fluid) {
    }

    @Override
    public @NotNull EntityDimensions getDefaultDimensions(@NotNull Pose pose) {
        return this.wasEyeInWater ? super.getDefaultDimensions(pose).scale(1.0f, 0.5f) : super.getDefaultDimensions(pose);
    }

    /*
     * ANIMATIONS
     */
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, Constants.LIVING_CONTROLLER, 5, event -> {
            var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
            if (event.isMoving() && !isDead && walkAnimation.speedOld > 0.15F) if (this.isUnderWater()) {
                if (walkAnimation.speedOld >= 0.35F) {
                    return event.setAndContinue(GigAnimationsDefault.RUSH_SWIM);
                } else {
                    return event.setAndContinue(GigAnimationsDefault.SWIM);
                }
            } else if (walkAnimation.speedOld >= 0.35F) return event.setAndContinue(GigAnimationsDefault.RUSH_SLITHER);
            else return event.setAndContinue(GigAnimationsDefault.SLITHER);
            else {
                if (this.tickCount < 5 && this.isBirthed()) return event.setAndContinue(GigAnimationsDefault.BIRTH);
                else {
                    if (this.isUnderWater()) return event.setAndContinue(GigAnimationsDefault.IDLE_WATER);
                    else return event.setAndContinue(GigAnimationsDefault.IDLE_LAND);
                }
            }
        }).setSoundKeyframeHandler(event -> {
            if (event.getKeyframeData().getSound().matches("stepSoundkey") && this.level().isClientSide)
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), GigSounds.BURSTER_CRAWL.get(),
                        SoundSource.HOSTILE, 0.25F, 1.0F, true);
        }));
        controllers.add(new AnimationController<>(this, Constants.ATTACK_CONTROLLER, 0,
                event -> PlayState.STOP).triggerableAnim(Constants.EAT, GigAnimationsDefault.CHOMP).triggerableAnim(
                "death", GigAnimationsDefault.DEATH));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
