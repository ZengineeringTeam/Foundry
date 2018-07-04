package exter.foundry.recipes.manager;

import java.util.List;

import exter.foundry.api.recipe.IInfuserRecipe;
import exter.foundry.api.recipe.manager.IInfuserRecipeManager;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import exter.foundry.recipes.InfuserRecipe;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

public class InfuserRecipeManager implements IInfuserRecipeManager
{
    public static final InfuserRecipeManager INSTANCE = new InfuserRecipeManager();

    private final NonNullList<IInfuserRecipe> recipes;

    private InfuserRecipeManager()
    {
        recipes = NonNullList.create();
    }

    @Override
    public void addRecipe(FluidStack result, FluidStack in_fluid, IItemMatcher item, int energy)
    {
        if (!FoundryMiscUtils.isInvalid(item))
            recipes.add(new InfuserRecipe(result, in_fluid, item, energy));
    }

    public void addRecipe(IInfuserRecipe recipe)
    {
        recipes.add(recipe);
    }

    @Override
    public IInfuserRecipe findRecipe(FluidStack fluid, ItemStack item)
    {
        if (fluid == null || item.isEmpty())
        {
            return null;
        }
        for (IInfuserRecipe ir : recipes)
        {
            if (ir.matchesRecipe(fluid, item))
            {
                return ir;
            }
        }
        return null;
    }

    @Override
    public List<IInfuserRecipe> getRecipes()
    {
        return recipes;
    }

    @Override
    public void removeRecipe(IInfuserRecipe recipe)
    {
        recipes.remove(recipe);
    }
}
