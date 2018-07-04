package exter.foundry.creativetab;

import exter.foundry.block.BlockFoundryMachine.EnumMachine;
import exter.foundry.block.FoundryBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class FoundryTab extends CreativeTabs
{

    public static final FoundryTab INSTANCE = new FoundryTab();

    private FoundryTab()
    {
        super("foundry");
    }

    @Override
    public ItemStack getTabIconItem()
    {
        return FoundryBlocks.block_machine.asItemStack(EnumMachine.CASTER);
    }

}
