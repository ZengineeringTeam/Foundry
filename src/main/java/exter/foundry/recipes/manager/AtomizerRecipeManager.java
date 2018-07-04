package exter.foundry.recipes.manager;

import java.util.List;

import exter.foundry.api.recipe.IAtomizerRecipe;
import exter.foundry.api.recipe.manager.IAtomizerRecipeManager;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import exter.foundry.recipes.AtomizerRecipe;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

public class AtomizerRecipeManager implements IAtomizerRecipeManager
{
    public static final AtomizerRecipeManager INSTANCE = new AtomizerRecipeManager();

    private final NonNullList<IAtomizerRecipe> recipes;

    private AtomizerRecipeManager()
    {
        recipes = NonNullList.create();
    }

    public void addRecipe(IAtomizerRecipe recipe)
    {
        recipes.add(recipe);

    }

    @Override
    public void addRecipe(IItemMatcher result, FluidStack in_fluid)
    {
        if (!FoundryMiscUtils.isInvalid(result))
            recipes.add(new AtomizerRecipe(result, in_fluid));
    }

    @Override
    public IAtomizerRecipe findRecipe(FluidStack fluid)
    {
        if (fluid == null || fluid.amount == 0)
        {
            return null;
        }
        for (IAtomizerRecipe ar : recipes)
        {
            if (ar.matchesRecipe(fluid))
            {
                return ar;
            }
        }
        return null;
    }

    @Override
    public List<IAtomizerRecipe> getRecipes()
    {
        return recipes;
    }

    @Override
    public void removeRecipe(IAtomizerRecipe recipe)
    {
        recipes.remove(recipe);
    }
}
