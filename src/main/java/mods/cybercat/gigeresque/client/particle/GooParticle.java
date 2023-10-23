package mods.cybercat.gigeresque.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;

public class GooParticle extends TextureSheetParticle {
    private final SpriteSet spriteProvider;
    private boolean reachedGround;

    public GooParticle(ClientLevel clientWorld, double d, double e, double f, double g, double h, double i, SpriteSet spriteProvider) {
        super(clientWorld, d, e, f);
        xd = g;
        yd = h;
        zd = i;
        setColor(0, 0, 0);
        quadSize *= 0.75f;
        lifetime = (int) (20.0 / (((double) random.nextFloat()) * 0.8 + 0.2));
        reachedGround = false;
        hasPhysics = false;
        this.spriteProvider = spriteProvider;
        setSpriteFromAge(spriteProvider);
    }

    @Override
    public void tick() {
        xo = x;
        yo = y;
        zo = z;
        if (age++ >= lifetime)
            remove();
        else {
            setSpriteFromAge(spriteProvider);
            if (onGround) {
                yd = 0.0;
                reachedGround = true;
            }
            if (reachedGround)
                yd += 0.002;
            this.move(xd, yd, zd);
            if (y == yo) {
                xd *= 1.1;
                zd *= 1.1;
            }
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public float getQuadSize(float tickDelta) {
        return quadSize * Mth.clamp((((float) age) + tickDelta) / ((float) lifetime) * 32.0f, 0.0f, 1.0f);
    }

}
