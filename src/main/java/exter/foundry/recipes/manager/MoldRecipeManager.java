package exter.foundry.recipes.manager;

import java.util.List;

import exter.foundry.api.recipe.IMoldRecipe;
import exter.foundry.api.recipe.manager.IMoldRecipeManager;
import exter.foundry.recipes.MoldRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class MoldRecipeManager implements IMoldRecipeManager
{
    public static final MoldRecipeManager INSTANCE = new MoldRecipeManager();

    private final NonNullList<IMoldRecipe> recipes;

    private MoldRecipeManager()
    {
        recipes = NonNullList.create();
    }

    public void addRecipe(IMoldRecipe recipe)
    {
        recipes.add(recipe);
    }

    @Override
    public void addRecipe(ItemStack result, int width, int height, int[] recipe)
    {
        recipes.add(new MoldRecipe(result, width, height, recipe));
    }

    @Override
    public IMoldRecipe findRecipe(int[] grid)
    {
        for (IMoldRecipe r : recipes)
        {
            if (r.matchesRecipe(grid))
            {
                return r;
            }
        }
        return null;
    }

    @Override
    public List<IMoldRecipe> getRecipes()
    {
        return recipes;
    }

    @Override
    public void removeRecipe(IMoldRecipe recipe)
    {
        recipes.remove(recipe);
    }
}
