package mods.cybercat.gigeresque.mixins.common.crawling;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.azure.bettercrawling.entity.mob.IEntityMovementHook;
import mods.azure.bettercrawling.entity.mob.IEntityReadWriteHook;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class CrawlingEntityMixin implements IEntityMovementHook, IEntityReadWriteHook {

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    private void onMovePre(MoverType type, Vec3 pos, CallbackInfo ci) {
        if (this.onMove(type, pos, true)) {
            ci.cancel();
        }
    }

    @Inject(method = "move", at = @At("RETURN"))
    private void onMovePost(MoverType type, Vec3 pos, CallbackInfo ci) {
        this.onMove(type, pos, false);
    }

    @Override
    public boolean onMove(MoverType type, Vec3 pos, boolean pre) {
        return false;
    }

    @Inject(method = "getOnPos", at = @At("RETURN"), cancellable = true)
    private void onGetOnPosition(CallbackInfoReturnable<BlockPos> ci) {
        BlockPos adjusted = this.getAdjustedOnPosition(ci.getReturnValue());
        if (adjusted != null) {
            ci.setReturnValue(adjusted);
        }
    }

    @Override
    public BlockPos getAdjustedOnPosition(BlockPos onPosition) {
        return null;
    }

    @WrapOperation(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity$MovementEmission;emitsAnything()Z"))
    public boolean bop(Entity.MovementEmission instance, Operation<Boolean> original) {
        return this.getAdjustedCanTriggerWalking(instance.emitsAnything());
    }


    @Override
    public boolean getAdjustedCanTriggerWalking(boolean canTriggerWalking) {
        return canTriggerWalking;
    }

    @Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", shift = At.Shift.AFTER))
    private void onRead(CompoundTag nbt, CallbackInfo ci) {
        this.onRead(nbt);
    }

    @Override
    public void onRead(CompoundTag nbt) {
    }

    @Inject(method = "saveWithoutId", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", shift = At.Shift.AFTER))
    private void onWrite(CompoundTag nbt, CallbackInfoReturnable<CompoundTag> ci) {
        this.onWrite(nbt);
    }

    @Override
    public void onWrite(CompoundTag nbt) {
    }

}
