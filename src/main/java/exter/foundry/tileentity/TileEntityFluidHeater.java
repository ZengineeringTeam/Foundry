package exter.foundry.tileentity;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.heatable.IHeatProvider;
import exter.foundry.api.recipe.IFluidHeaterFuel;
import exter.foundry.recipes.manager.FluidHeaterFuelManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileEntityFluidHeater extends TileEntityFoundry
{
    private final FluidTank tank;
    private final HeatProvider heatProvider = new HeatProvider();

    public static final int INVENTORY_CONTAINER_DRAIN = 0;
    public static final int INVENTORY_CONTAINER_FILL = 1;

    public TileEntityFluidHeater()
    {
        super();
        tank = new FluidTank(FoundryAPI.CRUCIBLE_TANK_CAPACITY)
        {
            @Override
            public boolean canFillFluidType(FluidStack fluid)
            {
                return canFill() && FluidHeaterFuelManager.INSTANCE.getFuel(fluid.getFluid()) != null;
            }
        };
    }

    @Override
    public int getSizeInventory()
    {
        return 0;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return false;
    }

    @Override
    public FluidTank getTank(int slot)
    {
        if (slot != 0)
        {
            return null;
        }
        return tank;
    }

    @Override
    public int getTankCount()
    {
        return 1;
    }

    @Override
    protected void onInitialize()
    {
    }

    @Override
    protected void updateClient()
    {
    }

    @Override
    protected void updateServer()
    {
    }

    @Override
    public <T> T getCapability(Capability<T> cap, EnumFacing facing)
    {
        if (cap == FoundryAPI.HEAT_PROVIDER_CAP && facing == EnumFacing.UP)
        {
            return FoundryAPI.HEAT_PROVIDER_CAP.cast(heatProvider);
        }
        return super.getCapability(cap, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> cap, EnumFacing facing)
    {
        return cap == FoundryAPI.HEAT_PROVIDER_CAP && facing == EnumFacing.UP || super.hasCapability(cap, facing);
    }

    @Override
    protected IFluidHandler getFluidHandler(EnumFacing facing)
    {
        return tank;
    }

    private static int MAX_PROVIDE = TileEntityHeatable.getMaxHeatRecieve(350000,
            FoundryAPI.CRUCIBLE_BASIC_TEMP_LOSS_RATE);

    private class HeatProvider implements IHeatProvider
    {
        @Override
        public int provideHeat(int max_heat)
        {
            FluidStack fluidStack = tank.drain(1, true);
            if (fluidStack == null)
            {
                return 0;
            }
            IFluidHeaterFuel fuel = FluidHeaterFuelManager.INSTANCE.getFuel(fluidStack.getFluid());
            if (fuel != null && fuel.getHeat() > TileEntityHeatable.TEMP_MIN)
            {
                return Math.min(fuel.getHeat(), MAX_PROVIDE);
            }
            return 0;
        }
    }

}
