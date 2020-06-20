package exter.foundry.tileentity;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.heatable.IHeatProvider;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;

public abstract class TileEntityHeatable extends TileEntityFoundry
{
    public static final int TEMP_MIN = 30000;

    public static int getStableTemperatureNeed(int temperature, int loss_rate)
    {
        return Math.floorDiv(temperature * loss_rate - TEMP_MIN, loss_rate - 1);
    }

    private final int MAX_HEAT_RECEIVE = getStableTemperatureNeed(getMaxTemperature(), getTemperatureLossRate())
            - getMaxTemperature();
    private int heat;

    public TileEntityHeatable()
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

    public static final Object2IntMap<IBlockState> STATE_SOURCES = new Object2IntOpenHashMap<>();

    @Override
    protected void updateServer()
    {
        int temp_max = getMaxTemperature();
        int temp_last = heat;

        if (canReceiveHeat())
        {
            IHeatProvider heater = getHeatProvider();
            int temp_heater = 0;
            if (heater != null)
            {
                temp_heater = Math.max(0, heater.provideHeat(temp_max, temp_last));
            }
            else if (!STATE_SOURCES.isEmpty())
            {
                temp_heater = STATE_SOURCES.getInt(world.getBlockState(getPos().down()));
            }
            if (temp_heater > 0)
            {
                int receive = getHeatReceive(temp_heater);
                heat += receive;
            }
        }

        heat -= getTemperatureLoss(temp_last);

        heat = MathHelper.clamp(heat, TEMP_MIN, temp_max);
    }

    private int getHeatReceive(int temp_heater)
    {
        if (heat > temp_heater)
        {
            return 0;
        }
        return Math.min(MAX_HEAT_RECEIVE, getTemperatureLoss(temp_heater));
    }

    public int getTemperatureLoss(int heat)
    {
        //        System.out.println("(" + heat + " - " + TEMP_MIN + ") / " + getTemperatureLossRate() + " = "
        //                + (heat - TEMP_MIN) / getTemperatureLossRate());
        return (heat - TEMP_MIN) / getTemperatureLossRate();
    }

    abstract public int getMaxTemperature();

    abstract public int getTemperatureLossRate();

    public final int getTemperature()
    {
        return heat;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("heat", heat);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compund)
    {
        super.readFromNBT(compund);

        if (compund.hasKey("heat", Constants.NBT.TAG_INT))
        {
            heat = MathHelper.clamp(compund.getInteger("heat"), TEMP_MIN, getMaxTemperature());
        }
    }

}
