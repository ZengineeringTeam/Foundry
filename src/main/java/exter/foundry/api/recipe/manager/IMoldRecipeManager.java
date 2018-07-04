package exter.foundry.api.recipe.manager;

import java.util.List;

import exter.foundry.api.recipe.IMoldRecipe;
import net.minecraft.item.ItemStack;

public interface IMoldRecipeManager
{
    public void addRecipe(ItemStack result, int width, int height, int[] recipe);

    public IMoldRecipe findRecipe(int[] grid);

    public List<IMoldRecipe> getRecipes();

    /**
     * Removes a recipe.
     * @param The recipe to remove.
     */
    public void removeRecipe(IMoldRecipe recipe);
}
