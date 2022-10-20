package mods.cybercat.gigeresque.common.block;

import mods.cybercat.gigeresque.common.config.ConfigAccessor;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class NestResinWebFullBlock extends Block {
	public NestResinWebFullBlock(Properties settings) {
		super(settings);
	}

	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (!(entity instanceof AlienEntity)
				&& !(entity instanceof Player && (((Player) entity).isCreative() || ((Player) entity).isSpectator()))
				&& entity instanceof LivingEntity && ConfigAccessor.isTargetAlienHost(entity)) {
			entity.makeStuckInBlock(state, new Vec3(0.25, 0.05000000074505806, 0.25));
			((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 10), entity);
		}
	}
}
