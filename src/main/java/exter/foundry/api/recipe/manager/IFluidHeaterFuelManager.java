package exter.foundry.api.recipe.manager;

import java.util.List;

import exter.foundry.api.recipe.IFluidHeaterFuel;
import net.minecraftforge.fluids.Fluid;

public interface IFluidHeaterFuelManager
{
    public void addFuel(IFluidHeaterFuel fuel);

    public void addFuel(Fluid fluid);

    public IFluidHeaterFuel getFuel(Fluid item);

    public List<IFluidHeaterFuel> getFuels();

    public int getHeatNeeded(int heat_loss_rate, int temperature);

    public void removeFuel(IFluidHeaterFuel fuel);
}
