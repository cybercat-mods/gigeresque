package mods.cybercat.gigeresque.common.status.effect.impl;

import java.awt.Color;
import java.util.SplittableRandom;

import mods.cybercat.gigeresque.common.block.Blocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.entity.impl.AlienEggEntity;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DNAStatusEffect extends StatusEffect {
	private BlockPos lightBlockPos = null;

	public DNAStatusEffect() {
		super(StatusEffectCategory.HARMFUL, Color.black.getRGB());
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}

	@Override
	public boolean isInstant() {
		return true;
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		super.applyUpdateEffect(entity, amplifier);
		if (this == GigStatusEffects.DNA)
			entity.heal(0);
	}

	@Override
	public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		SplittableRandom random = new SplittableRandom();
		int randomPhase = random.nextInt(0, 50);
		if (!(entity instanceof AlienEntity))
			if (randomPhase > 25) {
				if (entity instanceof PlayerEntity && !(((PlayerEntity) entity).isCreative()
						|| ((PlayerEntity) entity).isSpectator())) {
					AlienEggEntity egg = new AlienEggEntity(Entities.EGG, entity.world);
					egg.refreshPositionAndAngles(entity.getBlockPos(), entity.getYaw(), entity.getPitch());
					entity.world.spawnEntity(egg);
					entity.kill();
					return;
				} else if (!(entity instanceof PlayerEntity)) {
					AlienEggEntity egg = new AlienEggEntity(Entities.EGG, entity.world);
					egg.refreshPositionAndAngles(entity.getBlockPos(), entity.getYaw(), entity.getPitch());
					entity.world.spawnEntity(egg);
					entity.kill();
					return;
				}
			} else {
				if (entity instanceof PlayerEntity && !(((PlayerEntity) entity).isCreative()
						|| ((PlayerEntity) entity).isSpectator())) {
					entity.kill();
					boolean isInsideWaterBlock = entity.world.isWater(entity.getBlockPos());
					spawnGoo(entity, isInsideWaterBlock);
					return;
				} else if (!(entity instanceof PlayerEntity)) {
					entity.kill();
					boolean isInsideWaterBlock = entity.world.isWater(entity.getBlockPos());
					spawnGoo(entity, isInsideWaterBlock);
					return;
				}
			}
		super.onRemoved(entity, attributes, amplifier);
	}

	private void spawnGoo(LivingEntity entity, boolean isInWaterBlock) {
		if (lightBlockPos == null) {
			lightBlockPos = findFreeSpace(entity.world, entity.getBlockPos(), 1);
			if (lightBlockPos == null)
				return;
			entity.world.setBlockState(lightBlockPos, Blocks.BLACK_FLUID.getDefaultState());
		} else
			lightBlockPos = null;
	}

	private BlockPos findFreeSpace(World world, BlockPos blockPos, int maxDistance) {
		if (blockPos == null)
			return null;

		int[] offsets = new int[maxDistance * 2 + 1];
		offsets[0] = 0;
		for (int i = 2; i <= maxDistance * 2; i += 2) {
			offsets[i - 1] = i / 2;
			offsets[i] = -i / 2;
		}
		for (int x : offsets)
			for (int y : offsets)
				for (int z : offsets) {
					BlockPos offsetPos = blockPos.add(x, y, z);
					BlockState state = world.getBlockState(offsetPos);
					if (state.isAir() || state.getBlock().equals(Blocks.NEST_RESIN_WEB_CROSS))
						return offsetPos;
				}

		return null;
	}

}