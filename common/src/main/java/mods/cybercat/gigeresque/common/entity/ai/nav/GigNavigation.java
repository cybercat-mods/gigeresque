package mods.cybercat.gigeresque.common.entity.ai.nav;

import mod.azure.azurelib.common.api.common.ai.pathing.AzureNavigation;
import mod.azure.azurelib.common.internal.common.ai.pathing.AzurePathFinder;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class GigNavigation extends AzureNavigation {

    public GigNavigation(Mob entity, Level world) {
        super(entity, world);
    }

    @Override
    protected @NotNull PathFinder createPathFinder(int maxVisitedNodes) {
        this.nodeEvaluator = new CrawlPathNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        this.nodeEvaluator.setCanFloat(false);
        return new AzurePathFinder(this.nodeEvaluator, maxVisitedNodes);
    }
}
