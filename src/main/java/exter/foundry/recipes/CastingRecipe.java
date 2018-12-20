package exter.foundry.recipes;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

import exter.foundry.api.recipe.ICastingRecipe;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Metal Caster recipe manager
 */
public class CastingRecipe implements ICastingRecipe
{
    private final FluidStack fluid;
    private final IItemMatcher mold;
    private final IItemMatcher extra;

    private final IItemMatcher output;

    private final int speed;
    private final boolean consume_mold;

    public CastingRecipe(IItemMatcher result, FluidStack in_fluid, IItemMatcher in_mold, boolean in_comsume_mold, @Nullable IItemMatcher in_extra, int cast_speed)
    {
        Preconditions.checkArgument(in_fluid != null);
        Preconditions.checkArgument(in_mold != null);
        Preconditions.checkArgument(cast_speed > 0);
        Preconditions.checkArgument(result != null);
        output = result;
        fluid = in_fluid.copy();
        mold = in_mold;
        extra = in_extra;
        speed = cast_speed;
        consume_mold = in_comsume_mold;
    }

    @Override
    public boolean containsExtra(ItemStack stack)
    {
        return extra.apply(stack);
    }

    @Override
    public int getCastingSpeed()
    {
        return speed;
    }

    @Override
    public FluidStack getInput()
    {
        return fluid.copy();
    }

    @Override
    public IItemMatcher getInputExtra()
    {
        return extra;
    }

    @Override
    public ItemStack getMold()
    {
        return mold.getItem();
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
    public boolean matchesRecipe(ItemStack mold_stack, FluidStack fluid_stack, ItemStack in_extra)
    {
        return fluid_stack != null && fluid_stack.containsFluid(fluid) && mold.test(mold_stack)
                && (!requiresExtra() || extra.apply(in_extra));
    }

    @Override
    public boolean requiresExtra()
    {
        return extra != null;
    }

    @Override
    public boolean consumesMold()
    {
        return consume_mold;
    }
}
