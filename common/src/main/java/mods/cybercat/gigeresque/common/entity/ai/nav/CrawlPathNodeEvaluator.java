package mods.cybercat.gigeresque.common.entity.ai.nav;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.jetbrains.annotations.NotNull;

/**
 * Credit to Boston/AVP
 */
public class CrawlPathNodeEvaluator extends WalkNodeEvaluator {

    @Override
    public void prepare(@NotNull PathNavigationRegion level, @NotNull Mob mob) {
        super.prepare(level, mob);
        if (!mob.isInWater() || !mob.isVehicle())
            this.entityHeight = Mth.floor((mob.getBbHeight() / 16) + 1.0F);
    }
}
