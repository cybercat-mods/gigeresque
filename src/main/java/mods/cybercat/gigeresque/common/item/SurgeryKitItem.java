package mods.cybercat.gigeresque.common.item;

import mods.cybercat.gigeresque.Constants;
import mods.cybercat.gigeresque.common.config.ConfigAccessor;
import mods.cybercat.gigeresque.common.config.GigeresqueConfig;
import mods.cybercat.gigeresque.common.entity.Entities;
import mods.cybercat.gigeresque.common.entity.EntityIdentifiers;
import mods.cybercat.gigeresque.common.entity.impl.AquaticChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.ChestbursterEntity;
import mods.cybercat.gigeresque.common.entity.impl.RunnerbursterEntity;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import mods.cybercat.gigeresque.interfacing.Host;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SurgeryKitItem extends Item {

	public SurgeryKitItem() {
		super(new Item.Properties().durability(4));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		tryRemoveParasite(user.getItemInHand(hand), user, user, hand);
		return super.use(world, user, hand);
	}

	private void tryRemoveParasite(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
		var host = (Host) entity;
		if (host.hasParasite())
			if (!entity.level.isClientSide) {
				if (host.getTicksUntilImpregnation() < GigeresqueConfig.getImpregnationTickTimer())
					spawnParasite(entity);

				host.removeParasite();
				stack.hurtAndBreak(1, user, p -> p.broadcastBreakEvent(hand));
				entity.removeEffect(MobEffects.HUNGER);
				entity.removeEffect(MobEffects.DIG_SLOWDOWN);
				entity.addEffect(new MobEffectInstance(GigStatusEffects.TRAUMA, Constants.TPD));
				host.setBleeding(false);
			}
	}

	private void spawnParasite(LivingEntity entity) {
		var identifier = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
		var morphMappings = ConfigAccessor.getReversedMorphMappings();

		var runnerString = Entities.RUNNER_ALIEN.toString();
		ChestbursterEntity burster;

		var orDefault = morphMappings.getOrDefault(identifier.toString(), EntityIdentifiers.ALIEN.toString());
		if (runnerString.equals(orDefault))
			burster = new RunnerbursterEntity(Entities.RUNNERBURSTER, entity.level);
		else if (Entities.AQUATIC_ALIEN.toString().equals(orDefault))
			burster = new AquaticChestbursterEntity(Entities.AQUATIC_CHESTBURSTER, entity.level);
		else
			burster = new ChestbursterEntity(Entities.CHESTBURSTER, entity.level);

		burster.setHostId(identifier.toString());
		burster.moveTo(entity.blockPosition(), entity.getYRot(), entity.getXRot());
		entity.level.addFreshEntity(burster);
	}
}
