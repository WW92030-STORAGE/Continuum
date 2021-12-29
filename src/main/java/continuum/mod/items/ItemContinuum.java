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

public class ItemContinuum extends Item implements IModel {
	public ItemContinuum(String name) {
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CreativeTabs.MISC);
		
		ItemInit.ITEMS.add(this);
	}
	
	@Override
	public void registerModels() {
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
	
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ActionResult<ItemStack> res = super.onItemRightClick(world, player, hand);
		ItemStack i = res.getResult();
		int randomIndex;
		Item randomItem;
		if (!world.isRemote) {
			while (true) {
				randomIndex = (int)(Math.random() * Reference.ITEMS.size());
				randomItem = Reference.ITEMS.get(randomIndex);
				if (!Reference.UNOBTAINABLE.contains(randomItem)) break;
			}
			System.out.println("GENERATED " + randomItem + " = INDEX " + randomIndex);
			player.inventory.clearMatchingItems(ItemInit.CONTINUUM, -1, 1, null);
			EntityItem pick = player.dropItem(randomItem, 1);
			pick.setNoPickupDelay();
		}
	//	player.sendStatusMessage(new TextComponentString("CONTINUUM HAS DETECTED " + Reference.ITEMS.size() + " ITEMS"), false);
		return res;
	}
}

