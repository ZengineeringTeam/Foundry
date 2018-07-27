package exter.foundry.container;

import exter.foundry.container.slot.SlotCasterMold;
import exter.foundry.container.slot.SlotFluidContainer;
import exter.foundry.container.slot.SlotOutput;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.tileentity.TileEntityMetalCaster;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMetalCaster extends ContainerFoundry
{

    // Slot numbers
    public static final int SLOTS_TE = 0;

    public static final int SLOTS_TE_SIZE = 14;
    private static final int SLOTS_TE_MOLD_STORAGE = 5;
    private static final int SLOTS_TE_MOLD_STORAGE_SIZE = 9;
    public static final int SLOTS_INVENTORY = 14;
    private static final int SLOTS_HOTBAR = 14 + 3 * 9;
    private static final int SLOT_INVENTORY_X = 8;

    private static final int SLOT_INVENTORY_Y = 84;
    private static final int SLOT_HOTBAR_X = 8;

    private static final int SLOT_HOTBAR_Y = 142;
    private static final int SLOT_STORAGE_X = 116;

    private static final int SLOT_STORAGE_Y = 21;
    private final TileEntityMetalCaster te_caster;

    public ContainerMetalCaster(TileEntityMetalCaster caster, EntityPlayer player)
    {
        super(caster);
        te_caster = caster;
        te_caster.openInventory(player);
        int i, j;

        addSlotToContainer(new SlotOutput(te_caster, TileEntityMetalCaster.INVENTORY_OUTPUT, 86, 51));
        addSlotToContainer(new SlotCasterMold(te_caster, TileEntityMetalCaster.INVENTORY_MOLD, 66, 21));
        addSlotToContainer(new Slot(te_caster, TileEntityMetalCaster.INVENTORY_EXTRA, 86, 21));
        addSlotToContainer(new SlotFluidContainer(te_caster, TileEntityMetalCaster.INVENTORY_CONTAINER_INPUT, 11, 21));
        addSlotToContainer(new SlotOutput(te_caster, TileEntityMetalCaster.INVENTORY_CONTAINER_OUTPUT, 11, 51));
        for (i = 0; i < 3; ++i)
        {
            for (j = 0; j < 3; ++j)
            {
                addSlotToContainer(
                        new SlotCasterMold(te_caster, TileEntityMetalCaster.INVENTORY_MOLD_STORAGE + i * 3 + j,
                                SLOT_STORAGE_X + 18 * j, SLOT_STORAGE_Y + 18 * i));
            }
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
    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return te_caster.isUsableByPlayer(par1EntityPlayer);
    }

    @Override
    public void onContainerClosed(EntityPlayer player)
    {
        super.onContainerClosed(player);
        te_caster.closeInventory(player);
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
                if (CastingRecipeManager.INSTANCE.isItemMold(stack))
                {
                    int mold_slot = SLOTS_TE + TileEntityMetalCaster.INVENTORY_MOLD;
                    if (((SlotCasterMold) inventorySlots.get(mold_slot)).getStack().isEmpty())
                    {
                        if (!mergeItemStack(stack, mold_slot, mold_slot + 1, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else
                    {
                        mold_slot = SLOTS_TE + TileEntityMetalCaster.INVENTORY_MOLD_STORAGE;
                        if (!mergeItemStack(stack, mold_slot, mold_slot + SLOTS_TE_MOLD_STORAGE_SIZE, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                }
                else
                {
                    if (!mergeItemStack(stack, SLOTS_TE, TileEntityMetalCaster.INVENTORY_CONTAINER_OUTPUT + 1, true))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }
            else if (slot_index >= SLOTS_HOTBAR && slot_index < SLOTS_HOTBAR + 9)
            {
                if (!mergeItemStack(stack, SLOTS_INVENTORY, SLOTS_INVENTORY + 3 * 9, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (slot_index == SLOTS_TE + TileEntityMetalCaster.INVENTORY_MOLD)
            {
                if (!mergeItemStack(stack, SLOTS_TE_MOLD_STORAGE, SLOTS_TE_MOLD_STORAGE + SLOTS_TE_MOLD_STORAGE_SIZE,
                        false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (slot_index >= SLOTS_TE_MOLD_STORAGE
                    && slot_index < SLOTS_TE_MOLD_STORAGE + SLOTS_TE_MOLD_STORAGE_SIZE)
            {
                SlotCasterMold storage_slot = (SlotCasterMold) inventorySlots.get(slot_index);
                SlotCasterMold output_slot = (SlotCasterMold) inventorySlots
                        .get(SLOTS_TE + TileEntityMetalCaster.INVENTORY_MOLD);
                ItemStack tmp = storage_slot.getStack();
                if (tmp.isEmpty())
                {
                    return ItemStack.EMPTY;
                }
                slot_stack = tmp;
                storage_slot.putStack(output_slot.getStack());
                output_slot.putStack(tmp);
                storage_slot.onSlotChanged();
                output_slot.onSlotChanged();
            }
            else if (!mergeItemStack(stack, SLOTS_INVENTORY, SLOTS_HOTBAR + 9, true))
            {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (stack.getCount() == slot_stack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
        }

        return slot_stack;
    }
}
