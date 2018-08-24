package exter.foundry.recipes.manager;

import java.util.List;

import exter.foundry.api.recipe.IAlloyMixerRecipe;
import exter.foundry.api.recipe.manager.IAlloyMixerRecipeManager;
import exter.foundry.recipes.AlloyMixerRecipe;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

public class AlloyMixerRecipeManager implements IAlloyMixerRecipeManager
{
    public static final AlloyMixerRecipeManager INSTANCE = new AlloyMixerRecipeManager();

    private final NonNullList<IAlloyMixerRecipe> recipes;

    private AlloyMixerRecipeManager()
    {
        recipes = NonNullList.create();
    }

    @Override
    public void addRecipe(FluidStack out, FluidStack... in)
    {
        recipes.add(new AlloyMixerRecipe(out, in));
    }

    public void addRecipe(IAlloyMixerRecipe recipe)
    {
        recipes.add(recipe);
    }

    @Override
    public IAlloyMixerRecipe findRecipe(List<FluidStack> in)
    {
        in = AlloyMixerRecipe.sortedFluids(in);
        for (IAlloyMixerRecipe r : recipes)
        {
            if (r.matchesRecipe(in))
            {
                return r;
            }
        }
        return null;
    }

    @Override
    public List<IAlloyMixerRecipe> getRecipes()
    {
        return recipes;
    }

    @Override
    public void removeRecipe(IAlloyMixerRecipe recipe)
    {
        recipes.remove(recipe);
    }

}
