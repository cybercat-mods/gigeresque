package mods.cybercat.gigeresque.common.block.storage;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import mods.cybercat.gigeresque.common.block.entity.AlienStorageSporeEntity;
import mods.cybercat.gigeresque.common.entity.Entities;

public class AlienSarcophagusSporeBlock extends AlienSarcophagusBlock {

    public AlienSarcophagusSporeBlock() {
        super();
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide && world.getBlockEntity(pos) instanceof AlienStorageSporeEntity alienStorageEntity)
            player.openMenu(alienStorageEntity);
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return Entities.ALIEN_STORAGE_BLOCK_ENTITY_1_SPORE.create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, Entities.ALIEN_STORAGE_BLOCK_ENTITY_1_SPORE, AlienStorageSporeEntity::tick);
    }
}
