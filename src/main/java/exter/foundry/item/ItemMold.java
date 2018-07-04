package exter.foundry.item;

import java.util.HashMap;
import java.util.Map;

import exter.foundry.creativetab.FoundryTabMolds;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemMold extends Item {
	static public enum SubItem {
		INGOT(0, "moldIngot"),
		PLATE(1, "moldPlate"),
		GEAR(2, "moldGear"),
		ROD(3, "moldRod"),
		BLOCK(4, "moldBlock"),
		SLAB(5, "moldSlab"),
		STAIRS(6, "moldStairs"),
		PICKAXE(7, "moldPickaxe"),
		AXE(8, "moldAxe"),
		SWORD(9, "moldSword"),
		SHOVEL(10, "moldShovel"),
		HOE(11, "moldHoe"),
		HELMET(12, "moldHelmet"),
		CHESTPLATE(13, "moldChestplate"),
		LEGGINGS(14, "moldLeggings"),
		BOOTS(15, "moldBoots"),
		NUGGET(26, "moldNugget");

		static private final Map<Integer, SubItem> value_map = new HashMap<>();
		static {
			for (SubItem sub : values()) {
				value_map.put(sub.id, sub);
			}
		}

		static public SubItem fromId(int id) {
			return value_map.get(id);
		}

		public final int id;

		public final String name;

		SubItem(int id, String name) {
			this.id = id;
			this.name = name;
		}
	}

	public ItemMold() {
		super();
		maxStackSize = 1;
		setCreativeTab(FoundryTabMolds.INSTANCE);
		setHasSubtypes(true);
		setUnlocalizedName("mold");
		setRegistryName("mold");
	}

	@Override
	public void getSubItems(CreativeTabs tabs, NonNullList<ItemStack> list) {
		if (isInCreativeTab(tabs)) for (SubItem m : SubItem.values()) {
			ItemStack itemstack = new ItemStack(this, 1, m.id);
			list.add(itemstack);
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return "item.foundry." + SubItem.fromId(itemstack.getItemDamage()).name;
	}
}
