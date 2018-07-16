package exter.foundry.recipes.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exter.foundry.api.recipe.ICastingTableRecipe;
import exter.foundry.api.recipe.ICastingTableRecipe.TableType;
import exter.foundry.api.recipe.manager.ICastingTableRecipeManager;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import exter.foundry.recipes.CastingTableRecipe;
import exter.foundry.util.MiscUtil;
import net.minecraftforge.fluids.FluidStack;

public class CastingTableRecipeManager implements ICastingTableRecipeManager
{
    public static final CastingTableRecipeManager INSTANCE = new CastingTableRecipeManager();

    private final Map<TableType, Map<String, ICastingTableRecipe>> recipes;

    private CastingTableRecipeManager()
    {
        recipes = new EnumMap<>(TableType.class);
        for (TableType type : TableType.values())
        {
            recipes.put(type, new HashMap<>());
        }
    }

    public void addRecipe(TableType tableType, String name, ICastingTableRecipe recipe)
    {
        recipes.get(tableType).put(name, recipe);

    }

    @Override
    public void addRecipe(IItemMatcher result, FluidStack fluid, TableType type)
    {
        if (MiscUtil.isInvalid(result))
            return;
        ICastingTableRecipe recipe = new CastingTableRecipe(result, fluid, type);
        recipes.get(recipe.getTableType()).put(recipe.getInput().getFluid().getName(), recipe);
    }

    @Override
    public ICastingTableRecipe findRecipe(FluidStack fluid, TableType type)
    {
        if (type == null || fluid == null || fluid.amount == 0)
        {
            return null;
        }
        ICastingTableRecipe recipe = recipes.get(type).get(fluid.getFluid().getName());
        return recipe;
    }

    @Override
    public List<ICastingTableRecipe> getRecipes()
    {
        List<ICastingTableRecipe> result = new ArrayList<>();
        for (TableType type : TableType.values())
        {
            result.addAll(recipes.get(type).values());
        }
        return result;
    }

    public Map<TableType, Map<String, ICastingTableRecipe>> getRecipesMap()
    {
        return recipes;
    }

    public Collection<ICastingTableRecipe> getRecipes(TableType type)
    {
        return recipes.get(type).values();
    }

    @Override
    public void removeRecipe(ICastingTableRecipe recipe)
    {
        recipes.get(recipe.getTableType()).remove(recipe.getInput().getFluid().getName());
    }

    public void removeRecipe(TableType tableType, String name)
    {
        recipes.get(tableType).remove(name);
    }
}
