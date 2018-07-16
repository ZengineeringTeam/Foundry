package exter.foundry.recipes.manager;

import java.util.List;

import exter.foundry.api.recipe.IMeltingRecipe;
import exter.foundry.api.recipe.manager.IMeltingRecipeManager;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import exter.foundry.recipes.MeltingRecipe;
import exter.foundry.util.MiscUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

public class MeltingRecipeManager implements IMeltingRecipeManager
{

    public static final MeltingRecipeManager INSTANCE = new MeltingRecipeManager();

    private final NonNullList<IMeltingRecipe> recipes;

    private MeltingRecipeManager()
    {
        recipes = NonNullList.create();
    }

    @Override
    public void addRecipe(IItemMatcher solid, FluidStack fluid_stack)
    {
        if (!MiscUtil.isInvalid(solid))
            addRecipe(solid, fluid_stack, fluid_stack.getFluid().getTemperature(), 100);
    }

    @Override
    public void addRecipe(IItemMatcher solid, FluidStack fluid_stack, int melting_point)
    {
        addRecipe(solid, fluid_stack, melting_point, 100);
    }

    @Override
    public void addRecipe(IItemMatcher solid, FluidStack fluid_stack, int melting_point, int melting_speed)
    {
        addRecipe(new MeltingRecipe(solid, fluid_stack, melting_point, melting_speed));
    }

    public void addRecipe(IMeltingRecipe recipe)
    {
        recipes.add(recipe);
    }

    @Override
    public IMeltingRecipe findRecipe(ItemStack item)
    {
        if (item.isEmpty())
            return null;
        for (IMeltingRecipe r : recipes)
        {
            if (r.matchesRecipe(item))
                return r;
        }
        return null;
    }

    @Override
    public List<IMeltingRecipe> getRecipes()
    {
        return recipes;
    }

    @Override
    public void removeRecipe(IMeltingRecipe recipe)
    {
        recipes.remove(recipe);
    }
}
