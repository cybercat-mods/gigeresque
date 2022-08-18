package mods.cybercat.gigeresque.common.block.entity;

import mods.cybercat.gigeresque.common.block.storage.AlienJarBlock;
import mods.cybercat.gigeresque.common.entity.Entities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class JarStorageEntity extends LootableContainerBlockEntity implements IAnimatable {

	private DefaultedList<ItemStack> items = DefaultedList.ofSize(18, ItemStack.EMPTY);
	private final AnimationFactory factory = new AnimationFactory(this);
	private final ViewerCountManager stateManager = new ViewerCountManager() {

		@Override
		protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
			JarStorageEntity.this.setOpen(state, true);
		}

		@Override
		protected void onContainerClose(World world, BlockPos pos, BlockState state) {
			JarStorageEntity.this.setOpen(state, false);
		}

		@Override
		protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldViewerCount,
				int newViewerCount) {
		}

		@Override
		protected boolean isPlayerViewing(PlayerEntity player) {
			if (player.currentScreenHandler instanceof GenericContainerScreenHandler) {
				Inventory inventory = ((GenericContainerScreenHandler) player.currentScreenHandler).getInventory();
				return inventory == JarStorageEntity.this;
			}
			return false;
		}
	};

	public JarStorageEntity(BlockPos pos, BlockState state) {
		super(Entities.ALIEN_STORAGE_BLOCK_ENTITY_2, pos, state);
	}

	void setOpen(BlockState state, boolean open) {
		this.world.setBlockState(this.getPos(), (BlockState) state.with(AlienJarBlock.OPEN, open), Block.NOTIFY_ALL);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.items = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		if (!this.deserializeLootTable(nbt)) {
			Inventories.readNbt(nbt, this.items);
		}
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if (!this.serializeLootTable(nbt)) {
			Inventories.writeNbt(nbt, this.items);
		}
	}

	@Override
	public int size() {
		return 18;
	}

	@Override
	protected DefaultedList<ItemStack> getInvStackList() {
		return this.items;
	}

	@Override
	protected void setInvStackList(DefaultedList<ItemStack> list) {
		this.items = list;
	}

	@Override
	protected Text getContainerName() {
		return Text.translatable("container.barrel");
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory inventory) {
		return GenericContainerScreenHandler.createGeneric9x2(syncId, inventory);
	}

	@Override
	public void onOpen(PlayerEntity player) {
		if (!this.removed && !player.isSpectator()) {
			this.stateManager.openContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
		}
	}

	@Override
	public void onClose(PlayerEntity player) {
		if (!this.removed && !player.isSpectator()) {
			this.stateManager.closeContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
		}
	}

	public void tick() {
		if (!this.removed) {
			this.stateManager.updateViewerCount(this.getWorld(), this.getPos(), this.getCachedState());
		}
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<JarStorageEntity>(this, "controller", 0, this::predicate));
	}

	private <E extends BlockEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
//		if (this.getState() == true) {
//			if (randomPhase > 25) {
//				event.getController()
//						.setAnimation(new AnimationBuilder().addAnimation("open2", false).addAnimation("open_loop"));
//			} else {
//				event.getController()
//						.setAnimation(new AnimationBuilder().addAnimation("open", false).addAnimation("open_loop"));
//			}
//			return PlayState.CONTINUE;
//		} else {
//			event.getController()
//					.setAnimation(new AnimationBuilder().addAnimation("closing", false).addAnimation("closed", true));
//			return PlayState.CONTINUE;
//		}
		return PlayState.CONTINUE;
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}
}