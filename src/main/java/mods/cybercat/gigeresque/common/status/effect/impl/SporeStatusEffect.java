package mods.cybercat.gigeresque.common.status.effect.impl;

import mod.azure.azurelib.core.object.Color;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.tags.GigTags;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SporeStatusEffect extends MobEffect {

	public SporeStatusEffect() {
		super(MobEffectCategory.HARMFUL, Color.BLACK.getColor());
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public boolean isInstantenous() {
		return false;
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		super.applyEffectTick(entity, amplifier);
		if (this == GigStatusEffects.SPORE)
			entity.heal(0);
	}

	@Override
	public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
		var neoBurster = Entities.NEOBURSTER.create(entity.level());
		if (!(entity instanceof AlienEntity) && entity.getType().is(GigTags.NEOHOST)) {
			if (!(entity instanceof Player)) {
				neoBurster.moveTo(entity.blockPosition(), entity.getYRot(), entity.getXRot());
				spawnEffects(entity.level(), entity);
				entity.level().addFreshEntity(neoBurster);
				entity.hurt(entity.damageSources().generic(), Integer.MAX_VALUE);
				return;
			}
			if (entity instanceof Player playerEntity) {
				if (!playerEntity.isSpectator() && !playerEntity.isCreative()) {
					neoBurster.moveTo(playerEntity.blockPosition(), playerEntity.getYRot(), playerEntity.getXRot());
					spawnEffects(playerEntity.level(), playerEntity);
					playerEntity.level().addFreshEntity(neoBurster);
					playerEntity.hurt(playerEntity.damageSources().generic(), Integer.MAX_VALUE);
					return;
				}
			}
		}
		super.removeAttributeModifiers(entity, attributes, amplifier);
	}

	private void spawnEffects(Level world, LivingEntity entity) {
		if (!world.isClientSide())
			for (int i = 0; i < 2; i++)
				((ServerLevel) world).sendParticles(ParticleTypes.POOF, ((double) entity.getX()) + 0.5, entity.getY(), ((double) entity.getZ()) + 0.5, 1, entity.getRandom().nextGaussian() * 0.02, entity.getRandom().nextGaussian() * 0.02, entity.getRandom().nextGaussian() * 0.02, 0.15000000596046448);
	}

}