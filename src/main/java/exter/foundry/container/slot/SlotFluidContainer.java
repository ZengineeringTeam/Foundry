package exter.foundry.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class SlotFluidContainer extends Slot
{
    public SlotFluidContainer(IInventory inventory, int index, int x, int y)
    {
        super(inventory, index, x, y);
    }

    @Override
    public int getSlotStackLimit()
    {
        return 1;
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
    }
}
