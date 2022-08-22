package mods.cybercat.gigeresque.common.block;

import mods.cybercat.gigeresque.common.block.material.Materials;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MuralBlock extends GigBlock {

	public MuralBlock() {
		super(FabricBlockSettings.of(Materials.ROUGH_ALIEN_BLOCK).strength(3.0F, 6.0F)
				.sounds(BlockSoundGroup.NETHERRACK));
	}

	@Override
	public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity,
			ItemStack stack) {
		world.setBlockState(pos, GIgBlocks.ROUGH_ALIEN_BLOCK.getDefaultState(), Block.NOTIFY_ALL);
		AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(world, pos.getX(), pos.getY(),
				pos.getZ());
		areaEffectCloudEntity.setRadius(1.0F);
		areaEffectCloudEntity.setDuration(60);
		areaEffectCloudEntity
				.setRadiusGrowth(-areaEffectCloudEntity.getRadius() / (float) areaEffectCloudEntity.getDuration());
		areaEffectCloudEntity.addEffect(new StatusEffectInstance(GigStatusEffects.DNA, 600, 0));
		world.spawnEntity(areaEffectCloudEntity);
		super.afterBreak(world, player, pos, state, blockEntity, stack);
	}

}
