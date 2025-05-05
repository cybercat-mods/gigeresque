package mods.cybercat.gigeresque.common.entity.ai.goals.movement;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.tags.GigTags;

public class FleeExplodingCreeperGoal extends Goal {

    private static final Predicate<Entity> IS_EXPLODING_SELECTOR = entity -> isExplodingCreeper(entity) || entity.getType()
        .is(
            GigTags.EXPLODING_ENTITY
        ) ||
        entity instanceof PrimedTnt;

    protected final AlienEntity alienEntity;

    protected final double speedModifier = 1.3;

    private Entity entityToAvoid;

    private Path path;

    public FleeExplodingCreeperGoal(AlienEntity alienEntity) {
        this.alienEntity = alienEntity;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        var nearby = alienEntity.level()
            .getEntities(alienEntity, alienEntity.getBoundingBox().inflate(9.0, 3.0, 9.0), IS_EXPLODING_SELECTOR);
        if (nearby.isEmpty()) {
            return false;
        }
        entityToAvoid = getNearest(nearby);

        var target = DefaultRandomPos.getPosAway(alienEntity, 16, 7, entityToAvoid.position());
        if (target == null) {
            return false;
        }
        if (entityToAvoid.distanceToSqr(target.x, target.y, target.z) < entityToAvoid.distanceToSqr(alienEntity)) {
            return false;
        }

        path = alienEntity.getNavigation().createPath(target.x, target.y, target.z, 0);
        return path != null;
    }

    @Override
    public boolean canContinueToUse() {
        return alienEntity.getNavigation().isInProgress() && entityToAvoid.isAlive();
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        alienEntity.getNavigation().moveTo(path, speedModifier);
        alienEntity.setFleeingStatus(true);
    }

    @Override
    public void stop() {
        entityToAvoid = null;
        alienEntity.setFleeingStatus(false);
    }

    @Override
    public void tick() {
        alienEntity.getNavigation().setSpeedModifier(speedModifier);
        alienEntity.setTarget(null);
    }

    private <T extends Entity> T getNearest(List<T> entities) {
        var nearestDistSqr = Double.POSITIVE_INFINITY;
        T nearest = null;
        for (var entity : entities) {
            var distSqr = alienEntity.distanceToSqr(entity);
            if (distSqr < nearestDistSqr) {
                nearestDistSqr = distSqr;
                nearest = entity;
            }
        }
        return nearest;
    }

    private static boolean isExplodingCreeper(Entity entity) {
        if (!(entity instanceof Creeper creeper)) {
            return false;
        }
        return creeper.getSwellDir() > 0 || creeper.isIgnited();
    }
}
