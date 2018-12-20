package exter.foundry.api.recipe;

import net.minecraftforge.fluids.Fluid;

public interface IFluidHeaterFuel
{
    Fluid getFluid();

    default int getHeat()
    {
        return getFluid().getTemperature() * 100;
    }
}
