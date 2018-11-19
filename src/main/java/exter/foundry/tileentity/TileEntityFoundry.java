package exter.foundry.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.google.common.collect.ImmutableSet;

import exter.foundry.Foundry;
import exter.foundry.network.MessageTileEntitySync;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankPropertiesWrapper;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Base class for all machines.
 */
public abstract class TileEntityFoundry extends TileEntity implements ITickable, IInventory, ICapabilityProvider
{
    /**
     * Links an item slot to a tank for filling/draining containers.
     */
    public class ContainerSlot
    {
        final boolean fill;
        final int tank_slot;
        final int input_slot;
        final int output_slot;

        public ContainerSlot(int tank_slot, int input_slot, int output_slot, boolean fill)
        {
            this.tank_slot = tank_slot;
            this.input_slot = input_slot;
            this.output_slot = output_slot;
            this.fill = fill;
        }

        public void update() // TODO: plenty of bugs
        {
            if (container_timer > 0)
                return;
            ItemStack stackInput = getStackInSlot(input_slot);
            ItemStack stackOutput = getStackInSlot(output_slot);
            if (stackInput.isEmpty() || !stackOutput.isEmpty())
            {
                return;
            }

            FluidTank tank = getTank(tank_slot);
            if (stackInput.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
            {
                IFluidHandlerItem handler = stackInput
                        .getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
                //                ItemStack stackOutput = getStackInSlot(output_slot);
                //                if (!stackOutput.isEmpty() && (stackOutput.getCount() >= stackInput.getMaxStackSize()
                //                        || !stackInput.isItemEqual(stackOutput)
                //                        || !ItemStack.areItemStackTagsEqual(stackInput, stackOutput)))
                //                {
                //                    container_timer = 10;
                //                    return;
                //                }

                FluidStack transfer;
                FluidStack sample = handler.drain(Integer.MAX_VALUE, false);
                boolean isEmpty = sample == null;
                boolean isFull = isEmpty ? false : handler.fill(sample, false) <= 0;
                boolean flag = false;
                if ((fill && !isEmpty) || (!fill && isFull))
                {
                    transfer = FluidUtil.tryFluidTransfer(tank, handler, Fluid.BUCKET_VOLUME, true);
                    if (handler.drain(Integer.MAX_VALUE, false) == null) // is empty
                    {
                        inventory.set(output_slot, handler.getContainer());
                        inventory.set(input_slot, ItemStack.EMPTY);
                        updateInventoryItem(input_slot);
                        updateInventoryItem(output_slot);
                        flag = true;
                    }
                }
                else
                {
                    transfer = FluidUtil.tryFluidTransfer(handler, tank, Fluid.BUCKET_VOLUME, true);
                    if (handler.fill(sample, false) <= 0) // is full
                    {
                        inventory.set(output_slot, handler.getContainer());
                        inventory.set(input_slot, ItemStack.EMPTY);
                        updateInventoryItem(input_slot);
                        updateInventoryItem(output_slot);
                        flag = true;
                    }
                }
                if (transfer != null)
                {
                    container_timer = transfer.amount / 25;
                    if (!flag)
                    {
                        inventory.set(input_slot, handler.getContainer());
                        updateInventoryItem(input_slot);
                    }
                    updateTank(tank_slot);
                }
                else if (!flag)
                {
                    inventory.set(output_slot, inventory.get(input_slot));
                    inventory.set(input_slot, ItemStack.EMPTY);
                    updateInventoryItem(input_slot);
                    updateInventoryItem(output_slot);
                }
            }
        }
    }

    protected class FluidHandler implements IFluidHandler
    {
        private final int fill_tank;
        private final int drain_tank;
        private final IFluidTankProperties[] props;

        public FluidHandler(int fill_tank, int drain_tank)
        {
            this.fill_tank = fill_tank;
            this.drain_tank = drain_tank;
            props = new IFluidTankProperties[getTankCount()];
            for (int i = 0; i < props.length; i++)
            {
                props[i] = new FluidTankPropertiesWrapper(getTank(i));
            }
        }

        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain)
        {
            if (drain_tank < 0)
            {
                return null;
            }
            return drainTank(drain_tank, resource, doDrain);
        }

        @Override
        public FluidStack drain(int maxDrain, boolean doDrain)
        {
            if (drain_tank < 0)
            {
                return null;
            }
            return drainTank(drain_tank, maxDrain, doDrain);
        }

        @Override
        public int fill(FluidStack resource, boolean doFill)
        {
            if (fill_tank < 0)
            {
                return 0;
            }
            return fillTank(fill_tank, resource, doFill);
        }

        @Override
        public IFluidTankProperties[] getTankProperties()
        {
            return props;
        }
    }

    public class ItemHandler extends ItemStackHandler
    {
        protected final int slots;
        protected final ImmutableSet<Integer> insert_slots;
        protected final ImmutableSet<Integer> extract_slots;

        public ItemHandler(int slots, Set<Integer> insert_slots, Set<Integer> extract_slots)
        {
            super(TileEntityFoundry.this.inventory);
            this.slots = slots;
            this.insert_slots = ImmutableSet.copyOf(insert_slots);
            this.extract_slots = ImmutableSet.copyOf(extract_slots);
        }

        protected boolean canExtract(int slot)
        {
            return true;
        }

        protected boolean canInsert(int slot, ItemStack stack)
        {
            return isItemValidForSlot(slot, stack);
        }

        @Override
        public final ItemStack extractItem(int slot, int amount, boolean simulate)
        {
            if (!extract_slots.contains(slot) || !canExtract(slot))
            {
                return ItemStack.EMPTY;
            }
            return super.extractItem(slot, amount, simulate);
        }

        @Override
        public int getSlotLimit(int slot)
        {
            return getInventoryStackLimit();
        }

        @Override
        public final int getSlots()
        {
            return slots;
        }

        @Override
        public final ItemStack getStackInSlot(int slot)
        {
            return inventory.get(slot);
        }

        @Override
        public final ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
        {
            if (!insert_slots.contains(slot) || !canInsert(slot, stack))
            {
                return stack;
            }
            return super.insertItem(slot, stack, simulate);
        }

        @Override
        protected void onContentsChanged(int slot)
        {
            updateInventoryItem(slot);
            markDirty();
        }
    }

    public enum RedstoneMode
    {
        RSMODE_IGNORE(0), RSMODE_ON(1), RSMODE_OFF(2), RSMODE_PULSE(3);

        static public RedstoneMode fromID(int num)
        {
            for (RedstoneMode m : RedstoneMode.values())
            {
                if (m.id == num)
                {
                    return m;
                }
            }
            return RSMODE_IGNORE;
        }

        public final int id;

        private RedstoneMode(int num)
        {
            id = num;
        }
    }

    private RedstoneMode mode;

    private final List<ContainerSlot> conatiner_slots;
    private boolean initialized;

    protected boolean last_redstone_signal;
    protected boolean redstone_signal;
    protected final NonNullList<ItemStack> inventory;

    private int container_timer;

    public TileEntityFoundry()
    {
        conatiner_slots = new ArrayList<>(5);
        last_redstone_signal = false;
        redstone_signal = false;
        initialized = false;
        mode = RedstoneMode.RSMODE_IGNORE;
        inventory = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
        container_timer = 0;
    }

    protected final void addContainerSlot(ContainerSlot cs)
    {
        conatiner_slots.add(cs);
    }

    @Override
    public void clear()
    {

    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
        if (!world.isRemote && player instanceof EntityPlayerMP)
        {
            NBTTagCompound tag = new NBTTagCompound();
            super.writeToNBT(tag);
            tag.setInteger("rsmode", mode.id);
            sendPacketToPlayer(tag, (EntityPlayerMP) player);
        }
    }

    @Override
    public final ItemStack decrStackSize(int slot, int amount)
    {
        if (!getStackInSlot(slot).isEmpty())
        {
            ItemStack is;

            if (getStackInSlot(slot).getCount() <= amount)
            {
                is = getStackInSlot(slot);
                setStackInSlot(slot, ItemStack.EMPTY);
                updateInventoryItem(slot);
                return is;
            }
            else
            {
                is = getStackInSlot(slot).splitStack(amount);
                updateInventoryItem(slot);
                return is;
            }
        }
        else
        {
            return ItemStack.EMPTY;
        }

    }

    protected final FluidStack drainTank(int slot, FluidStack resource, boolean doDrain)
    {
        FluidTank tank = getTank(slot);
        if (resource.isFluidEqual(tank.getFluid()))
        {
            FluidStack drained = tank.drain(resource.amount, doDrain);
            if (doDrain && drained != null && drained.amount > 0)
            {
                updateTank(slot);
                markDirty();
            }
            return drained;
        }
        return null;
    }

    protected final FluidStack drainTank(int slot, int maxDrain, boolean doDrain)
    {
        FluidTank tank = getTank(slot);
        FluidStack drained = tank.drain(maxDrain, doDrain);
        if (doDrain && drained != null && drained.amount > 0)
        {
            updateTank(slot);
            markDirty();
        }
        return drained;
    }

    protected final int fillTank(int slot, FluidStack resource, boolean doFill)
    {
        FluidTank tank = getTank(slot);
        int filled = tank.fill(resource, doFill);
        if (doFill && filled > 0)
        {
            updateTank(slot);
            markDirty();
        }
        return filled;
    }

    @Override
    public <T> T getCapability(Capability<T> cap, EnumFacing facing)
    {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            IFluidHandler fluid_handler = getFluidHandler(facing);
            if (fluid_handler != null)
            {
                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getFluidHandler(facing));
            }
            else
            {
                return super.getCapability(cap, facing);
            }
        }
        else if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            IItemHandler item_handler = getItemHandler(facing);
            if (item_handler != null)
            {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getItemHandler(facing));
            }
            else
            {
                return super.getCapability(cap, facing);
            }
        }
        else
        {
            return super.getCapability(cap, facing);
        }
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return null;
    }

    @Override
    public int getField(int id)
    {
        return 0;
    }

    @Override
    public int getFieldCount()
    {
        return 0;
    }

    protected IFluidHandler getFluidHandler(EnumFacing facing)
    {
        return null;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    protected IItemHandler getItemHandler(EnumFacing facing)
    {
        return null;
    }

    @Override
    public String getName()
    {
        return null;
    }

    public RedstoneMode getRedstoneMode()
    {
        return mode;
    }

    @Override
    public final ItemStack getStackInSlot(int slot)
    {
        return inventory.get(slot);
    }

    public abstract FluidTank getTank(int slot);

    public abstract int getTankCount();

    @Override
    public final SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
    }

    @Override
    public boolean hasCapability(Capability<?> cap, EnumFacing facing)
    {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return getFluidHandler(facing) != null;
        }
        else if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            return getItemHandler(facing) != null;
        }
        else
        {
            return super.hasCapability(cap, facing);
        }
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        initialized = false;
        onChunkUnload();
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack s : inventory)
            if (!s.isEmpty())
                return false;
        return true;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return world.getTileEntity(getPos()) != this ? false : player.getDistanceSq(getPos()) <= 64.0D;
    }

    @Override
    public final void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        if (world.isRemote)
        {
            readFromNBT(pkt.getNbtCompound());
        }
    }

    protected abstract void onInitialize();

    @Override
    public void openInventory(EntityPlayer player)
    {
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        int i;
        for (i = 0; i < getTankCount(); i++)
        {
            NBTTagCompound tag = (NBTTagCompound) compound.getTag("Tank_" + String.valueOf(i));
            if (tag != null)
            {
                FluidTank tank = getTank(i);
                tank.setFluid(null);
                tank.readFromNBT(tag);
            }
        }

        for (i = 0; i < getSizeInventory(); i++)
        {
            NBTTagCompound tag = (NBTTagCompound) compound.getTag("Item_" + String.valueOf(i));
            if (tag != null)
            {
                ItemStack stack = ItemStack.EMPTY;
                if (!tag.getBoolean("empty"))
                {
                    stack = new ItemStack(tag);
                }
                inventory.set(i, stack);
            }
        }
        if (compound.hasKey("rsmode"))
        {
            mode = RedstoneMode.fromID(compound.getInteger("rsmode"));
        }
        if (compound.hasKey("bucket_timer"))
        {
            container_timer = compound.getInteger("container_timer");
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int slot)
    {
        if (!getStackInSlot(slot).isEmpty())
        {
            ItemStack is = getStackInSlot(slot);
            setStackInSlot(slot, ItemStack.EMPTY);
            updateInventoryItem(slot);
            markDirty();
            return is;
        }
        else
        {
            return ItemStack.EMPTY;
        }
    }

    protected void sendPacketToNearbyPlayers(NBTTagCompound data)
    {
        data.setInteger("dim", world.provider.getDimension());
        Foundry.NETWORK.sendToAllAround(new MessageTileEntitySync(data),
                new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 192));
    }

    protected void sendPacketToPlayer(NBTTagCompound data, EntityPlayerMP player)
    {
        data.setInteger("dim", world.provider.getDimension());
        Foundry.NETWORK.sendTo(new MessageTileEntitySync(data), player);
    }

    protected void sendToServer(NBTTagCompound tag)
    {
        if (world.isRemote)
        {
            super.writeToNBT(tag);
            tag.setInteger("dim", world.provider.getDimension());
            Foundry.NETWORK.sendToServer(new MessageTileEntitySync(tag));
        }
    }

    @Override
    public void setField(int id, int value)
    {

    }

    @Override
    public final void setInventorySlotContents(int slot, ItemStack stack)
    {
        setStackInSlot(slot, stack);

        if (stack.getCount() > getInventoryStackLimit())
        {
            stack.setCount(getInventoryStackLimit());
        }
        updateInventoryItem(slot);
        markDirty();
    }

    public void setRedstoneMode(RedstoneMode new_mode)
    {
        if (mode != new_mode)
        {
            mode = new_mode;
            if (world.isRemote)
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setInteger("rsmode", mode.id);
                sendToServer(tag);
            }
        }
    }

    public void setStackInSlot(int slot, ItemStack stack)
    {
        inventory.set(slot, stack);
    }

    @Override
    public void update()
    {
        if (!(initialized || isInvalid()))
        {
            updateRedstone();
            onInitialize();
            initialized = true;
        }

        if (!world.isRemote)
        {
            for (ContainerSlot cs : conatiner_slots)
            {
                cs.update();
            }
            if (container_timer > 0)
            {
                container_timer--;
            }
            updateServer();
        }
        else
        {
            updateClient();
        }
        last_redstone_signal = redstone_signal;
    }

    protected abstract void updateClient();

    @OverridingMethodsMustInvokeSuper
    protected void updateInventoryItem(int slot)
    {
    }

    public void updateRedstone()
    {
        redstone_signal = world.isBlockIndirectlyGettingPowered(getPos()) > 0;
    }

    protected abstract void updateServer();

    @OverridingMethodsMustInvokeSuper
    protected void updateTank(int slot)
    {
    }

    protected final void writeInventoryItemToNBT(NBTTagCompound compound, int slot)
    {
        ItemStack is = getStackInSlot(slot);
        NBTTagCompound tag = new NBTTagCompound();
        if (is != null)
        {
            tag.setBoolean("empty", false);
            is.writeToNBT(tag);
        }
        else
        {
            tag.setBoolean("empty", true);
        }
        compound.setTag("Item_" + String.valueOf(slot), tag);
    }

    protected final void writeTankToNBT(NBTTagCompound compound, int slot)
    {
        NBTTagCompound tag = new NBTTagCompound();
        getTank(slot).writeToNBT(tag);
        compound.setTag("Tank_" + String.valueOf(slot), tag);
    }

    protected void writeTileToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        int i;
        super.writeToNBT(compound);
        for (i = 0; i < getTankCount(); i++)
        {
            writeTankToNBT(compound, i);
        }
        for (i = 0; i < getSizeInventory(); i++)
        {
            writeInventoryItemToNBT(compound, i);
        }
        compound.setInteger("rsmode", mode.id);
        compound.setInteger("container_timer", container_timer);
        return compound;
    }

    public void sendDataToClientSide(EntityPlayerMP player)
    {
        if (!world.isRemote)
        {
            sendPacketToPlayer(getUpdateTag(), player);
        }
    }

    public void sendDataToClientSide()
    {
        if (!world.isRemote)
        {
            sendPacketToNearbyPlayers(getUpdateTag());
        }
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return writeToNBT(new NBTTagCompound());
    }
}
