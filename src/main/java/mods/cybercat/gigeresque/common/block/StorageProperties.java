package mods.cybercat.gigeresque.common.block;

import net.minecraft.state.property.EnumProperty;

public class StorageProperties {

	public static final EnumProperty<StorageStates> STORAGE_STATE = EnumProperty.of("storage_state",
			StorageStates.class);
}
