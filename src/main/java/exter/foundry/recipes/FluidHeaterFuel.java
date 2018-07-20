package exter.foundry.recipes;

import exter.foundry.api.recipe.IFluidHeaterFuel;
import net.minecraftforge.fluids.Fluid;

public class FluidHeaterFuel implements IFluidHeaterFuel
{
    private final Fluid fluid;

    public FluidHeaterFuel(Fluid fluid)
    {
        this.fluid = fluid;
    }

    @Override
    public Fluid getFluid()
    {
        return fluid;
    }

}
