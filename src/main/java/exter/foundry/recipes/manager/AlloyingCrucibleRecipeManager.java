package exter.foundry.recipes.manager;

import java.util.List;

import exter.foundry.api.recipe.IAlloyingCrucibleRecipe;
import exter.foundry.api.recipe.manager.IAlloyingCrucibleRecipeManager;
import exter.foundry.recipes.AlloyingCrucibleRecipe;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

public class AlloyingCrucibleRecipeManager implements IAlloyingCrucibleRecipeManager
{
    public static final AlloyingCrucibleRecipeManager INSTANCE = new AlloyingCrucibleRecipeManager();

    private final NonNullList<IAlloyingCrucibleRecipe> recipes;

    private AlloyingCrucibleRecipeManager()
    {
        recipes = NonNullList.create();
    }

    @Override
    public void addRecipe(FluidStack out, FluidStack in_a, FluidStack in_b)
    {
        recipes.add(new AlloyingCrucibleRecipe(out, in_a, in_b));
    }

    public void addRecipe(IAlloyingCrucibleRecipe recipe)
    {
        recipes.add(recipe);
    }

    @Override
    public IAlloyingCrucibleRecipe findRecipe(FluidStack in_a, FluidStack in_b)
    {
        for (IAlloyingCrucibleRecipe r : recipes)
        {
            if (r.matchesRecipe(in_a, in_b))
            {
                return r;
            }
        }
        return null;
    }

    @Override
    public List<IAlloyingCrucibleRecipe> getRecipes()
    {
        return recipes;
    }

    @Override
    public void removeRecipe(IAlloyingCrucibleRecipe recipe)
    {
        recipes.remove(recipe);
    }
}
