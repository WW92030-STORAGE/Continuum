package continuum.mod.items;

import continuum.mod.util.Reference;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MODID);

    public static final RegistryObject<Item> CONTINUUM = ITEMS.register("continuum_gemstone",
            () -> new ContinuumItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> HOUSING = ITEMS.register("housing_query",
            () -> new HousingItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> SPAWN = ITEMS.register("npc_attractor",
            () -> new SpawnItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static void register(IEventBus ieb) {
        ITEMS.register(ieb);
    }
}

