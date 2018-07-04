package exter.foundry.recipes;

import exter.foundry.api.recipe.IAtomizerRecipe;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Metal Caster recipe manager
 */
public class AtomizerRecipe implements IAtomizerRecipe
{
    private final FluidStack fluid;

    private final IItemMatcher output;

    public AtomizerRecipe(IItemMatcher result, FluidStack in_fluid)
    {
        if (in_fluid == null)
        {
            throw new IllegalArgumentException("Atomizer recipe input cannot be null");
        }
        output = result;
        fluid = in_fluid.copy();
    }

    @Override
    public FluidStack getInput()
    {
        return fluid.copy();
    }

    @Override
    public ItemStack getOutput()
    {
        return output.getItem();
    }

    @Override
    public IItemMatcher getOutputMatcher()
    {
        return output;
    }

    @Override
    public boolean matchesRecipe(FluidStack fluid_stack)
    {
        if (getOutput().isEmpty())
        {
            return false;
        }
        return fluid_stack != null && fluid_stack.containsFluid(fluid);
    }
}
