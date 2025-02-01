package mods.cybercat.gigeresque.common.block.petrifiedblocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import mods.cybercat.gigeresque.common.block.entity.SporeEggDispatcher;
import mods.cybercat.gigeresque.common.block.petrifiedblocks.PetrifiedObjectBlock;
import mods.cybercat.gigeresque.common.block.storage.StorageProperties;
import mods.cybercat.gigeresque.common.block.storage.StorageStates;
import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;

public class PetrifiedOjbectEntity extends BlockEntity {

    public static final EnumProperty<StorageStates> CHEST_STATE = StorageProperties.STORAGE_STATE;

    public static SporeEggDispatcher animationDispatcher;

    public PetrifiedOjbectEntity(BlockPos pos, BlockState state) {
        super(GigEntities.PETRIFIED_OBJECT.get(), pos, state);
        animationDispatcher = new SporeEggDispatcher(this);
    }

    public StorageStates getChestState() {
        return this.getBlockState().getValue(PetrifiedOjbectEntity.CHEST_STATE);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PetrifiedOjbectEntity blockEntity) {
        if (blockEntity.getLevel() != null && blockEntity.getLevel().isClientSide()) {
            if (blockEntity.getChestState().equals(StorageStates.OPENED)) {
                GigCommonMethods.setAnimation(animationDispatcher::sendHatched);
            } else {
                GigCommonMethods.setAnimation(animationDispatcher::sendIdle);
            }
        }
        if (blockEntity.level != null && (level.getRandom().nextInt(0, 200) == 0)) {
            int i = state.getValue(PetrifiedObjectBlock.HATCH);
            if (i < level.getRandom().nextInt(2, 25) && state.getValue(CHEST_STATE) == StorageStates.CLOSED) {
                level.playSound(
                    null,
                    pos,
                    SoundEvents.STONE_HIT,
                    SoundSource.BLOCKS,
                    0.3f,
                    0.9f + level.getRandom().nextFloat() * 0.2f
                );
                level.setBlock(
                    pos,
                    state.setValue(PetrifiedObjectBlock.HATCH, i + 1).setValue(CHEST_STATE, StorageStates.CLOSED),
                    2
                );
            } else if (i >= 24 && state.getValue(CHEST_STATE) == StorageStates.CLOSED) {
                level.addParticle(
                    new BlockParticleOption(ParticleTypes.BLOCK, Blocks.STONE.defaultBlockState()),
                    pos.getX() + (level.getRandom().nextDouble()),
                    pos.getY() + 0.5D * (level.getRandom().nextDouble()),
                    pos.getZ() + (level.getRandom().nextDouble()),
                    0,
                    0,
                    0
                );
                level.playSound(
                    null,
                    pos,
                    SoundEvents.STONE_BREAK,
                    SoundSource.BLOCKS,
                    0.3f,
                    0.9f + level.getRandom().nextFloat() * 0.2f
                );
                level.setBlockAndUpdate(
                    pos,
                    state.setValue(CHEST_STATE, StorageStates.OPENED).setValue(PetrifiedObjectBlock.HATCH, 24)
                );
                var facehugger = GigEntities.FACEHUGGER.get().create(level);
                assert facehugger != null;
                facehugger.moveTo(pos.getX() + 0.3 + 0, (double) pos.getY() + 1, pos.getZ() + 0.3, 0.0f, 0.0f);
                level.addFreshEntity(facehugger);
            }
        }
    }
}
