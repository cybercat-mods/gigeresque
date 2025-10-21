package mods.cybercat.gigeresque.common.entity.ai.nav;

import mod.azure.azurelib.common.ai.pathing.AzureNavigation;
import mod.azure.azurelib.common.ai.pathing.AzurePathFinder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.CommonMod;
import mods.cybercat.gigeresque.common.entity.AlienEntity;

public class GigNavigation extends AzureNavigation {

    private final AlienEntity alien;

    public GigNavigation(AlienEntity alien, Level level) {
        super(alien, level);
        this.alien = alien;
    }

    @Override
    protected @NotNull PathFinder createPathFinder(int maxVisitedNodes) {
        this.nodeEvaluator = new GigNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        this.nodeEvaluator.setCanFloat(true);
        return new AzurePathFinder(this.nodeEvaluator, maxVisitedNodes);
    }

    @Override
    protected @NotNull Vec3 getTempMobPos() {
        if (!alien.climbingManager.canClimb) {
            return super.getTempMobPos();
        }
        return mob.position();
    }

    @Override
    protected boolean canUpdatePath() {
        if (!alien.climbingManager.canClimb) {
            return super.canUpdatePath();
        }
        return true; // maybe limit this to on ground, climbing or in water?
    }

    @Override
    protected void followThePath() {
        if (path != null && CommonMod.config.generalConfigs.enableDevparticles) {
            for (var node : path.nodes) {
                ((ServerLevel) level).sendParticles(
                    ParticleTypes.BUBBLE,
                    node.x + 0.5,
                    node.y + 0.5,
                    node.z + 0.5,
                    1,
                    0,
                    0,
                    0,
                    0
                );
            }
        }
        super.followThePath();
    }

    @Override
    public void tick() {
        super.tick();
        if (alien.climbingManager.canClimb) {
            // prevents trying to set the wanted position to the ground
            if (path != null && !isDone()) {
                Vec3 wanted = path.getNextEntityPos(mob);
                this.mob.getMoveControl().setWantedPosition(wanted.x, wanted.y, wanted.z, speedModifier);
            }
        }
    }
}
