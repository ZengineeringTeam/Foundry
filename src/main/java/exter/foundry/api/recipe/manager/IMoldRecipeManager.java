package exter.foundry.api.recipe.manager;

import java.util.List;

import exter.foundry.api.recipe.IMoldRecipe;
import net.minecraft.item.ItemStack;

public interface IMoldRecipeManager
{
    void addRecipe(ItemStack result, int width, int height, int[] recipe);

    IMoldRecipe findRecipe(int[] grid);

    List<IMoldRecipe> getRecipes();

    /**
     * Removes a recipe.
     * @param The recipe to remove.
     */
    void removeRecipe(IMoldRecipe recipe);
}
