package com.bvanseg.gigeresque.mixins;

import com.bvanseg.gigeresque.CustomSpawnGroup;
import net.minecraft.entity.SpawnGroup;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Boston Vanseghi
 */
@Mixin(SpawnGroup.class)
public class SpawnGroupMixin {
    @SuppressWarnings("InvokerTarget")
    @Invoker("<init>")
    private static SpawnGroup newSpawnGroup(
            String internalName,
            int internalId,
            String name,
            int spawnCap,
            boolean peaceful,
            boolean rare,
            int immediateDespawnRange
    ) {
        throw new AssertionError();
    }

    @SuppressWarnings("ShadowTarget")
    @Shadow
    private static @Final
    @Mutable
    SpawnGroup[] field_6301;

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "<clinit>", at = @At(value = "FIELD", opcode = Opcodes.PUTSTATIC, target = "Lnet/minecraft/entity/SpawnGroup;field_6301:[Lnet/minecraft/entity/SpawnGroup;", shift = At.Shift.AFTER))
    private static void addCustomSpawnGroup(CallbackInfo ci) {
        var spawnGroups = new ArrayList<>(Arrays.asList(field_6301));
        var last = spawnGroups.get(spawnGroups.size() - 1);

        // This means our code will still work if other mods or Mojang add more spawn groups!
        var alien = newSpawnGroup(
                "ALIEN",
                last.ordinal() + 1,
                "alien",
                70,
                false,
                false,
                128
        );

        CustomSpawnGroup.ALIEN = alien;
        spawnGroups.add(alien);
        field_6301 = spawnGroups.toArray(new SpawnGroup[0]);
    }
}
