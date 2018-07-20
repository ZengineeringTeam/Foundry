package exter.foundry.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import exter.foundry.api.recipe.IMoldRecipe;
import exter.foundry.integration.ModIntegrationCrafttweaker;
import exter.foundry.recipes.MoldRecipe;
import exter.foundry.recipes.manager.MoldRecipeManager;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.foundry.MoldStation")
@ZenRegister
public class CrTMoldStationHandler
{
    public static class MoldStationAction extends AddRemoveAction
    {

        IMoldRecipe recipe;

        public MoldStationAction(IMoldRecipe recipe)
        {
            this.recipe = recipe;
        }

        @Override
        protected void add()
        {
            MoldRecipeManager.INSTANCE.addRecipe(recipe);
        }

        @Override
        public String getDescription()
        {
            StringBuilder builder = new StringBuilder();
            builder.append(String.format("( %d, %d, [", recipe.getWidth(), recipe.getHeight()));
            boolean comma = false;
            for (int r : recipe.getRecipeGrid())
            {
                if (comma)
                {
                    builder.append(',');
                }
                builder.append(String.format(" %d", r));
                comma = true;
            }
            builder.append(String.format("] ) -> %s", CrTHelper.getItemDescription(recipe.getOutput())));
            return builder.toString();
        }

        @Override
        public String getRecipeType()
        {
            return "mold station";
        }

        @Override
        protected void remove()
        {
            MoldRecipeManager.INSTANCE.removeRecipe(recipe);
        }
    }

    @ZenMethod
    static public void addRecipe(IItemStack output, int width, int height, int[] grid)
    {
        ModIntegrationCrafttweaker.queueAdd(() -> {
            IMoldRecipe recipe = null;
            try
            {
                recipe = new MoldRecipe(CraftTweakerMC.getItemStack(output), width, height, grid);
            }
            catch (IllegalArgumentException e)
            {
                CrTHelper.printCrt("Invalid mold station recipe: " + e.getMessage());
                return;
            }
            CraftTweakerAPI.apply(new MoldStationAction(recipe).action_add);
        });
    }

    @ZenMethod
    static public void removeRecipe(int[] grid)
    {
        ModIntegrationCrafttweaker.queueRemove(() -> {
            if (grid.length != 36)
            {
                CraftTweakerAPI.logWarning("Invalid mold station grid size: expected 36 instead of " + grid.length);
                return;
            }
            IMoldRecipe recipe = MoldRecipeManager.INSTANCE.findRecipe(grid);
            if (recipe == null)
            {
                CraftTweakerAPI.logWarning("Mold station recipe not found.");
                return;
            }
            CraftTweakerAPI.apply(new MoldStationAction(recipe).action_remove);
        });
    }

    @ZenMethod
    public static void clear()
    {
        ModIntegrationCrafttweaker.queueClear(MoldRecipeManager.INSTANCE.getRecipes());
    }
}
