package exter.foundry.recipes.manager;

import java.util.List;

import exter.foundry.api.recipe.ICastingRecipe;
import exter.foundry.api.recipe.manager.ICastingRecipeManager;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.CastingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

public class CastingRecipeManager implements ICastingRecipeManager
{
    public static final CastingRecipeManager INSTANCE = new CastingRecipeManager();
    private final NonNullList<ICastingRecipe> recipes;

    private CastingRecipeManager()
    {
        recipes = NonNullList.create();
    }

    public void addRecipe(ICastingRecipe recipe)
    {
        recipes.add(recipe);
    }

    @Override
    public void addRecipe(IItemMatcher result, FluidStack in_fluid, IItemMatcher in_mold, boolean comsume_mold, IItemMatcher in_extra, int cast_speed)
    {
        ICastingRecipe recipe = new CastingRecipe(result, in_fluid, in_mold, comsume_mold, in_extra, cast_speed);
        if (recipe.requiresExtra())
        {
            recipes.add(0, recipe);
        }
        else
        {
            recipes.add(recipe);
        }
    }

    @Override
    public void addRecipe(IItemMatcher result, FluidStack in_fluid, ItemMold.SubItem in_mold, boolean comsume_mold, IItemMatcher in_extra, int cast_speed)
    {
        ICastingRecipe recipe = new CastingRecipe(result, in_fluid, in_mold.getMatcher(), comsume_mold, in_extra,
                cast_speed);
        if (recipe.requiresExtra())
        {
            recipes.add(0, recipe);
        }
        else
        {
            recipes.add(recipe);
        }
    }

    public void addRecipe(int i, ICastingRecipe recipe)
    {
        recipes.add(i, recipe);
    }

    @Override
    public ICastingRecipe findRecipe(FluidStack fluid, ItemStack mold, ItemStack extra)
    {
        if (mold.isEmpty() || fluid == null || fluid.amount == 0)
            return null;
        for (ICastingRecipe cr : recipes)
        {
            if (cr.matchesRecipe(mold, fluid, extra))
                return cr;
        }
        return null;
    }

    @Override
    public List<ICastingRecipe> getRecipes()
    {
        return recipes;
    }

    @Override
    public boolean isItemMold(ItemStack stack)
    {
        if (stack.isEmpty())
            return false;
        for (ICastingRecipe recipe : recipes)
        {
            if (recipe.getMold().isItemEqual(stack))
                return true;
        }
        return false;
    }

    @Override
    public void removeRecipe(ICastingRecipe recipe)
    {
        recipes.remove(recipe);
    }
}
