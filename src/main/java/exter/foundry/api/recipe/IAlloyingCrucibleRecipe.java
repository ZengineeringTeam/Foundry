package exter.foundry.api.recipe;

import net.minecraftforge.fluids.FluidStack;

public interface IAlloyingCrucibleRecipe
{
    /**
     * Get the recipe's input A.
     */
    FluidStack getInputA();

    /**
     * Get the recipe's input B.
     */
    FluidStack getInputB();

    /**
     * Get the recipe's output.
     */
    FluidStack getOutput();

    /**
     * Check if the fluids matches this recipe.
     * @param input_a fluid to compare.
     * @param input_b fluid to compare.
     * @return true if the fluids matches, false otherwise.
     */
    boolean matchesRecipe(FluidStack input_a, FluidStack input_b);
}
