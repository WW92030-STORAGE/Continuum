package continuum.mod.init;

import java.util.ArrayList;
import java.util.List;

import continuum.mod.blocks.BlockContinuum;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockInit {
public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final Block CONTINUUMBLOCK = new BlockContinuum("continuum_block", Material.IRON, CreativeTabs.MISC);
}

