package exter.foundry.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import exter.foundry.api.recipe.IInfuserRecipe;
import exter.foundry.integration.ModIntegrationCrafttweaker;
import exter.foundry.recipes.InfuserRecipe;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.foundry.Infuser")
public class CrTInfuserHandler
{
    public static class InfuserAction extends AddRemoveAction
    {

        IInfuserRecipe recipe;

        public InfuserAction(IInfuserRecipe recipe)
        {
            this.recipe = recipe;
        }

        @Override
        protected void add()
        {
            InfuserRecipeManager.INSTANCE.addRecipe(recipe);
        }

        @Override
        public String getDescription()
        {
            return String.format("( %s, %s ) -> %s", CrTHelper.getFluidDescription(recipe.getInputFluid()),
                    CrTHelper.getItemDescription(recipe.getInput()), CrTHelper.getFluidDescription(recipe.getOutput()));
        }

        @Override
        public String getRecipeType()
        {
            return "infuser";
        }

        @Override
        protected void remove()
        {
            InfuserRecipeManager.INSTANCE.removeRecipe(recipe);
        }
    }

    @ZenMethod
    static public void addRecipe(ILiquidStack output, ILiquidStack input, IIngredient substance, int energy)
    {
        ModIntegrationCrafttweaker.queueAdd(() -> {
            IInfuserRecipe recipe = null;
            try
            {
                recipe = new InfuserRecipe(CraftTweakerMC.getLiquidStack(output), CraftTweakerMC.getLiquidStack(input),
                        CrTHelper.getIngredient(substance), energy);
            }
            catch (IllegalArgumentException e)
            {
                CrTHelper.printCrt("Invalid infuser recipe: " + e.getMessage());
                return;
            }
            CraftTweakerAPI.apply(new InfuserAction(recipe).action_add);
        });
    }

    @ZenMethod
    static public void removeRecipe(ILiquidStack input, IItemStack substance)
    {
        ModIntegrationCrafttweaker.queueRemove(() -> {
            IInfuserRecipe recipe = InfuserRecipeManager.INSTANCE.findRecipe(CraftTweakerMC.getLiquidStack(input),
                    CraftTweakerMC.getItemStack(substance));
            if (recipe == null)
            {
                CraftTweakerAPI.logWarning("Infuser recipe not found.");
                return;
            }
            CraftTweakerAPI.apply(new InfuserAction(recipe).action_remove);
        });
    }

    @ZenMethod
    public static void clear()
    {
        ModIntegrationCrafttweaker.queueClear(InfuserRecipeManager.INSTANCE.getRecipes());
    }
}
