package mods.cybercat.gigeresque.common.entity.helper;

import com.google.common.collect.ImmutableList;
import mod.azuredoom.bettercrawling.common.*;
import mod.azuredoom.bettercrawling.interfaces.*;
import mods.cybercat.gigeresque.common.block.GigBlocks;
import mods.cybercat.gigeresque.common.entity.AlienEntity;
import mods.cybercat.gigeresque.common.tags.GigTags;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Rotations;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public abstract class CrawlerAlien extends AlienEntity implements IClimberEntity, IMobEntityLivingTickHook, ILivingEntityLookAtHook, IMobEntityTickHook, ILivingEntityRotationHook, ILivingEntityDataManagerHook, ILivingEntityTravelHook, IEntityMovementHook, IEntityReadWriteHook, ILivingEntityJumpHook {
    private static final UUID FOLLOW_RANGE_INCREASE_ID = UUID.fromString("9e815957-3a8e-4b65-cfbc-eba39d2a06b4");
    private static final AttributeModifier FOLLOW_RANGE_INCREASE = new AttributeModifier(FOLLOW_RANGE_INCREASE_ID,
            " Better Crawling follow range increase", 8.0D, AttributeModifier.Operation.ADDITION);
    private static final EntityDataAccessor<Float> MOVEMENT_TARGET_X;
    private static final EntityDataAccessor<Float> MOVEMENT_TARGET_Y;
    private static final EntityDataAccessor<Float> MOVEMENT_TARGET_Z;
    private static final ImmutableList<EntityDataAccessor<Optional<BlockPos>>> PATHING_TARGETS;
    private static final ImmutableList<EntityDataAccessor<Direction>> PATHING_SIDES;
    private static final EntityDataAccessor<Rotations> ROTATION_BODY;

    static {
        MOVEMENT_TARGET_X = SynchedEntityData.defineId(CrawlerAlien.class, EntityDataSerializers.FLOAT);
        MOVEMENT_TARGET_Y = SynchedEntityData.defineId(CrawlerAlien.class, EntityDataSerializers.FLOAT);
        MOVEMENT_TARGET_Z = SynchedEntityData.defineId(CrawlerAlien.class, EntityDataSerializers.FLOAT);

        ImmutableList.Builder<EntityDataAccessor<Optional<BlockPos>>> pathingTargets = ImmutableList.builder();
        ImmutableList.Builder<EntityDataAccessor<Direction>> pathingSides = ImmutableList.builder();
        for (var i = 0; i < 8; i++) {
            pathingTargets.add(
                    SynchedEntityData.defineId(CrawlerAlien.class, EntityDataSerializers.OPTIONAL_BLOCK_POS));
            pathingSides.add(SynchedEntityData.defineId(CrawlerAlien.class, EntityDataSerializers.DIRECTION));
        }
        PATHING_TARGETS = pathingTargets.build();
        PATHING_SIDES = pathingSides.build();

        ROTATION_BODY = SynchedEntityData.defineId(CrawlerAlien.class, EntityDataSerializers.ROTATIONS);
    }

    public float prevOrientationYawDelta;
    public float nextStepDistance;
    private double prevAttachmentOffsetX;
    private double prevAttachmentOffsetY;
    private double prevAttachmentOffsetZ;
    private double attachmentOffsetX;
    private double attachmentOffsetY;
    private double attachmentOffsetZ;
    private Vec3 attachmentNormal = new Vec3(0, 1, 0);
    private Vec3 prevAttachmentNormal = new Vec3(0, 1, 0);
    private float orientationYawDelta;
    private double lastAttachmentOffsetX;
    private double lastAttachmentOffsetY;
    private double lastAttachmentOffsetZ;
    private Vec3 lastAttachmentOrientationNormal = new Vec3(0, 1, 0);
    private int attachedTicks = 5;
    private Vec3 attachedSides = new Vec3(0, 0, 0);
    private boolean canClimbInWater = false;
    private boolean canClimbInLava = false;
    private boolean isTravelingInFluid = false;
    private float collisionsInclusionRange = 2.0f;
    private float collisionsSmoothingRange = 1.25f;
    private Orientation orientation;
    private Pair<Direction, Vec3> groundDirection = Pair.of(Direction.DOWN, new Vec3(0, -1, 0));
    private Orientation renderOrientation;
    private Vec3 preWalkingPosition;
    private double preMoveY;
    private Vec3 jumpDir;

    protected CrawlerAlien(EntityType<? extends AlienEntity> entityType, Level level) {
        super(entityType, level);
        this.setMaxUpStep(0.1f);
        this.orientation = this.calculateOrientation(1);
        this.groundDirection = this.getGroundDirection();
        this.moveControl = new ClimberMoveController<>(this);
        this.lookControl = new ClimberLookController<>(this);
        this.jumpControl = new ClimberJumpController<>(this);
        Objects.requireNonNull(this.getAttribute(Attributes.FOLLOW_RANGE)).addPermanentModifier(FOLLOW_RANGE_INCREASE);
    }

    @Override
    public void travel(@NotNull Vec3 vec3) {
        if (this.tickCount % 10 == 0) {
            this.refreshDimensions();
            this.updateOffsetsAndOrientation();
        }
        super.travel(vec3);
    }

    private static double calculateXOffset(AABB aabb, AABB other, double offsetX) {
        if (other.maxY > aabb.minY && other.minY < aabb.maxY && other.maxZ > aabb.minZ && other.minZ < aabb.maxZ) {
            if (offsetX > 0.0D && other.maxX <= aabb.minX) {
                var dx = aabb.minX - other.maxX;
                if (dx < offsetX) offsetX = dx;
            } else if (offsetX < 0.0D && other.minX >= aabb.maxX) {
                var dx = aabb.maxX - other.minX;
                if (dx > offsetX) offsetX = dx;
            }
        }
        return offsetX;
    }

    private static double calculateYOffset(AABB aabb, AABB other, double offsetY) {
        if (other.maxX > aabb.minX && other.minX < aabb.maxX && other.maxZ > aabb.minZ && other.minZ < aabb.maxZ) {
            if (offsetY > 0.0D && other.maxY <= aabb.minY) {
                var dy = aabb.minY - other.maxY;
                if (dy < offsetY) offsetY = dy;
            } else if (offsetY < 0.0D && other.minY >= aabb.maxY) {
                var dy = aabb.maxY - other.minY;
                if (dy > offsetY) offsetY = dy;
            }
        }
        return offsetY;
    }

    private static double calculateZOffset(AABB aabb, AABB other, double offsetZ) {
        if (other.maxX > aabb.minX && other.minX < aabb.maxX && other.maxY > aabb.minY && other.minY < aabb.maxY) {
            if (offsetZ > 0.0D && other.maxZ <= aabb.minZ) {
                var dz = aabb.minZ - other.maxZ;
                if (dz < offsetZ) offsetZ = dz;
            } else if (offsetZ < 0.0D && other.minZ >= aabb.maxZ) {
                var dz = aabb.maxZ - other.minZ;
                if (dz > offsetZ) offsetZ = dz;
            }
        }
        return offsetZ;
    }

    @Override
    public @NotNull PathNavigation createNavigation(@NotNull Level world) {
        return new BetterSpiderPathNavigator<>(this, world, false);
    }

    @Override
    public boolean shouldTrackPathingTargets() {
        return true;
    }

    @Override
    public boolean canClimbOnBlock(BlockState state, BlockPos pos) {
        return !state.is(GigTags.NEST_BLOCKS) || !state.is(GigBlocks.ACID_BLOCK) || !state.is(Blocks.WATER);
    }

    @Override
    public float getPathingMalus(BlockGetter cache, Mob entity, BlockPathTypes nodeType, BlockPos pos, Vec3i direction, Predicate<Direction> sides) {
        if (direction.getY() != 0) {
            var hasClimbableNeigbor = false;
            var offsetPos = new BlockPos.MutableBlockPos();
            for (var offset : Direction.values()) {
                if (sides.test(offset)) {
                    offsetPos.set(pos.getX() + offset.getStepX(), pos.getY() + offset.getStepY(),
                            pos.getZ() + offset.getStepZ());
                    var state = cache.getBlockState(offsetPos);
                    if (this.canClimbOnBlock(state, offsetPos)) hasClimbableNeigbor = true;
                }
            }
            if (!hasClimbableNeigbor) return -1.0f;
        }
        return entity.getPathfindingMalus(nodeType);
    }

    @Override
    public void defineSynchedData() {
        super.defineSynchedData();
        if (this.shouldTrackPathingTargets()) {
            this.entityData.define(MOVEMENT_TARGET_X, 0.0f);
            this.entityData.define(MOVEMENT_TARGET_Y, 0.0f);
            this.entityData.define(MOVEMENT_TARGET_Z, 0.0f);
            for (EntityDataAccessor<Optional<BlockPos>> pathingTarget : PATHING_TARGETS)
                this.entityData.define(pathingTarget, Optional.empty());
            for (EntityDataAccessor<Direction> pathingSide : PATHING_SIDES)
                this.entityData.define(pathingSide, Direction.DOWN);
        }
        this.entityData.define(ROTATION_BODY, new Rotations(0, 0, 0));
    }

    @Override
    public void onWrite(CompoundTag nbt) {
        nbt.putDouble("bettercrawling.AttachmentNormalX", this.attachmentNormal.x);
        nbt.putDouble("bettercrawling.AttachmentNormalY", this.attachmentNormal.y);
        nbt.putDouble("bettercrawling.AttachmentNormalZ", this.attachmentNormal.z);
        nbt.putInt("bettercrawling.AttachedTicks", this.attachedTicks);
    }

    @Override
    public void onRead(CompoundTag nbt) {
        this.prevAttachmentNormal = this.attachmentNormal = new Vec3(nbt.getDouble("bettercrawling.AttachmentNormalX"),
                nbt.getDouble("bettercrawling.AttachmentNormalY"), nbt.getDouble("bettercrawling.AttachmentNormalZ"));
        this.attachedTicks = nbt.getInt("bettercrawling.AttachedTicks");
        this.orientation = this.calculateOrientation(1);
    }

    @Override
    public boolean canClimbInWater() {
        return this.canClimbInWater;
    }

    @Override
    public void setCanClimbInWater(boolean value) {
        this.canClimbInWater = value;
    }

    @Override
    public boolean canClimbInLava() {
        return this.canClimbInLava;
    }

    @Override
    public void setCanClimbInLava(boolean value) {
        this.canClimbInLava = value;
    }

    @Override
    public float getCollisionsInclusionRange() {
        return this.collisionsInclusionRange;
    }

    @Override
    public void setCollisionsInclusionRange(float range) {
        this.collisionsInclusionRange = range;
    }

    @Override
    public float getCollisionsSmoothingRange() {
        return this.collisionsSmoothingRange;
    }

    @Override
    public void setCollisionsSmoothingRange(float range) {
        this.collisionsSmoothingRange = range;
    }

    @Override
    public float getBridgePathingMalus(Mob entity, BlockPos pos, Node fallPathPoint) {
        return -1.0f;
    }

    @Override
    public void onPathingObstructed(Direction facing) {

    }

    @Override
    public float getMovementSpeed() {
        var attribute = this.getAttribute(Attributes.MOVEMENT_SPEED); // MOVEMENT_SPEED
        return attribute != null ? (float) attribute.getValue() : 1.0f;
    }

    private void updateWalkingSide() {
        var entityBox = this.getBoundingBox();
        var closestFacingDst = Double.MAX_VALUE;
        Direction closestFacing = null;
        var weighting = new Vec3(0, 0, 0);
        var stickingDistance = this.zza != 0 ? 1.5f : 0.1f;

        for (var facing : Direction.values()) {
            if (null == facing) continue;
            var collisionBoxes = this.getCollisionBoxes(
                    entityBox.inflate(0.2f).expandTowards(facing.getStepX() * stickingDistance,
                            facing.getStepY() * stickingDistance, facing.getStepZ() * stickingDistance));
            var closestDst = Double.MAX_VALUE;

            for (var collisionBox : collisionBoxes) {
                closestDst = switch (facing) {
                    case EAST, WEST -> Math.min(closestDst, Math.abs(
                            calculateXOffset(entityBox, collisionBox, -facing.getStepX() * stickingDistance)));
                    case UP, DOWN -> Math.min(closestDst, Math.abs(
                            calculateYOffset(entityBox, collisionBox, -facing.getStepY() * stickingDistance)));
                    case NORTH, SOUTH -> Math.min(closestDst, Math.abs(
                            calculateZOffset(entityBox, collisionBox, -facing.getStepZ() * stickingDistance)));
                };
            }

            if (closestDst < closestFacingDst) {
                closestFacingDst = closestDst;
                closestFacing = facing;
            }
            if (closestDst < Double.MAX_VALUE) weighting = weighting.add(
                    new Vec3(facing.getStepX(), facing.getStepY(), facing.getStepZ()).scale(
                            1 - Math.min(closestDst, stickingDistance) / stickingDistance));
        }

        if (closestFacing == null) this.groundDirection = Pair.of(Direction.DOWN, new Vec3(0, -1, 0));
        else this.groundDirection = Pair.of(closestFacing, weighting.normalize().add(0, -0.001f, 0).normalize());
    }

    @Override
    public Pair<Direction, Vec3> getGroundDirection() {
        return this.groundDirection;
    }

    @Override
    public Direction getGroundSide() {
        return this.groundDirection.getKey();
    }

    @Override
    public Orientation getOrientation() {
        return this.orientation;
    }

    @Override
    public Orientation getRenderOrientation() {
        return this.renderOrientation;
    }

    @Override
    public void setRenderOrientation(Orientation orientation) {
        this.renderOrientation = orientation;
    }

    @Override
    public float getAttachmentOffset(Direction.Axis axis, float partialTicks) {
        return switch (axis) {
            case Y ->
                    (float) (this.prevAttachmentOffsetY + (this.attachmentOffsetY - this.prevAttachmentOffsetY) * partialTicks);
            case Z ->
                    (float) (this.prevAttachmentOffsetZ + (this.attachmentOffsetZ - this.prevAttachmentOffsetZ) * partialTicks);
            default ->
                    (float) (this.prevAttachmentOffsetX + (this.attachmentOffsetX - this.prevAttachmentOffsetX) * partialTicks);
        };
    }

    @Override
    public Vec3 onLookAt(Anchor anchor, Vec3 vec) {
        var dir = vec.subtract(this.position());
        dir = this.getOrientation().getLocal(dir);
        return dir;
    }

    @Override
    public void onTick() {
        if (!this.level().isClientSide && this.level() instanceof ServerLevel serverlevel) {
            var entityTracker = serverlevel.getChunkSource().chunkMap.entityMap.get(this.getId());

            // Prevent premature syncing of position causing overly smoothed movement
            if (entityTracker != null && entityTracker.serverEntity.tickCount % entityTracker.serverEntity.updateInterval == 0) {
                var look = this.getOrientation().getGlobal(this.getYRot(), this.getXRot());
                this.entityData.set(ROTATION_BODY, new Rotations((float) look.x, (float) look.y, (float) look.z));

                if (this.shouldTrackPathingTargets()) {
                    if (this.xxa != 0) {
                        var forwardVector = this.getOrientation().getGlobal(this.getYRot(), 0);
                        var strafeVector = this.getOrientation().getGlobal(this.getYRot() + 90.0f, 0);
                        var offset = forwardVector.scale(this.zza).add(strafeVector.scale(this.xxa)).normalize();

                        this.entityData.set(MOVEMENT_TARGET_X, (float) (this.getX() + offset.x));
                        this.entityData.set(MOVEMENT_TARGET_Y,
                                (float) (this.getY() + this.getBbHeight() * 0.5f + offset.y));
                        this.entityData.set(MOVEMENT_TARGET_Z, (float) (this.getZ() + offset.z));
                    } else {
                        this.entityData.set(MOVEMENT_TARGET_X, (float) this.getMoveControl().getWantedX());
                        this.entityData.set(MOVEMENT_TARGET_Y, (float) this.getMoveControl().getWantedY());
                        this.entityData.set(MOVEMENT_TARGET_Z, (float) this.getMoveControl().getWantedZ());
                    }
                    var path = this.getNavigation().getPath();
                    if (path != null) {
                        var i = 0;
                        for (var pathingTarget : PATHING_TARGETS) {
                            var pathingSide = PATHING_SIDES.get(i);
                            if (path.getNextNodeIndex() + i < path.getNodeCount()) {
                                var point = path.getNode(path.getNextNodeIndex() + i);
                                this.entityData.set(pathingTarget,
                                        Optional.of(new BlockPos(point.x, point.y, point.z)));
                                if (point instanceof DirectionalPathPoint dirpoint) {
                                    var dir = dirpoint.getPathSide();
                                    this.entityData.set(pathingSide, Objects.requireNonNullElse(dir, Direction.DOWN));
                                }

                            } else {
                                this.entityData.set(pathingTarget, Optional.empty());
                                this.entityData.set(pathingSide, Direction.DOWN);
                            }
                            i++;
                        }
                    } else {
                        for (var pathingTarget : PATHING_TARGETS)
                            this.entityData.set(pathingTarget, Optional.empty());
                        for (var pathingSide : PATHING_SIDES)
                            this.entityData.set(pathingSide, Direction.DOWN);
                    }
                }
            }
        }
    }

    @Override
    public void onLivingTick() {
        this.updateWalkingSide();
    }

    @Override
    @Nullable
    public Vec3 getTrackedMovementTarget() {
        if (this.shouldTrackPathingTargets())
            return new Vec3(this.entityData.get(MOVEMENT_TARGET_X), this.entityData.get(MOVEMENT_TARGET_Y),
                    this.entityData.get(MOVEMENT_TARGET_Z));
        return null;
    }

    @Override
    @Nullable
    public List<PathingTarget> getTrackedPathingTargets() {
        if (this.shouldTrackPathingTargets()) {
            List<PathingTarget> pathingTargets = new ArrayList<>(PATHING_TARGETS.size());
            var i = 0;
            for (var key : PATHING_TARGETS) {
                var pos = this.entityData.get(key).orElse(null);
                if (pos != null) pathingTargets.add(new PathingTarget(pos, this.entityData.get(PATHING_SIDES.get(i))));
                i++;
            }
            return pathingTargets;
        }

        return null;
    }

    @Override
    public float getVerticalOffset(float partialTicks) {
        return 0.075f;
    }

    private void forEachCollisonBox(AABB aabb, Shapes.DoubleLineConsumer action) {
        var minChunkX = ((Mth.floor(aabb.minX - 1.0E-7D) - 1) >> 4);
        var maxChunkX = ((Mth.floor(aabb.maxX + 1.0E-7D) + 1) >> 4);
        var minChunkZ = ((Mth.floor(aabb.minZ - 1.0E-7D) - 1) >> 4);
        var maxChunkZ = ((Mth.floor(aabb.maxZ + 1.0E-7D) + 1) >> 4);
        var width = maxChunkX - minChunkX + 1;
        var depth = maxChunkZ - minChunkZ + 1;
        var blockReaderCache = new BlockGetter[width * depth];
        var collisionReader = this.level();

        for (var cx = minChunkX; cx <= maxChunkX; cx++)
            for (var cz = minChunkZ; cz <= maxChunkZ; cz++)
                blockReaderCache[(cx - minChunkX) + (cz - minChunkZ) * width] = collisionReader.getChunkForCollisions(
                        cx, cz);

        CollisionGetter cachedCollisionReader = new CollisionGetter() {
            @Override
            public int getHeight() {
                return level().getHeight();
            }

            @Override
            public int getMinBuildHeight() {
                return level().getMinBuildHeight();
            }

            @Override
            public BlockEntity getBlockEntity(@NotNull BlockPos pos) {
                return collisionReader.getBlockEntity(pos);
            }

            @Override
            public @NotNull BlockState getBlockState(@NotNull BlockPos pos) {
                return collisionReader.getBlockState(pos);
            }

            @Override
            public @NotNull FluidState getFluidState(@NotNull BlockPos pos) {
                return collisionReader.getFluidState(pos);
            }

            @Override
            public @NotNull WorldBorder getWorldBorder() {
                return collisionReader.getWorldBorder();
            }

            @Override
            public @NotNull List<VoxelShape> getEntityCollisions(Entity entity, @NotNull AABB aabb) {
                return collisionReader.getEntityCollisions(entity, aabb);
            }

            @Override
            public BlockGetter getChunkForCollisions(int chunkX, int chunkZ) {
                return blockReaderCache[(chunkX - minChunkX) + (chunkZ - minChunkZ) * width];
            }
        };
        var shapes = cachedCollisionReader.getBlockCollisions(this, aabb);
        shapes.forEach(shape -> shape.forAllBoxes(action));
    }

    private List<AABB> getCollisionBoxes(AABB aabb) {
        List<AABB> boxes = new ArrayList<>();
        this.forEachCollisonBox(aabb,
                (minX, minY, minZ, maxX, maxY, maxZ) -> boxes.add(new AABB(minX, minY, minZ, maxX, maxY, maxZ)));
        return boxes;
    }

    @Override
    public float getBlockSlipperiness(BlockPos pos) {
        return this.level().getBlockState(pos).getBlock().getFriction() * 0.91f;
    }

    private void updateOffsetsAndOrientation() {
        var direction = this.getOrientation().getGlobal(this.getYRot(), this.getXRot());
        var isAttached = false;
        var baseStickingOffsetX = 0.0f;
        var baseStickingOffsetY = this.getVerticalOffset(1);
        var baseStickingOffsetZ = 0.0f;
        var baseOrientationNormal = new Vec3(0, 1, 0);

        if (!this.isTravelingInFluid && this.onGround() && this.getVehicle() == null) {
            var p = this.position();
            var s = p.add(0, this.getBbHeight() * 0.5f, 0);
            var inclusionBox = new AABB(s.x, s.y, s.z, s.x, s.y, s.z).inflate(this.collisionsInclusionRange);
            var attachmentPoint = CollisionSmoothingUtil.findClosestPoint(
                    consumer -> this.forEachCollisonBox(inclusionBox, consumer), s, this.attachmentNormal.scale(-1),
                    this.collisionsSmoothingRange, 1.0f, 0.001f, 20, 0.05f, s);
            var entityBox = this.getBoundingBox();

            if (attachmentPoint != null && !this.isDeadOrDying()) {
                var attachmentPos = attachmentPoint.getLeft();
                var dx = Math.max(entityBox.minX - attachmentPos.x, attachmentPos.x - entityBox.maxX);
                var dy = Math.max(entityBox.minY - attachmentPos.y, attachmentPos.y - entityBox.maxY);
                var dz = Math.max(entityBox.minZ - attachmentPos.z, attachmentPos.z - entityBox.maxZ);

                if (Math.max(dx, Math.max(dy, dz)) < 0.5f) {
                    if (!this.isDeadOrDying()) isAttached = true;
                    this.lastAttachmentOffsetX = Mth.clamp(attachmentPos.x - p.x, -this.getBbWidth() / 2,
                            this.getBbWidth() / 2);
                    this.lastAttachmentOffsetY = Mth.clamp(attachmentPos.y - p.y, 0, this.getBbHeight());
                    this.lastAttachmentOffsetZ = Mth.clamp(attachmentPos.z - p.z, -this.getBbWidth() / 2,
                            this.getBbWidth() / 2);
                    this.lastAttachmentOrientationNormal = attachmentPoint.getRight();
                }
            }
        }

        this.prevAttachmentOffsetX = this.attachmentOffsetX;
        this.prevAttachmentOffsetY = this.attachmentOffsetY;
        this.prevAttachmentOffsetZ = this.attachmentOffsetZ;
        this.prevAttachmentNormal = this.attachmentNormal;
        var attachmentBlend = this.attachedTicks * 0.2f;
        this.attachmentOffsetX = baseStickingOffsetX + (this.lastAttachmentOffsetX - baseStickingOffsetX) * attachmentBlend;
        this.attachmentOffsetY = baseStickingOffsetY + (this.lastAttachmentOffsetY - baseStickingOffsetY) * attachmentBlend;
        this.attachmentOffsetZ = baseStickingOffsetZ + (this.lastAttachmentOffsetZ - baseStickingOffsetZ) * attachmentBlend;
        this.attachmentNormal = baseOrientationNormal.add(
                this.lastAttachmentOrientationNormal.subtract(baseOrientationNormal).scale(
                        attachmentBlend)).normalize();

        if (!isAttached) this.attachedTicks = Math.max(0, this.attachedTicks - 1);
        else this.attachedTicks = Math.min(5, this.attachedTicks + 1);
        if (this.isDeadOrDying()) this.attachedTicks = 0;

        this.orientation = this.calculateOrientation(1);
        var newRotations = this.getOrientation().getLocalRotation(direction);
        var yawDelta = newRotations.getLeft() - this.getYRot();
        var pitchDelta = newRotations.getRight() - this.getXRot();

        this.prevOrientationYawDelta = this.orientationYawDelta;
        this.orientationYawDelta = yawDelta;

        this.yRot = Mth.wrapDegrees(this.yRot + yawDelta);
        this.yRotO = this.wrapAngleInRange(this.yRotO/* + yawDelta */, this.yRot);
        this.lerpYRot = Mth.wrapDegrees(this.lerpYRot + yawDelta);

        this.yBodyRot = Mth.wrapDegrees(this.yBodyRot + yawDelta);
        this.yBodyRotO = this.wrapAngleInRange(this.yBodyRotO/* + yawDelta */, this.yBodyRot);

        this.yHeadRot = Mth.wrapDegrees(this.yHeadRot + yawDelta);
        this.yHeadRotO = this.wrapAngleInRange(this.yHeadRotO/* + yawDelta */, this.yHeadRot);
        this.lyHeadRot = Mth.wrapDegrees(this.lyHeadRot + yawDelta);

        this.xRot = Mth.wrapDegrees(this.xRot + pitchDelta);
        this.xRotO = this.wrapAngleInRange(this.xRotO/* + pitchDelta */, this.xRot);
        this.lerpXRot = Mth.wrapDegrees(this.lerpXRot + pitchDelta);
    }

    private float wrapAngleInRange(float angle, float target) {
        while (target - angle < -180.0F) angle -= 360.0F;
        while (target - angle >= 180.0F) angle += 360.0F;
        return angle;
    }

    @Override
    public Orientation calculateOrientation(float partialTicks) {
        var attachmentNormal = this.prevAttachmentNormal.add(
                this.attachmentNormal.subtract(this.prevAttachmentNormal).scale(partialTicks));
        var localZ = new Vec3(0, 0, 1);
        var localY = new Vec3(0, 1, 0);
        var localX = new Vec3(1, 0, 0);
        var componentZ = (float) localZ.dot(attachmentNormal);
        float componentY;
        var componentX = (float) localX.dot(attachmentNormal);
        var yaw = (float) Math.toDegrees(Mth.atan2(componentX, componentZ));

        localZ = new Vec3(Math.sin(Math.toRadians(yaw)), 0, Math.cos(Math.toRadians(yaw)));
        localY = new Vec3(0, 1, 0);
        localX = new Vec3(Math.sin(Math.toRadians(yaw - 90)), 0, Math.cos(Math.toRadians(yaw - 90)));

        componentZ = (float) localZ.dot(attachmentNormal);
        componentY = (float) localY.dot(attachmentNormal);
        componentX = (float) localX.dot(attachmentNormal);

        var pitch = (float) Math.toDegrees(
                Mth.atan2(Mth.sqrt(componentX * componentX + componentZ * componentZ), componentY));
        var m = new Matrix4f();

        m.multiply(new Matrix4f((float) Math.toRadians(yaw), 0, 1, 0));
        m.multiply(new Matrix4f((float) Math.toRadians(pitch), 1, 0, 0));
        m.multiply(
                new Matrix4f((float) Math.toRadians(Math.signum(0.5f - componentY - componentZ - componentX) * yaw), 0,
                        1, 0));

        localZ = m.multiply(new Vec3(0, 0, -1));
        localY = m.multiply(new Vec3(0, 1, 0));
        localX = m.multiply(new Vec3(1, 0, 0));

        return new Orientation(attachmentNormal, localZ, localY, localX, componentZ, componentY, componentX, yaw,
                pitch);
    }

    @Override
    public float getTargetYaw(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        return (float) this.lerpYRot;
    }

    @Override
    public float getTargetPitch(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        return (float) this.lerpXRot;
    }

    @Override
    public float getTargetHeadYaw(float yaw, int rotationIncrements) {
        return (float) this.lyHeadRot;
    }

    @Override
    public void onNotifyDataManagerChange(EntityDataAccessor<?> key) {
        if (ROTATION_BODY.equals(key)) {
            var rotation = this.entityData.get(ROTATION_BODY);
            var look = new Vec3(rotation.getX(), rotation.getY(), rotation.getZ());
            var rotations = this.getOrientation().getLocalRotation(look);
            this.lerpYRot = rotations.getLeft();
            this.lerpXRot = rotations.getRight();
        }
    }

    private double getGravity() {
        if (this.isNoGravity()) return 0;
        var gravity = 0.08d;
        var isFalling = this.getDeltaMovement().y <= 0.0D;
        if (isFalling && this.hasEffect(MobEffects.SLOW_FALLING)) gravity = 0.1D;
        return gravity;
    }

    private Vec3 getStickingForce(Pair<Direction, Vec3> walkingSide) {
        var uprightness = Math.max(this.attachmentNormal.y, 0);
        var stickingForce = this.getGravity() * uprightness + 0.08D * (1 - uprightness);
        return walkingSide.getRight().scale(stickingForce);
    }

    @Override
    public void setJumpDirection(Vec3 dir) {
        this.jumpDir = dir != null ? dir.normalize() : null;
    }

    @Override
    public boolean onJump() {
        if (this.jumpDir != null) {
            var jumpStrength = this.getJumpPower();
            if (this.hasEffect(MobEffects.JUMP))
                jumpStrength += 0.1F * (float) (Objects.requireNonNull(this.getEffect(MobEffects.JUMP)).getAmplifier() + 1);
            var motion = this.getDeltaMovement();
            var orthogonalMotion = this.jumpDir.scale(this.jumpDir.dot(motion));
            var tangentialMotion = motion.subtract(orthogonalMotion);
            this.setDeltaMovement(tangentialMotion.x + this.jumpDir.x * jumpStrength,
                    tangentialMotion.y + this.jumpDir.y * jumpStrength,
                    tangentialMotion.z + this.jumpDir.z * jumpStrength);
            if (this.isSprinting()) this.setDeltaMovement(
                    this.getDeltaMovement().add(this.getOrientation().getGlobal(this.yRot, 0).scale(0.2f)));
            this.hasImpulse = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean onTravel(Vec3 relative, boolean pre) {
        if (pre) {
            var canTravel = this.isEffectiveAi() || this.isControlledByLocalInstance();
            this.isTravelingInFluid = false;
            var fluidState = this.level().getFluidState(this.blockPosition());

            if (!this.canClimbInWater && this.isInWater() && this.isAffectedByFluids() && !this.canStandOnFluid(
                    fluidState)) {
                this.isTravelingInFluid = true;
                if (canTravel) return false;
            } else if (!this.canClimbInLava && this.isInLava() && this.isAffectedByFluids() && !this.canStandOnFluid(
                    fluidState)) {
                this.isTravelingInFluid = true;
                if (canTravel) return false;
            } else if (canTravel) this.travelOnGround(relative);
            if (!canTravel) this.calculateEntityAnimation(true);
            this.updateOffsetsAndOrientation();
            return true;
        } else {
            this.updateOffsetsAndOrientation();
            return false;
        }
    }

    private float getRelevantMoveFactor(float slipperiness) {
        return this.onGround() ? this.getSpeed() * (0.16277136F / (slipperiness * slipperiness * slipperiness)) : this.getFlyingSpeed();
    }

    private void travelOnGround(Vec3 relative) {
        var forwardVector = this.getOrientation().getGlobal(this.yRot, 0);
        var strafeVector = this.getOrientation().getGlobal(this.yRot + 90.0f, 0);
        var upVector = this.getOrientation().getGlobal(this.yRot, -90.0f);
        var stickingForce = this.getStickingForce(this.getGroundDirection());
        var isFalling = this.getDeltaMovement().y <= 0.0D;

        if (isFalling && this.hasEffect(MobEffects.SLOW_FALLING)) this.fallDistance = 0;

        var forward = (float) relative.z;
        var strafe = (float) relative.x;

        if (forward != 0 || strafe != 0) {
            var slipperiness = 0.91f;

            if (this.onGround()) slipperiness = this.getBlockSlipperiness(
                    new BlockPos(this.blockPosition()).relative(this.getGroundDirection().getLeft()));

            var f = forward * forward + strafe * strafe;
            if (f >= 1.0E-4F) {
                f = Math.max(Mth.sqrt(f), 1.0f);
                f = this.getRelevantMoveFactor(slipperiness) / f;
                forward *= f;
                strafe *= f;
                var movementOffset = new Vec3(forwardVector.x * forward + strafeVector.x * strafe,
                        forwardVector.y * forward + strafeVector.y * strafe,
                        forwardVector.z * forward + strafeVector.z * strafe);
                var px = this.getX();
                var py = this.getY();
                var pz = this.getZ();
                var motion = this.getDeltaMovement();
                var aabb = this.getBoundingBox();
                // Probe actual movement vector
                this.move(MoverType.SELF, movementOffset);
                var movementDir = new Vec3(this.getX() - px, this.getY() - py, this.getZ() - pz).normalize();
                this.setBoundingBox(aabb);
                this.setLocationFromBoundingbox();
                this.setDeltaMovement(motion);
                // Probe collision normal
                var probeVector = new Vec3(Math.abs(movementDir.x) < 0.001D ? -Math.signum(upVector.x) : 0,
                        Math.abs(movementDir.y) < 0.001D ? -Math.signum(upVector.y) : 0,
                        Math.abs(movementDir.z) < 0.001D ? -Math.signum(upVector.z) : 0).normalize().scale(0.0001D);
                this.move(MoverType.SELF, probeVector);
                var collisionNormal = new Vec3(
                        Math.abs(this.getX() - px - probeVector.x) > 0.000001D ? Math.signum(-probeVector.x) : 0,
                        Math.abs(this.getY() - py - probeVector.y) > 0.000001D ? Math.signum(-probeVector.y) : 0,
                        Math.abs(this.getZ() - pz - probeVector.z) > 0.000001D ? Math.signum(
                                -probeVector.z) : 0).normalize();
                this.setBoundingBox(aabb);
                this.setLocationFromBoundingbox();
                this.setDeltaMovement(motion);
                // Movement vector projected to surface
                var surfaceMovementDir = movementDir.subtract(
                        collisionNormal.scale(collisionNormal.dot(movementDir))).normalize();
                var isInnerCorner = Math.abs(collisionNormal.x) + Math.abs(collisionNormal.y) + Math.abs(
                        collisionNormal.z) > 1.0001f;
                // Only project movement vector to surface if not moving across inner corner, otherwise it'd get stuck in the corner
                if (!isInnerCorner) movementDir = surfaceMovementDir;
                // Nullify sticking force along movement vector projected to surface
                stickingForce = stickingForce.subtract(
                        surfaceMovementDir.scale(surfaceMovementDir.normalize().dot(stickingForce)));
                this.setDeltaMovement(
                        this.getDeltaMovement().add(movementDir.scale(Mth.sqrt(forward * forward + strafe * strafe))));
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().add(stickingForce));

        var px = this.getX();
        var py = this.getY();
        var pz = this.getZ();
        var motion = this.getDeltaMovement();
        this.move(MoverType.SELF, motion);
        Vec3 prevAttachedSides = this.attachedSides;
        this.attachedSides = new Vec3(Math.abs(this.getX() - px - motion.x) > 0.001D ? -Math.signum(motion.x) : 0,
                Math.abs(this.getY() - py - motion.y) > 0.001D ? -Math.signum(motion.y) : 0,
                Math.abs(this.getZ() - pz - motion.z) > 0.001D ? -Math.signum(motion.z) : 0);
        var slipperiness = 0.91f;

        if (this.onGround()) {
            this.fallDistance = 0;
            slipperiness = this.getBlockSlipperiness(new BlockPos(blockPosition()).relative(groundDirection.getLeft()));
        }

        motion = this.getDeltaMovement();
        var orthogonalMotion = upVector.scale(upVector.dot(motion));
        var tangentialMotion = motion.subtract(orthogonalMotion);

        this.setDeltaMovement(tangentialMotion.x * slipperiness + orthogonalMotion.x * 0.98f,
                tangentialMotion.y * slipperiness + orthogonalMotion.y * 0.98f,
                tangentialMotion.z * slipperiness + orthogonalMotion.z * 0.98f);

        var detachedX = this.attachedSides.x != prevAttachedSides.x && Math.abs(this.attachedSides.x) < 0.001D;
        var detachedY = this.attachedSides.y != prevAttachedSides.y && Math.abs(this.attachedSides.y) < 0.001D;
        var detachedZ = this.attachedSides.z != prevAttachedSides.z && Math.abs(this.attachedSides.z) < 0.001D;

        if (detachedX || detachedY || detachedZ) {
            var stepHeight = this.maxUpStep();
            this.setMaxUpStep(0);
            var prevOnGround = this.onGround();
            var prevCollidedHorizontally = this.horizontalCollision;
            var prevCollidedVertically = this.verticalCollision;
            // Offset so that AABB is moved above the new surface
            this.move(MoverType.SELF,
                    new Vec3(detachedX ? -prevAttachedSides.x * 0.25f : 0, detachedY ? -prevAttachedSides.y * 0.25f : 0,
                            detachedZ ? -prevAttachedSides.z * 0.25f : 0));
            var axis = prevAttachedSides.normalize();
            var attachVector = upVector.scale(-1);
            attachVector = attachVector.subtract(axis.scale(axis.dot(attachVector)));

            if (Math.abs(attachVector.x) > Math.abs(attachVector.y) && Math.abs(attachVector.x) > Math.abs(
                    attachVector.z)) attachVector = new Vec3(Math.signum(attachVector.x), 0, 0);
            else if (Math.abs(attachVector.y) > Math.abs(attachVector.z))
                attachVector = new Vec3(0, Math.signum(attachVector.y), 0);
            else attachVector = new Vec3(0, 0, Math.signum(attachVector.z));

            var attachDst = motion.length() + 0.1f;
            var aabb = this.getBoundingBox();
            motion = this.getDeltaMovement();

            // Offset AABB towards new surface until it touches
            for (var i = 0; i < 2 && !this.onGround(); i++)
                this.move(MoverType.SELF, attachVector.scale(attachDst));

            this.setMaxUpStep(stepHeight);

            // Attaching failed, fall back to previous position
            if (!this.onGround()) {
                this.setBoundingBox(aabb);
                this.setLocationFromBoundingbox();
                this.setDeltaMovement(motion);
                this.setOnGround(prevOnGround);
                this.horizontalCollision = prevCollidedHorizontally;
                this.verticalCollision = prevCollidedVertically;
            } else this.setDeltaMovement(Vec3.ZERO);
        }

        this.calculateEntityAnimation(true);
    }

    @Override
    public boolean onMove(MoverType type, Vec3 pos, boolean pre) {
        if (pre) {
            this.preWalkingPosition = this.position();
            this.preMoveY = this.getY();
        } else {
            if (Math.abs(this.getY() - this.preMoveY - pos.y) > 0.000001D)
                this.setDeltaMovement(this.getDeltaMovement().multiply(1, 0, 1));

            this.setOnGround(this.horizontalCollision || this.verticalCollision);
        }

        return false;
    }

    @Override
    public BlockPos getAdjustedOnPosition(BlockPos onPosition) {
        var verticalOffset = this.getVerticalOffset(1);
        var x = Mth.floor(
                this.getX() + this.attachmentOffsetX - (float) this.attachmentNormal.x * (verticalOffset + 0.2f));
        var y = Mth.floor(
                this.getY() + this.attachmentOffsetY - (float) this.attachmentNormal.y * (verticalOffset + 0.2f));
        var z = Mth.floor(
                this.getZ() + this.attachmentOffsetZ - (float) this.attachmentNormal.z * (verticalOffset + 0.2f));
        var pos = new BlockPos(x, y, z);

        if (this.level().isEmptyBlock(pos) && this.attachmentNormal.y < 0.0f) {
            var posDown = pos.below();
            var stateDown = this.level().getBlockState(pos.below());

            if (stateDown.is(BlockTags.FENCES) || stateDown.is(
                    BlockTags.WALLS) || stateDown.getBlock() instanceof FenceGateBlock) return posDown;
        }

        return pos;
    }

    @Override
    public boolean getAdjustedCanTriggerWalking(boolean canTriggerWalking) {
        if (this.preWalkingPosition != null && this.canClimberTriggerWalking() && !this.isPassenger()) {
            var moved = this.position().subtract(this.preWalkingPosition);
            this.preWalkingPosition = null;
            var pos = this.getOnPos();
            var state = this.level().getBlockState(pos);
            var dx = moved.x;
            var dy = moved.y;
            var dz = moved.z;
            var tangentialMovement = moved.subtract(this.attachmentNormal.scale(this.attachmentNormal.dot(moved)));

            this.walkDist = (float) (this.walkDist + tangentialMovement.length() * 0.6D);
            this.moveDist = (float) (this.moveDist + Math.sqrt(dx * dx + dy * dy + dz * dz) * 0.6D);

            if (this.moveDist > this.nextStepDistance && !state.isAir()) {
                this.nextStepDistance = this.nextStep();
                this.playStepSound(pos, state);
            } else if (state.isAir()) this.processFlappingMovement();
        }

        return false;
    }

    @Override
    public boolean canClimberTriggerWalking() {
        return true;
    }

    public void setLocationFromBoundingbox() {
        AABB axisalignedbb = this.getBoundingBox();
        this.setPosRaw((axisalignedbb.minX + axisalignedbb.maxX) / 2.0D, axisalignedbb.minY,
                (axisalignedbb.minZ + axisalignedbb.maxZ) / 2.0D);
    }

}