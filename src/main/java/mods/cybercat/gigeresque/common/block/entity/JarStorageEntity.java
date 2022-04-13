package mods.cybercat.gigeresque.common.block.entity;

import java.util.SplittableRandom;

import mods.cybercat.gigeresque.common.block.inventory.ImplementedInventory;
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
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class JarStorageEntity extends LootableContainerBlockEntity
		implements ImplementedInventory, SidedInventory, NamedScreenHandlerFactory, IAnimatable {

	private DefaultedList<ItemStack> items = DefaultedList.ofSize(27, ItemStack.EMPTY);
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
	public DefaultedList<ItemStack> getItems() {
		return items;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		Inventories.readNbt(nbt, items);
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		Inventories.writeNbt(nbt, items);
	}

	@Override
	public int[] getAvailableSlots(Direction var1) {
		int[] result = new int[getItems().size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = i;
		}
		return result;
	}

	public static void copyInventory(JarStorageEntity from, JarStorageEntity to) {
		DefaultedList<ItemStack> defaultedList = from.getInvStackList();
		from.setInvStackList(to.getInvStackList());
		to.setInvStackList(defaultedList);
	}

	@Override
	public boolean canInsert(int var1, ItemStack var2, Direction var3) {
		return true;
	}

	@Override
	public boolean canExtract(int var1, ItemStack var2, Direction var3) {
		return true;
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory inventory) {
		return GenericContainerScreenHandler.createGeneric9x2(syncId, inventory);
	}

	@Override
	public Text getDisplayName() {
		return new TranslatableText(getCachedState().getBlock().getTranslationKey());
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
		return new TranslatableText("container.chest");
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
	public void onOpen(PlayerEntity player) {
		if (!this.removed && !player.isSpectator()) {
			this.stateManager.openContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
			JarStorageEntity.state = true;
		}
	}

	@Override
	public void onClose(PlayerEntity player) {
		if (!this.removed && !player.isSpectator()) {
			this.stateManager.closeContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
			JarStorageEntity.state = false;
		}
	}

	protected void onInvOpenOrClose(World world, BlockPos pos, BlockState state, int oldViewerCount,
			int newViewerCount) {
		Block block = state.getBlock();
		world.addSyncedBlockEvent(pos, block, 1, newViewerCount);
	}
}