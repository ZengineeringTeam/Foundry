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
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

/**
 * Base class for all machines.
 */
@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "ic2")
public abstract class TileEntityFoundryPowered extends TileEntityFoundry implements IEnergySink
{
    protected final EnergyStorageMachine energyStorage;

    private class EnergyStorageMachine extends EnergyStorage
    {
        public EnergyStorageMachine(int capacity, int maxReceive, int maxExtract)
        {
            super(capacity, maxReceive, maxExtract);
        }

        public void setEnergy(int energy)
        {
            energy = Math.min(energy, getMaxEnergyStored());
        }

        public int useEnergy(int maxExtract, boolean simulate)
        {
            int energyExtracted = Math.min(energy, maxExtract);
            if (!simulate)
                energy -= energyExtracted;
            return energyExtracted;
        }
    }

    public static int RATIO_RF = 1;
    public static int RATIO_EU = 4;

    private boolean added_enet;

    public TileEntityFoundryPowered()
    {
        energyStorage = new EnergyStorageMachine(getFoundryEnergyCapacity(), 512, 0);
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
            return CapabilityEnergy.ENERGY.cast(energyStorage);
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
        return energyStorage.getEnergyStored();
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
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        if (compound.hasKey("energy"))
        {
            energyStorage.setEnergy(compound.getInteger("energy"));
        }
    }

    private double receiveEU(double eu, boolean do_receive)
    {
        return (double) receiveFoundryEnergy((int) (eu * RATIO_EU), do_receive) / RATIO_EU;
    }

    private int receiveFoundryEnergy(int energy, boolean do_receive)
    {
        return energyStorage.receiveEnergy(energy, !do_receive);
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

    @Override
    public void updateRedstone()
    {
        redstone_signal = world.isBlockIndirectlyGettingPowered(getPos()) > 0;
    }

    @Override
    protected void updateServer()
    {
    }

    public int useFoundryEnergy(int amount, boolean do_use)
    {
        return energyStorage.useEnergy(amount, !do_use);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        if (compound == null)
        {
            compound = new NBTTagCompound();
        }
        super.writeToNBT(compound);
        compound.setInteger("energy", energyStorage.getEnergyStored());
        return compound;
    }
}
