package exter.foundry.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import exter.foundry.api.recipe.IAlloyingCrucibleRecipe;
import exter.foundry.integration.ModIntegrationCrafttweaker;
import exter.foundry.recipes.AlloyingCrucibleRecipe;
import exter.foundry.recipes.manager.AlloyingCrucibleRecipeManager;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.foundry.AlloyingCrucible")
public class CrTAlloyingCrucibleHandler
{
    public static class AlloyingCrucibleAction extends AddRemoveAction
    {

        IAlloyingCrucibleRecipe recipe;

        public AlloyingCrucibleAction(IAlloyingCrucibleRecipe recipe)
        {
            this.recipe = recipe;
        }

        @Override
        protected void add()
        {
            AlloyingCrucibleRecipeManager.INSTANCE.addRecipe(recipe);
        }

        @Override
        public String getDescription()
        {
            return String.format("(%s,%s) -> %s", CrTHelper.getFluidDescription(recipe.getInputA()),
                    CrTHelper.getFluidDescription(recipe.getInputB()),
                    CrTHelper.getFluidDescription(recipe.getOutput()));
        }

        @Override
        public String getRecipeType()
        {
            return "alloying crucible";
        }

        @Override
        protected void remove()
        {
            AlloyingCrucibleRecipeManager.INSTANCE.removeRecipe(recipe);
        }
    }

    @ZenMethod
    static public void addRecipe(ILiquidStack output, ILiquidStack input_a, ILiquidStack input_b)
    {
        ModIntegrationCrafttweaker.queueAdd(() -> {
            FluidStack out = CraftTweakerMC.getLiquidStack(output);
            FluidStack in_a = CraftTweakerMC.getLiquidStack(input_a);
            FluidStack in_b = CraftTweakerMC.getLiquidStack(input_b);

            IAlloyingCrucibleRecipe recipe = null;
            try
            {
                recipe = new AlloyingCrucibleRecipe(out, in_a, in_b);
            }
            catch (IllegalArgumentException e)
            {
                CrTHelper.printCrt("Invalid alloying crucible recipe: " + e.getMessage());
                return;
            }
            CraftTweakerAPI.apply(new AlloyingCrucibleAction(recipe).action_add);
        });
    }

    @ZenMethod
    static public void removeRecipe(ILiquidStack input_a, ILiquidStack input_b)
    {
        ModIntegrationCrafttweaker.queueRemove(() -> {
            FluidStack in_a = CraftTweakerMC.getLiquidStack(input_a);
            FluidStack in_b = CraftTweakerMC.getLiquidStack(input_b);

            IAlloyingCrucibleRecipe recipe = AlloyingCrucibleRecipeManager.INSTANCE.findRecipe(in_a, in_b);
            if (recipe == null)
            {
                recipe = AlloyingCrucibleRecipeManager.INSTANCE.findRecipe(in_b, in_a);
            }
            if (recipe == null)
            {
                recipe = AlloyingCrucibleRecipeManager.INSTANCE.findRecipe(in_a, in_b);
                CrTHelper.printCrt("Alloy mixer recipe not found.");
                return;
            }
            CraftTweakerAPI.apply(new AlloyingCrucibleAction(recipe).action_remove);
        });
    }

    @ZenMethod
    public static void clear()
    {
        ModIntegrationCrafttweaker.queueClear(AlloyingCrucibleRecipeManager.INSTANCE.getRecipes());
    }
}
