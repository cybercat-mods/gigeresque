package mods.cybercat.gigeresque.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import mods.cybercat.gigeresque.CommonMod;
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
    public void onUseTick(
        @NotNull Level level,
        @NotNull LivingEntity livingEntity,
        @NotNull ItemStack itemstack,
        int remainingUseDuration
    ) {
        super.onUseTick(level, livingEntity, itemstack, remainingUseDuration);

        if (level instanceof ServerLevel serverlevel && livingEntity instanceof Player player) {
            var blockpos = serverlevel.findNearestMapStructure(GigTags.GIG_DUNGEONS, player.blockPosition(), 100, false);
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
                    if (CommonMod.config.enableLogging)
                        CommonMod.LOGGER.info("Distance Category: {}", distanceCategory);
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
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        if (entity instanceof Player player && !player.getCooldowns().isOnCooldown(this)) {
            animationDispatcher.sendClosingAnimation(entity, stack);
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return 1;
    }

    @Override
    public void appendHoverText(
        @NotNull ItemStack stack,
        Item.@NotNull TooltipContext context,
        @NotNull List<Component> tooltipComponents,
        @NotNull TooltipFlag tooltipFlag
    ) {
        tooltipComponents.add(
            Component.translatable("item.gigeresque.tracker.tooltip").withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.ITALIC)
        );
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
