package mods.cybercat.gigeresque.common.item;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.config.ConfigAccessor;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.entity.EntityIdentifiers;
import mods.cybercat.gigeresque.common.entity.impl.AquaticChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.ChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.RunnerbursterEntity;
import mods.cybercat.gigeresque.common.status.effect.StatusEffects;
import mods.cybercat.gigeresque.interfacing.Host;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class SurgeryKitItem extends Item {
	public SurgeryKitItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		tryRemoveParasite(user.getStackInHand(hand), user, user, hand);
		return super.use(world, user, hand);
	}

	private void tryRemoveParasite(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		var host = (Host) entity;
		if (host.hasParasite()) {
			if (!entity.world.isClient) {
				if (host.getTicksUntilImpregnation() < 9600) {
					spawnParasite(entity);
				}

				host.removeParasite();
				stack.damage(1, user, p -> p.sendToolBreakStatus(hand));
				entity.removeStatusEffect(net.minecraft.entity.effect.StatusEffects.HUNGER);
				entity.removeStatusEffect(net.minecraft.entity.effect.StatusEffects.MINING_FATIGUE);
				entity.addStatusEffect(new StatusEffectInstance(StatusEffects.TRAUMA, Constants.TPD));
				host.setBleeding(false);
			}
		}
	}

	private void spawnParasite(LivingEntity entity) {
		var identifier = Registry.ENTITY_TYPE.getId(entity.getType());
		var morphMappings = ConfigAccessor.getReversedMorphMappings();

		String runnerString = Entities.RUNNER_ALIEN.toString();
		ChestbursterEntity burster;

		String orDefault = morphMappings.getOrDefault(identifier.toString(), EntityIdentifiers.ALIEN.toString());
		if (runnerString.equals(orDefault)) {
			burster = new RunnerbursterEntity(Entities.RUNNERBURSTER, entity.world);
		} else if (Entities.AQUATIC_ALIEN.toString().equals(orDefault)) {
			burster = new AquaticChestbursterEntity(Entities.AQUATIC_CHESTBURSTER, entity.world);
		} else {
			burster = new ChestbursterEntity(Entities.CHESTBURSTER, entity.world);
		}

		burster.setHostId(identifier.toString());
		burster.refreshPositionAndAngles(entity.getBlockPos(), entity.getYaw(), entity.getPitch());
		entity.world.spawnEntity(burster);
	}
}
