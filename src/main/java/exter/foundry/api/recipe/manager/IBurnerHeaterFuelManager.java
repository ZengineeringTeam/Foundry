package exter.foundry.api.recipe.manager;

import java.util.List;

import exter.foundry.api.recipe.IBurnerHeaterFuel;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import net.minecraft.item.ItemStack;

public interface IBurnerHeaterFuelManager
{

    public void addFuel(IBurnerHeaterFuel fuel);

    public void addFuel(IItemMatcher item, int burn_time, int heat);

    public IBurnerHeaterFuel getFuel(ItemStack item);

    public List<IBurnerHeaterFuel> getFuels();

    public int getHeatNeeded(int heat_loss_rate, int temperature);

    public void removeFuel(IBurnerHeaterFuel fuel);
}
