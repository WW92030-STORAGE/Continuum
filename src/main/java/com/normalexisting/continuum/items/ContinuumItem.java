package com.normalexisting.continuum.items;

import com.normalexisting.continuum.Reference;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class ContinuumItem extends Item {
    public ContinuumItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
            int randomIndex;
            Item randomItem;
            while (true) {
                randomIndex = (int)(Math.random() * Reference.ITEMS.size());
                randomItem = Reference.ITEMS.get(randomIndex);
                if (!Reference.UNOBTAINABLE.contains(randomItem)) break;
            }

            System.out.println("GENERATED " + randomItem + " = INDEX " + randomIndex);
            ItemStack is = player.getItemInHand(hand);
            is.shrink(1);

            ItemEntity pick = player.drop(new ItemStack(randomItem), false);
            pick.setNoPickUpDelay();

            // player.getCooldowns().addCooldown(this, 20);
        }

        return super.use(level, player, hand);
    }
}