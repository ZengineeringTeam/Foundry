package exter.foundry.recipes;

import exter.foundry.api.recipe.ICastingTableRecipe;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Casting Table recipe manager
 */
public class CastingTableRecipe implements ICastingTableRecipe
{
    private final FluidStack fluid;
    private final ICastingTableRecipe.TableType type;

    private final IItemMatcher output;

    public CastingTableRecipe(IItemMatcher result, FluidStack fluid, ICastingTableRecipe.TableType type)
    {

        if (type == null)
        {
            throw new IllegalArgumentException("Casting Table type cannot be null.");
        }
        if (fluid == null)
        {
            throw new IllegalArgumentException("Casting Table recipe fluid cannot be null.");
        }

        this.type = type;
        output = result;
        this.fluid = fluid.copy();
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
    public TableType getTableType()
    {
        return type;
    }
}
