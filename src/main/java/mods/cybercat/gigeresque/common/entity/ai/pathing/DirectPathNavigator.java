package mods.cybercat.gigeresque.common.entity.ai.pathing;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;

public class DirectPathNavigator extends MobNavigation {

    private MobEntity mob;
    private float yMobOffset = 0;

    public DirectPathNavigator(MobEntity mob, World world) {
        this(mob, world, 0);
    }

    public DirectPathNavigator(MobEntity mob, World world, float yMobOffset) {
        super(mob, world);
        this.mob = mob;
        this.yMobOffset = yMobOffset;
    }

    public void tick() {
        ++this.tickCount;
    }

    public boolean moveTo(double x, double y, double z, double speedIn) {
        mob.getMoveControl().moveTo(x, y, z, speedIn);
        return true;
    }

    public boolean moveTo(Entity entityIn, double speedIn) {
        mob.getMoveControl().moveTo(entityIn.getX(), entityIn.getY() + yMobOffset, entityIn.getZ(), speedIn);
        return true;
    }

}