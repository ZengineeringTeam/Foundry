package exter.foundry.creativetab;

import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class FoundryTabMaterials extends CreativeTabs
{

    public static final FoundryTabMaterials INSTANCE = new FoundryTabMaterials();

    private FoundryTabMaterials()
    {
        super("foundryMaterials");
    }

    @Override
    public ItemStack getTabIconItem()
    {
        return FoundryItems.component(ItemComponent.SubItem.REFRACTORYCLAY);
    }

}
