package continuum.mod.items;

import continuum.mod.util.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class ContinuumItemDispenseBehavior extends DefaultDispenseItemBehavior {
    private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
    public ContinuumItemDispenseBehavior() {

    }

    protected ItemStack execute(BlockSource src, ItemStack is) {
        System.out.println(src.getPos());
        Direction direction = src.getBlockState().getValue(DispenserBlock.FACING);
        Level level = src.getLevel();
        double xp = src.x() + (double)((float)direction.getStepX() * 1.125F);
        double yp = src.y() + (double)((float)direction.getStepY() * 1.125F);
        double zp = src.z() + (double)((float)direction.getStepZ() * 1.125F);
        BlockPos pos2 = src.getPos().relative(direction);
        if (level.isClientSide()) return is;
        is.shrink(1);

        int randomIndex;
        Item randomItem;
        while (true) {
            randomIndex = (int)(Math.random() * Reference.ITEMS.size());
            randomItem = Reference.ITEMS.get(randomIndex);
            if (!Reference.UNOBTAINABLE.contains(randomItem)) break;
        }

        System.out.println("BREAKING NEWS: 9 OUT OF 10 [[Furries]] THINK [[" + randomItem + "]] SHOULD BE SOLD FOR " + randomIndex + " KROMER."); // For legal reasons this is a joke message used for debugging purposes.

        level.addFreshEntity(new ItemEntity(level, xp, yp, zp, new ItemStack(randomItem, 1)));

        return is;
    }
}
