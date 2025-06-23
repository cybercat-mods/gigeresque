package mods.cybercat.gigeresque.bvanseg;

import net.minecraft.nbt.CompoundTag;

import java.time.Duration;

public class Cooldown implements NBTSerializable {

    public static Cooldown withCooldownTime(String name, Duration cooldownTime) {
        return withCooldownTimeInTicks(name, TickUtil.toTicks(cooldownTime));
    }

    public static Cooldown withCooldownTimeInTicks(String name, long maxCooldownInTicks) {
        return new Cooldown(name, maxCooldownInTicks);
    }

    private final long maxCooldownInTicks;

    private final String name;

    private long cooldownInTicks;

    private Cooldown(String name, long maxCooldownInTicks) {
        this.maxCooldownInTicks = maxCooldownInTicks;
        this.name = name;
    }

    public void tick() {
        cooldownInTicks = Math.max(cooldownInTicks - 1, 0);
    }

    public boolean isActive() {
        return cooldownInTicks > 0;
    }

    public void reset() {
        this.cooldownInTicks = maxCooldownInTicks;
    }

    public String getName() {
        return name;
    }

    @Override
    public void load(CompoundTag compoundTag) {
        compoundTag.putLong(name, cooldownInTicks);
    }

    @Override
    public void save(CompoundTag compoundTag) {
        if (compoundTag.contains(name)) {
            this.cooldownInTicks = compoundTag.getLong(name);
        }
    }
}
