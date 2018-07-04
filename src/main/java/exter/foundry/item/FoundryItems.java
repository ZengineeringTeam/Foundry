package exter.foundry.item;

import exter.foundry.FoundryRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public class FoundryItems
{
    static public ItemComponent item_component;
    static public ItemMold item_mold;

    static public ItemStack component(ItemComponent.SubItem sub)
    {
        return component(sub, 1);
    }

    static public ItemStack component(ItemComponent.SubItem sub, int amount)
    {
        return new ItemStack(item_component, amount, sub.id);
    }

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
        item_component = new ItemComponent();
        item_mold = new ItemMold();

        FoundryRegistry.ITEMS.add(item_component);
        FoundryRegistry.ITEMS.add(item_mold);
    }
}
