package exter.foundry.container.slot;

import com.google.common.base.Predicate;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotFiltered extends Slot
{
    private final Predicate<ItemStack> filter;

    public SlotFiltered(IInventory inventory, int par2, int par3, int par4, Predicate<ItemStack> filter)
    {
        super(inventory, par2, par3, par4);
        this.filter = filter;
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return filter.test(stack);
    }
}
