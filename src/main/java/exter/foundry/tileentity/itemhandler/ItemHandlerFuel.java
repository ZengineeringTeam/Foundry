package exter.foundry.tileentity.itemhandler;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import exter.foundry.tileentity.TileEntityFoundry;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class ItemHandlerFuel extends TileEntityFoundry.ItemHandler
{

    public ItemHandlerFuel(TileEntityFoundry te, int slots, Set<Integer> insert_slots, Set<Integer> extract_slots, Set<Integer> fuel_slots)
    {
        te.super(slots, insert_slots, extract_slots);
    }
}
