package exter.foundry.creativetab;

import exter.foundry.FoundryRegistry;
import exter.foundry.block.BlockFoundryMachine.EnumMachine;
import exter.foundry.block.BlockLiquidMetal;
import exter.foundry.block.FoundryBlocks;
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
    public ItemStack getTabIconItem()
    {
        return FoundryBlocks.block_machine.asItemStack(EnumMachine.CASTER);
    }

    @Override
    public void displayAllRelevantItems(NonNullList<ItemStack> list)
    {
        super.displayAllRelevantItems(list);
        // TODO: some buckets are missing. change the block register events maybe
        FoundryRegistry.BLOCKS.stream().filter(block -> block instanceof BlockLiquidMetal).forEach(block -> {
            ItemStack bucket = FluidUtil.getFilledBucket(
                    new FluidStack(FluidRegistry.getFluid(((BlockLiquidMetal) block).getFluid().getName()), 1));
            if (!bucket.isEmpty())
            {
                list.add(bucket);
            }

        });
    }

}
