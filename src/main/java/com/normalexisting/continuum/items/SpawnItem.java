package com.normalexisting.continuum.items;

import com.normalexisting.continuum.entities.ModEntityTypes;
import com.normalexisting.continuum.entities.NPCEntity;
import com.normalexisting.continuum.procedures.DataProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.concurrent.atomic.AtomicBoolean;

public class SpawnItem extends Item {
    public SpawnItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        if (level.isClientSide()) return super.useOn(context);
        if (hand != InteractionHand.MAIN_HAND) return super.useOn(context);
        ItemStack is = context.getItemInHand();

        AtomicBoolean lol = new AtomicBoolean(false);

        level.getCapability(DataProvider.DATA).ifPresent(data -> {
            if (data.getNPC()) lol.set(true);
        });

        if (lol.get()) return super.useOn(context);

        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        Entity en = new NPCEntity(ModEntityTypes.NPC.get(), level);
        en.setPos(x, y, z);
        en.setYRot((float)(Math.random() * 720 * Math.PI));
        level.addFreshEntity(en);

        level.getCapability(DataProvider.DATA).ifPresent(data -> {
            data.setNPC(true);
        });

        return super.useOn(context);
    }
}