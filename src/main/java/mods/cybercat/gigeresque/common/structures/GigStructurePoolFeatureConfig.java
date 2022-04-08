package mods.cybercat.gigeresque.common.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.FeatureConfig;

public class GigStructurePoolFeatureConfig implements FeatureConfig {

	public static final Codec<GigStructurePoolFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance
			.group((StructurePool.REGISTRY_CODEC.fieldOf("start_pool"))
					.forGetter(GigStructurePoolFeatureConfig::getStartPool),
					(Codec.intRange(0, 35).fieldOf("size")).forGetter(GigStructurePoolFeatureConfig::getSize))
			.apply(instance, GigStructurePoolFeatureConfig::new));

	private final RegistryEntry<StructurePool> startPool;
	private final int size;

	public GigStructurePoolFeatureConfig(RegistryEntry<StructurePool> startPool, int size) {
		this.startPool = startPool;
		this.size = size;
	}

	public int getSize() {
		return this.size;
	}

	public RegistryEntry<StructurePool> getStartPool() {
		return this.startPool;
	}
}