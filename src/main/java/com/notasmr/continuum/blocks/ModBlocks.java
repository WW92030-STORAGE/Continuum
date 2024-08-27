package com.notasmr.continuum.blocks;

import com.notasmr.continuum.items.ModItems;
import com.notasmr.continuum.util.Reference;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModBlocks {
    Block importthing = Blocks.REDSTONE_BLOCK;
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MODID);

    public static final RegistryObject<Block> CONTINUUM = registerBlock("continuum_block",
            () -> new ContinuumBlock(AbstractBlock.Properties.of(Material.GLASS).harvestLevel(2).harvestTool(ToolType.PICKAXE).noOcclusion().randomTicks().strength(5f).requiresCorrectToolForDrops().lightLevel((p_50872_) -> {
                return 15;
            }
            )));


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(ItemGroup.TAB_MISC)));
    }

    public static void register(IEventBus ieb) {
        BLOCKS.register(ieb);
    }
}
