package mods.cybercat.gigeresque.common.block;

import mods.cybercat.gigeresque.common.config.ConfigAccessor;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class NestResinWebFullBlock extends Block {
	public NestResinWebFullBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!(entity instanceof AlienEntity)
				&& !(entity instanceof PlayerEntity
						&& (((PlayerEntity) entity).isCreative() || ((PlayerEntity) entity).isSpectator()))
				&& entity instanceof LivingEntity && ConfigAccessor.isTargetAlienHost(entity)) {
			entity.slowMovement(state, new Vec3d(0.25, 0.05000000074505806, 0.25));
			((LivingEntity) entity).setStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 10), entity);
		}
	}
}
