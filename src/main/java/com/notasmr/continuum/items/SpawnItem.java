package com.notasmr.continuum.items;

import com.notasmr.continuum.entities.ModEntityTypes;
import com.notasmr.continuum.entities.NPCEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.concurrent.atomic.AtomicBoolean;

public class SpawnItem extends Item {
    public SpawnItem(Properties properties) {
        super(properties);
    }

    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        Entity en = new NPCEntity(ModEntityTypes.DARKNESS.get(), level);
        en.setPos(player.getX(), player.getY(), player.getZ());
        en.setYHeadRot((float)(Math.random() * 720 * Math.PI));
        level.addFreshEntity(en);

        return super.use(level, player, hand);
    }

}
