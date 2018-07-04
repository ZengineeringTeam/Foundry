package exter.foundry.integration.minetweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import exter.foundry.api.recipe.IAlloyFurnaceRecipe;
import exter.foundry.integration.ModIntegrationMinetweaker;
import exter.foundry.recipes.AlloyFurnaceRecipe;
import exter.foundry.recipes.manager.AlloyFurnaceRecipeManager;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.foundry.AlloyFurnace")
public class MTAlloyFurnaceHandler
{
    public static class AlloyFurnaceAction extends AddRemoveAction
    {

        IAlloyFurnaceRecipe recipe;

        public AlloyFurnaceAction(IAlloyFurnaceRecipe recipe)
        {
            this.recipe = recipe;
        }

        @Override
        protected void add()
        {
            AlloyFurnaceRecipeManager.INSTANCE.addRecipe(recipe);
        }

        @Override
        public String getDescription()
        {
            return String.format("( %s, %s ) -> %s", MTHelper.getItemDescription(recipe.getInputA()),
                    MTHelper.getItemDescription(recipe.getInputB()), MTHelper.getItemDescription(recipe.getOutput()));
        }

        @Override
        public String getRecipeType()
        {
            return "alloy furnace";
        }

        @Override
        protected void remove()
        {
            AlloyFurnaceRecipeManager.INSTANCE.removeRecipe(recipe);
        }
    }

    @ZenMethod
    static public void addRecipe(IItemStack output, IIngredient input_a, IIngredient input_b)
    {
        ModIntegrationMinetweaker.queueAdd(() -> {
            IAlloyFurnaceRecipe recipe = null;
            try
            {
                recipe = new AlloyFurnaceRecipe(CraftTweakerMC.getItemStack(output), MTHelper.getIngredient(input_a),
                        MTHelper.getIngredient(input_b));
            }
            catch (IllegalArgumentException e)
            {
                MTHelper.printCrt("Invalid alloy furnace recipe: " + e.getMessage());
                return;
            }
            CraftTweakerAPI.apply(new AlloyFurnaceAction(recipe).action_add);
        });
    }

    @ZenMethod
    static public void removeRecipe(IItemStack input_a, IItemStack input_b)
    {
        ModIntegrationMinetweaker.queueRemove(() -> {
            IAlloyFurnaceRecipe recipe = AlloyFurnaceRecipeManager.INSTANCE
                    .findRecipe(CraftTweakerMC.getItemStack(input_a), CraftTweakerMC.getItemStack(input_b));
            if (recipe == null)
            {
                CraftTweakerAPI.logWarning("Alloy furnace recipe not found.");
                return;
            }
            CraftTweakerAPI.apply(new AlloyFurnaceAction(recipe).action_remove);
        });
    }

    @ZenMethod
    public static void clear()
    {
        ModIntegrationMinetweaker.queueClear(AlloyFurnaceRecipeManager.INSTANCE.getRecipes());
    }
}
