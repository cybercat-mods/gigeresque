package com.bvanseg.gigeresque.common.util;

import com.bvanseg.gigeresque.common.config.ConfigAccessorJava;
import com.bvanseg.gigeresque.common.entity.AlienEntityJava;
import com.bvanseg.gigeresque.common.entity.impl.ClassicAlienEntityJava;
import com.bvanseg.gigeresque.common.entity.impl.FacehuggerEntityJava;
import com.bvanseg.gigeresque.interfacing.Eggmorphable;
import com.bvanseg.gigeresque.interfacing.Host;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.SwordItem;

public class EntityUtils {
    private EntityUtils() {
    }

    public static boolean isPotentialHost(Entity entity) {
        if (entity == null) return false;
        boolean playerCondition = !(entity instanceof PlayerEntity) || !((PlayerEntity) entity).isCreative() && !entity.isSpectator();

        if (ConfigAccessorJava.isTargetBlacklisted(FacehuggerEntityJava.class, entity)) return false;
        if (ConfigAccessorJava.isTargetWhitelisted(FacehuggerEntityJava.class, entity)) return true;

        boolean vehicleCondition = (entity.getVehicle() == null) || !(entity.getVehicle() instanceof AlienEntityJava);

        return entity.isAlive() &&
                entity instanceof LivingEntity &&
                !(entity instanceof AlienEntityJava) &&
                entity.getPassengerList().isEmpty() &&
                ((Host) entity).doesNotHaveParasite() &&
                ((Eggmorphable) entity).isNotEggmorphing() &&
                ((LivingEntity) entity).getGroup() != EntityGroup.UNDEAD &&
                vehicleCondition &&
                playerCondition;
    }

    public static boolean isEggmorphable(Entity entity) {
        if (entity == null) return false;
        boolean playerCondition = !(entity instanceof PlayerEntity) || !((PlayerEntity) entity).isCreative() && !entity.isSpectator();

        if (ConfigAccessorJava.isTargetBlacklisted(ClassicAlienEntityJava.class, entity)) return false;
        if (ConfigAccessorJava.isTargetWhitelisted(ClassicAlienEntityJava.class, entity)) return true;

        boolean weakCondition = !(entity instanceof LivingEntity) || (((LivingEntity) entity).getHealth() / ((LivingEntity) entity).getMaxHealth() < 0.25f) || ((LivingEntity) entity).getHealth() <= 4f;

        boolean threatCondition = !(entity instanceof LivingEntity) || ((LivingEntity) entity).getActiveItem().getItem() instanceof SwordItem || ((LivingEntity) entity).getActiveItem().getItem() instanceof RangedWeaponItem;

        return entity.isAlive() &&
                entity instanceof LivingEntity &&
                !(entity instanceof AlienEntityJava) &&
                ((Host) entity).doesNotHaveParasite() &&
                ((Eggmorphable) entity).isNotEggmorphing() &&
                ((LivingEntity) entity).getGroup() != EntityGroup.UNDEAD &&
                playerCondition &&
                weakCondition &&
                !threatCondition;
    }

    public static boolean isFacehuggerAttached(Entity entity) {
        return entity != null && entity.getPassengerList().stream().anyMatch(it -> it instanceof FacehuggerEntityJava);
    }
}
