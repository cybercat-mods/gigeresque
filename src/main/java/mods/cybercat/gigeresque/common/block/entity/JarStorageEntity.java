package mods.cybercat.gigeresque.common.block.entity;

import mods.cybercat.gigeresque.common.block.StorageProperties;
import mods.cybercat.gigeresque.common.block.StorageStates;
import mods.cybercat.gigeresque.common.entity.Entities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class JarStorageEntity extends RandomizableContainerBlockEntity implements IAnimatable {

	private NonNullList<ItemStack> items = NonNullList.withSize(18, ItemStack.EMPTY);
	private final AnimationFactory factory = new AnimationFactory(this);
	public static final EnumProperty<StorageStates> CHEST_STATE = StorageProperties.STORAGE_STATE;
	private final ContainerOpenersCounter stateManager = new ContainerOpenersCounter() {

		@Override
		protected void onOpen(Level world, BlockPos pos, BlockState state) {
		}

		@Override
		protected void onClose(Level world, BlockPos pos, BlockState state) {
		}

		@Override
		protected void openerCountChanged(Level world, BlockPos pos, BlockState state, int oldViewerCount,
				int newViewerCount) {
			JarStorageEntity.this.onInvOpenOrClose(world, pos, state, oldViewerCount, newViewerCount);
		}

		@Override
		protected boolean isOwnContainer(Player player) {
			if (player.containerMenu instanceof ChestMenu) {
				Container inventory = ((ChestMenu) player.containerMenu).getContainer();
				return inventory == JarStorageEntity.this;
			}
			return false;
		}
	};

	public JarStorageEntity(BlockPos pos, BlockState state) {
		super(Entities.ALIEN_STORAGE_BLOCK_ENTITY_2, pos, state);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		if (!this.tryLoadLootTable(nbt)) {
			ContainerHelper.loadAllItems(nbt, this.items);
		}
	}

	@Override
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		if (!this.trySaveLootTable(nbt)) {
			ContainerHelper.saveAllItems(nbt, this.items);
		}
	}

	@Override
	public int getContainerSize() {
		return 18;
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.items;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> list) {
		this.items = list;
	}

	@Override
	protected Component getDefaultName() {
		return Component.translatable("block.gigeresque.alien_storage_block2");
	}

	@Override
	protected AbstractContainerMenu createMenu(int syncId, Inventory inventory) {
		return new ChestMenu(MenuType.GENERIC_9x2, syncId, inventory, this, 2);
	}

	@Override
	public void startOpen(Player player) {
		if (!this.isRemoved() && !player.isSpectator()) {
			this.stateManager.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
	}

	@Override
	public void stopOpen(Player player) {
		if (!this.isRemoved() && !player.isSpectator()) {
			this.stateManager.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
	}

	public void tick() {
		if (!this.isRemoved()) {
			this.stateManager.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
		}
	}

	protected void onInvOpenOrClose(Level world, BlockPos pos, BlockState state, int oldViewerCount,
			int newViewerCount) {
		Block block = state.getBlock();
		world.blockEvent(pos, block, 1, newViewerCount);
		if (oldViewerCount != newViewerCount) {
			if (newViewerCount > 0) {
				world.setBlockAndUpdate(pos, state.setValue(CHEST_STATE, StorageStates.OPENED));
			} else {
				world.setBlockAndUpdate(pos, state.setValue(CHEST_STATE, StorageStates.CLOSING));
			}
		}
	}

	public StorageStates getChestState() {
		return this.getBlockState().getValue(JarStorageEntity.CHEST_STATE);
	}

	public void setChestState(StorageStates state) {
		this.getLevel().setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(CHEST_STATE, state));
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<JarStorageEntity>(this, "controller", 0, this::predicate));
	}

	private <E extends BlockEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		if (getChestState().equals(StorageStates.CLOSING)) {
			event.getController()
					.setAnimation(new AnimationBuilder().addAnimation("closing", false).addAnimation("closed", true));
			return PlayState.CONTINUE;
		} else if (getChestState().equals(StorageStates.OPENED)) {
			event.getController()
					.setAnimation(new AnimationBuilder().addAnimation("opening", false).addAnimation("opened"));
			return PlayState.CONTINUE;
		}
		event.getController().setAnimation(new AnimationBuilder().addAnimation("closed", true));
		return PlayState.CONTINUE;
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}
}