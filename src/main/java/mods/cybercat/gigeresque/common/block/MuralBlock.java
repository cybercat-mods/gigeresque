package mods.cybercat.gigeresque.common.block;

import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class MuralBlock extends GigBlock {

    public MuralBlock() {
        super(BlockBehaviour.Properties.of().strength(3.0F, 6.0F).sound(SoundType.NETHERRACK).explosionResistance(10));
    }

    @Override
    public void playerDestroy(@NotNull Level level, @NotNull Player player, @NotNull BlockPos pos, @NotNull BlockState state, BlockEntity blockEntity, @NotNull ItemStack stack) {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.setBlock(pos, GigBlocks.ROUGH_ALIEN_BLOCK.defaultBlockState(), Block.UPDATE_ALL);
            var areaEffectCloudEntity = new AreaEffectCloud(serverLevel, pos.getX(), pos.getY(), pos.getZ());
            areaEffectCloudEntity.setRadius(1.0F);
            areaEffectCloudEntity.setDuration(60);
            areaEffectCloudEntity.setRadiusPerTick(
                    -areaEffectCloudEntity.getRadius() / areaEffectCloudEntity.getDuration());
            areaEffectCloudEntity.addEffect(new MobEffectInstance(GigStatusEffects.DNA, 600, 0));
            serverLevel.addFreshEntity(areaEffectCloudEntity);
        }
        super.playerDestroy(level, player, pos, state, blockEntity, stack);
    }

}
