package exter.foundry.container;

import exter.foundry.container.slot.SlotFluidContainer;
import exter.foundry.tileentity.TileEntityRefractoryHopper;
import exter.foundry.tileentity.TileEntityRefractoryTankBasic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class ContainerRefractoryTank extends Container
{
    // Slot numbers
    private static final int SLOTS_TE = 0;

    private static final int SLOTS_TE_SIZE = 2;
    private static final int SLOTS_INVENTORY = 2;

    private static final int SLOTS_HOTBAR = 2 + 3 * 9;
    private static final int SLOT_INVENTORY_X = 8;

    private static final int SLOT_INVENTORY_Y = 83;
    private static final int SLOT_HOTBAR_X = 8;

    private static final int SLOT_HOTBAR_Y = 141;
    private final TileEntityRefractoryTankBasic te_tank;

    public ContainerRefractoryTank(TileEntityRefractoryTankBasic tank, EntityPlayer player)
    {
        te_tank = tank;
        te_tank.openInventory(player);
        int i, j;

        addSlotToContainer(
                new SlotFluidContainer(te_tank, TileEntityRefractoryHopper.INVENTORY_CONTAINER_DRAIN, 94, 20));
        addSlotToContainer(
                new SlotFluidContainer(te_tank, TileEntityRefractoryHopper.INVENTORY_CONTAINER_FILL, 94, 48));

        //Player Inventory
        for (i = 0; i < 3; ++i)
        {
            for (j = 0; j < 9; ++j)
            {
                addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, SLOT_INVENTORY_X + j * 18,
                        SLOT_INVENTORY_Y + i * 18));
            }
        }
        for (i = 0; i < 9; ++i)
        {
            addSlotToContainer(new Slot(player.inventory, i, SLOT_HOTBAR_X + i * 18, SLOT_HOTBAR_Y));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return te_tank.isUsableByPlayer(par1EntityPlayer);
    }

    @Override
    public void onContainerClosed(EntityPlayer player)
    {
        super.onContainerClosed(player);
        te_tank.closeInventory(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot_index)
    {
        ItemStack slot_stack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(slot_index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack stack = slot.getStack();
            slot_stack = stack.copy();

            if (slot_index >= SLOTS_INVENTORY && slot_index <= SLOTS_HOTBAR + 9)
            {
                if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null))
                {
                    FluidStack f = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
                            .getTankProperties()[0].getContents();
                    if (f != null && mergeItemStack(stack, TileEntityRefractoryHopper.INVENTORY_CONTAINER_DRAIN,
                            TileEntityRefractoryHopper.INVENTORY_CONTAINER_DRAIN + 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                    else if (mergeItemStack(stack, TileEntityRefractoryHopper.INVENTORY_CONTAINER_FILL,
                            TileEntityRefractoryHopper.INVENTORY_CONTAINER_FILL + 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                if (!mergeItemStack(stack, SLOTS_TE, SLOTS_TE + SLOTS_TE_SIZE, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (slot_index >= SLOTS_HOTBAR && slot_index < SLOTS_HOTBAR + 9)
            {
                if (!mergeItemStack(stack, SLOTS_INVENTORY, SLOTS_INVENTORY + 3 * 9, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!mergeItemStack(stack, SLOTS_INVENTORY, SLOTS_HOTBAR + 9, true))
            {
                return ItemStack.EMPTY;
            }

            slot.onSlotChanged();
            if (stack.getCount() == slot_stack.getCount())
            {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, stack);
        }

        return slot_stack;
    }
}
