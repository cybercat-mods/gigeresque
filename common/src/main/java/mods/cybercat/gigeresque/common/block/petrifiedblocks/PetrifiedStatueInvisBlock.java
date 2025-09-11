package mods.cybercat.gigeresque.common.block.petrifiedblocks;

import mods.cybercat.gigeresque.common.block.GigBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class PetrifiedStatueInvisBlock extends Block {

    private static final VoxelShape OUTLINE_SHAPE = net.minecraft.world.level.block.Block.box(0, 0, 0, 16, 16, 16);

    public PetrifiedStatueInvisBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(30.0f, 15.0f).noOcclusion().noLootTable());
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        if (world.isClientSide)
            return state;

        var radius = new Vec3i(2, 2, 2);

        for (BlockPos testPos : BlockPos.betweenClosed(pos.subtract(radius), pos.offset(radius))) {
            BlockState testState;

            if ((testState = world.getBlockState(testPos)).is(GigBlocks.PETRIFIED_STATUE_BLOCK.get())) {
                world.destroyBlock(testPos, true);
                Block.dropResources(testState, world, testPos);
            } else if (testState.is(this))
                world.setBlock(testPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        }
        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public @NotNull VoxelShape getShape(
            @NotNull BlockState state,
            @NotNull BlockGetter world,
            @NotNull BlockPos pos,
            @NotNull CollisionContext context
    ) {
        return OUTLINE_SHAPE;
    }
}
