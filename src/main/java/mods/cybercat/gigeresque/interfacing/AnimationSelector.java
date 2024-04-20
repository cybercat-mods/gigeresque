package mods.cybercat.gigeresque.interfacing;

import mod.azure.azurelib.common.api.common.animatable.GeoEntity;
import net.minecraft.world.entity.PathfinderMob;

@FunctionalInterface
public interface AnimationSelector<T extends PathfinderMob & AbstractAlien & GeoEntity> {
    void select(T entity);
}
