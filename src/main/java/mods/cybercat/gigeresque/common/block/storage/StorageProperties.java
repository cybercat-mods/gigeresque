package mods.cybercat.gigeresque.common.block.storage;

import net.minecraft.world.level.block.state.properties.EnumProperty;

public class StorageProperties {

    public static final EnumProperty<StorageStates> STORAGE_STATE = EnumProperty.create("storage_state", StorageStates.class);
}
