package com.notasmr.continuum.procedures;

import com.notasmr.continuum.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class DataAttacher {
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<World> event) {
        if (!(event.getObject() instanceof World)) return;
        for (int i = 0; i < 2; i++) System.out.println(event.getObject() + " - CAPABILITIES");
        event.addCapability(new ResourceLocation(Reference.MODID, "properties"), new DataProvider());
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(AttachCapabilitiesEvent<World> event) {
        World world = event.getObject();
        event.addCapability(new ResourceLocation("continuum:housing_data"), new DataProvider());
    }
}
