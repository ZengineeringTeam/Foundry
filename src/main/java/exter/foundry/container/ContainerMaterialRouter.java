package exter.foundry.container;

import exter.foundry.container.slot.SlotOutput;
import exter.foundry.tileentity.TileEntityMaterialRouter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMaterialRouter extends ContainerFoundry
{

    // Slot numbers
    private static final int SLOTS_TE = 0;
    private static final int SLOTS_TE_SIZE = 1;

    //private static final int SLOTS_TE_SIZE = 3;
    private static final int SLOTS_INVENTORY = 9;
    private static final int SLOTS_HOTBAR = 9 + 3 * 9;
    private static final int SLOT_INVENTORY_X = 32;

    private static final int SLOT_INVENTORY_Y = 147;
    private static final int SLOT_HOTBAR_X = 32;

    private static final int SLOT_HOTBAR_Y = 205;
    private final TileEntityMaterialRouter te_router;

    public ContainerMaterialRouter(TileEntityMaterialRouter router, EntityPlayer player)
    {
        super(router);
        te_router = router;
        te_router.openInventory(player);
        int i, j;

        for (i = 0; i < TileEntityMaterialRouter.SLOT_OUTPUT; i++)
        {
            addSlotToContainer(new Slot(router, i, 7, 19 + i * 18));
        }
        for (i = 0; i < 6; i++)
        {
            addSlotToContainer(new SlotOutput(router, i + TileEntityMaterialRouter.SLOT_OUTPUT, 7, 90 + i * 18));
        }

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
    public void detectAndSendChanges()
    {
        // NO-OP
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return te_router.isUsableByPlayer(par1EntityPlayer);
    }

    @Override
    public void onContainerClosed(EntityPlayer player)
    {
        super.onContainerClosed(player);
        te_router.closeInventory(player);
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
