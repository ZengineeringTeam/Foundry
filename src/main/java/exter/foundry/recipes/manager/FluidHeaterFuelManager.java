package exter.foundry.recipes.manager;

import java.util.List;

import javax.annotation.Nullable;

import exter.foundry.api.recipe.IFluidHeaterFuel;
import exter.foundry.recipes.FluidHeaterFuel;
import exter.foundry.tileentity.TileEntityFoundryHeatable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.Fluid;

public class FluidHeaterFuelManager implements exter.foundry.api.recipe.manager.IFluidHeaterFuelManager
{
    public static final FluidHeaterFuelManager INSTANCE = new FluidHeaterFuelManager();

    private final NonNullList<IFluidHeaterFuel> fuels;

    private FluidHeaterFuelManager()
    {
        fuels = NonNullList.create();
    }

    @Override
    public void addFuel(IFluidHeaterFuel fuel)
    {
        if (!fuels.contains(fuel))
            fuels.add(fuel);
    }

    @Override
    public void addFuel(Fluid fluid)
    {
        if (fluid != null && getFuel(fluid) == null)
        {
            addFuel(new FluidHeaterFuel(fluid));
        }
    }

    @Override
    @Nullable
    public IFluidHeaterFuel getFuel(Fluid fluid)
    {
        for (IFluidHeaterFuel f : fuels)
            if (f.getFluid().getName().equals(fluid.getName()))
                return f;
        return null;
    }

    @Override
    public List<IFluidHeaterFuel> getFuels()
    {
        return fuels;
    }

    @Override
    public int getHeatNeeded(int heat_loss_rate, int temperature)
    {
        return TileEntityFoundryHeatable.getMaxHeatRecieve(temperature, heat_loss_rate);
    }

    @Override
    public void removeFuel(IFluidHeaterFuel fuel)
    {
        fuels.remove(fuel);
    }

}
