package continuum.mod.procedures;

import java.util.Map;
import java.util.TreeMap;

import continuum.mod.util.Reference;
import net.minecraft.block.properties.IProperty;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DispensedItem implements IBehaviorDispenseItem {
	public boolean DEBUG = false;
	public DispensedItem() {
		
	}

	// Blocks.BlockDispenser
	@Override
	public ItemStack dispense(IBlockSource source, ItemStack stack) {
		if (DEBUG) return stack;
		stack.shrink(1);
		int randomIndex;
		Item randomItem;
		while (true) {
			randomIndex = (int)(Math.random() * Reference.ITEMS.size());
			randomItem = Reference.ITEMS.get(randomIndex);
			if (!Reference.UNOBTAINABLE.contains(randomItem)) break;
		}
		
		World world = source.getWorld();
		BlockPos pos = source.getBlockPos();
		EntityItem ie = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
		ie.setItem(new ItemStack(randomItem));
		world.spawnEntity(ie);
		
		System.out.println("GENERATED " + randomItem + " = INDEX " + randomIndex);
		return stack;
	}
}
