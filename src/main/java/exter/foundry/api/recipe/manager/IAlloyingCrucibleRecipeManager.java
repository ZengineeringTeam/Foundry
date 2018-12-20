package exter.foundry.api.recipe.manager;

import java.util.List;

import exter.foundry.api.recipe.IAlloyingCrucibleRecipe;
import net.minecraftforge.fluids.FluidStack;

public interface IAlloyingCrucibleRecipeManager
{
    /**
     * Register an Alloying Crucible recipe.
     * @param out Output.
     * @param in_a Input A.
     * @param in_b Input B.
     */
    void addRecipe(FluidStack out, FluidStack in_a, FluidStack in_b);

    /**
     * Find a valid recipe that contains the given inputs.
     * A recipe is found if the recipe's inputs contains the fluid in the parameters.
     * @param in_a FluidStack for the first input.
     * @param in_b FluidStack for the second input.
     * @param order [Output] Order in which the input fluids are matched.
     * @return
     */
    IAlloyingCrucibleRecipe findRecipe(FluidStack in_a, FluidStack in_b);

    /**
     * Get a list of all the recipes
     * @return List of all the recipes
     */
    List<IAlloyingCrucibleRecipe> getRecipes();

    /**
     * Removes a recipe.
     * @param The recipe to remove.
     */
    void removeRecipe(IAlloyingCrucibleRecipe recipe);
}
