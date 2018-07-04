package exter.foundry.recipes.manager;

import java.util.List;

import exter.foundry.api.recipe.IBurnerHeaterFuel;
import exter.foundry.api.recipe.manager.IBurnerHeaterFuelManager;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import exter.foundry.recipes.BurnerHeaterFuel;
import exter.foundry.tileentity.TileEntityFoundryHeatable;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class BurnerHeaterFuelManager implements IBurnerHeaterFuelManager
{
    public static final BurnerHeaterFuelManager INSTANCE = new BurnerHeaterFuelManager();

    private final NonNullList<IBurnerHeaterFuel> fuels;

    private BurnerHeaterFuelManager()
    {
        fuels = NonNullList.create();
    }

    @Override
    public void addFuel(IBurnerHeaterFuel fuel)
    {
        if (!fuels.contains(fuel))
            fuels.add(fuel);
    }

    @Override
    public void addFuel(IItemMatcher fuel, int burn_time, int heat)
    {
        if (!FoundryMiscUtils.isInvalid(fuel))
            addFuel(new BurnerHeaterFuel(fuel, burn_time, heat));
    }

    @Override
    public IBurnerHeaterFuel getFuel(ItemStack item)
    {
        for (IBurnerHeaterFuel f : fuels)
            if (f.getFuel().apply(item))
                return f;
        return null;
    }

    @Override
    public List<IBurnerHeaterFuel> getFuels()
    {
        return fuels;
    }

    @Override
    public int getHeatNeeded(int temperature, int temp_loss_rate)
    {
        return TileEntityFoundryHeatable.getMaxHeatRecieve(temperature, temp_loss_rate);
    }

    @Override
    public void removeFuel(IBurnerHeaterFuel fuel)
    {
        fuels.remove(fuel);
    }
}
