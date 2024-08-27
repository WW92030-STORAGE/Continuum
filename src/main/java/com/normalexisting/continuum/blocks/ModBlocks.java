package com.normalexisting.continuum.blocks;

import com.normalexisting.continuum.Reference;
import com.normalexisting.continuum.items.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    Block importthing = Blocks.REDSTONE_BLOCK;
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MODID);

    public static final RegistryObject<Block> CONTINUUM = registerBlock("continuum_block",
            () -> new ContinuumBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).noOcclusion().randomTicks().strength(6f).requiresCorrectToolForDrops().lightLevel((p_50872_) -> {
                return 15;
            })));

    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> sup) {
        RegistryObject<T> retval = BLOCKS.register(name, sup);
        registerBlockItem(name, retval);
        return retval;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus ieb) {
        BLOCKS.register(ieb);
    }
}

