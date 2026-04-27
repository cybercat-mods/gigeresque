package mods.cybercat.gigeresque.client.entity.render.helper;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import mods.cybercat.gigeresque.CommonMod;

/**
 * Datapack-driven version of the original head-offset table.
 * <p>
 * Datapack location: {@code data/<namespace>/gigeresque_head_offsets/<entity>.json}
 * <p>
 * Each JSON file looks like:
 *
 * <pre>{@code
 * {
 *   "entity": "minecraft:cow",
 *   "vertical_offset": "-size_y",
 *   "face_offset": "size_z + (size_z / 2) + parasite_height"
 * }
 * }</pre>
 * <p>
 * See {@link OffsetExpression} for the supported expression grammar.
 * <p>
 * Credit to Boston for the original head-offset table code.
 */
public record EntityHeadOffsetData(
    OffsetExpression verticalOffset,
    OffsetExpression faceOffset
) {

    public static final Codec<EntityHeadOffsetData> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            OffsetExpression.CODEC.fieldOf("vertical_offset").forGetter(EntityHeadOffsetData::verticalOffset),
            OffsetExpression.CODEC.fieldOf("face_offset").forGetter(EntityHeadOffsetData::faceOffset)
        ).apply(instance, EntityHeadOffsetData::new)
    );

    public static volatile Map<EntityType<?>, EntityHeadOffsetData> ENTITY_HEAD_OFFSET_DATA_BY_TYPE = Map.of();

    public static OffsetResult resolve(EntityType<?> hostType, EntityHeadData head, Entity parasite) {
        EntityHeadOffsetData data = ENTITY_HEAD_OFFSET_DATA_BY_TYPE.get(hostType);
        if (data == null) {
            return null;
        }
        OffsetExpression.OffsetContext ctx = new OffsetExpression.OffsetContext(
            head.size().x,
            head.size().y,
            head.size().z,
            head.pivot().x,
            head.pivot().y,
            head.pivot().z,
            parasite.getBbHeight(),
            parasite.getBbWidth()
        );
        return new OffsetResult(data.verticalOffset.evaluate(ctx), data.faceOffset.evaluate(ctx));
    }

    public record OffsetResult(
        double vertical,
        double face
    ) {}

    public static class ReloadListener extends SimpleJsonResourceReloadListener {

        public ReloadListener() {
            super(new com.google.gson.GsonBuilder().create(), "gigeresque_head_offsets");
        }

        @Override
        protected void apply(Map<ResourceLocation, JsonElement> jsons, @NotNull ResourceManager rm, @NotNull ProfilerFiller profiler) {
            Map<EntityType<?>, EntityHeadOffsetData> map = new HashMap<>();
            for (var entry : jsons.entrySet()) {
                var file = entry.getKey();
                try {
                    var obj = GsonHelper.convertToJsonObject(entry.getValue(), "head_offset");
                    ResourceLocation entityId;
                    if (obj.has("entity")) {
                        entityId = ResourceLocation.parse(GsonHelper.getAsString(obj, "entity"));
                    } else {
                        entityId = ResourceLocation.fromNamespaceAndPath("minecraft", file.getPath());
                    }

                    var type = BuiltInRegistries.ENTITY_TYPE.getOptional(entityId)
                        .orElseThrow(() -> new IllegalArgumentException("Unknown entity type: " + entityId));

                    var data = CODEC.parse(JsonOps.INSTANCE, entry.getValue())
                        .getOrThrow(IllegalArgumentException::new);

                    map.put(type, data);
                } catch (Exception e) {
                    CommonMod.LOGGER.error("Failed to load head offset {}: {}", file, e.getMessage());
                }
            }
            ENTITY_HEAD_OFFSET_DATA_BY_TYPE = Map.copyOf(map);
            CommonMod.LOGGER.info("Loaded {} entity head offset entries", map.size());
        }
    }
}
