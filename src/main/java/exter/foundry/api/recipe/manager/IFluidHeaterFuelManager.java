package exter.foundry.api.recipe.manager;

import java.util.List;

import exter.foundry.api.recipe.IFluidHeaterFuel;
import net.minecraftforge.fluids.Fluid;

public interface IFluidHeaterFuelManager
{
    void addFuel(IFluidHeaterFuel fuel);

    void addFuel(Fluid fluid);

    IFluidHeaterFuel getFuel(Fluid fluid);

    List<IFluidHeaterFuel> getFuels();

    void removeFuel(IFluidHeaterFuel fuel);
}
