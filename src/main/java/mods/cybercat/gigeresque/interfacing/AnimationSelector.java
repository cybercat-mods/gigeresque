package mods.cybercat.gigeresque.interfacing;

import mods.cybercat.gigeresque.common.entity.AlienEntity;

@FunctionalInterface
public interface AnimationSelector<T extends AlienEntity> {
    void select(T entity);
}
