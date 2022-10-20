package mods.cybercat.gigeresque.common.block;

import net.minecraft.util.StringRepresentable;

public enum StorageStates implements StringRepresentable {
	OPEN("open"), OPENED("opened"), CLOSE("close"), CLOSED("closed"), CLOSING("closing");

	private final String name;

	private StorageStates(String name) {
		this.name = name;
	}

	@Override
	public String getSerializedName() {
		return this.name;
	}
}