package com.bvanseg.gigeresque.common.entity;

import com.bvanseg.gigeresque.ConstantsJava;
import com.bvanseg.gigeresque.common.block.AcidBlockJava;
import com.bvanseg.gigeresque.common.block.BlocksJava;
import com.bvanseg.gigeresque.common.util.DamageSourceUtils;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AlienEntityJava extends HostileEntity {
    protected int getAcidDiameter() {
        return 3;
    }

    protected AlienEntityJava(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        setPathfindingPenalty(PathNodeType.DANGER_FIRE, 16.0f);
        setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, -1.0f);

        navigation.setCanSwim(true);
    }

    @Override
    public void tick() {
        super.tick();

        if (!world.isClient && world.getBlockState(getBlockPos()).getBlock() == BlocksJava.NEST_RESIN && this.age % ConstantsJava.TPS == 0) {
            this.heal(0.0833f);
        }
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    private void generateAcidPool(int xOffset, int zOffset) {
        BlockPos pos = this.getBlockPos().add(xOffset, 0, zOffset);
        BlockState posState = world.getBlockState(pos);

        BlockState newState = BlocksJava.ACID_BLOCK.getDefaultState();

        if (posState.getBlock() == Blocks.WATER) {
            newState = newState.with(Properties.WATERLOGGED, true);
        }

        if (!(posState.getBlock() instanceof AirBlock) && !(posState.getBlock() instanceof FluidBlock) && !(posState.getBlock() instanceof TorchBlock)) {
            return;
        }
        world.setBlockState(pos, newState);
    }

    @Override
    public void onDeath(DamageSource source) {
        if (DamageSourceUtils.isDamageSourceNotPuncturing(source)) {
            super.onDeath(source);
            return;
        }

        if (!this.world.isClient) {
            if (getAcidDiameter() == 1) {
                generateAcidPool(0, 0);
            } else {
                int radius = (getAcidDiameter() - 1) / 2;

                for (int x = -radius; x <= radius; x++) {
                    for (int z = -radius; z <= radius; z++) {
                        generateAcidPool(x, z);
                    }
                }
            }
        }
        super.onDeath(source);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!this.world.isClient) {
            Entity attacker = source.getAttacker();
            if (attacker != null) {
                if (attacker instanceof LivingEntity) {
                    this.brain.remember(MemoryModuleType.ATTACK_TARGET, (LivingEntity) attacker);
                }
            }
        }

        if (DamageSourceUtils.isDamageSourceNotPuncturing(source)) return super.damage(source, amount);

        if (!this.world.isClient) {
            int acidThickness = this.getHealth() < (this.getMaxHealth() / 2) ? 1 : 0;

            if (this.getHealth() < (this.getMaxHealth() / 4)) {
                acidThickness += 1;
            }

            if (amount >= 5) {
                acidThickness += 1;
            }

            if (amount > (this.getMaxHealth() / 10)) {
                acidThickness += 1;
            }

            if (acidThickness == 0) return super.damage(source, amount);

            BlockState newState = BlocksJava.ACID_BLOCK.getDefaultState().with(AcidBlockJava.THICKNESS, acidThickness);

            if (this.getBlockStateAtPos().getBlock() == Blocks.WATER) {
                newState = newState.with(Properties.WATERLOGGED, true);
            }

            world.setBlockState(this.getBlockPos(), newState);
        }

        return super.damage(source, amount);
    }
}
