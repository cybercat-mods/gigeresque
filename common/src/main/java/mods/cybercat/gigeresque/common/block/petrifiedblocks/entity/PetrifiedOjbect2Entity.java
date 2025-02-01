package mods.cybercat.gigeresque.common.block.petrifiedblocks.entity;

import mods.cybercat.gigeresque.common.entity.helper.GigCommonMethods;
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

import mods.cybercat.gigeresque.common.block.petrifiedblocks.PetrifiedObjectBlock;
import mods.cybercat.gigeresque.common.block.storage.StorageProperties;
import mods.cybercat.gigeresque.common.block.storage.StorageStates;
import mods.cybercat.gigeresque.common.entity.GigEntities;

public class PetrifiedOjbect2Entity extends BlockEntity {

    public static final EnumProperty<StorageStates> CHEST_STATE = StorageProperties.STORAGE_STATE;

    public static PetrifiedDispatcher animationDispatcher;

    public PetrifiedOjbect2Entity(BlockPos pos, BlockState state) {
        super(GigEntities.PETRIFIED_OBJECT_2.get(), pos, state);
        animationDispatcher = new PetrifiedDispatcher(this);
    }

    public StorageStates getChestState() {
        return this.getBlockState().getValue(PetrifiedOjbect2Entity.CHEST_STATE);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PetrifiedOjbect2Entity blockEntity) {
        if (blockEntity.getLevel() != null && blockEntity.getLevel().isClientSide()) {
            GigCommonMethods.setAnimation(animationDispatcher::setPetrifiedCommand);
        }
        if (blockEntity.level != null && (level.getRandom().nextInt(0, 200) == 0)) {
            int i = state.getValue(PetrifiedObjectBlock.HATCH);
            if (i < level.getRandom().nextInt(2, 25) && state.getValue(CHEST_STATE) == StorageStates.CLOSED) {
                level.playSound(null, pos, SoundEvents.STONE_HIT, SoundSource.BLOCKS, 0.3f, 0.9f + level.getRandom().nextFloat() * 0.2f);
                level.setBlock(pos, state.setValue(PetrifiedObjectBlock.HATCH, i + 1).setValue(CHEST_STATE, StorageStates.CLOSED), 2);
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
                level.playSound(null, pos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 0.3f, 0.9f + level.getRandom().nextFloat() * 0.2f);
                level.setBlockAndUpdate(pos, state.setValue(CHEST_STATE, StorageStates.OPENED).setValue(PetrifiedObjectBlock.HATCH, 24));
                var chestbursterEntity = GigEntities.CHESTBURSTER.get().create(level);
                if (chestbursterEntity != null) {
                    chestbursterEntity.moveTo(pos.getX(), pos.getY(), pos.getZ(), 0.0f, 0.0f);
                    level.addFreshEntity(chestbursterEntity);
                    level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                }
            }
        }
    }
}
