package exter.foundry.tileentity;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.ICastingRecipe;
import exter.foundry.config.FoundryConfig;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.util.MiscUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityMetalCaster extends TileEntityPowered
{
    static public final int ENERGY_REQUIRED = FoundryConfig.metalCasterPower ? 10000 : 0;

    static public final int INVENTORY_OUTPUT = 0;
    static public final int INVENTORY_MOLD = 1;
    static public final int INVENTORY_EXTRA = 2;
    static public final int INVENTORY_CONTAINER_INPUT = 3;
    static public final int INVENTORY_CONTAINER_OUTPUT = 4;
    static public final int INVENTORY_MOLD_STORAGE = 5;
    static public final int INVENTORY_MOLD_STORAGE_SIZE = 9;

    static private final Set<Integer> IH_SLOTS_INPUT = ImmutableSet.of(INVENTORY_EXTRA);
    static private final Set<Integer> IH_SLOTS_OUTPUT = ImmutableSet.of(INVENTORY_OUTPUT);

    private final FluidTank tank;
    private final IFluidHandler fluid_handler;
    private final ItemHandler item_handler;

    private ICastingRecipe currentRecipe;

    private int progress;
    private int totalTick;

    public TileEntityMetalCaster()
    {
        super();

        tank = new FluidTank(FoundryAPI.CASTER_TANK_CAPACITY);
        fluid_handler = new FluidHandler(0, 0);
        item_handler = new ItemHandler(getSizeInventory(), IH_SLOTS_INPUT, IH_SLOTS_OUTPUT);

        currentRecipe = null;

        addContainerSlot(new ContainerSlot(0, INVENTORY_CONTAINER_INPUT, INVENTORY_CONTAINER_OUTPUT, false));
    }

    private void beginCasting()
    {
        if (currentRecipe != null && canCastCurrentRecipe() && ((!FoundryConfig.metalCasterPower) || getStoredFoundryEnergy() >= ENERGY_REQUIRED))
        {
            if (FoundryConfig.metalCasterPower)
                useFoundryEnergy(ENERGY_REQUIRED, true);
            totalTick = progress = currentRecipe.getCastingTime();
        }
    }

    private boolean canCastCurrentRecipe()
    {
        if (currentRecipe.requiresExtra())
        {
            if (!currentRecipe.containsExtra(inventory.get(INVENTORY_EXTRA)))
            {
                return false;
            }
        }

        ItemStack recipe_output = currentRecipe.getOutput();

        ItemStack inv_output = inventory.get(INVENTORY_OUTPUT);
        if (!inv_output.isEmpty() && (!inv_output.isItemEqual(recipe_output) || inv_output.getCount() + recipe_output.getCount() > inv_output.getMaxStackSize()))
        {
            return false;
        }
        return true;
    }

    private void checkCurrentRecipe()
    {
        if (currentRecipe == null)
        {
            totalTick = progress = -1;
            return;
        }

        if (!currentRecipe.matchesRecipe(inventory.get(INVENTORY_MOLD), tank.getFluid(), inventory.get(INVENTORY_EXTRA)))
        {
            totalTick = progress = -1;
            currentRecipe = null;
            return;
        }
    }

    @Override
    protected IFluidHandler getFluidHandler(EnumFacing facing)
    {
        return fluid_handler;
    }

    @Override
    protected IItemHandler getItemHandler(EnumFacing side)
    {
        return item_handler;
    }

    public int getProgress()
    {
        return progress;
    }

    @Override
    public int getSizeInventory()
    {
        return 14;
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
    public boolean isItemValidForSlot(int slot, ItemStack itemstack)
    {
        return slot == INVENTORY_EXTRA;
    }

    @Override
    public void readFromNBT(NBTTagCompound compund)
    {
        super.readFromNBT(compund);

        if (compund.hasKey("progress"))
        {
            progress = compund.getInteger("progress");
        }
        if (compund.hasKey("total"))
        {
            totalTick = compund.getInteger("total");
        }
    }

    @Override
    protected void updateClient()
    {
    }

    @Override
    protected void updateServer()
    {
        super.updateServer();
        int last_progress = progress;

        checkCurrentRecipe();

        if (currentRecipe == null)
        {
            currentRecipe = CastingRecipeManager.INSTANCE.findRecipe(tank.getFluid(), inventory.get(INVENTORY_MOLD), inventory.get(INVENTORY_EXTRA));
            totalTick = progress = -1;
        }

        if (progress < 0)
        {
            switch (getRedstoneMode())
            {
            case RSMODE_IGNORE:
                beginCasting();
                break;
            case RSMODE_OFF:
                if (!redstone_signal)
                {
                    beginCasting();
                }
                break;
            case RSMODE_ON:
                if (redstone_signal)
                {
                    beginCasting();
                }
                break;
            case RSMODE_PULSE:
                if (redstone_signal && !last_redstone_signal)
                {
                    beginCasting();
                }
                break;
            }
        }
        else
        {
            if (canCastCurrentRecipe())
            {
                --progress;
                if (progress == 0)
                {
                    totalTick = progress = -1;
                    tank.drain(currentRecipe.getInput().amount, true);
                    if (currentRecipe.requiresExtra())
                    {
                        decrStackSize(INVENTORY_EXTRA, currentRecipe.getInputExtra().getAmount());
                        updateInventoryItem(INVENTORY_EXTRA);
                    }
                    if (inventory.get(INVENTORY_OUTPUT).isEmpty())
                    {
                        inventory.set(INVENTORY_OUTPUT, currentRecipe.getOutput());
                    }
                    else
                    {
                        inventory.get(INVENTORY_OUTPUT).grow(currentRecipe.getOutput().getCount());
                    }
                    if (currentRecipe.consumesMold())
                    {
                        inventory.get(INVENTORY_MOLD).shrink(1);
                    }
                    updateInventoryItem(INVENTORY_OUTPUT);
                    updateTank(0);
                    markDirty();
                }
            }
            else
            {
                totalTick = progress = -1;
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setInteger("progress", progress);
        compound.setInteger("total", totalTick);
        return super.writeToNBT(compound);
    }

    @Override
    public int getFoundryEnergyCapacity()
    {
        return 40000;
    }

    public int getTotalTick()
    {
        return totalTick;
    }

    @Override
    public boolean hasCapability(Capability<?> cap, EnumFacing facing)
    {
        if (cap == CapabilityEnergy.ENERGY)
        {
            return FoundryConfig.metalCasterPower;
        }
        return super.hasCapability(cap, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> cap, EnumFacing facing)
    {
        if (!FoundryConfig.metalCasterPower && cap == CapabilityEnergy.ENERGY)
        {
            return null;
        }
        return super.getCapability(cap, facing);
    }
}
