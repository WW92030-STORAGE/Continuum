package com.normalexisting.continuum;

import com.normalexisting.continuum.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

public class Reference {
    public static final String MODID = "continuum";
    public static final float SPEED = 0.375f;
    public static final int RANGE = 128;
    public static final double DEG = 180.0 / Math.PI;
    public static final double TAU = 2.0 * Math.PI;
    public static final double EPSILON = 0.000000001;

    // Items and shit

    public static final long DUSK = 12542;

    public static ArrayList<Item> ITEMS;
    public static ArrayList<Block> BLOCKS;
    public static ArrayList<Class> HOSTILE_MOBS = new ArrayList<Class>();
    public static ArrayList<EntityType<?>> HOSTILE_ENTRIES = new ArrayList<EntityType<?>>();

    public static HashSet<Item> UNOBTAINABLE = new HashSet<Item>();
    public static HashSet<Block> DOORS = new HashSet<Block>();
    public static HashSet<Block> LIGHTS = new HashSet<Block>();
    public static TreeSet<String> MOB_BLACKLIST = new TreeSet<String>();


    // Methods

    public static double rem(double a, double m) {
        double res = a % m;
        while (a < 0) a += m;
        return a % m;
    }

    public static double atan(double dx, double dy) {
        double res = Math.atan2(dx, dy);
        //	if (dx < 0) res += Math.PI;
        res = (res % TAU) + 10 * TAU;
        return res % TAU;
    }

    public static void open(String u) {
        try {
            Desktop d = Desktop.getDesktop();
            d.browse(new URI(u));
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public static ArrayList<Entity> aabb(Entity e, int x1, int y1, int z1, int x2, int y2, int z2) {
        BlockPos bp1 = new BlockPos(x1, y1, z1);
        BlockPos bp2 = new BlockPos(x2, y2, z2);
        AABB aabb = new AABB(bp1, bp2);
        ArrayList<Entity> things = (ArrayList<Entity>) e.level().getEntities(e, aabb);
        for (int i = 0; i < things.size(); i++) {
            if (things.get(i).equals(e)) {
                things.remove(i);
                break;
            }
        }
        return things;
    }
    public static ArrayList<Entity> aabb(Entity e, double x1, double y1, double z1, double x2, double y2, double z2) {
        return aabb(e, (int)Math.floor(x1), (int)Math.floor(y1), (int)Math.floor(z1), (int)Math.ceil(x2), (int)Math.ceil(y2), (int)Math.ceil(z2));
    }

    public static Item i(Block b) {
        return b.asItem();
    }

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
        UNOBTAINABLE.add(i(Blocks.MUD));
        UNOBTAINABLE.add(i(Blocks.POWDER_SNOW));

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
        LIGHTS.add(Blocks.OCHRE_FROGLIGHT);
        LIGHTS.add(Blocks.PEARLESCENT_FROGLIGHT);
        LIGHTS.add(Blocks.VERDANT_FROGLIGHT);
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
        Collection<EntityType<?>> c = ForgeRegistries.ENTITY_TYPES.getValues();
        ArrayList<Class> res = new ArrayList<Class>();
        for (EntityType i : c) res.add(i.getBaseClass());
        return res;
    }

    public static ArrayList<Class> entityList(MobCategory cat) {
        Collection<EntityType<?>> c = ForgeRegistries.ENTITY_TYPES.getValues();
        for (EntityType e : c) System.out.println(e.getBaseClass() + " | " + e.getDescriptionId());

        ArrayList<Class> res = new ArrayList<Class>();
        for (EntityType i : c) {
            if (i.getCategory() == cat) {
                res.add(i.getBaseClass());
                HOSTILE_ENTRIES.add(i);
            }
        }
        return res;
    }

    public static void initData() {
        Reference.ITEMS = Reference.itemList();
        Reference.BLOCKS = Reference.blockList();
        Reference.HOSTILE_MOBS = Reference.entityList(MobCategory.MONSTER);
    }
}
