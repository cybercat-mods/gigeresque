package mods.cybercat.gigeresque.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class BloodParticle extends TextureSheetParticle {

    private final SpriteSet spriteProvider;
    @SuppressWarnings("unused")
    private boolean reachedGround;

    BloodParticle(ClientLevel clientWorld, double d, double e, double f, double g, double h, double i, SpriteSet spriteProvider) {
        super(clientWorld, d, e, f);
        xd = g;
        yd = h;
        zd = i;
        var red = 170 / 255.0f;
        var green = 0 / 255.0f;
        var blue = 0 / 255.0f;
        var colorRed = Mth.nextFloat(random, red - 0.05f, red + 0.05f);
        setColor(colorRed, green, blue);
        quadSize *= 0.75f;
        lifetime = (int) (20.0 / ((random.nextFloat()) * 0.8 + 0.2));
        reachedGround = false;
        hasPhysics = true;
        this.spriteProvider = spriteProvider;
        setSpriteFromAge(spriteProvider);
    }

    @Override
    public void tick() {
        xo = x;
        yo = y;
        zo = z;
        if (age++ >= lifetime) remove();
        else {
            setSpriteFromAge(spriteProvider);
            if (onGround) {
                yd = 0.0;
                reachedGround = true;
            }
            this.move(xd, yd, zd);
            if (y == yo) {
                xd *= 1.1;
                zd *= 1.1;
            }
        }
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public float getQuadSize(float tickDelta) {
        return quadSize * Mth.clamp(((age) + tickDelta) / (lifetime) * 32.0f, 0.0f, 1.0f);
    }

}
