package mods.cybercat.gigeresque.mixins.client.render;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import mods.cybercat.gigeresque.common.status.effect.GigStatusEffects;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Mixin(EffectRenderingInventoryScreen.class)
public abstract class EffectRenderingMixin {

    @ModifyExpressionValue(
            method = "renderBackgrounds",
            at = @At(value = "INVOKE", target = "Ljava/lang/Iterable;iterator()Ljava/util/Iterator;")
    )
    private Iterator<MobEffectInstance> filterEffectsBackgrounds(Iterator<MobEffectInstance> iterator) {
        List<MobEffectInstance> filteredList = StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false)
                .filter(effect -> !effect.is(GigStatusEffects.DUNGEON_EFFECT))
                .collect(Collectors.toList());

        return filteredList.iterator();
    }

    @ModifyExpressionValue(
            method = "renderIcons",
            at = @At(value = "INVOKE", target = "Ljava/lang/Iterable;iterator()Ljava/util/Iterator;")
    )
    private Iterator<MobEffectInstance> filterEffectsIcons(Iterator<MobEffectInstance> iterator) {
        List<MobEffectInstance> filteredList = StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false)
                .filter(effect -> !effect.is(GigStatusEffects.DUNGEON_EFFECT))
                .collect(Collectors.toList());

        return filteredList.iterator();
    }

    @ModifyExpressionValue(
            method = "renderLabels",
            at = @At(value = "INVOKE", target = "Ljava/lang/Iterable;iterator()Ljava/util/Iterator;")
    )
    private Iterator<MobEffectInstance> filterEffectsLabels(Iterator<MobEffectInstance> iterator) {
        List<MobEffectInstance> filteredList = StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false)
                .filter(effect -> !effect.is(GigStatusEffects.DUNGEON_EFFECT))
                .collect(Collectors.toList());

        return filteredList.iterator();
    }
}
