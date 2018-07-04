package exter.foundry.integration.minetweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import exter.foundry.api.recipe.IAlloyMixerRecipe;
import exter.foundry.integration.ModIntegrationMinetweaker;
import exter.foundry.recipes.AlloyMixerRecipe;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.foundry.AlloyMixer")
public class MTAlloyMixerHandler
{
    public static class AlloyMixerAction extends AddRemoveAction
    {

        IAlloyMixerRecipe recipe;

        public AlloyMixerAction(IAlloyMixerRecipe recipe)
        {
            this.recipe = recipe;
        }

        @Override
        protected void add()
        {
            AlloyMixerRecipeManager.INSTANCE.addRecipe(recipe);
        }

        @Override
        public String getDescription()
        {
            StringBuilder builder = new StringBuilder();
            builder.append("(");
            boolean comma = false;
            for (FluidStack input : recipe.getInputs())
            {
                if (comma)
                {
                    builder.append(',');
                }
                builder.append(' ');
                builder.append(MTHelper.getFluidDescription(input));
                comma = true;
            }
            builder.append(String.format(" ) -> %s", MTHelper.getFluidDescription(recipe.getOutput())));
            return builder.toString();
        }

        @Override
        public String getRecipeType()
        {
            return "alloy mixer";
        }

        @Override
        protected void remove()
        {
            AlloyMixerRecipeManager.INSTANCE.removeRecipe(recipe);
        }
    }

    @ZenMethod
    static public void addRecipe(ILiquidStack output, ILiquidStack[] inputs)
    {
        ModIntegrationMinetweaker.queueAdd(() -> {
            FluidStack out = (FluidStack) output.getInternal();
            FluidStack[] in = new FluidStack[inputs.length];

            int i;
            for (i = 0; i < inputs.length; i++)
            {
                in[i] = CraftTweakerMC.getLiquidStack(inputs[i]);
            }

            IAlloyMixerRecipe recipe = null;
            try
            {
                recipe = new AlloyMixerRecipe(out, in);
            }
            catch (IllegalArgumentException e)
            {
                MTHelper.printCrt("Invalid alloy mixer recipe: " + e.getMessage());
                return;
            }
            CraftTweakerAPI.apply(new AlloyMixerAction(recipe).action_add);
        });
    }

    @ZenMethod
    static public void removeRecipe(ILiquidStack[] inputs)
    {
        ModIntegrationMinetweaker.queueRemove(() -> {

            FluidStack[] in = new FluidStack[inputs.length];

            int i;
            for (i = 0; i < inputs.length; i++)
            {
                in[i] = CraftTweakerMC.getLiquidStack(inputs[i]);
            }

            IAlloyMixerRecipe recipe = AlloyMixerRecipeManager.INSTANCE.findRecipe(in, null);
            if (recipe == null)
            {
                CraftTweakerAPI.logWarning("Alloy mixer recipe not found.");
                return;
            }
            CraftTweakerAPI.apply(new AlloyMixerAction(recipe).action_remove);
        });
    }

    @ZenMethod
    public static void clear()
    {
        ModIntegrationMinetweaker.queueClear(AlloyMixerRecipeManager.INSTANCE.getRecipes());
    }
}
