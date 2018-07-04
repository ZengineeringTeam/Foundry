package exter.foundry.recipes.manager;

import java.util.List;

import exter.foundry.api.recipe.IAlloyFurnaceRecipe;
import exter.foundry.api.recipe.manager.IAlloyFurnaceRecipeManager;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import exter.foundry.recipes.AlloyFurnaceRecipe;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class AlloyFurnaceRecipeManager implements IAlloyFurnaceRecipeManager
{
    public static final AlloyFurnaceRecipeManager INSTANCE = new AlloyFurnaceRecipeManager();

    private final NonNullList<IAlloyFurnaceRecipe> recipes;

    private AlloyFurnaceRecipeManager()
    {
        recipes = NonNullList.create();
    }

    public void addRecipe(IAlloyFurnaceRecipe recipe)
    {
        recipes.add(recipe);
    }

    @Override
    public void addRecipe(ItemStack out, IItemMatcher in_a, IItemMatcher in_b)
    {
        if (!FoundryMiscUtils.isInvalid(in_a) && !FoundryMiscUtils.isInvalid(in_b))
            recipes.add(new AlloyFurnaceRecipe(out, in_a, in_b));
    }

    @Override
    public void addRecipe(ItemStack out, IItemMatcher[] in_a, IItemMatcher[] in_b)
    {
        for (IItemMatcher a : in_a)
        {
            for (IItemMatcher b : in_b)
            {
                if (a != null && b != null && !out.isEmpty())
                    addRecipe(out, a, b);
            }
        }
    }

    @Override
    public IAlloyFurnaceRecipe findRecipe(ItemStack in_a, ItemStack in_b)
    {
        for (IAlloyFurnaceRecipe r : recipes)
        {
            if (r.matchesRecipe(in_a, in_b))
            {
                return r;
            }
        }
        return null;
    }

    @Override
    public List<IAlloyFurnaceRecipe> getRecipes()
    {
        return recipes;
    }

    @Override
    public void removeRecipe(IAlloyFurnaceRecipe recipe)
    {
        recipes.remove(recipe);
    }
}
