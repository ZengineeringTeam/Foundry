package exter.foundry.item;

import java.util.HashMap;
import java.util.Map;

import exter.foundry.creativetab.FoundryTabMaterials;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemComponent extends Item
{
    static public enum SubItem
    {
        HEATINGCOIL(0, "componentHeatingCoil"),
        REFRACTORYCLAY(1, "componentRefractoryClay"),
        REFRACTORYBRICK(2, "componentRefractoryBrick"),
        REFRACTORYCLAY_SMALL(16, "componentSmallRefractoryClay"),
        INFERNOCLAY(17, "componentInfernoClay"),
        INFERNOBRICK(18, "componentInfernoBrick"),
        ROD_CUPRONICKEL(24, "rodCupronickel");

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

        SubItem(int id, String name)
        {
            this.id = id;
            this.name = name;
        }
    }

    public ItemComponent()
    {
        super();
        setCreativeTab(FoundryTabMaterials.INSTANCE);
        setHasSubtypes(true);
        setUnlocalizedName("component");
        setRegistryName("component");
    }

    @Override
    public int getItemBurnTime(ItemStack fuel)
    {
        return 0;
    }

    @Override
    public void getSubItems(CreativeTabs tabs, NonNullList<ItemStack> list)
    {
        if (isInCreativeTab(tabs))
            for (SubItem c : SubItem.values())
            {
                list.add(new ItemStack(this, 1, c.id));
            }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        return "item.foundry." + SubItem.fromId(itemstack.getItemDamage()).name;
    }
}
