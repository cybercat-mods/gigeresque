package mods.cybercat.gigeresque.common.entity.impl.aqua;

import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.helper.Growable;
import mods.cybercat.gigeresque.common.entity.impl.classic.ChestbursterEntity;

public class AquaticChestbursterEntity extends ChestbursterEntity implements Growable {

    public AquaticChestbursterEntity(EntityType<? extends AquaticChestbursterEntity> type, Level world) {
        super(type, world);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.15F, 1.0F, true);
    }

    @Override
    public LivingEntity growInto() {
        var entity = GigEntities.AQUATIC_ALIEN.get().create(level());

        if (hasCustomName() && entity != null)
            entity.setCustomName(this.getCustomName());

        return entity;
    }

    @Override
    public @NotNull EntityDimensions getDefaultDimensions(@NotNull Pose pose) {
        return this.wasEyeInWater ? super.getDefaultDimensions(pose).scale(1.0f, 0.5f) : super.getDefaultDimensions(pose);
    }

    @Override
    protected void handleIdleAnimations() {if (this.isInWater()) {
        GigCommonMethods.setAnimation(animationDispatcher::sendIdleWater);
        } else {
            GigCommonMethods.setAnimation(animationDispatcher::sendIdleLand);
        }
    }
}
