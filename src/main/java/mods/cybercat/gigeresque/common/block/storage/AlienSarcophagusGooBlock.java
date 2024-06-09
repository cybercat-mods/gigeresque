package mods.cybercat.gigeresque.common.block.storage;

import mods.cybercat.gigeresque.common.block.entity.AlienStorageGooEntity;
import mods.cybercat.gigeresque.common.entity.Entities;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class AlienSarcophagusGooBlock extends AlienSarcophagusBlock {

    public AlienSarcophagusGooBlock() {
        super(FabricBlockSettings.of().sounds(SoundType.DRIPSTONE_BLOCK).strength(5.0f, 8.0f).nonOpaque());
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof AlienStorageGooEntity alienStorageEntity)
            player.openMenu(alienStorageEntity);
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return Entities.ALIEN_STORAGE_BLOCK_ENTITY_1_GOO.create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return createTickerHelper(type, Entities.ALIEN_STORAGE_BLOCK_ENTITY_1_GOO, AlienStorageGooEntity::tick);
    }
}
