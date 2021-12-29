package continuum.mod.init;

import java.util.ArrayList;
import java.util.List;

import continuum.mod.items.ItemContinuum;
import continuum.mod.items.ItemHousing;
import continuum.mod.items.IteratingTest;
import net.minecraft.item.Item;

public class ItemInit {
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	public static final Item CONTINUUM = new ItemContinuum("continuum_gemstone");
	public static final Item HOUSING = new ItemHousing("housing_query");
	public static final Item TEST = new IteratingTest("continuum_iterator");
}
