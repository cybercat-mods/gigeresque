package mods.cybercat.gigeresque.mixins.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import mods.cybercat.gigeresque.common.block.GigBlocks;

@Mixin(BrushableBlockEntity.class)
public abstract class BrushableBlockEntityMixin extends BlockEntity {

    protected final RandomSource random = RandomSource.create();

    @Shadow
    private ItemStack item;

    @Shadow
    private Direction hitDirection;

    public BrushableBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Inject(method = "dropContent", at = { @At("HEAD") })
    private void dropEggTest(Player player, CallbackInfo ci) {
        if (this.level != null && this.level.getServer() != null && random.nextInt(0, 100) > 95) {
            var d = EntityType.ITEM.getWidth();
            var e = 1.0 - d;
            var f = d / 2.0;
            var direction = Objects.requireNonNullElse(this.hitDirection, Direction.UP);
            var blockPos = this.worldPosition.relative(direction, 1);
            var g = blockPos.getX() + 0.5 * e + f;
            var h = blockPos.getY() + 0.5 + (EntityType.ITEM.getHeight() / 2.0F);
            var i = blockPos.getZ() + 0.5 * e + f;
            var itemEntity = new ItemEntity(this.level, g, h, i, GigBlocks.PETRIFIED_OBJECT_BLOCK.asItem().getDefaultInstance());
            itemEntity.setDeltaMovement(Vec3.ZERO);
            this.level.addFreshEntity(itemEntity);
            this.item = ItemStack.EMPTY;
        }
    }
}
