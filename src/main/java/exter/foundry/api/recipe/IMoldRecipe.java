package exter.foundry.api.recipe;

import net.minecraft.item.ItemStack;

public interface IMoldRecipe
{
    public int getHeight();

    public ItemStack getOutput();

    public int[] getRecipeGrid();

    public int getWidth();

    public boolean matchesRecipe(int[] grid);
}
