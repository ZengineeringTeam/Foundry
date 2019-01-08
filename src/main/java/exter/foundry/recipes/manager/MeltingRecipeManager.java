package exter.foundry.recipes.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exter.foundry.Foundry;
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
    private final Map<IMeltingRecipe, String> desc;

    private MeltingRecipeManager()
    {
        recipes = NonNullList.create();
        desc = new HashMap<>();
    }

    @Override
    public void addRecipe(IItemMatcher solid, FluidStack fluid_stack)
    {
        addRecipe(solid, fluid_stack, fluid_stack.getFluid().getTemperature());
    }

    @Override
    public void addRecipe(IItemMatcher solid, FluidStack fluid_stack, int melting_point)
    {
        addRecipe(solid, fluid_stack, melting_point, 100);
    }

    @Override
    public void addRecipe(IItemMatcher solid, FluidStack fluid_stack, int melting_point, int melting_speed)
    {
        if (!MiscUtil.isInvalid(solid))
            addRecipe(new MeltingRecipe(solid, fluid_stack, melting_point, melting_speed));
    }

    public void addRecipe(IMeltingRecipe recipe)
    {
        if (desc.values().stream().anyMatch(k -> k.equals(recipe.toString())))
        {
            Foundry.LOGGER.warn("Detected duplicated recipe: " + recipe.toString());
            return;
        }
        recipes.add(recipe);
        desc.put(recipe, recipe.toString());
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
