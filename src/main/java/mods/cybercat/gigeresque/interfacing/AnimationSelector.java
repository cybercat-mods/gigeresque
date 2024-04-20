package mods.cybercat.gigeresque.interfacing;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import software.bernie.geckolib.animatable.GeoEntity;

@FunctionalInterface
public interface AnimationSelector<T extends PathfinderMob & AbstractAlien> {
    void select(T entity);
}
