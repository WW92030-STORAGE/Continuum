package com.notasmr.continuum.entities;

import com.notasmr.continuum.util.Reference;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {
    public static final float RANGE = 32;
    public static final float SPEED = 0.3F;
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES
            = DeferredRegister.create(ForgeRegistries.ENTITIES, Reference.MODID);

    public static final RegistryObject<EntityType<NPCEntity>> DARKNESS =
            ENTITY_TYPES.register("npc",
                    () -> EntityType.Builder.of(NPCEntity::new, EntityClassification.CREATURE)
                            .sized(0.6F, 1.8F)
                            .build(new ResourceLocation(Reference.MODID, "darkness").toString()));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
