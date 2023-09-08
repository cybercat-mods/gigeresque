package mods.cybercat.gigeresque.common.block;

import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.util.GigEntityUtils;
import mods.cybercat.gigeresque.interfacing.Host;
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
		if (entity instanceof AlienEntity)
			return;
		if ((entity instanceof Player && (((Player) entity).isCreative() || ((Player) entity).isSpectator())))
			return;
		if (entity instanceof LivingEntity livingEntity && GigEntityUtils.isTargetHostable(entity) && !((Host)entity).hasParasite()) {
			livingEntity.makeStuckInBlock(state, new Vec3(0.25, 0.0, 0.25));
			livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 10), entity);
		}
	}
}
