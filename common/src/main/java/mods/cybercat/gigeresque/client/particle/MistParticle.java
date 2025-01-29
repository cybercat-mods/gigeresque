package mods.cybercat.gigeresque.client.particle;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import mods.cybercat.gigeresque.Constants;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * This class is licensed under the MIT License.
 * See the full license here: <a href="https://github.com/PigCart/particle-rain/blob/main/LICENSE">MIT License</a>.
 * <br>
 * This is a modified version of the original class:
 * <a href="https://github.com/PigCart/particle-rain/blob/1.21/src/main/java/pigcart/particlerain/particle/GroundFogParticle.java">
 * GroundFogParticle.java</a>.
 * <br>
 * Credit to PigCart and the Particle Rain project for the original implementation.
 */
public class MistParticle extends TextureSheetParticle {

    private float xdxd;
    private float zdzd;
    protected BlockPos.MutableBlockPos pos;
    private static final float FADE_DURATION = 40f;
    private float targetAlpha = 0.6f;
    boolean shouldFadeOut = false;

    public MistParticle(ClientLevel level, double x, double y, double z, SpriteSet provider) {
        super(level, x, y, z);
        this.setSize(0.0001F, 0.0001F);
        this.lifetime = 30000;
        this.alpha = 0.0F;
        this.pos = new BlockPos.MutableBlockPos(x, y, z);
        Constants.particleCount++;
        this.hasPhysics = true;

        this.setSprite(provider.get(level.getRandom()));
        this.lifetime = 30000;
        this.quadSize = 1.5f;
        this.setColor(0.678f, 0.847f, 0.902f);

        this.roll = level.random.nextFloat() * Mth.PI;
        this.oRoll = this.roll;

        this.xdxd = (this.random.nextFloat() - 0.5F) / 100;
        this.zdzd = (this.random.nextFloat() - 0.5F) / 100;
    }

    @Override
    public void tick() {
        super.tick();
        this.pos.set(this.x, this.y - 0.2, this.z);
        this.removeIfOOB();
        if (shouldFadeOut) {
            fadeOut();
        } else {
            fadeIn();
        }
        if (this.onGround) this.remove();

        this.xd = this.xdxd;
        this.zd = this.zdzd;
        this.gravity = 0.0002F;
    }

    public void fadeIn() {
        if (age < FADE_DURATION) {
            float progress = age / FADE_DURATION;
            this.alpha = Mth.lerp(progress, 0, targetAlpha);
            this.quadSize = Mth.lerp(progress, 0, 1.5f);
        }
    }

    public void fadeOut() {
        float progress = Math.min((age - (lifetime - FADE_DURATION)) / FADE_DURATION, 1.0f);
        if (progress > 0) {
            this.alpha = Mth.lerp(progress, targetAlpha, 0);
            this.quadSize = Mth.lerp(progress, 1.5f, 0);
            if (progress >= 1.0f) {
                remove();
            }
        }
    }

    @Override
    public void remove() {
        if (this.isAlive()) {
            Constants.particleCount--;
        }
        super.remove();
    }

    void removeIfOOB() {
        Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
        if (cameraEntity == null || cameraEntity.distanceToSqr(this.x, this.y, this.z) > Mth.square(25)) {
            shouldFadeOut = true;
        }
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float f) {
        Vec3 camPos = camera.getPosition();
        float x = (float) (Mth.lerp(f, this.xo, this.x) - camPos.x());
        float y = (float) (Mth.lerp(f, this.yo, this.y) - camPos.y());
        float z = (float) (Mth.lerp(f, this.zo, this.z) - camPos.z());
        Vector3f localPos = new Vector3f(x, y, z);

        // rotate particle around y axis to face player
        Quaternionf quaternion = Axis.YP.rotation((float) Math.atan2(x, z) + Mth.PI);
        // rotate particle by angle between y axis and camera location
        float yAngle = (float) Math.asin(y / localPos.length());
        quaternion.rotateX(yAngle);
        quaternion.rotateZ((float) Math.atan2(x, z));
        // the z rotation doubles up on the -y axis instead of negating it like the positive axis. idk how to fix
        // for now we remove them before it gets to look too weird
        if (yAngle < -1) shouldFadeOut = true;

        quaternion.rotateZ(Mth.lerp(f, this.oRoll, this.roll));
        this.renderRotatedQuad(vertexConsumer, quaternion, x, y, z, f);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

}
