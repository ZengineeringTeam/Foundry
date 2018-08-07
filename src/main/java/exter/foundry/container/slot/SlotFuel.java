package exter.foundry.container.slot;

import exter.foundry.recipes.manager.BurnerHeaterFuelManager;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class SlotFuel extends Slot
{

    public SlotFuel(IInventory inventoryIn, int index, int xPosition, int yPosition)
    {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        if (stack.getItem() == Items.LAVA_BUCKET)
        {
            return false;
        }
        return BurnerHeaterFuelManager.INSTANCE.getFuel(stack) != null || TileEntityFurnace.isItemFuel(stack);
    }

}
