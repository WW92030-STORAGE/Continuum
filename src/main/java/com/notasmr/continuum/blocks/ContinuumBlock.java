package com.notasmr.continuum.blocks;

import com.notasmr.continuum.items.ModItems;
import com.notasmr.continuum.util.Reference;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ContinuumBlock extends Block {
    Block reference0 = Blocks.TORCH;
    public ContinuumBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType use(BlockState state, World level, BlockPos blockPos, PlayerEntity player,
                                Hand hand, BlockRayTraceResult blockHitResult) {
        // Server: Main Hand & Off Hand
        // Client: Main Hand & Off Hand

        // player.sendSystemMessage(Component.literal("!!!"));

        return super.use(state, level, blockPos, player, hand, blockHitResult);
    }
}
