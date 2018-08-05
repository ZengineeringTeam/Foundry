package exter.foundry.tileentity;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.heatable.IHeatProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.Optional;

public class TileEntityInductionHeater extends TileEntityPowered
{
    private class HeatProvider implements IHeatProvider
    {
        @Override
        public int provideHeat(int max_heat, int heat)
        {
            int used = useFoundryEnergy(max_heat * 3 / 20, true); // TODO: dynamic
            return used * 20 / 3;
        }
    }

    private final HeatProvider heat_provider;

    public TileEntityInductionHeater()
    {
        super();
        heat_provider = new HeatProvider();
    }

    @Override
    public <T> T getCapability(Capability<T> cap, EnumFacing facing)
    {
        if (cap == FoundryAPI.HEAT_PROVIDER_CAP && facing == EnumFacing.UP)
        {
            return FoundryAPI.HEAT_PROVIDER_CAP.cast(heat_provider);
        }
        return super.getCapability(cap, facing);
    }

    @Optional.Method(modid = "IC2")
    @Override
    public int getSinkTier()
    {
        return 2;
    }

    @Override
    public int getSizeInventory()
    {
        return 0;
    }

    @Override
    public FluidTank getTank(int slot)
    {
        return null;
    }

    @Override
    public int getTankCount()
    {
        return 0;
    }

    @Override
    public boolean hasCapability(Capability<?> cap, EnumFacing facing)
    {
        return cap == FoundryAPI.HEAT_PROVIDER_CAP && facing == EnumFacing.UP || super.hasCapability(cap, facing);
    }

    @Override
    public int getFoundryEnergyCapacity()
    {
        return 25000;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return false;
    }

    @Override
    protected void updateClient()
    {
    }

    @Override
    protected void updateServer()
    {
        super.updateServer();
    }
}
