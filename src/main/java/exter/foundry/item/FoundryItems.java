package exter.foundry.item;

import exter.foundry.Foundry;
import exter.foundry.FoundryRegistry;
import exter.foundry.creativetab.FoundryTab;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public class FoundryItems
{
    static public Item item_small_clay;
    static public ItemMold item_mold;

    static public ItemStack mold(ItemMold.SubItem sub)
    {
        return mold(sub, 1);
    }

    static public ItemStack mold(ItemMold.SubItem sub, int amount)
    {
        return new ItemStack(item_mold, amount, sub.id);
    }

    static public void registerItems(Configuration config)
    {
        item_small_clay = new Item().setRegistryName(Foundry.MODID, "small_clay")
                .setUnlocalizedName(Foundry.MODID + ".small_clay").setCreativeTab(FoundryTab.INSTANCE);
        item_mold = new ItemMold();

        FoundryRegistry.ITEMS.add(item_small_clay);
        FoundryRegistry.ITEMS.add(item_mold);
    }
}
