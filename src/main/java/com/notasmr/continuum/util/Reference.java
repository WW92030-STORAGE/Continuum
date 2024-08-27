package com.notasmr.continuum.util;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

public class Reference {
    public static final int HOUSE_MAX_VOLUME = 16 * 16 * 16;
    public static final int HOUSE_MIN_VOLUME = 3 * 3 * 3;

    public static final String MODID = "continuum";
    public static final String NAME = "Continuum";
    public static final String VERSION = "1.0.0";

    public static final double DEG = 180.0 / Math.PI;
    public static final double TAU = 2.0 * Math.PI;
    public static final double EPSILON = 0.000000001;

    public static final long DUSK = 12542;

    public static ArrayList<Item> ITEMS;
    public static ArrayList<Block> BLOCKS;
    public static ArrayList<Class> HOSTILE_MOBS = new ArrayList<Class>();
    public static ArrayList<EntityType<?>> HOSTILE_ENTRIES = new ArrayList<EntityType<?>>();

    public static HashSet<Item> UNOBTAINABLE = new HashSet<Item>();
    public static HashSet<Block> DOORS = new HashSet<Block>();
    public static HashSet<Block> LIGHTS = new HashSet<Block>();
    public static TreeSet<String> MOB_BLACKLIST = new TreeSet<String>();

    public static double atan(double dx, double dy) {
        double res = Math.atan2(dx, dy);
        //	if (dx < 0) res += Math.PI;
        res = (res % TAU) + 10 * TAU;
        return res % TAU;
    }

    public static Item i(Block b) {
        return b.asItem();
    }

    public static void disp(String s, PlayerEntity p) {
        p.sendMessage(new StringTextComponent(s), p.getUUID());
    }

    // LOGISTICAL METHODS

    public static void setup() {
        UNOBTAINABLE.add(Items.AIR);
        UNOBTAINABLE.add(i(Blocks.BEDROCK));
        UNOBTAINABLE.add(i(Blocks.BARRIER));
        UNOBTAINABLE.add(i(Blocks.COMMAND_BLOCK));
        UNOBTAINABLE.add(i(Blocks.CHAIN_COMMAND_BLOCK));
        UNOBTAINABLE.add(i(Blocks.REPEATING_COMMAND_BLOCK));
        UNOBTAINABLE.add(i(Blocks.END_PORTAL_FRAME));
        UNOBTAINABLE.add(i(Blocks.COMMAND_BLOCK));
        UNOBTAINABLE.add(i(Blocks.STRUCTURE_BLOCK));
        UNOBTAINABLE.add(i(Blocks.STRUCTURE_VOID));
        UNOBTAINABLE.add(Items.COMMAND_BLOCK_MINECART);
        UNOBTAINABLE.add(i(Blocks.WATER));
        UNOBTAINABLE.add(i(Blocks.LAVA));

        DOORS.add(Blocks.OAK_DOOR);
        DOORS.add(Blocks.SPRUCE_DOOR);
        DOORS.add(Blocks.BIRCH_DOOR);
        DOORS.add(Blocks.JUNGLE_DOOR);
        DOORS.add(Blocks.ACACIA_DOOR);
        DOORS.add(Blocks.DARK_OAK_DOOR);

        LIGHTS.add(Blocks.TORCH);
        LIGHTS.add(Blocks.WALL_TORCH);
        LIGHTS.add(Blocks.SOUL_TORCH);
        LIGHTS.add(Blocks.SOUL_WALL_TORCH);
        LIGHTS.add(Blocks.GLOWSTONE);
        LIGHTS.add(Blocks.SEA_LANTERN);
        LIGHTS.add(Blocks.JACK_O_LANTERN);
        LIGHTS.add(Blocks.END_ROD);
        LIGHTS.add(Blocks.SHROOMLIGHT);
        LIGHTS.add(Blocks.LANTERN);
        LIGHTS.add(Blocks.SOUL_LANTERN);
        LIGHTS.add(Blocks.CRYING_OBSIDIAN);

        MOB_BLACKLIST.add("entity.minecraft.ender_dragon");
        MOB_BLACKLIST.add("entity.darkness.darknessx");
        MOB_BLACKLIST.add("entity.continuum.npc");

        // DispenserBlock.registerBehavior(ModItems.CONTINUUM.get(), new ContinuumItemDispenseBehavior());
    }

    public static ArrayList<Item> itemList() {
        Collection<Item> c = ForgeRegistries.ITEMS.getValues();
        ArrayList<Item> res = new ArrayList<Item>();
        for (Item i : c) res.add(i);
        return res;
    }

    public static ArrayList<Block> blockList() {
        Collection<Block> c = ForgeRegistries.BLOCKS.getValues();
        ArrayList<Block> res = new ArrayList<Block>();
        for (Block i : c) res.add(i);
        return res;
    }

    public static ArrayList<Class> entityList() {
        Collection<EntityType<?>> c = ForgeRegistries.ENTITIES.getValues();
        ArrayList<Class> res = new ArrayList<Class>();
        for (EntityType i : c) res.add(i.getClass());
        return res;
    }

    public static ArrayList<Class> entityList(EntityClassification cat) {
        Collection<EntityType<?>> c = ForgeRegistries.ENTITIES.getValues();
        for (EntityType e : c) System.out.println(e.getClass() + " | " + e.getDescriptionId());

        ArrayList<Class> res = new ArrayList<Class>();
        for (EntityType i : c) {
            if (i.getCategory() == cat) {
                res.add(i.getClass());
                HOSTILE_ENTRIES.add(i);
            }
        }
        return res;
    }

    public static void initData() {
        Reference.ITEMS = Reference.itemList();
        Reference.BLOCKS = Reference.blockList();
        Reference.HOSTILE_MOBS = Reference.entityList(EntityClassification.MONSTER);
    }
}
