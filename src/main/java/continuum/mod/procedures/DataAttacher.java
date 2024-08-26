package continuum.mod.procedures;

import continuum.mod.util.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class DataAttacher {
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Level> event) {
        if (!(event.getObject() instanceof Level)) return;
        for (int i = 0; i < 2; i++) System.out.println(event.getObject() + " - CAPABILITIES");
        event.addCapability(new ResourceLocation(Reference.MODID, "properties"), new DataProvider());
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(WorldData.class);
    }
}
