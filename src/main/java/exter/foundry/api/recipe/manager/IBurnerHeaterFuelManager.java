package exter.foundry.api.recipe.manager;

import java.util.List;

import exter.foundry.api.recipe.IBurnerHeaterFuel;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import net.minecraft.item.ItemStack;

public interface IBurnerHeaterFuelManager
{

    void addFuel(IBurnerHeaterFuel fuel);

    void addFuel(IItemMatcher item, int burn_time, int heat);

    IBurnerHeaterFuel getFuel(ItemStack item);

    List<IBurnerHeaterFuel> getFuels();

    void removeFuel(IBurnerHeaterFuel fuel);
}
