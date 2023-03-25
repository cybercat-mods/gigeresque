package mods.cybercat.gigeresque.common.util;

import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class GigVibrationSelector {
	public static final Codec<GigVibrationSelector> CODEC = RecordCodecBuilder.create(instance -> instance.group(GigVibrationInfo.CODEC.optionalFieldOf("event").forGetter(vibrationSelector -> vibrationSelector.currentVibrationData.map(Pair::getLeft)), (Codec.LONG.fieldOf("tick")).forGetter(vibrationSelector -> vibrationSelector.currentVibrationData.map(Pair::getRight).orElse(-1L))).apply(instance, GigVibrationSelector::new));
	private Optional<Pair<GigVibrationInfo, Long>> currentVibrationData;

	public GigVibrationSelector(Optional<GigVibrationInfo> optional, long l) {
		this.currentVibrationData = optional.map(vibrationInfo -> Pair.of(vibrationInfo, l));
	}

	public GigVibrationSelector() {
		this.currentVibrationData = Optional.empty();
	}

	public void addCandidate(GigVibrationInfo vibrationInfo, long l) {
		if (this.shouldReplaceVibration(vibrationInfo, l))
			this.currentVibrationData = Optional.of(Pair.of(vibrationInfo, l));
	}

	private boolean shouldReplaceVibration(GigVibrationInfo vibrationInfo, long l) {
		if (this.currentVibrationData.isEmpty())
			return true;
		var pair = this.currentVibrationData.get();
		var m = pair.getRight();

		if (l != m)
			return false;

		var vibrationInfo2 = pair.getLeft();

		if (vibrationInfo.distance() < vibrationInfo2.distance())
			return true;
		if (vibrationInfo.distance() > vibrationInfo2.distance())
			return false;

		return GigVibrationListener.getGameEventFrequency(vibrationInfo.gameEvent()) > GigVibrationListener.getGameEventFrequency(vibrationInfo2.gameEvent());
	}

	public Optional<GigVibrationInfo> chosenCandidate(long l) {
		if (this.currentVibrationData.isEmpty())
			return Optional.empty();
		if (this.currentVibrationData.get().getRight() < l)
			return Optional.of(this.currentVibrationData.get().getLeft());
		return Optional.empty();
	}

	public void startOver() {
		this.currentVibrationData = Optional.empty();
	}
}
