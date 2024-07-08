package mods.cybercat.gigeresque.common.block.storage;

import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageEntity;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageGooEntity;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageHuggerEntity;
import mods.cybercat.gigeresque.common.block.entity.AlienStorageSporeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class AlienSarcophagusInvisBlock extends Block {

    private static final VoxelShape OUTLINE_SHAPE = Block.box(0, 0, 0, 16, 16, 16);

    public AlienSarcophagusInvisBlock() {
        super(Properties.of().sound(SoundType.DRIPSTONE_BLOCK).strength(5.0f, 8.0f).noOcclusion().noLootTable());
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide) {
            var radius = new Vec3i(2, 2, 2);
            for (BlockPos testPos : BlockPos.betweenClosed(pos.subtract(radius), pos.offset(radius))) {
                if (level.getBlockState(testPos).is(GigBlocks.ALIEN_STORAGE_BLOCK_1.get())) {
                    if (level.getBlockEntity(testPos) instanceof AlienStorageEntity idolStorageEntity)
                        player.openMenu(idolStorageEntity);
                    return InteractionResult.SUCCESS;
                }
                if (level.getBlockState(testPos).is(GigBlocks.ALIEN_STORAGE_BLOCK_1_GOO.get())) {
                    if (level.getBlockEntity(testPos) instanceof AlienStorageGooEntity idolStorageEntity)
                        player.openMenu(idolStorageEntity);
                    return InteractionResult.SUCCESS;
                }
                if (level.getBlockState(testPos).is(GigBlocks.ALIEN_STORAGE_BLOCK_1_HUGGER.get())) {
                    if (level.getBlockEntity(testPos) instanceof AlienStorageHuggerEntity idolStorageEntity)
                        player.openMenu(idolStorageEntity);
                    return InteractionResult.SUCCESS;
                }
                if (level.getBlockState(testPos).is(GigBlocks.ALIEN_STORAGE_BLOCK_1_SPORE.get())) {
                    if (level.getBlockEntity(testPos) instanceof AlienStorageSporeEntity idolStorageEntity)
                        player.openMenu(idolStorageEntity);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        if (world.isClientSide) return state;
        var radius = new Vec3i(2, 2, 2);
        for (BlockPos testPos : BlockPos.betweenClosed(pos.subtract(radius), pos.offset(radius))) {
            BlockState testState;
            if ((testState = world.getBlockState(testPos)).is(GigBlocks.ALIEN_STORAGE_BLOCK_1.get()) ||
                    (testState = world.getBlockState(testPos)).is(GigBlocks.ALIEN_STORAGE_BLOCK_1_GOO.get()) ||
                    (testState = world.getBlockState(testPos)).is(GigBlocks.ALIEN_STORAGE_BLOCK_1_SPORE.get()) ||
                    (testState = world.getBlockState(testPos)).is(GigBlocks.ALIEN_STORAGE_BLOCK_1_HUGGER.get())) {
                world.destroyBlock(testPos, true);
                Block.dropResources(testState, world, testPos);
            } else if (testState.is(this)) world.setBlock(testPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        }
        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return OUTLINE_SHAPE;
    }

}
