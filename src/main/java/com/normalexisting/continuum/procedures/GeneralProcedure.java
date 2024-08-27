package com.normalexisting.continuum.procedures;

import com.normalexisting.continuum.Reference;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class GeneralProcedure {
    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;
        if(!(event.getEntity() instanceof ServerPlayer player)) return;
        Reference.initData();

        Player p = (Player)event.getEntity();
        p.sendSystemMessage(Component.literal("CONTINUUM HAS DETECTED " + Reference.ITEMS.size() + " ITEMS"));
        p.sendSystemMessage(Component.literal("CONTINUUM HAS DETECTED " + Reference.BLOCKS.size() + " BLOCKS"));
        p.sendSystemMessage(Component.literal("CONTINUUM HAS DETECTED " + Reference.HOSTILE_MOBS.size() + " HOSTILES"));
        for (EntityType<?> c : Reference.HOSTILE_ENTRIES) System.out.println(c);

        event.getLevel().getCapability(DataProvider.DATA).ifPresent(data -> {
            player.sendSystemMessage(Component.literal("NPC EXISTS = " + data.getNPC()));
            player.sendSystemMessage(Component.literal("HOUSE POSITION = " + data.getX() + " " + data.getY() + " " + data.getZ()));
        });
    }
}
