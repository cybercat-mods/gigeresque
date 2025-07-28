package mods.cybercat.gigeresque.mixins.common.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mods.cybercat.gigeresque.common.tags.GigTags;

@Mixin(BucketItem.class)
public class BucketMixin_DontBucketBlackFluid {

    @Inject(
        method = "use",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/BucketItem;getPlayerPOVHitResult(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/ClipContext$Fluid;)Lnet/minecraft/world/phys/BlockHitResult;"
        ),
        cancellable = true
    )
    public void gigeresque$blockBucketBlackFluid(
        Level level,
        Player player,
        InteractionHand hand,
        CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir
    ) {
        var itemstack = player.getItemInHand(hand);
        var blockHitResult = Item.getPlayerPOVHitResult(
            level,
            player,
            ClipContext.Fluid.SOURCE_ONLY
        );
        if (blockHitResult.getType() == HitResult.Type.BLOCK && !player.isCreative()) {
            var blockpos = blockHitResult.getBlockPos();
            if (level.getFluidState(blockpos).is(GigTags.BLACK_FLUID)) {
                cir.setReturnValue(InteractionResultHolder.fail(itemstack));
            }
        }
    }
}
