
package gg.moonflower.etched.core.registry;

import gg.moonflower.etched.common.entity.MinecartJukebox;
import gg.moonflower.etched.core.Etched;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.village.poi.PoiType;

public class EtchedEntities {

    public static final EntityType<MinecartJukebox> JUKEBOX_MINECART =
            FabricEntityTypeBuilder.<MinecartJukebox>create(MobCategory.MISC, MinecartJukebox::new)
                    .dimensions(EntityDimensions.fixed(0.98F, 0.7F))
                    .trackRangeChunks(8)
                    .build();

    public static final ResourceKey<PoiType> BARD_POI_KEY = ResourceKey.create(
            Registries.POINT_OF_INTEREST_TYPE,
            new ResourceLocation(Etched.MOD_ID, "bard")
    );

    public static void register() {
        Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                new ResourceLocation(Etched.MOD_ID, "jukebox_minecart"),
                JUKEBOX_MINECART
        );
    }
}