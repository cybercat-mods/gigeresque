package mods.cybercat.gigeresque.common.block.entity;

import java.util.SplittableRandom;

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
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class JarStorageEntity extends LootableContainerBlockEntity implements IAnimatable {

	private DefaultedList<ItemStack> items = DefaultedList.ofSize(18, ItemStack.EMPTY);
	private final AnimationFactory factory = new AnimationFactory(this);
	public static boolean state;
	SplittableRandom random = new SplittableRandom();
	int randomPhase = random.nextInt(0, 50);
	private final ViewerCountManager stateManager = new ViewerCountManager() {

		@Override
		protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
			JarStorageEntity.setState(true);
		}

		@Override
		protected void onContainerClose(World world, BlockPos pos, BlockState state) {
			JarStorageEntity.setState(false);
		}

		@Override
		protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldViewerCount,
				int newViewerCount) {
			JarStorageEntity.this.onInvOpenOrClose(world, pos, state, oldViewerCount, newViewerCount);
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

	public boolean getState() {
		return state;
	}

	public static boolean setState(boolean state1) {
		return state1 == true ? JarStorageEntity.state == true : JarStorageEntity.state == false;
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

	public static void copyInventory(JarStorageEntity from, JarStorageEntity to) {
		DefaultedList<ItemStack> defaultedList = from.getInvStackList();
		from.setInvStackList(to.getInvStackList());
		to.setInvStackList(defaultedList);
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory inventory) {
		return GenericContainerScreenHandler.createGeneric9x2(syncId, inventory);
	}

	@Override
	public Text getDisplayName() {
		return Text.translatable(getCachedState().getBlock().getTranslationKey());
	}

	@Override
	protected Text getContainerName() {
		return Text.translatable("container.chest");
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<JarStorageEntity>(this, "controller", 0, this::predicate));
	}

	private <E extends BlockEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		if (this.getState() == true) {
			if (randomPhase > 25) {
				event.getController()
						.setAnimation(new AnimationBuilder().addAnimation("open2", false).addAnimation("open_loop"));
			} else {
				event.getController()
						.setAnimation(new AnimationBuilder().addAnimation("open", false).addAnimation("open_loop"));
			}
			return PlayState.CONTINUE;
		} else {
			event.getController()
					.setAnimation(new AnimationBuilder().addAnimation("closing", false).addAnimation("closed", true));
			return PlayState.CONTINUE;
		}
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	@Override
	public boolean onSyncedBlockEvent(int type, int data) {
		if (type == 1) {
			return true;
		}
		return super.onSyncedBlockEvent(type, data);
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

	@Override
	protected DefaultedList<ItemStack> getInvStackList() {
		return this.items;
	}

	@Override
	protected void setInvStackList(DefaultedList<ItemStack> list) {
		this.items = list;
	}

	protected void onInvOpenOrClose(World world, BlockPos pos, BlockState state, int oldViewerCount,
			int newViewerCount) {
		Block block = state.getBlock();
		world.addSyncedBlockEvent(pos, block, 1, newViewerCount);
	}

	public void onScheduledTick() {
		if (!this.removed) {
			this.stateManager.updateViewerCount(this.getWorld(), this.getPos(), this.getCachedState());
		}
	}

	public static int getPlayersLookingInChestCount(BlockView world, BlockPos pos) {
		BlockEntity blockEntity;
		BlockState blockState = world.getBlockState(pos);
		if (blockState.hasBlockEntity() && (blockEntity = world.getBlockEntity(pos)) instanceof JarStorageEntity) {
			return ((JarStorageEntity) blockEntity).stateManager.getViewerCount();
		}
		return 0;
	}

	@Override
	public int size() {
		return 18;
	}
}