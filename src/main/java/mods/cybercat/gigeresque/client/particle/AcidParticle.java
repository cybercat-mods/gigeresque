package mods.cybercat.gigeresque.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class AcidParticle extends TextureSheetParticle {

    protected final SpriteSet spriteProvider;

    protected boolean reachedGround;

    public AcidParticle(ClientLevel clientWorld, double d, double e, double f, double g, double h, double i, SpriteSet spriteProvider) {
        super(clientWorld, d, e, f);
        xd = g;
        yd = h;
        zd = i;
        var red = 160 / 255.0f;
        var green = 158 / 255.0f;
        var blue = 9 / 255.0f;
        var colorRed = Mth.nextFloat(random, red - 0.05f, red + 0.05f);
        var colorGreen = Mth.nextFloat(random, green - 0.05f, green + 0.05f);
        var colorBlue = Mth.nextFloat(random, blue - 0.015f, blue + 0.015f);
        this.setColor(colorRed, colorGreen, colorBlue);
        this.gravity = 3.0E-6F;
        this.quadSize *= 0.75f;
        this.lifetime = (int) (10.0 / ((random.nextFloat()) * 0.8 + 0.2));
        this.reachedGround = false;
        this.hasPhysics = true;
        this.spriteProvider = spriteProvider;
        this.setSpriteFromAge(spriteProvider);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ < this.lifetime && this.alpha > 0.0F) {
            this.xd += this.random.nextFloat() / 5000.0F * (this.random.nextBoolean() ? 1 : -1);
            this.zd += this.random.nextFloat() / 5000.0F * (this.random.nextBoolean() ? 1 : -1);
            this.yd -= this.gravity;
            this.move(this.xd, this.yd, this.zd);
            if (this.age >= this.lifetime && this.alpha > 0.01F)
                this.alpha -= 0.015F;
        } else
            this.remove();
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
