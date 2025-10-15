package mods.cybercat.gigeresque.common.entity.ai.nav;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.common.entity.AlienEntity;

public class GigNodeEvaluator extends WalkNodeEvaluator {

    boolean climbingAllowed;

    @Override
    public void prepare(@NotNull PathNavigationRegion level, @NotNull Mob mob) {
        super.prepare(level, mob);
        var alien = (AlienEntity) mob;
        if (alien.climbingManager.canClimb && !alien.isInWater() && !alien.isVehicle()) {
            // this assumes that all aliens capable of climbing will correctly adjust their hitbox to actually allow
            // them to follow the path
            entityHeight = 1;
            climbingAllowed = true;
        } else {
            climbingAllowed = false;
        }
    }

    @Override
    public @NotNull Node getStart() {
        return getStartNode(mob.blockPosition());
    }

    @Override
    public int getNeighbors(Node @NotNull [] outputArray, @NotNull Node p_node) {
        var nodeCount = super.getNeighbors(outputArray, p_node);
        if (!climbingAllowed) {
            return nodeCount;
        }

        // add climbable nodes
        for (var dir : Direction.values()) {
            int x = dir.getStepX() + p_node.x;
            int y = dir.getStepY() + p_node.y;
            int z = dir.getStepZ() + p_node.z;
            if (addClimbableNodeIfAbsent(x, y, z, outputArray, nodeCount)) {
                nodeCount++;
            }
        }

        return nodeCount;
    }

    public boolean addClimbableNodeIfAbsent(int x, int y, int z, Node[] outputArray, int nodeCount) {
        if (nodeCount >= outputArray.length) {
            return false;
        }

        for (int i = 0; i < nodeCount; i++) {
            var node = outputArray[i];
            if (node.x == x && node.y == y && node.z == z) {
                return false;
            }
        }
        var node = getNode(x, y, z);
        node.type = getPathTypeFromState(mob.level(), new BlockPos(x, y, z));
        if (node.closed) {
            return false;
        }
        if (node.type == PathType.BLOCKED) {
            return false;
        }
        if (!climbable(mob.level(), x, y, z, false)) {
            return false;
        }
        outputArray[nodeCount] = node;
        return true;
    }

    public static boolean climbable(Level level, int x, int y, int z, boolean generous) {
        var reachBox = new AABB(x, y, z, x + 1, y + 1, z + 1).inflate(generous ? 1.5 : 0.5);
        return !level.noBlockCollision(null, reachBox);
    }

}
