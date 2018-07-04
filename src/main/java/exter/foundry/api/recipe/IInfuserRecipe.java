package exter.foundry.api.recipe;

import exter.foundry.api.recipe.matcher.IItemMatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IInfuserRecipe
{
    public int getEnergyNeeded();

    /**
     * Get the substance required.
     * @return The substance required.
     */
    public IItemMatcher getInput();

    /**
     * Get the fluid required.
     * @return FluidStack containing the required fluid.
     */
    public FluidStack getInputFluid();

    /**
     * Get the produced fluid.
     * @return The fluid that the recipe produces.
     */
    public FluidStack getOutput();

    /**
     * Check if a fluid stack and substance stack matches this recipe
     * @param in_fluid Fluid to check.
     * @param substance_type Substance type to check.
     * @param substance_amount Substance amount to check.
     * @return true if the fluid and substance matches, false otherwise.
     */
    public boolean matchesRecipe(FluidStack in_fluid, ItemStack in_item);
}
