package mods.cybercat.gigeresque.mixins.common.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mods.cybercat.gigeresque.common.item.SurgeryKitItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * @author Boston Vanseghi
 */
@Mixin(Villager.class)
public abstract class VillagerEntityMixin extends AbstractVillager {

	public VillagerEntityMixin(EntityType<Villager> type, Level world) {
		super(type, world);
	}

	@Inject(method = { "mobInteract" }, at = { @At("HEAD") }, cancellable = true)
	protected InteractionResult mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> callbackInfo) {
		if (player.getItemInHand(hand).getItem() instanceof SurgeryKitItem)
			callbackInfo.setReturnValue(super.mobInteract(player, hand));
		return super.mobInteract(player, hand);
	}
}
