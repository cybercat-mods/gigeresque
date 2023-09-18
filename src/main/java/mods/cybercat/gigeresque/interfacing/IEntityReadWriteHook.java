package mods.cybercat.gigeresque.interfacing;

import net.minecraft.nbt.CompoundTag;

public interface IEntityReadWriteHook {
	public void onRead(CompoundTag nbt);
	
	public void onWrite(CompoundTag nbt);
}
