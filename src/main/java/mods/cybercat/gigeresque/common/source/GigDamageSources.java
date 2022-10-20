package mods.cybercat.gigeresque.common.source;

import java.util.List;
import java.util.SplittableRandom;

import net.minecraft.world.damagesource.DamageSource;

public class GigDamageSources {
	private GigDamageSources() {
	}

	public static final DamageSource ACID = new DamageSource("alien.acid") {
	};
	public static final DamageSource CHESTBURSTING = new DamageSource("chestburst") {
	};
	public static final DamageSource EGGMORPHING = new DamageSource("eggmorph") {
	};
	public static final DamageSource GOO = new DamageSource("goo") {
	};
	public static final DamageSource DNA = new DamageSource("dna") {
	};

	private static List<? extends String> arr = List.of("execution1", "execution2");
	private static SplittableRandom random = new SplittableRandom();
	private static int randomIndex = random.nextInt(arr.size());
	public static final DamageSource EXECUTION = new DamageSource(arr.get(randomIndex)) {
	};
}
