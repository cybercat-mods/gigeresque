package mods.cybercat.gigeresque.mixins;

import mods.cybercat.gigeresque.common.fluid.BlackFluid;
import mods.cybercat.gigeresque.hacky.BlackFluidClientExtensions;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Consumer;

@Mixin(BlackFluid.class)
public abstract class FluidMixin extends FlowingFluid {

    @Override
    public @NotNull FluidType getFluidType() {
        return new FluidType(FluidType.Properties.create()
                .descriptionId("block.gigeresque.black_fuild_block").canSwim(false)
                .canDrown(false).pathType(PathType.LAVA).adjacentPathType(null)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_EMPTY)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .lightLevel(15).density(3000).viscosity(6000)) {
            @Override
            public boolean canConvertToSource(@NotNull FluidState state, @NotNull LevelReader reader, @NotNull BlockPos pos) {
                if (reader instanceof Level level) {
                    return level.getGameRules().getBoolean(GameRules.RULE_WATER_SOURCE_CONVERSION);
                } else {
                    return super.canConvertToSource(state, reader, pos);
                }
            }

            @Override
            public double motionScale(@NotNull Entity entity) {
                return 0.0023333333333333335;
            }

            @Override
            public void setItemMovement(@NotNull ItemEntity entity) {
                Vec3 vec3 = entity.getDeltaMovement();
                entity.setDeltaMovement(vec3.x * 0.949999988079071, vec3.y + (vec3.y < 0.05999999865889549 ? 5.0E-4F : 0.0F), vec3.z * 0.949999988079071);
            }

            @Override
            public void initializeClient(@NotNull Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(new BlackFluidClientExtensions());
            }
        };
    }
}