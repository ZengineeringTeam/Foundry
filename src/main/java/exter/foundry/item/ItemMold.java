package exter.foundry.item;

import java.util.HashMap;
import java.util.Map;

import exter.foundry.creativetab.FoundryTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;

public class ItemMold extends Item
{
    static public enum SubItem implements IStringSerializable
    {
        INGOT(0, "ingot"),
        PLATE(1, "plate"),
        GEAR(2, "gear"),
        ROD(3, "rod"),
        BLOCK(4, "block"),
        SLAB(5, "slab"),
        STAIRS(6, "stairs"),
        PICKAXE(7, "pickaxe"),
        AXE(8, "axe"),
        SWORD(9, "sword"),
        SHOVEL(10, "shovel"),
        HOE(11, "hoe"),
        HELMET(12, "helmet"),
        CHESTPLATE(13, "chestplate"),
        LEGGINGS(14, "leggings"),
        BOOTS(15, "boots"),
        NUGGET(16, "nugget"),
        HAMMER(17, "hammer"),
        SICKLE(18, "sickle"),
        SHEARS(19, "shears"),
        EXCAVATOR(20, "excavator");

        static private final Map<Integer, SubItem> value_map = new HashMap<>();
        static
        {
            for (SubItem sub : values())
            {
                value_map.put(sub.id, sub);
            }
        }

        static public SubItem fromId(int id)
        {
            return value_map.get(id);
        }

        public final int id;
        public final String name;
        public boolean registered = true;

        SubItem(int id, String name)
        {
            this.id = id;
            this.name = name;
        }

        @Override
        public String getName()
        {
            return name;
        }

        public ItemStack getItem()
        {
            return getItem(1);
        }

        public ItemStack getItem(int amount)
        {
            return new ItemStack(FoundryItems.item_mold, amount, id);
        }
    }

    public ItemMold()
    {
        super();
        maxStackSize = 1;
        setCreativeTab(FoundryTab.INSTANCE);
        setHasSubtypes(true);
        setUnlocalizedName("mold");
        setRegistryName("mold");
    }

    @Override
    public void getSubItems(CreativeTabs tabs, NonNullList<ItemStack> list)
    {
        if (isInCreativeTab(tabs))
            for (SubItem m : SubItem.values())
            {
                if (m.registered)
                {
                    ItemStack itemstack = new ItemStack(this, 1, m.id);
                    list.add(itemstack);
                }
            }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        return "item.foundry.mold." + SubItem.fromId(itemstack.getItemDamage()).name;
    }
}
