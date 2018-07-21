package exter.foundry.recipes;

import exter.foundry.api.recipe.IFluidHeaterFuel;
import net.minecraftforge.fluids.Fluid;

public class FluidHeaterFuelImpl implements IFluidHeaterFuel
{
    private final Fluid fluid;
    private final int heat;

    public FluidHeaterFuelImpl(Fluid fluid, int heat)
    {
        this.fluid = fluid;
        this.heat = heat;
    }

    @Override
    public Fluid getFluid()
    {
        return fluid;
    }

    @Override
    public int getHeat()
    {
        return heat;
    }

}
