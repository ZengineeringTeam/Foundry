package exter.foundry.item;

import exter.foundry.block.IBlockVariants;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockMulti extends ItemBlock
{

    public ItemBlockMulti(Block block)
    {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int dmg)
    {
        return dmg;
    }

    protected int getSubIndex(ItemStack stack)
    {
        return stack.getItemDamage();
    }

    @Override
    public final String getUnlocalizedName(ItemStack stack)
    {
        return ((IBlockVariants) block).getUnlocalizedName(getSubIndex(stack));
    }
}
