package exter.foundry.tileentity;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import exter.foundry.api.recipe.ICastingTableRecipe;
import exter.foundry.recipes.manager.CastingTableRecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.FluidTankPropertiesWrapper;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.IItemHandler;

public abstract class TileEntityCastingTableBase extends TileEntityFoundry
{
    protected class FluidHandler implements IFluidHandler
    {
        private final IFluidTankProperties[] props;

        public FluidHandler()
        {
            props = new IFluidTankProperties[getTankCount()];
            for (int i = 0; i < props.length; i++)
            {
                props[i] = new FluidTankPropertiesWrapper(getTank(i));
            }
        }

        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain)
        {
            if (progress > 0)
            {
                return null;
            }
            FluidStack result = drainTank(0, resource, doDrain);
            if (doDrain)
            {
                setRecipe(tank.getFluid());
                sendDataToClientSide();
            }
            return result;
        }

        @Override
        public FluidStack drain(int maxDrain, boolean doDrain)
        {
            if (progress > 0)
            {
                return null;
            }
            FluidStack result = drainTank(0, maxDrain, doDrain);
            if (doDrain)
            {
                setRecipe(tank.getFluid());
                sendDataToClientSide();
            }
            return result;
        }

        @Override
        public int fill(FluidStack resource, boolean doFill)
        {
            if (!inventory.get(0).isEmpty())
            {
                return 0;
            }
            ICastingTableRecipe testRecipe = CastingTableRecipeManager.INSTANCE.findRecipe(resource, getTableType());
            if (testRecipe == null)
            {
                return 0;
            }
            if (doFill && (tank.getFluid() == null || tank.getFluid().amount == 0))
            {
                setRecipe(resource);
                sendDataToClientSide();
            }
            return fillTank(0, resource, doFill);
        }

        @Override
        public IFluidTankProperties[] getTankProperties()
        {
            return props;
        }
    }

    private class ItemHandlerTable extends ItemHandler
    {
        public ItemHandlerTable(int slots, Set<Integer> insert_slots, Set<Integer> extract_slots)
        {
            super(slots, insert_slots, extract_slots);
        }

        @Override
        protected boolean canInsert(int slot, ItemStack stack)
        {
            return false;
        }
    }

    static public final int CAST_TIME = 200;

    static private final Set<Integer> IH_SLOTS_INPUT = ImmutableSet.of();
    static private final Set<Integer> IH_SLOTS_OUTPUT = ImmutableSet.of(0);

    private final FluidTank tank;

    private final IFluidHandler fluid_handler;

    private ICastingTableRecipe recipe;

    private int progress;
    private final ItemHandlerTable item_handler;

    public TileEntityCastingTableBase()
    {
        super();

        tank = new FluidTank(getDefaultCapacity());
        fluid_handler = new FluidHandler();

        progress = 0;
        recipe = null;
        item_handler = new ItemHandlerTable(getSizeInventory(), IH_SLOTS_INPUT, IH_SLOTS_OUTPUT);
    }

    abstract public int getDefaultCapacity();

    @Override
    protected IFluidHandler getFluidHandler(EnumFacing facing)
    {
        return fluid_handler;
    }

    @Override
    protected IItemHandler getItemHandler(EnumFacing facing)
    {
        return item_handler;
    }

    public final int getProgress()
    {
        return progress;
    }

    @Override
    public final int getSizeInventory()
    {
        return 1;
    }

    abstract public ICastingTableRecipe.TableType getTableType();

    @Override
    public final FluidTank getTank(int slot)
    {
        return tank;
    }

    @Override
    public final int getTankCount()
    {
        return 1;
    }

    @Override
    public final boolean isItemValidForSlot(int slot, ItemStack itemstack)
    {
        return false;
    }

    @Override
    protected final void onInitialize()
    {
    }

    @Override
    public final void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        if (compound.hasKey("Tank_0"))
        {
            setRecipe(tank.getFluid());
        }
        if (compound.hasKey("progress"))
        {
            progress = compound.getInteger("progress");
        }
        if (world != null && world.isRemote && compound.hasKey("tank_capacity"))
        {
            tank.setCapacity(compound.getInteger("tank_capacity"));
        }
    }

    @Deprecated
    @Override
    public final ItemStack removeStackFromSlot(int slot)
    {
        if (progress > 0)
        {
            return ItemStack.EMPTY;
        }
        return super.removeStackFromSlot(slot);
    }

    private void setRecipe(FluidStack fluid)
    {
        if (fluid == null || fluid.amount == 0)
        {
            recipe = null;
            tank.setCapacity(getDefaultCapacity());
            return;
        }

        recipe = CastingTableRecipeManager.INSTANCE.findRecipe(fluid, getTableType());
        if (recipe != null)
        {
            if (recipe.getOutput().isEmpty())
            {
                recipe = null;
                tank.setCapacity(getDefaultCapacity());
                sendDataToClientSide();
                return;
            }
            tank.setCapacity(recipe.getInput().amount);
            sendDataToClientSide();
        }
    }

    @Override
    protected void updateClient()
    {
    }

    @Override
    protected final void updateServer()
    {
        int last_progress = progress;

        if (progress > 0)
        {
            if (--progress == 0)
            {
                setInventorySlotContents(0, recipe.getOutput());
                tank.setFluid(null);
                updateTank(0);
                setRecipe(null);
            }
        }
        else if (inventory.get(0).isEmpty() && recipe != null && tank.getFluid().amount == recipe.getInput().amount)
        {
            progress = CAST_TIME;
        }

        if (last_progress != progress && last_progress % 10 == 0)
        {
            sendDataToClientSide();
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
        compound.setInteger("progress", progress);
        return compound;
    }

    @Override
    protected void updateInventoryItem(int slot)
    {
        super.updateInventoryItem(slot);
        sendDataToClientSide();
    }

    @Override
    protected void updateTank(int slot)
    {
        super.updateTank(slot);
        sendDataToClientSide();
    }
}
