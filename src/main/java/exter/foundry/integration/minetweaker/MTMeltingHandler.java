package exter.foundry.integration.minetweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import exter.foundry.api.recipe.IMeltingRecipe;
import exter.foundry.integration.ModIntegrationMinetweaker;
import exter.foundry.recipes.MeltingRecipe;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.foundry.Melting")
public class MTMeltingHandler
{
    public static class MeltingAction extends AddRemoveAction
    {

        IMeltingRecipe recipe;

        public MeltingAction(IMeltingRecipe recipe)
        {
            this.recipe = recipe;
        }

        @Override
        protected void add()
        {
            MeltingRecipeManager.INSTANCE.addRecipe(recipe);
        }

        @Override
        public String getDescription()
        {
            return String.format("%s -> %s", MTHelper.getItemDescription(recipe.getInput()),
                    MTHelper.getFluidDescription(recipe.getOutput()));
        }

        @Override
        public String getRecipeType()
        {
            return "melting";
        }

        @Override
        protected void remove()
        {
            MeltingRecipeManager.INSTANCE.removeRecipe(recipe);
        }
    }

    @ZenMethod
    static public void addRecipe(ILiquidStack output, IIngredient input, @Optional int melting_point, @Optional int speed)
    {
        ModIntegrationMinetweaker.queueAdd(() -> {
            IMeltingRecipe recipe = null;
            try
            {
                recipe = new MeltingRecipe(MTHelper.getIngredient(input), CraftTweakerMC.getLiquidStack(output),
                        melting_point == 0 ? output.getTemperature() : melting_point, speed == 0 ? 100 : speed);
            }
            catch (IllegalArgumentException e)
            {
                MTHelper.printCrt("Invalid melting recipe.");
                return;
            }
            CraftTweakerAPI.apply(new MeltingAction(recipe).action_add);
        });
    }

    @ZenMethod
    static public void removeRecipe(IItemStack input)
    {
        ModIntegrationMinetweaker.queueRemove(() -> {
            IMeltingRecipe recipe = MeltingRecipeManager.INSTANCE.findRecipe(CraftTweakerMC.getItemStack(input));
            if (recipe == null)
            {
                CraftTweakerAPI.logWarning("Melting recipe not found.");
                return;
            }
            CraftTweakerAPI.apply(new MeltingAction(recipe).action_remove);
        });
    }

    @ZenMethod
    public static void clear()
    {
        ModIntegrationMinetweaker.queueClear(MeltingRecipeManager.INSTANCE.getRecipes());
    }
}
