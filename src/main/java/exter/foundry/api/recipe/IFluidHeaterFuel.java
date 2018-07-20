package exter.foundry.api.recipe;

import net.minecraftforge.fluids.Fluid;

public interface IFluidHeaterFuel
{
    public Fluid getFluid();

    default public int getHeat()
    {
        return getFluid().getTemperature() * 100;
    }
}
