package mods.cybercat.gigeresque.common.block;

import net.minecraft.util.StringIdentifiable;

public enum StorageStates implements StringIdentifiable {
	OPEN("open"), OPENED("opened"), CLOSE("close"), CLOSED("closed"), CLOSING("closing");

	private final String name;

	private StorageStates(String name) {
		this.name = name;
	}

	@Override
	public String asString() {
		return this.name;
	}
}