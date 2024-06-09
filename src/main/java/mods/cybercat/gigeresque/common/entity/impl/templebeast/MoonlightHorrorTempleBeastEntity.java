package mods.cybercat.gigeresque.common.entity.impl.templebeast;

import mod.azure.azurelib.common.internal.common.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.common.internal.common.core.animation.AnimatableManager;
import mod.azure.azurelib.common.internal.common.core.animation.AnimationController;
import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.helper.GigAnimationsDefault;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class MoonlightHorrorTempleBeastEntity extends AlienEntity {
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    public MoonlightHorrorTempleBeastEntity(EntityType<? extends AlienEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public int getAcidDiameter() {
        return 3;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH,
                Gigeresque.config.moonlightHorrorTempleBeastXenoHealth).add(Attributes.ARMOR,
                Gigeresque.config.moonlightHorrorTempleBeastXenoArmor).add(Attributes.ARMOR_TOUGHNESS, 0.0).add(
                Attributes.KNOCKBACK_RESISTANCE, 0.0).add(Attributes.FOLLOW_RANGE, 16.0).add(Attributes.MOVEMENT_SPEED,
                0.23000000417232513).add(Attributes.ATTACK_DAMAGE,
                Gigeresque.config.moonlightHorrorTempleBeastAttackDamage).add(Attributes.ATTACK_KNOCKBACK, 0.3);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, Constants.LIVING_CONTROLLER, 5, event -> {
            return event.setAndContinue(GigAnimationsDefault.IDLE);
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
