package exter.foundry.api.recipe;

import exter.foundry.api.recipe.matcher.IItemMatcher;

public interface IBurnerHeaterFuel
{

    public int getBurnTime();

    public IItemMatcher getFuel();

    public int getHeat();
}
