package exter.foundry.api.recipe.matcher;

import java.util.List;

import exter.foundry.integration.minetweaker.MTHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public class ItemStackMatcher implements IItemMatcher
{
    private final ItemStack match;

    public ItemStackMatcher(Block match)
    {
        this(new ItemStack(match));
    }

    public ItemStackMatcher(Item match)
    {
        this(new ItemStack(match));
    }

    public ItemStackMatcher(ItemStack match)
    {
        if (match.isEmpty())
            throw new IllegalArgumentException("Invalid ItemStackMatcher: Cannot use an empty stack!");
        this.match = match.copy();
    }

    @Override
    public boolean apply(ItemStack input)
    {
        return OreDictionary.itemMatches(match, input, false) && ItemStack.areItemStackTagsEqual(input, match)
                && input.getCount() >= match.getCount();
    }

    @Override
    public int getAmount()
    {
        return match.getCount();
    }

    @Override
    public ItemStack getItem()
    {
        return match.copy();
    }

    @Override
    public List<ItemStack> getItems()
    {
        return NonNullList.withSize(1, match.copy());
    }

    @Override
    public String toString()
    {
        return "ItemStackMatcher(Stack: " + MTHelper.getItemDescription(match) + ")";
    }
}
