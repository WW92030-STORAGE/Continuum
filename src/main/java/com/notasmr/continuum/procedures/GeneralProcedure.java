package com.notasmr.continuum.procedures;

import com.notasmr.continuum.util.Reference;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class GeneralProcedure {
    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinWorldEvent event) {
        if (event.getWorld().isClientSide()) return;
        if(!(event.getEntity() instanceof ServerPlayerEntity)) return;
        Reference.initData();

        PlayerEntity p = (PlayerEntity)event.getEntity();
        Reference.disp("CONTINUUM HAS DETECTED " + Reference.ITEMS.size() + " ITEMS", p);
        Reference.disp("CONTINUUM HAS DETECTED " + Reference.BLOCKS.size() + " BLOCKS", p);
        Reference.disp("CONTINUUM HAS DETECTED " + Reference.HOSTILE_MOBS.size() + " HOSTILES", p);
        for (EntityType<?> c : Reference.HOSTILE_ENTRIES) System.out.println(c);

        event.getWorld().getCapability(CapabilityWorldData.DATA).ifPresent(data -> {
            Reference.disp("NPC EXISTS = " + data.getNPC(), (PlayerEntity)event.getEntity());
            Reference.disp("NPC HOUSE POS = " + data.getHousePos(), (PlayerEntity)event.getEntity());
        });
    }
}