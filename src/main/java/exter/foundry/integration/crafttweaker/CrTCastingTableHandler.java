package exter.foundry.integration.crafttweaker;

import java.util.HashMap;
import java.util.Map;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import exter.foundry.api.recipe.ICastingTableRecipe;
import exter.foundry.api.recipe.ICastingTableRecipe.TableType;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.integration.ModIntegrationCrafttweaker;
import exter.foundry.recipes.CastingTableRecipe;
import exter.foundry.recipes.manager.CastingTableRecipeManager;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.foundry.CastingTable")
@ZenRegister
public class CrTCastingTableHandler
{
    public static class CastingTableAction extends AddRemoveAction
    {

        ICastingTableRecipe recipe;

        public CastingTableAction(ICastingTableRecipe recipe)
        {
            this.recipe = recipe;
        }

        @Override
        protected void add()
        {
            CastingTableRecipeManager.INSTANCE.addRecipe(recipe.getTableType(), recipe.getInput().getFluid().getName(),
                    recipe);
        }

        @Override
        public String getDescription()
        {
            return String.format("( %s, %s ) -> %s", CrTHelper.getFluidDescription(recipe.getInput()),
                    recipe.getTableType().toString(), CrTHelper.getItemDescription(recipe.getOutput()));
        }

        @Override
        public String getRecipeType()
        {
            return "casting table";
        }

        @Override
        protected void remove()
        {
            CastingTableRecipeManager.INSTANCE.removeRecipe(recipe.getTableType(),
                    recipe.getInput().getFluid().getName());
        }
    }

    @ZenMethod
    static public void addBlockRecipe(IItemStack output, ILiquidStack input)
    {
        addRecipe(output, input, ICastingTableRecipe.TableType.BLOCK);
    }

    @ZenMethod
    static public void addIngotRecipe(IItemStack output, ILiquidStack input)
    {
        addRecipe(output, input, ICastingTableRecipe.TableType.INGOT);
    }

    @ZenMethod
    static public void addPlateRecipe(IItemStack output, ILiquidStack input)
    {
        addRecipe(output, input, ICastingTableRecipe.TableType.PLATE);
    }

    static private void addRecipe(IItemStack output, ILiquidStack input, ICastingTableRecipe.TableType table)
    {
        ModIntegrationCrafttweaker.queueAdd(() -> {
            ItemStackMatcher out = new ItemStackMatcher(CraftTweakerMC.getItemStack(output));
            FluidStack in = CraftTweakerMC.getLiquidStack(input);
            CastingTableRecipe recipe;
            try
            {
                recipe = new CastingTableRecipe(out, in, table);
            }
            catch (IllegalArgumentException e)
            {
                CrTHelper.printCrt("Invalid casting recipe: " + e.getMessage());
                return;
            }
            CraftTweakerAPI.apply(new CastingTableAction(recipe).action_add);
        });
    }

    @ZenMethod
    static public void addRodRecipe(IItemStack output, ILiquidStack input)
    {
        addRecipe(output, input, ICastingTableRecipe.TableType.ROD);
    }

    @ZenMethod
    static public void removeBlockRecipe(ILiquidStack input)
    {
        removeRecipe(input, ICastingTableRecipe.TableType.BLOCK);
    }

    @ZenMethod
    static public void removeIngotRecipe(ILiquidStack input)
    {
        removeRecipe(input, ICastingTableRecipe.TableType.INGOT);
    }

    @ZenMethod
    static public void removePlateRecipe(ILiquidStack input)
    {
        removeRecipe(input, ICastingTableRecipe.TableType.PLATE);
    }

    static public void removeRecipe(ILiquidStack input, ICastingTableRecipe.TableType table)
    {
        ModIntegrationCrafttweaker.queueRemove(() -> {
            ICastingTableRecipe recipe = CastingTableRecipeManager.INSTANCE
                    .findRecipe(CraftTweakerMC.getLiquidStack(input), table);
            if (recipe == null)
            {
                CraftTweakerAPI.logWarning("Casting table recipe not found.");
                return;
            }
            CraftTweakerAPI.apply(new CastingTableAction(recipe).action_remove);
        });
    }

    @ZenMethod
    static public void removeRodRecipe(ILiquidStack input)
    {
        removeRecipe(input, ICastingTableRecipe.TableType.ROD);
    }

    @ZenMethod
    public static void clearRods()
    {
        ModIntegrationCrafttweaker.queueClear(CastingTableRecipeManager.INSTANCE.getRecipes(TableType.ROD));
    }

    @ZenMethod
    public static void clearPlates()
    {
        ModIntegrationCrafttweaker.queueClear(CastingTableRecipeManager.INSTANCE.getRecipes(TableType.PLATE));
    }

    @ZenMethod
    public static void clearIngots()
    {
        ModIntegrationCrafttweaker.queueClear(CastingTableRecipeManager.INSTANCE.getRecipes(TableType.INGOT));
    }

    @ZenMethod
    public static void clearBlocks()
    {
        ModIntegrationCrafttweaker.queueClear(CastingTableRecipeManager.INSTANCE.getRecipes(TableType.BLOCK));
    }

    @ZenMethod
    public static void clearAll()
    {
        ModIntegrationCrafttweaker.queueClear(() -> {
            Map<TableType, Map<String, ICastingTableRecipe>> recipes = CastingTableRecipeManager.INSTANCE
                    .getRecipesMap();
            for (TableType type : TableType.values())
            {
                recipes.put(type, new HashMap<>());
            }
        });
    }
}
