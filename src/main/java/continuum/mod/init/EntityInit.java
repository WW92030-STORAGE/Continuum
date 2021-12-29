package continuum.mod.init;

import java.util.ArrayList;
import java.util.Iterator;

import continuum.mod.Main;
import continuum.mod.entities.EntityNPC;
import continuum.mod.util.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityInit {
	public static final int RANGE = 32;
	public static final double SPEED = 0.3;
	public static final boolean PERSISTENCE = true; // TOWN NPCS ARE ALWAYS EXISTING
	
	public static void registerEntities() {
		registerLivingEntity("darkness", EntityNPC.class, Reference.ENTITY_NPC, Math.max(128, RANGE), 0x808080, 0x808080, false);
	}
	
	private static Biome[] biomes(net.minecraft.util.registry.RegistryNamespaced<ResourceLocation, Biome> in) {
		Iterator<Biome> iter = in.iterator();
		ArrayList<Biome> list = new ArrayList<Biome>();
		while (iter.hasNext())
			list.add(iter.next());
		return list.toArray(new Biome[list.size()]);
	}
	
	private static void registerEntity(String name, Class<? extends Entity> c, int id, int range, boolean natural) {
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID + ":" + name), c, name, id, Main.instance, range, 1, true);
	}
	
	private static void registerLivingEntity(String name, Class<? extends Entity> c, int id, int range, int prim, int seco, boolean natural) {
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID + ":" + name), c, name, id, Main.instance, range, 1, true, prim, seco);
		Biome[] biomes = biomes(Biome.REGISTRY);
		if (natural) EntityRegistry.addSpawn((Class<? extends EntityLiving>) c, 40, 3, 3, EnumCreatureType.MONSTER, biomes);
	}
}

