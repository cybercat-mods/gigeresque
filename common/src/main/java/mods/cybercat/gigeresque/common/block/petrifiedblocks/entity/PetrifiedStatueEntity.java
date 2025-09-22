package mods.cybercat.gigeresque.common.block.petrifiedblocks.entity;

import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.PetrifiedStatueBlock;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;


public class PetrifiedStatueEntity extends BlockEntity {

    public static PetrifiedDispatcher animationDispatcher;

    public PetrifiedStatueEntity(BlockPos pos, BlockState state) {
        super(GigEntities.PETRIFIED_STATUE.get(), pos, state);
        animationDispatcher = new PetrifiedDispatcher(this);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PetrifiedStatueEntity blockEntity) {
        if (blockEntity.level != null) {
            if (!blockEntity.level.isClientSide)
                BlockPos.betweenClosed(pos, pos.relative(state.getValue(PetrifiedStatueBlock.FACING), 1).above(2))
                        .forEach(
                                testPos -> {
                                    if (
                                            !testPos.equals(pos) && !level.getBlockState(testPos)
                                                    .is(
                                                            GigBlocks.PETRIFIED_STATUE_BLOCK_INVIS.get()
                                                    )
                                    )
                                        level.setBlock(
                                                testPos,
                                                GigBlocks.PETRIFIED_STATUE_BLOCK_INVIS.get().defaultBlockState(),
                                                Block.UPDATE_ALL
                                        );
                                }
                        );
        }
    }
}
