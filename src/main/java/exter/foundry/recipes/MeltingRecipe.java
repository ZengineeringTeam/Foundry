package exter.foundry.recipes;

import exter.foundry.api.recipe.IMeltingRecipe;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import exter.foundry.integration.crafttweaker.CrTHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Metal Smelter recipe manager
 */
public class MeltingRecipe implements IMeltingRecipe
{
    /**
     * Produced fluid and amount.
     */
    private final FluidStack fluid;

    /**
     * Item required.
     * It can be an {@link ItemStack} of the item or a @{link String} of it's Ore Dictionary name.
     */
    private final IItemMatcher solid;

    /**
     * Melting point of the item in K.
     */
    private final int melting_point;

    private final int melting_speed;

    public MeltingRecipe(IItemMatcher item, FluidStack fluid_stack, int melt, int speed)
    {

        if (fluid_stack == null)
            throw new IllegalArgumentException("Melting recipe fluid cannot be null.");
        if (melt < 296)
            melt = 296;
        if (speed < 1)
            speed = 1;

        solid = item;
        fluid = fluid_stack.copy();
        melting_point = melt;
        melting_speed = speed;
    }

    @Override
    public IItemMatcher getInput()
    {
        return solid;
    }

    @Override
    public int getMeltingPoint()
    {
        return melting_point;
    }

    @Override
    public int getMeltingSpeed()
    {
        return melting_speed;
    }

    @Override
    public FluidStack getOutput()
    {
        return fluid.copy();
    }

    @Override
    public boolean matchesRecipe(ItemStack item)
    {
        return solid.apply(item);
    }

    @Override
    public String toString()
    {
        return String.format("Melting recipe: %s -> %s", CrTHelper.getItemDescription(solid),
                CrTHelper.getFluidDescription(fluid));
    }
}
