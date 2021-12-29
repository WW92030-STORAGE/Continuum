package continuum.mod.items;

import continuum.mod.Main;
import continuum.mod.init.ItemInit;
import continuum.mod.util.IModel;
import continuum.mod.util.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class IteratingTest extends Item implements IModel {
	public IteratingTest(String name) {
		setUnlocalizedName(name);
		setRegistryName(name);
	//	setCreativeTab(CreativeTabs.MISC);
		
		ItemInit.ITEMS.add(this);
	}
	
	@Override
	public void registerModels() {
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
	
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ActionResult<ItemStack> res = super.onItemRightClick(world, player, hand);
		ItemStack is = res.getResult();
		if (!world.isRemote) {
			for (int i = 0; i < Reference.ITEMS.size(); i++) {
				Item item = Reference.ITEMS.get(i);
				if (Reference.UNOBTAINABLE.contains(item)) continue;
				System.out.println("GENERATED " + item + " = INDEX " + i);
				EntityItem pick = player.dropItem(item, 1);
				pick.setNoPickupDelay();
				pick.setOwner(player.getName());
			}
		}
	//	player.sendStatusMessage(new TextComponentString("CONTINUUM HAS DETECTED " + Reference.ITEMS.size() + " ITEMS"), false);
		return res;
	}
}

