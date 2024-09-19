package mods.cybercat.gigeresque.common.worlddata;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class PandoraData extends SavedData {
    private static boolean triggered = false;

    private PandoraData() {
    }

    private PandoraData(CompoundTag tag, HolderLookup.Provider registryLookup) {
    }

    public static SavedData.Factory<PandoraData> factory() {
        return new SavedData.Factory<>(PandoraData::new, PandoraData::new, null);
    }

    public static boolean isTriggered(){
        return triggered;
    }

    public static void setIsTriggered(boolean setValue) {
        PandoraData.triggered = setValue;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        tag.putBoolean("pandora_trigger", isTriggered());
        return tag;
    }
}
