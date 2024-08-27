package com.notasmr.continuum.items;

import com.notasmr.continuum.util.Reference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MODID);
    public static final RegistryObject<Item> CONTINUUM_SHARD = ITEMS.register("continuum_gemstone",
            () -> new ContinuumItem(new Item.Properties().tab(ItemGroup.TAB_MISC)));

    public static final RegistryObject<Item> HOUSING_QUERY = ITEMS.register("housing_query",
            () -> new HousingItem(new Item.Properties().tab(ItemGroup.TAB_MISC)));

    public static final RegistryObject<Item> SPAWN = ITEMS.register("npc_attractor",
            () -> new SpawnItem(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
