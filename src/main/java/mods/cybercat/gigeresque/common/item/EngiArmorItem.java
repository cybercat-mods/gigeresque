package mods.cybercat.gigeresque.common.item;

import java.util.ArrayList;
import java.util.List;

import mods.cybercat.gigeresque.common.item.group.GigItemGroups;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class EngiArmorItem extends ArmorItem implements IAnimatable {

	private AnimationFactory factory = new AnimationFactory(this);

	public EngiArmorItem(ArmorMaterial materialIn, EquipmentSlot slot) {
		super(materialIn, slot, new Item.Properties().tab(GigItemGroups.GENERAL).stacksTo(1));
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<EngiArmorItem>(this, "controller", 20, this::predicate));
	}

	private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {

		// This is all the extradata this event carries. The livingentity is the entity
		// that's wearing the armor. The itemstack and equipmentslottype are self
		// explanatory.
		LivingEntity livingEntity = event.getExtraDataOfType(LivingEntity.class).get(0);

		// Always loop the animation but later on in this method we'll decide whether or
		// not to actually play it
		event.getController()
				.setAnimation(new AnimationBuilder().addAnimation("todo", false).addAnimation("todo_closed", true));

		// If the living entity is an armorstand just play the animation nonstop
		if (livingEntity instanceof ArmorStand) {
			return PlayState.CONTINUE;
		}

		// elements 2 to 6 are the armor so we take the sublist. Armorlist now only
		// contains the 4 armor slots
		List<Item> armorList = new ArrayList<>(4);
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.getType() == EquipmentSlot.Type.ARMOR) {
				if (livingEntity.getItemBySlot(slot) != null) {
					armorList.add(livingEntity.getItemBySlot(slot).getItem());
				}
			}
		}

		// Make sure the player is wearing the Helmet. If they are, continue playing
		// the animation, otherwise stop
//		boolean isWearingAll = armorList.containsAll(Arrays.asList(GigItems.ENGI_ARMOR_HELMET));
//		return isWearingAll ? PlayState.CONTINUE : PlayState.STOP;
		return PlayState.CONTINUE;
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

}