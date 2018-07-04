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

    private final int[] recipe_order;

    private AlloyMixerRecipeManager()
    {
        recipes = NonNullList.create();
        recipe_order = new int[4];
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
    public IAlloyMixerRecipe findRecipe(FluidStack[] in, int[] order)
    {
        int inputs = 0;
        IAlloyMixerRecipe result = null;
        if (order != null && order.length < 4)
        {
            order = null;
        }
        for (IAlloyMixerRecipe r : recipes)
        {
            List<FluidStack> rinputs = r.getInputs();
            if (r.matchesRecipe(in, recipe_order) && rinputs.size() > inputs)
            {
                if (order != null)
                {
                    System.arraycopy(recipe_order, 0, order, 0, recipe_order.length);
                }
                inputs = rinputs.size();
                result = r;
            }
        }
        return result;
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
