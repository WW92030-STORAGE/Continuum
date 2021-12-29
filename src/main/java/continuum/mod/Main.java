package continuum.mod;

import continuum.mod.procedures.GeneralProcedure;
import continuum.mod.proxy.CommonProxy;
import continuum.mod.util.Reference;
import continuum.mod.util.handlers.RegistryHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class Main 
{
	@Instance
	public static Main instance;
	
	@SidedProxy(clientSide = Reference.CLIENT, serverSide = Reference.COMMON)
	public static CommonProxy proxy;
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event) {	
		RegistryHandler.preInitRegistries(event);
		GeneralProcedure pro = new GeneralProcedure();
		pro.preInit(event);
		Reference.setup(); // Item blacklist
	}
	
	@EventHandler
	public static void init(FMLInitializationEvent event) {
		RegistryHandler.InitRegistries(event);
	}
	
	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
	}
}