package mods.cybercat.gigeresque.common.block.entity;

import mod.azure.azurelib.animatable.GeoBlockEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.block.storage.StorageProperties;
import mods.cybercat.gigeresque.common.block.storage.StorageStates;
import mods.cybercat.gigeresque.common.entity.Entities;

public class AlienStorageEntity extends RandomizableContainerBlockEntity implements GeoBlockEntity {

    public static final EnumProperty<StorageStates> CHEST_STATE = StorageProperties.STORAGE_STATE;

    protected final ContainerOpenersCounter stateManager = new ContainerOpenersCounter() {

        @Override
        protected void onOpen(@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state) {
            assert AlienStorageEntity.this.level != null;
            AlienStorageEntity.this.level.playSound(
                null,
                pos,
                SoundEvents.ITEM_FRAME_BREAK,
                SoundSource.BLOCKS,
                1.0f,
                1.0f
            );
        }

        @Override
        protected void onClose(@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state) {
            assert AlienStorageEntity.this.level != null;
            AlienStorageEntity.this.level.playSound(
                null,
                pos,
                SoundEvents.ITEM_FRAME_BREAK,
                SoundSource.BLOCKS,
                1.0f,
                1.0f
            );
        }

        @Override
        protected void openerCountChanged(
            @NotNull Level world,
            @NotNull BlockPos pos,
            @NotNull BlockState state,
            int oldViewerCount,
            int newViewerCount
        ) {
            AlienStorageEntity.this.onInvOpenOrClose(world, pos, state, oldViewerCount, newViewerCount);
        }

        @Override
        protected boolean isOwnContainer(Player player) {
            if (player.containerMenu instanceof ChestMenu menu)
                return menu.getContainer() == AlienStorageEntity.this;
            return false;
        }
    };

    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    private NonNullList<ItemStack> items = NonNullList.withSize(36, ItemStack.EMPTY);

    public AlienStorageEntity(BlockPos pos, BlockState state) {
        super(Entities.ALIEN_STORAGE_BLOCK_ENTITY_1, pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AlienStorageEntity blockEntity) {
        if (blockEntity.level != null) {
            if (!blockEntity.level.isClientSide)
                BlockPos.betweenClosed(pos, pos.above(2)).forEach(testPos -> {
                    if (!testPos.equals(pos) && !level.getBlockState(testPos).is(GigBlocks.ALIEN_STORAGE_BLOCK_INVIS))
                        level.setBlock(testPos, GigBlocks.ALIEN_STORAGE_BLOCK_INVIS.defaultBlockState(), Block.UPDATE_ALL);
                });
            if (!blockEntity.isRemoved())
                blockEntity.stateManager.recheckOpeners(
                    Objects.requireNonNull(blockEntity.getLevel()),
                    blockEntity.getBlockPos(),
                    blockEntity.getBlockState()
                );
        }
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(nbt))
            ContainerHelper.loadAllItems(nbt, this.items);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        if (!this.trySaveLootTable(nbt))
            ContainerHelper.saveAllItems(nbt, this.items);
    }

    @Override
    public int getContainerSize() {
        return 36;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(@NotNull NonNullList<ItemStack> list) {
        this.items = list;
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("block.gigeresque.alien_storage_block1");
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int syncId, @NotNull Inventory inventory) {
        return new ChestMenu(MenuType.GENERIC_9x4, syncId, inventory, this, 4);
    }

    @Override
    public void startOpen(@NotNull Player player) {
        if (!this.isRemoved() && !player.isSpectator())
            this.stateManager.incrementOpeners(
                player,
                Objects.requireNonNull(this.getLevel()),
                this.getBlockPos(),
                this.getBlockState()
            );
    }

    @Override
    public void stopOpen(@NotNull Player player) {
        if (!this.isRemoved() && !player.isSpectator())
            this.stateManager.decrementOpeners(
                player,
                Objects.requireNonNull(this.getLevel()),
                this.getBlockPos(),
                this.getBlockState()
            );
    }

    public void tick() {
        if (!this.isRemoved())
            this.stateManager.recheckOpeners(
                Objects.requireNonNull(this.getLevel()),
                this.getBlockPos(),
                this.getBlockState()
            );
    }

    protected void onInvOpenOrClose(Level world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
        world.blockEvent(pos, state.getBlock(), 1, newViewerCount);
        if (oldViewerCount != newViewerCount)
            if (newViewerCount > 0)
                world.setBlockAndUpdate(pos, state.setValue(CHEST_STATE, StorageStates.OPENED));
            else
                world.setBlockAndUpdate(pos, state.setValue(CHEST_STATE, StorageStates.CLOSING));
    }

    public StorageStates getChestState() {
        return this.getBlockState().getValue(AlienStorageEntity.CHEST_STATE);
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, event -> {
            if (
                getChestState().equals(StorageStates.CLOSING) && !event.isCurrentAnimation(
                    RawAnimation.begin().thenPlay("opening").thenPlayAndHold("opened")
                )
            )
                return event.setAndContinue(RawAnimation.begin().thenPlay("closing").thenPlayAndHold("closed"));
            else if (
                getChestState().equals(StorageStates.OPENED) && !event.isCurrentAnimation(
                    RawAnimation.begin().thenPlay("closing").thenPlayAndHold("closed")
                )
            )
                return event.setAndContinue(RawAnimation.begin().thenPlay("opening").thenPlayAndHold("opened"));
            return event.setAndContinue(RawAnimation.begin().thenLoop("closed"));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
