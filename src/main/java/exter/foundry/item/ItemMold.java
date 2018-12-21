package exter.foundry.item;

import java.util.HashMap;
import java.util.Map;

import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.creativetab.FoundryTab;
import net.minecraft.creativetab.CreativeTabs;
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
        NUGGET(7, "nugget"),
        PICKAXE(8, "pickaxe"),
        AXE(9, "axe"),
        SWORD(10, "sword"),
        SHOVEL(11, "shovel"),
        HOE(12, "hoe"),
        SHEARS(13, "shears"),
        HELMET(14, "helmet"),
        CHESTPLATE(15, "chestplate"),
        LEGGINGS(16, "leggings"),
        BOOTS(17, "boots"),
        //Thermal Foundation
        SICKLE(18, "sickle"),
        HAMMER(19, "hammer"),
        EXCAVATOR(20, "excavator"),
        //Ender IO
        GRINDINGBALL(21, "grindingball");
        //Tech Reborn & Industrial Craft
        //CABLE(22, "cable");

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
        private final ItemStackMatcher matcher;

        SubItem(int id, String name)
        {
            this.id = id;
            this.name = name;
            this.matcher = new ItemStackMatcher(new ItemStack(FoundryItems.item_mold, 1, id));
        }

        @Override
        public String getName()
        {
            return name;
        }

        public ItemStackMatcher getMatcher()
        {
            return matcher;
        }

        public ItemStack getItem()
        {
            return matcher.getItem();
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
