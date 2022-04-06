package mods.cybercat.gigeresque.common.block.entity;

import mods.cybercat.gigeresque.common.block.inventory.ImplementedInventory;
import mods.cybercat.gigeresque.common.entity.Entities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
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

public class AlienStorageEntity extends LootableContainerBlockEntity
		implements ImplementedInventory, SidedInventory, NamedScreenHandlerFactory {

	private DefaultedList<ItemStack> items = DefaultedList.ofSize(27, ItemStack.EMPTY);

	public AlienStorageEntity(BlockPos pos, BlockState state) {
		super(Entities.ALIEN_STORAGE_BLOCK_ENTITY, pos, state);
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

	public static void copyInventory(AlienStorageEntity from, AlienStorageEntity to) {
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
		return GenericContainerScreenHandler.createGeneric9x3(syncId, inventory, this);
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
}
