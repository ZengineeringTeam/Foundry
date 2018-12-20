package exter.foundry.api.recipe.manager;

import java.util.List;

import exter.foundry.api.recipe.IAlloyMixerRecipe;
import net.minecraftforge.fluids.FluidStack;

public interface IAlloyMixerRecipeManager
{
    /**
     * Register an Alloy Mixer recipe.
     * @param out Output (fluid type and amount).
     * @param in Inputs (fluid type and amount required), length must be less or equal 4.
     */
    void addRecipe(FluidStack out, FluidStack... in);

    IAlloyMixerRecipe findRecipe(List<FluidStack> in);

    /**
     * Get a list of all the recipes
     * @return List of all the recipes
     */
    List<IAlloyMixerRecipe> getRecipes();

    /**
     * Removes a recipe.
     * @param The recipe to remove.
     */
    void removeRecipe(IAlloyMixerRecipe recipe);
}
