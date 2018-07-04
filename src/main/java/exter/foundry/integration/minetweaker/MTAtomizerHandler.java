package exter.foundry.integration.minetweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import exter.foundry.api.recipe.IAtomizerRecipe;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.integration.ModIntegrationMinetweaker;
import exter.foundry.recipes.AtomizerRecipe;
import exter.foundry.recipes.manager.AtomizerRecipeManager;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.foundry.Atomizer")
public class MTAtomizerHandler
{
    public static class AtomizerAction extends AddRemoveAction
    {
        IAtomizerRecipe recipe;

        public AtomizerAction(IAtomizerRecipe recipe)
        {
            this.recipe = recipe;
        }

        @Override
        protected void add()
        {
            AtomizerRecipeManager.INSTANCE.addRecipe(recipe);
        }

        @Override
        public String getDescription()
        {
            return String.format("%s -> %s", MTHelper.getFluidDescription(recipe.getInput()),
                    MTHelper.getItemDescription(recipe.getOutput()));
        }

        @Override
        public String getRecipeType()
        {
            return "atomizer";
        }

        @Override
        protected void remove()
        {
            AtomizerRecipeManager.INSTANCE.removeRecipe(recipe);
        }
    }

    @ZenMethod
    static public void addRecipe(IItemStack output, ILiquidStack input)
    {
        ModIntegrationMinetweaker.queueAdd(() -> {
            IAtomizerRecipe recipe = null;
            try
            {
                recipe = new AtomizerRecipe(new ItemStackMatcher(CraftTweakerMC.getItemStack(output)),
                        CraftTweakerMC.getLiquidStack(input));
            }
            catch (IllegalArgumentException e)
            {
                MTHelper.printCrt("Invalid atomizer recipe: " + e.getMessage());
                return;
            }
            CraftTweakerAPI.apply(new AtomizerAction(recipe).action_add);
        });
    }

    @ZenMethod
    static public void removeRecipe(ILiquidStack input)
    {
        ModIntegrationMinetweaker.queueRemove(() -> {

            IAtomizerRecipe recipe = AtomizerRecipeManager.INSTANCE.findRecipe(CraftTweakerMC.getLiquidStack(input));
            if (recipe == null)
            {
                CraftTweakerAPI.logWarning("Atomizer recipe not found.");
                return;
            }
            CraftTweakerAPI.apply(new AtomizerAction(recipe).action_remove);
        });
    }

    @ZenMethod
    public static void clear()
    {
        ModIntegrationMinetweaker.queueClear(AtomizerRecipeManager.INSTANCE.getRecipes());
    }
}
