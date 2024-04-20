package mods.cybercat.gigeresque.common.block.entity;

import mod.azure.azurelib.animatable.GeoBlockEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.util.AzureLibUtil;
import mods.cybercat.gigeresque.common.block.PetrifiedObjectBlock;
import mods.cybercat.gigeresque.common.block.storage.StorageProperties;
import mods.cybercat.gigeresque.common.block.storage.StorageStates;
import mods.cybercat.gigeresque.common.entity.Entities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class PetrifiedOjbectEntity extends BlockEntity implements GeoBlockEntity {

    public static final EnumProperty<StorageStates> CHEST_STATE = StorageProperties.STORAGE_STATE;
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    public PetrifiedOjbectEntity(BlockPos pos, BlockState state) {
        super(Entities.PETRIFIED_OBJECT, pos, state);
    }

    public StorageStates getChestState() {
        return this.getBlockState().getValue(PetrifiedOjbectEntity.CHEST_STATE);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, event -> {
            if (getChestState().equals(StorageStates.OPENED))
                return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("hatched_empty"));
            else return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("idle"));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PetrifiedOjbectEntity blockEntity) {
        if (blockEntity.level != null && (level.getRandom().nextInt(0, 200) == 0)) {
            int i = state.getValue(PetrifiedObjectBlock.HATCH);
            if (i < level.getRandom().nextInt(2, 25) && state.getValue(CHEST_STATE) == StorageStates.CLOSED) {
                level.playSound(null, pos, SoundEvents.HONEY_BLOCK_SLIDE, SoundSource.BLOCKS, 0.3f,
                        0.9f + level.getRandom().nextFloat() * 0.2f);
                level.setBlock(pos,
                        state.setValue(PetrifiedObjectBlock.HATCH, i + 1).setValue(CHEST_STATE, StorageStates.CLOSED),
                        2);
            } else if (i >= 24 && state.getValue(CHEST_STATE) == StorageStates.CLOSED) {
                level.playSound(null, pos, SoundEvents.TURTLE_EGG_HATCH, SoundSource.BLOCKS, 0.3f,
                        0.9f + level.getRandom().nextFloat() * 0.2f);
                level.setBlockAndUpdate(pos,
                        state.setValue(CHEST_STATE, StorageStates.OPENED).setValue(PetrifiedObjectBlock.HATCH, 24));
                var facehugger = Entities.FACEHUGGER.create(level);
                assert facehugger != null;
                facehugger.moveTo(pos.getX() + 0.3 + 0, pos.getY() + 1, pos.getZ() + 0.3, 0.0f, 0.0f);
                level.addFreshEntity(facehugger);
            }
        }
    }
}
