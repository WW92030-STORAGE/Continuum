package continuum.mod.procedures;

import continuum.mod.data_housing.WorldData;
import continuum.mod.items.ItemHousing;
import continuum.mod.util.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class GeneralProcedure {
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void playerConnected(PlayerEvent.PlayerLoggedInEvent event) {
		WorldData wd = WorldData.get(event.player.world);
		
		Reference.initData();
		
		String npc = wd.getNPC() ? "NPC EXISTS" : "NPC DOES NOT EXIST";
		
		EntityPlayer e = event.player;
		
		e.sendStatusMessage(new TextComponentString("CONTINUUM HAS DETECTED " + Reference.ITEMS.size() + " ITEMS"), false);
		e.sendStatusMessage(new TextComponentString("CONTINUUM HAS DETECTED " + Reference.BLOCKS.size() + " BLOCKS"), false);
		e.sendStatusMessage(new TextComponentString("CONTINUUM HAS DETECTED " + Reference.HOSTILE_MOBS.size() + " HOSTILES"), false);
		for (EntityEntry c : Reference.HOSTILE_ENTRIES) System.out.println(c.getRegistryName());
	//	e.sendStatusMessage(new TextComponentString("CURRENT HOUSE POSITION = [" + wd.getX() + ", " + wd.getY() + ", " + wd.getZ() + "]"), false);
	//	e.sendStatusMessage(new TextComponentString(npc), false);
		
		System.out.println("NEW HOUSE POSITION = [" + wd.getX() + ", " + wd.getY() + ", " + wd.getZ() + "]");
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void playerDisconnected(PlayerEvent.PlayerLoggedOutEvent event) {
		WorldData wd = WorldData.get(event.player.world);
		System.out.println("SAVING WORLD - NPC EXISTS = " + wd.getNPC());
		System.out.println("SAVING WORLD - HOUSE POSTIION = [" + wd.getX() + ", " + wd.getY() + ", " + wd.getZ() + "]");
	}
	
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
	}
}
