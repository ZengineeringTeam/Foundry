package exter.foundry.container.slot;

import exter.foundry.recipes.manager.BurnerHeaterFuelManager;
import exter.foundry.tileentity.TileEntityBurnerHeater;
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
        return TileEntityBurnerHeater.isValidFuel(stack);
    }

}
