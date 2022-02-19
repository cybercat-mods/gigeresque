package com.bvanseg.gigeresque.common.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SoundUtil {
    private SoundUtil() {}

    public static void playServerSound(World world, PlayerEntity player, BlockPos blockPos, SoundEvent event, SoundCategory category, float volume, float pitch) {
        if (!world.isClient) {
            world.playSound(player, blockPos, event, category, volume, pitch);
        }
    }
    public static void playServerSound(World world, PlayerEntity player, BlockPos blockPos, SoundEvent event, SoundCategory category) {
        playServerSound(world, player, blockPos, event, category, 1.0f, 1.0f);
    }

    public static void playServerSound(World world, PlayerEntity player, BlockPos blockPos, SoundEvent event, SoundCategory category, float volume) {
        playServerSound(world, player, blockPos, event, category, volume, 1.0f);
    }
}
