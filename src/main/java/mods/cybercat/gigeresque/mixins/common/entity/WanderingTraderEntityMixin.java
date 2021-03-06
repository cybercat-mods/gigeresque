package mods.cybercat.gigeresque.mixins.common.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mods.cybercat.gigeresque.common.item.SurgeryKitItem;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/**
 * @author Boston Vanseghi
 */
@Mixin(WanderingTraderEntity.class)
public abstract class WanderingTraderEntityMixin extends MerchantEntity {

	public WanderingTraderEntityMixin(EntityType<WanderingTraderEntity> type, World world) {
		super(type, world);
	}

	@Inject(method = { "interactMob" }, at = { @At("HEAD") }, cancellable = true)
	protected ActionResult interactMob(PlayerEntity player, Hand hand,
			CallbackInfoReturnable<ActionResult> callbackInfo) {
		if (player.getStackInHand(hand).getItem() instanceof SurgeryKitItem) {
			callbackInfo.setReturnValue(super.interactMob(player, hand));
		}
		return super.interactMob(player, hand);
	}
}
