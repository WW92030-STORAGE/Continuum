package com.notasmr.continuum.items;

import com.notasmr.continuum.util.Reference;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ContinuumItem extends Item {
    public ContinuumItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        if(!level.isClientSide() && hand == Hand.MAIN_HAND) {
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
