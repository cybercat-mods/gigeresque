package mods.cybercat.gigeresque.common.entity.ai.pathing;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;

public class DirectPathNavigator extends GroundPathNavigation {

    private final Mob entity;
    private float yMobOffset = 0;

    public DirectPathNavigator(Mob mob, Level world) {
        this(mob, world, 0);
    }

    public DirectPathNavigator(Mob mob, Level world, float yMobOffset) {
        super(mob, world);
        this.entity = mob;
        this.yMobOffset = yMobOffset;
    }

    @Override
    public void tick() {
        ++this.tick;
    }

    @Override
    public boolean moveTo(double x, double y, double z, double speedIn) {
        entity.getMoveControl().setWantedPosition(x, y, z, speedIn);
        return true;
    }

    @Override
    public boolean moveTo(Entity entityIn, double speedIn) {
        entity.getMoveControl().setWantedPosition(entityIn.getX(), entityIn.getY() + yMobOffset, entityIn.getZ(), speedIn);
        return true;
    }

}