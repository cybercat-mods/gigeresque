package mods.cybercat.gigeresque.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

import mods.cybercat.gigeresque.common.block.entity.PetrifiedOjbectEntity;
import mods.cybercat.gigeresque.common.block.storage.StorageProperties;
import mods.cybercat.gigeresque.common.block.storage.StorageStates;
import mods.cybercat.gigeresque.common.entity.Entities;

public class PetrifiedObjectBlock extends BaseEntityBlock {

    public static final IntegerProperty HATCH = BlockStateProperties.AGE_25;

    public static final EnumProperty<StorageStates> STORAGE_STATE = StorageProperties.STORAGE_STATE;

    protected PetrifiedObjectBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.STONE).randomTicks().strength(15, 15).noLootTable());
        this.registerDefaultState(
            this.stateDefinition.any().setValue(HATCH, 0).setValue(STORAGE_STATE, StorageStates.CLOSED)
        );
    }

    @Override
    public void playerDestroy(
        @NotNull Level level,
        @NotNull Player player,
        @NotNull BlockPos pos,
        @NotNull BlockState state,
        @Nullable BlockEntity blockEntity,
        @NotNull ItemStack itemStack
    ) {
        if (state.getValue(STORAGE_STATE) == StorageStates.OPENED) {
            player.awardStat(Stats.BLOCK_MINED.get(this));
            player.causeFoodExhaustion(0.005F);
            if (level instanceof ServerLevel serverLevel) {
                var radius = (2 - 1) / 2;
                for (int i = 0; i < 2; i++) {
                    int x = serverLevel.getRandom().nextInt(2) - radius;
                    int z = serverLevel.getRandom().nextInt(2) - radius;
                    var acidEntity = Entities.ACID.create(serverLevel);
                    assert acidEntity != null;
                    acidEntity.moveTo(pos.offset(x, 0, z), 0, 0);
                    serverLevel.addFreshEntity(acidEntity);
                }
            }
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        } else {
            dropResources(state, level, pos);
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }
    }

    public static void dropResources(@NotNull BlockState state, Level level, @NotNull BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            var d = EntityType.ITEM.getHeight() / 2.0;
            var x = pos.getX() + 0.5 + Mth.nextDouble(level.random, -0.25, 0.25);
            var y = pos.getY() + 0.5 + Mth.nextDouble(level.random, -0.25, 0.25) - d;
            var z = pos.getZ() + 0.5 + Mth.nextDouble(level.random, -0.25, 0.25);
            var itemEntity = new ItemEntity(
                level,
                x,
                y,
                z,
                GigBlocks.PETRIFIED_OBJECT_BLOCK.asItem().getDefaultInstance()
            );
            itemEntity.setDefaultPickUpDelay();
            level.addFreshEntity(itemEntity);
            state.spawnAfterBreak(serverLevel, pos, ItemStack.EMPTY, false);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HATCH, STORAGE_STATE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return Entities.PETRIFIED_OBJECT.create(pos, state);
    }

    @Override
    public @NotNull VoxelShape getShape(
        @NotNull BlockState state,
        @NotNull BlockGetter world,
        @NotNull BlockPos pos,
        @NotNull CollisionContext context
    ) {
        return Stream.of(
            Block.box(3.5, 0, 3.5, 12.5, 2, 12.5),
            Block.box(3, 1, 3, 13, 10, 13),
            Block.box(4, 10, 3.5, 12, 12, 12.5),
            Block.box(5, 11.5, 4.5, 11, 13.5, 11.5)
        )
            .reduce(
                (v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)
            )
            .get();
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        @NotNull Level level,
        @NotNull BlockState state,
        @NotNull BlockEntityType<T> type
    ) {
        return createTickerHelper(type, Entities.PETRIFIED_OBJECT, PetrifiedOjbectEntity::tick);
    }
}
