package com.notasmr.continuum.util;

import com.notasmr.continuum.entities.ModEntityTypes;
import com.notasmr.continuum.entities.NPCEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusEvents {
    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.DARKNESS.get(), NPCEntity.registerAttributes().build());
    }
}
