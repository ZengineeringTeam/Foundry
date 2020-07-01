package exter.foundry.integration.crafttweaker;

import java.util.List;
import java.util.stream.Collectors;

import crafttweaker.api.item.IIngredient;
import crafttweaker.mc1120.item.MCItemStack;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import net.minecraft.item.ItemStack;

public class CrTItemMatcher implements IItemMatcher
{

    private final IIngredient ingredient;

    public CrTItemMatcher(IIngredient ingredient)
    {
        this.ingredient = ingredient;
    }

    @Override
    public boolean apply(ItemStack input)
    {
        if (input.isEmpty())
        {
            return false;
        }
        return ingredient.matches(new MCItemStack(input));
    }

    @Override
    public int getAmount()
    {
        return ingredient.getAmount();
    }

    @Override
    public ItemStack getItem()
    {
        List<ItemStack> items = getItems();
        return items.isEmpty() ? ItemStack.EMPTY : items.get(0);
    }

    @Override
    public List<ItemStack> getItems()
    {
        return ingredient.getItems().stream().filter($ -> $ instanceof MCItemStack)
                .map($ -> (ItemStack) $.getInternal()).collect(Collectors.toList());
    }

}
