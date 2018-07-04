package exter.foundry.tileentity;

import java.lang.reflect.InvocationTargetException;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

/**
 * Base class for all machines.
 */
@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "ic2")
public abstract class TileEntityFoundryPowered extends TileEntityFoundry implements IEnergySink
{

    private class ForgeEnergyConsumer implements IEnergyStorage
    {

        @Override
        public boolean canExtract()
        {
            return false;
        }

        @Override
        public boolean canReceive()
        {
            return true;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate)
        {
            return 0;
        }

        @Override
        public int getEnergyStored()
        {
            return energy_stored / RATIO_FE;
        }

        @Override
        public int getMaxEnergyStored()
        {
            return 0;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate)
        {
            return receiveFoundryEnergy(maxReceive * RATIO_FE, !simulate, false) / RATIO_FE;
        }
    }

    static public int RATIO_RF = 1;
    static public int RATIO_FE = 1;

    static public int RATIO_EU = 4;

    private boolean added_enet;
    protected boolean update_energy;
    protected boolean update_energy_tick;
    private int energy_stored;

    private final ForgeEnergyConsumer fe;

    public TileEntityFoundryPowered()
    {
        fe = new ForgeEnergyConsumer();
        update_energy = false;
        update_energy_tick = true;
        added_enet = false;
    }

    @Optional.Method(modid = "ic2")
    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing direction)
    {
        return true;
    }

    @Override
    public <T> T getCapability(Capability<T> cap, EnumFacing facing)
    {
        if (cap == CapabilityEnergy.ENERGY)
        {
            return CapabilityEnergy.ENERGY.cast(fe);
        }
        else
        {
            return super.getCapability(cap, facing);
        }
    }

    @Optional.Method(modid = "ic2")
    @Override
    public double getDemandedEnergy()
    {
        return (double) (getFoundryEnergyCapacity() - getStoredFoundryEnergy()) / RATIO_EU;
    }

    public abstract int getFoundryEnergyCapacity();

    @Optional.Method(modid = "ic2")
    @Override
    public int getSinkTier()
    {
        return 1;
    }

    public int getStoredFoundryEnergy()
    {
        int capacity = getFoundryEnergyCapacity();
        if (energy_stored > capacity)
        {
            return capacity;
        }
        else
        {
            return energy_stored;
        }
    }

    @Override
    public boolean hasCapability(Capability<?> cap, EnumFacing facing)
    {
        if (cap == CapabilityEnergy.ENERGY)
        {
            return true;
        }
        else
        {
            return super.hasCapability(cap, facing);
        }
    }

    @Optional.Method(modid = "ic2")
    @Override
    public double injectEnergy(EnumFacing directionFrom, double amount, double voltage)
    {
        double use_amount = Math.max(Math.min(amount, getDemandedEnergy()), 0);

        return amount - receiveEU(use_amount, true);
    }

    @Optional.Method(modid = "ic2")
    public void loadEnet()
    {
        if (!added_enet && !getWorld().isRemote)
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            added_enet = true;
        }
    }

    @Override
    public void onChunkUnload()
    {
        if (Loader.isModLoaded("ic2"))
            unloadEnet();
    }

    @Override
    protected void onInitialize()
    {
        update_energy_tick = true;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        if (compound.hasKey("energy"))
        {
            energy_stored = compound.getInteger("energy");
        }
    }

    private double receiveEU(double eu, boolean do_receive)
    {
        return (double) receiveFoundryEnergy((int) (eu * RATIO_EU), do_receive, true) / RATIO_EU;
    }

    private int receiveFoundryEnergy(int en, boolean do_receive, boolean allow_overflow)
    {
        if (!allow_overflow)
        {
            int needed = getFoundryEnergyCapacity() - energy_stored;
            if (en > needed)
            {
                en = needed;
            }
        }
        if (do_receive)
        {
            energy_stored += en;
            if (en > 0)
            {
                if (update_energy && !world.isRemote)
                {
                    update_energy_tick = true;
                }
            }
        }
        return en;
    }

    @Optional.Method(modid = "ic2")
    public void unloadEnet()
    {
        if (added_enet)
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            added_enet = false;
        }
    }

    @Override
    public void update()
    {
        if (!added_enet)
        {
            try
            {
                getClass().getMethod("loadEnet").invoke(this);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
            catch (IllegalArgumentException e)
            {
                throw new RuntimeException(e);
            }
            catch (InvocationTargetException e)
            {
                throw new RuntimeException(e);
            }
            catch (NoSuchMethodException e)
            {
                if (Loader.isModLoaded("ic2"))
                {
                    throw new RuntimeException(e);
                }
            }
            catch (SecurityException e)
            {
                throw new RuntimeException(e);
            }
        }
        super.update();
    }

    private void updateFoundryEnergy()
    {
        if (update_energy)
        {
            updateValue("energy", energy_stored);
        }
    }

    @Override
    public void updateRedstone()
    {
        redstone_signal = world.isBlockIndirectlyGettingPowered(getPos()) > 0;
    }

    @Override
    protected void updateServer()
    {
        if (update_energy_tick)
        {
            updateFoundryEnergy();
            update_energy_tick = false;
        }
    }

    public int useFoundryEnergy(int amount, boolean do_use)
    {
        if (amount > energy_stored)
        {
            amount = energy_stored;
        }
        if (do_use)
        {
            energy_stored -= amount;
            updateFoundryEnergy();
        }
        return amount;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        if (compound == null)
        {
            compound = new NBTTagCompound();
        }
        super.writeToNBT(compound);
        compound.setInteger("energy", energy_stored);
        return compound;
    }
}
