package mods.cybercat.gigeresque.common.item;

import mod.azure.azurelib.common.internal.common.AzureLib;
import mod.azure.azurelib.common.platform.Services;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import mods.cybercat.gigeresque.common.entity.GigEntities;
import mods.cybercat.gigeresque.common.item.animator.TrackerAnimationDispatcher;
import mods.cybercat.gigeresque.common.tags.GigTags;

public class TrackerItem extends Item {

    private TrackerAnimationDispatcher animationDispatcher;

    public TrackerItem() {
        super(new Properties());
        animationDispatcher = new TrackerAnimationDispatcher();
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemstack, int remainingUseDuration) {
        super.onUseTick(level, livingEntity, itemstack, remainingUseDuration);

        if (level instanceof ServerLevel serverlevel && livingEntity instanceof Player player) {
            var blockpos = serverlevel.findNearestMapStructure(GigTags.GIG_EXPLORER_MAPS, player.blockPosition(), 100, false);
            if (blockpos != null && !player.getCooldowns().isOnCooldown(this)) {
                animationDispatcher.sendOpeningAnimation(player, itemstack);
                var viewVector = player.getViewVector(1.0F);
                var spawnX = player.getX() + viewVector.x * 5;
                var spawnZ = player.getZ() + viewVector.z * 5;
                var dx = blockpos.getX() + 0.5 - spawnX; // Center of the block
                var dz = blockpos.getZ() + 0.5 - spawnZ; // Center of the block
                var rotation = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90);

                var horizontalDistance = Math.sqrt(dx * dx + dz * dz);

                int distanceCategory;
                if (horizontalDistance <= 50)
                    distanceCategory = 3; // Close (within 50 blocks)
                else if (horizontalDistance <= 500)
                    distanceCategory = 2; // Mid-range (50 - 75 blocks)
                else
                    distanceCategory = 1; // Far (greater than 75 blocks)
                var hologramEntity = GigEntities.ENGINEER_HOLOGRAM.get().create(level);
                if (hologramEntity != null) {
                    if (Services.PLATFORM.isDevelopmentEnvironment())
                        AzureLib.LOGGER.info("Distance Category: {}", distanceCategory);
                    hologramEntity.setPos(spawnX, player.getY(), spawnZ);
                    hologramEntity.setDistanceState(distanceCategory);
                    hologramEntity.setDistanceFromStructure((int) horizontalDistance);
                    hologramEntity.setYRot(rotation);
                    hologramEntity.setOnGround(true);
                    level.addFreshEntity(hologramEntity);
                }
            } else {
                level.playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundEvents.CRAFTER_FAIL,
                    SoundSource.NEUTRAL,
                    1.0F,
                    1.0F
                );
            }
            player.getCooldowns().addCooldown(this, 120);
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(
        @NotNull Level world,
        Player user,
        @NotNull InteractionHand hand
    ) {
        final var itemStack = user.getItemInHand(hand);
        user.startUsingItem(hand);
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (entity instanceof Player player && !player.getCooldowns().isOnCooldown(this)) {
            animationDispatcher.sendClosingAnimation(entity, stack);
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }
}
