package exter.foundry.api.recipe.manager;

import java.util.List;

import exter.foundry.api.recipe.IFluidHeaterFuel;
import net.minecraftforge.fluids.Fluid;

public interface IFluidHeaterFuelManager
{
    public void addFuel(IFluidHeaterFuel fuel);

    public void addFuel(Fluid fluid);

    public IFluidHeaterFuel getFuel(Fluid fluid);

    public List<IFluidHeaterFuel> getFuels();

    public void removeFuel(IFluidHeaterFuel fuel);
}
