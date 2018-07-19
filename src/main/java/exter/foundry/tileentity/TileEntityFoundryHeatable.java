package exter.foundry.tileentity;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.heatable.IHeatProvider;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public abstract class TileEntityFoundryHeatable extends TileEntityFoundry
{
    static public final int TEMP_MIN = 29000;

    static public int getMaxHeatRecieve(int max_heat, int temp_loss_rate)
    {
        return (max_heat - TEMP_MIN) / temp_loss_rate;
    }

    private int heat;

    public TileEntityFoundryHeatable()
    {
        super();
        heat = TEMP_MIN;
    }

    abstract protected boolean canReceiveHeat();

    private final IHeatProvider getHeatProvider()
    {
        TileEntity te = world.getTileEntity(getPos().down());
        if (te != null && te.hasCapability(FoundryAPI.HEAT_PROVIDER_CAP, EnumFacing.UP))
        {
            return te.getCapability(FoundryAPI.HEAT_PROVIDER_CAP, EnumFacing.UP);
        }
        return null;
    }

    abstract protected int getMaxTemperature();

    public final int getTemperature()
    {
        return heat;
    }

    abstract protected int getTemperatureLossRate();

    @Override
    public void readFromNBT(NBTTagCompound compund)
    {
        super.readFromNBT(compund);

        if (compund.hasKey("heat"))
        {
            heat = compund.getInteger("heat");
            if (heat < TEMP_MIN)
            {
                heat = TEMP_MIN;
            }
            int temp_max = getMaxTemperature();
            if (heat > temp_max)
            {
                heat = temp_max;
            }
        }
    }

    @Override
    protected void updateServer()
    {
        int last_heat = heat;

        int temp_max = getMaxTemperature();

        if (canReceiveHeat())
        {
            IHeatProvider heater = getHeatProvider();

            if (heater != null)
            {
                heat += heater.provideHeat(getMaxHeatRecieve(temp_max, getTemperatureLossRate()));
            }
        }
        heat -= (heat - TEMP_MIN) / getTemperatureLossRate();
        if (heat > temp_max)
        {
            heat = temp_max;
        }
        if (heat < TEMP_MIN)
        {
            heat = TEMP_MIN;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        if (compound == null)
        {
            compound = new NBTTagCompound();
        }
        super.writeToNBT(compound);
        compound.setInteger("heat", heat);
        return compound;
    }
}
