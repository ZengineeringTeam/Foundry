package exter.foundry.integration.crafttweaker;

import javax.annotation.Nullable;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.ICastingRecipe;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.integration.ModIntegrationCrafttweaker;
import exter.foundry.recipes.CastingRecipe;
import exter.foundry.recipes.manager.CastingRecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.foundry.Casting")
public class CrTCastingHandler
{
    public static class CastingAction extends AddRemoveAction
    {

        ICastingRecipe recipe;

        public CastingAction(ICastingRecipe recipe)
        {
            this.recipe = recipe;
        }

        @Override
        protected void add()
        {
            if (recipe.requiresExtra())
            {
                CastingRecipeManager.INSTANCE.addRecipe(0, recipe);
            }
            else
            {
                CastingRecipeManager.INSTANCE.addRecipe(recipe);
            }
        }

        @Override
        public String getDescription()
        {
            IItemMatcher extra = recipe.getInputExtra();
            if (extra == null)
            {
                return String.format("( %s, %s ) -> %s", CrTHelper.getFluidDescription(recipe.getInput()),
                        CrTHelper.getItemDescription(recipe.getMold()),
                        CrTHelper.getItemDescription(recipe.getOutput()));
            }
            return String.format("( %s, %s, %s ) -> %s", CrTHelper.getFluidDescription(recipe.getInput()),
                    CrTHelper.getItemDescription(recipe.getMold()),
                    CrTHelper.getItemDescription(recipe.getInputExtra()),
                    CrTHelper.getItemDescription(recipe.getOutput()));
        }

        @Override
        public String getRecipeType()
        {
            return "casting";
        }

        @Override
        protected void remove()
        {
            CastingRecipeManager.INSTANCE.removeRecipe(recipe);
        }
    }

    @ZenMethod
    static public void addRecipe(IItemStack output, ILiquidStack input, IItemStack mold, @Optional IIngredient extra, @Optional int tick, @Optional boolean consumes_mold)
    {
        ModIntegrationCrafttweaker.queueAdd(() -> {
            ICastingRecipe recipe = null;
            try
            {
                FluidStack inputFluid = CraftTweakerMC.getLiquidStack(input);
                recipe = new CastingRecipe(new ItemStackMatcher(CraftTweakerMC.getItemStack(output)),
                        inputFluid, new ItemStackMatcher(CraftTweakerMC.getItemStack(mold)),
                        consumes_mold, extra == null ? null : CrTHelper.getIngredient(extra), tick == 0 ? FoundryUtils.getCastTime(inputFluid) : tick);
            }
            catch (IllegalArgumentException e)
            {
                CrTHelper.printCrt("Invalid casting recipe: " + e.getMessage());
                return;
            }
            CraftTweakerAPI.apply(new CastingAction(recipe).action_add);
        });
    }

    @ZenMethod
    static public void removeRecipe(ILiquidStack input, IItemStack mold, @Optional IIngredient extra)
    {
        ModIntegrationCrafttweaker.queueRemove(() -> {
            ICastingRecipe recipe = findCastingForRemoval(CraftTweakerMC.getLiquidStack(input),
                    CraftTweakerMC.getItemStack(mold), CraftTweakerMC.getIngredient(extra));
            if (recipe == null)
            {
                CraftTweakerAPI.logWarning("Casting recipe not found: " + getDebugDescription(input, mold, extra));
                return;
            }
            CraftTweakerAPI.apply(new CastingAction(recipe).action_remove);
        });
    }

    public static String getDebugDescription(ILiquidStack input, IItemStack mold, @Nullable IIngredient extra)
    {
        if (extra == null)
            return String.format("( %s, %s )", CrTHelper.getFluidDescription(input),
                    CrTHelper.getItemDescription(mold));
        return String.format("( %s, %s, %s )", CrTHelper.getFluidDescription(input), CrTHelper.getItemDescription(mold),
                CrTHelper.getItemDescription(CrTHelper.getIngredient(extra)));
    }

    public static ICastingRecipe findCastingForRemoval(FluidStack fluid, ItemStack mold, Ingredient extra)
    {
        if (mold.isEmpty() || fluid == null)
            return null;
        for (ICastingRecipe cr : CastingRecipeManager.INSTANCE.getRecipes())
        {
            if (cr.getInput().getFluid().getName().equals(fluid.getFluid().getName())
                    && ItemStack.areItemStacksEqual(mold, cr.getMold()))
            {
                if (cr.getInputExtra() == null)
                    return cr;
                boolean matchesExtra = false;
                for (ItemStack item : extra.getMatchingStacks())
                {
                    if (cr.getInputExtra().apply(item))
                        return cr;
                }
            }
        }
        return null;
    }

    @ZenMethod
    public static void clearRecipes()
    {
        ModIntegrationCrafttweaker.queueClear(CastingRecipeManager.INSTANCE.getRecipes());
    }

}
