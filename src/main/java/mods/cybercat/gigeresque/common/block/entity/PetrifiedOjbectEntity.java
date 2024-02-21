package mods.cybercat.gigeresque.common.block.entity;

import mod.azure.azurelib.animatable.GeoBlockEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.util.AzureLibUtil;
import mods.cybercat.gigeresque.common.block.PetrifiedObjectBlock;
import mods.cybercat.gigeresque.common.entity.Entities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PetrifiedOjbectEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    public PetrifiedOjbectEntity(BlockPos pos, BlockState state) {
        super(Entities.PETRIFIED_OBJECT, pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PetrifiedOjbectEntity blockEntity) {
        if (blockEntity.level != null && (level.getRandom().nextInt(0, 200) == 0)) {
            var i = state.getValue(PetrifiedObjectBlock.HATCH);
            if (i < level.getRandom().nextInt(2, 24)) {
                level.playSound(null, pos, SoundEvents.TURTLE_EGG_CRACK, SoundSource.BLOCKS, 0.3f,
                        0.9f + level.getRandom().nextFloat() * 0.2f);
                level.setBlock(pos, state.setValue(PetrifiedObjectBlock.HATCH, i + 1), 2);
            }else {
                level.playSound(null, pos, SoundEvents.TURTLE_EGG_HATCH, SoundSource.BLOCKS, 0.3f,
                        0.9f + level.getRandom().nextFloat() * 0.2f);
                level.removeBlock(pos, false);
                var facehugger = Entities.FACEHUGGER.create(level);
                facehugger.moveTo((double) pos.getX() + 0.3 + 0, pos.getY(), (double) pos.getZ() + 0.3, 0.0f, 0.0f);
                level.addFreshEntity(facehugger);
            }
        }
    }
}
