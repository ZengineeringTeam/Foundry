package exter.foundry.integration.crafttweaker.orestack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import crafttweaker.annotations.BracketHandler;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemCondition;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.item.IItemTransformer;
import crafttweaker.api.item.IItemTransformerNew;
import crafttweaker.api.item.IngredientOr;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import exter.foundry.api.recipe.matcher.OreMatcher;
import net.minecraft.item.ItemStack;

@BracketHandler(priority = 100)
public class CrTOreStack implements IIngredient
{
    private final OreMatcher stack;

    public CrTOreStack(OreMatcher stack)
    {
        this.stack = stack;
    }

    @Override
    public IIngredient amount(int amount)
    {
        return new CrTOreStack(new OreMatcher(stack.getOreName(), stack.getAmount() * amount));
    }

    @Override
    public IItemStack applyTransform(IItemStack arg0, IPlayer arg1)
    {
        return null;
    }

    @Override
    public boolean contains(IIngredient ingredient)
    {
        List<IItemStack> items = ingredient.getItems();
        for (IItemStack item : items)
        {
            if (!matches(item))
                return false;
        }

        return true;
    }

    @Override
    public int getAmount()
    {
        return stack.getAmount();
    }

    @Override
    public Object getInternal()
    {
        return stack;
    }

    @Override
    public IItemStack[] getItemArray()
    {
        return getItems().toArray(new IItemStack[0]);
    }

    @Override
    public List<IItemStack> getItems()
    {
        List<IItemStack> result = new ArrayList<>();
        for (ItemStack item : stack.getItems())
        {
            result.add(CraftTweakerMC.getIItemStack(item));
        }
        return result;
    }

    @Override
    public List<ILiquidStack> getLiquids()
    {
        return Collections.emptyList();
    }

    @Override
    public String getMark()
    {
        return null;
    }

    @Override
    public boolean hasTransformers()
    {
        return false;
    }

    @Override
    public IIngredient marked(String arg0)
    {
        return this;
    }

    @Override
    public boolean matches(IItemStack iitem)
    {
        ItemStack item = CraftTweakerMC.getItemStack(iitem);
        return stack.apply(item);
    }

    @Override
    public boolean matches(ILiquidStack arg0)
    {
        return false;
    }

    @Override
    public boolean matchesExact(IItemStack iitem)
    {
        return matches(iitem);
    }

    @Override
    public IIngredient only(IItemCondition arg0)
    {
        return this;
    }

    @Override
    public IIngredient or(IIngredient ingredient)
    {
        return new IngredientOr(this, ingredient);
    }

    @Override
    public IIngredient transform(IItemTransformer arg0)
    {
        return this;
    }

    @Override
    public IIngredient transformNew(IItemTransformerNew transformer)
    {
        return this;
    }

    @Override
    public IItemStack applyNewTransform(IItemStack item)
    {
        return item;
    }

    @Override
    public boolean hasNewTransformers()
    {
        return false;
    }

    @Override
    public String toCommandString()
    {
        return stack.toString();
    }

    @Override
    public String toString()
    {
        return toCommandString();
    }
}
