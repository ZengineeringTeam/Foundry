package exter.foundry.creativetab;

import exter.foundry.block.BlockMachine.EnumMachine;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.fluid.FoundryFluidRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public class FoundryTab extends CreativeTabs
{

    public static final FoundryTab INSTANCE = new FoundryTab();

    private FoundryTab()
    {
        super("foundry");
    }

    @Override
    public ItemStack createIcon()
    {
        return FoundryBlocks.block_machine.asItemStack(EnumMachine.CASTER);
    }

    @Override
    public void displayAllRelevantItems(NonNullList<ItemStack> list)
    {
        super.displayAllRelevantItems(list);
        FoundryFluidRegistry.getMap().forEach((name, strategy) -> {
            if (strategy.enabled())
            {
                ItemStack bucket = FluidUtil.getFilledBucket(new FluidStack(FluidRegistry.getFluid(name), 1));
                if (!bucket.isEmpty())
                {
                    list.add(bucket);
                }
            }
        });
    }

}
