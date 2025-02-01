package mods.cybercat.gigeresque.common.entity.ai.nav;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.jetbrains.annotations.NotNull;

public class CrawlPathNodeEvaluator extends WalkNodeEvaluator {

    protected float newEntityHeight;

    protected float newEntityWidth;

    public CrawlPathNodeEvaluator(float newEntityHeight, float newEntityWidth) {
        this.newEntityHeight = newEntityHeight;
        this.newEntityWidth = newEntityWidth;
    }

    @Override
    public void prepare(@NotNull PathNavigationRegion level, @NotNull Mob mob) {
        super.prepare(level, mob);
        this.entityHeight = (int) Math.floor(this.newEntityHeight + 1.0F);
        this.entityWidth = (int) Math.floor(this.newEntityWidth + 1.0F);
    }
}
