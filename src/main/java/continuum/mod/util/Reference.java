package continuum.mod.util;

import java.awt.Desktop;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

import continuum.mod.init.ItemInit;
import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.EntityEntry;

public class Reference {
	public static final String MODID = "continuum";
	public static final String NAME = "CONTINUUM";
	public static final String VERSION = "1.0.0";
	public static final String COMMON = "continuum.mod.proxy.CommonProxy";
	public static final String CLIENT = "continuum.mod.proxy.ClientProxy";
	
	public static final int ENTITY_NPC = 86;
	
	public static final double DEG = 180.0 / Math.PI;
	public static final double EPSILON = 0.000000001;
	
	public static final long DUSK = 12542;
	
	public static ArrayList<Item> ITEMS;
	public static ArrayList<Block> BLOCKS;
	public static ArrayList<Class> HOSTILE_MOBS = new ArrayList<Class>();
	public static ArrayList<EntityEntry> HOSTILE_ENTRIES = new ArrayList<EntityEntry>();
	
	public static HashSet<Item> UNOBTAINABLE = new HashSet<Item>();
	public static HashSet<Block> DOORS = new HashSet<Block>();
	public static HashSet<Block> LIGHTS = new HashSet<Block>();
	public static TreeSet<String> MOB_BLACKLIST = new TreeSet<String>();
	
	public static void open(String url) {
		try {
			Desktop d = Desktop.getDesktop();
			d.browse(new URI(url));
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static Item i(Block b) {
		return Item.getItemFromBlock(b);
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
		UNOBTAINABLE.add(i(Blocks.FLOWING_WATER));
		UNOBTAINABLE.add(i(Blocks.FLOWING_LAVA));
		UNOBTAINABLE.add(ItemInit.TEST);
		
		DOORS.add(Blocks.OAK_DOOR);
		DOORS.add(Blocks.SPRUCE_DOOR);
		DOORS.add(Blocks.BIRCH_DOOR);
		DOORS.add(Blocks.JUNGLE_DOOR);
		DOORS.add(Blocks.ACACIA_DOOR);
		DOORS.add(Blocks.DARK_OAK_DOOR);
		
		LIGHTS.add(Blocks.TORCH);
		LIGHTS.add(Blocks.GLOWSTONE);
		LIGHTS.add(Blocks.SEA_LANTERN);
		LIGHTS.add(Blocks.LIT_PUMPKIN);
		LIGHTS.add(Blocks.END_ROD);
		
		MOB_BLACKLIST.add("minecraft:ender_dragon");
		MOB_BLACKLIST.add("darkness:darknessx");
	}
	
	public static ArrayList<Item> itemList() {
		Collection<Item> c = (net.minecraftforge.fml.common.registry.ForgeRegistries.ITEMS).getValuesCollection();
		ArrayList<Item> res = new ArrayList<Item>();
		for (Item i : c) res.add(i);
		return res;
	}
	
	public static ArrayList<Block> blockList() {
		Collection<Block> c = (net.minecraftforge.fml.common.registry.ForgeRegistries.BLOCKS).getValuesCollection();
		ArrayList<Block> res = new ArrayList<Block>();
		for (Block i : c) res.add(i);
		return res;
	}
	
	public static ArrayList<Class> entityList() {
		Collection<EntityEntry> c = (net.minecraftforge.fml.common.registry.ForgeRegistries.ENTITIES).getValuesCollection();
		ArrayList<Class> res = new ArrayList<Class>();
		for (EntityEntry i : c) res.add(i.getEntityClass());
		return res;
	}
	
	public static ArrayList<Class> entityList(Class filter) {
		Collection<EntityEntry> c = (net.minecraftforge.fml.common.registry.ForgeRegistries.ENTITIES).getValuesCollection();
	//	for (EntityEntry e : c) System.out.println(e.getName() + " = " + e.getEntityClass() + " | " + e.getRegistryName());
		
		ArrayList<Class> res = new ArrayList<Class>();
		for (EntityEntry i : c) {
			Class x = i.getEntityClass();
			if (x.isAssignableFrom(filter) || filter.isAssignableFrom(x)) {
				res.add(x);
				HOSTILE_ENTRIES.add(i);
			}
		}
		return res;
	}
	
	public static void initData() {
		Reference.ITEMS = Reference.itemList();
		Reference.BLOCKS = Reference.blockList();
		Reference.HOSTILE_MOBS = Reference.entityList(EntityMob.class);
	}
}