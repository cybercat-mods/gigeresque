package mods.cybercat.gigeresque.common.block.entity;

import mod.azure.azurelib.animatable.GeoBlockEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.util.AzureLibUtil;
import mods.cybercat.gigeresque.common.Gigeresque;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.common.tags.GigTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class SporeBlockEntity extends BlockEntity implements GeoBlockEntity {

	private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

	public SporeBlockEntity(BlockPos pos, BlockState state) {
		super(Entities.SPORE_ENTITY, pos, state);
	}

	@Override
	public void registerControllers(ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, event -> event.setAndContinue(RawAnimation.begin().thenLoop("idle"))));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}

	public static void tick(Level world, BlockPos pos, BlockState state, SporeBlockEntity blockEntity) {
		if (world != null)
			if (blockEntity.level.getGameTime() % 20L == 0L) {
				if (world.isClientSide())
					for (var k = 0; k < 4; ++k)
						world.addParticle(ParticleTypes.ASH, pos.getX() + 1.0D * (world.getRandom().nextDouble()), pos.getY() + 0.5D * (world.getRandom().nextDouble()), pos.getZ() + 1.0D * (world.getRandom().nextDouble()), (world.getRandom().nextDouble() - 0.5D) * 2.0D, -world.getRandom().nextDouble(), (world.getRandom().nextDouble() - 0.5D) * 2.0D);
				if (!blockEntity.level.isClientSide)
					blockEntity.getLevel().getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(3D, 3D, 3D)).forEach(e -> {
						if (e.getType().is(GigTags.NEOHOST) && !e.hasEffect(GigStatusEffects.SPORE)) {
							if (!(e instanceof Player))
								blockEntity.particleCloud(e);
							if (e instanceof Player playerEntity)
								if (!playerEntity.isSpectator() && !playerEntity.isCreative())
									blockEntity.particleCloud(playerEntity);
						}
					});
			}
	}

	public void particleCloud(LivingEntity entity) {
		var areaEffectCloudEntity = new AreaEffectCloud(this.level, worldPosition.getX(), worldPosition.getY() + 0.5, worldPosition.getZ());
		areaEffectCloudEntity.setRadius(3.0F);
		areaEffectCloudEntity.setDuration(3);
		areaEffectCloudEntity.setRadiusPerTick(-areaEffectCloudEntity.getRadius() / (float) areaEffectCloudEntity.getDuration());
		areaEffectCloudEntity.setParticle(ParticleTypes.ASH);
		if (!entity.hasEffect(GigStatusEffects.SPORE))
			areaEffectCloudEntity.addEffect(new MobEffectInstance(GigStatusEffects.SPORE, Gigeresque.config.sporeTickTimer, 0));
		this.level.addFreshEntity(areaEffectCloudEntity);
	}
}
