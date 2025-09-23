package mods.cybercat.gigeresque.mixins.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.item.GigItems;


@Mixin(BrushableBlockEntity.class)
public abstract class BrushableBlockEntityMixin extends BlockEntity {

    protected final RandomSource random = RandomSource.create();

    @Shadow
    private ItemStack item;

    @Shadow
    private ResourceKey<LootTable> lootTable;

    public BrushableBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Inject(method = "unpackLootTable", at = { @At("HEAD") })
    private void gigeresque$dropEggTest(Player player, CallbackInfo ci) {
        if (this.lootTable != null && this.level != null && this.level.getServer() != null && random.nextInt(0, 100) > 90) {
            ItemStack randomItem;
            switch (this.random.nextInt(0, 10)) {
                case 5 -> randomItem = GigBlocks.PETRIFIED_OBJECT_BLOCK_ITEM.get().getDefaultInstance();
                case 6 -> randomItem = GigBlocks.PETRIFIED_OBJECT_1_BLOCK_ITEM.get().getDefaultInstance();
                case 7 -> randomItem = GigBlocks.PETRIFIED_OBJECT_2_BLOCK_ITEM.get().getDefaultInstance();
                case 8 -> randomItem = GigBlocks.PETRIFIED_OBJECT_3_BLOCK_ITEM.get().getDefaultInstance();
                case 9 -> randomItem = GigBlocks.PETRIFIED_OBJECT_4_BLOCK_ITEM.get().getDefaultInstance();
                case 10 -> randomItem = GigBlocks.PETRIFIED_OBJECT_5_BLOCK_ITEM.get().getDefaultInstance();
                default -> randomItem = GigItems.TRACKER.get().getDefaultInstance();
            }
            this.item = randomItem;
            this.lootTable = null;
            this.setChanged();
        }
    }
}
