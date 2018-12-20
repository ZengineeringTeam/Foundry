package exter.foundry.api.recipe;

import exter.foundry.api.recipe.matcher.IItemMatcher;

public interface IBurnerHeaterFuel
{

    int getBurnTime();

    IItemMatcher getFuel();

    int getHeat();
}
