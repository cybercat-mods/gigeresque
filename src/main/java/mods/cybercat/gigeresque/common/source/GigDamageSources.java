package mods.cybercat.gigeresque.common.source;

import java.util.List;
import java.util.SplittableRandom;

import mods.cybercat.gigeresque.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public record GigDamageSources() {
	
	private static List<? extends String> list = List.of("execution1", "execution2");
	private static SplittableRandom random = new SplittableRandom();
	private static int randomIndex = random.nextInt(list.size());
	
    public static final ResourceKey<DamageType> ACID = ResourceKey.create(Registries.DAMAGE_TYPE, Constants.modResource("acid"));
    public static final ResourceKey<DamageType> CHESTBURSTING = ResourceKey.create(Registries.DAMAGE_TYPE, Constants.modResource("chestburst"));
    public static final ResourceKey<DamageType> EGGMORPHING = ResourceKey.create(Registries.DAMAGE_TYPE, Constants.modResource("eggmorph"));
    public static final ResourceKey<DamageType> GOO = ResourceKey.create(Registries.DAMAGE_TYPE, Constants.modResource("goo"));
    public static final ResourceKey<DamageType> DNA = ResourceKey.create(Registries.DAMAGE_TYPE, Constants.modResource("dna"));
    public static final ResourceKey<DamageType> XENO = ResourceKey.create(Registries.DAMAGE_TYPE, Constants.modResource("xeno"));
    public static final ResourceKey<DamageType> SPORE = ResourceKey.create(Registries.DAMAGE_TYPE, Constants.modResource("spore"));
    public static final ResourceKey<DamageType> EXECUTION = ResourceKey.create(Registries.DAMAGE_TYPE, Constants.modResource(list.get(randomIndex)));
    
    public static DamageSource of(Level level, ResourceKey<DamageType> key) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key));
    }
}
