package mods.cybercat.gigeresque.interfacing;

import net.minecraft.world.entity.PathfinderMob;

@FunctionalInterface
public interface AnimationSelector<T extends PathfinderMob & AbstractAlien> {

    void select(T entity);
}
