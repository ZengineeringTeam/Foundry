package exter.foundry.api.recipe.manager;

import java.util.List;

import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.ICastingRecipe;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import exter.foundry.item.ItemMold;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface ICastingRecipeManager
{

    /**
     * Register a Metal Caster recipe.
     * Note: the mold must be registered with {@link RegisterMold}.
     * @param result Item produced.
     * @param in_fluid Fluid required (fluid type and amount).
     * @param in_mold Mold required.
     * @param in_extra Extra item required (null, if no extra item is required).
     */
    default void addRecipe(IItemMatcher result, FluidStack in_fluid, IItemMatcher in_mold, boolean comsume_mold, IItemMatcher in_extra)
    {
        addRecipe(result, in_fluid, in_mold, comsume_mold, in_extra, FoundryUtils.getCastTime(in_fluid));
    }

    /**
     * Register a Metal Caster recipe.
     * Note: the mold must be registered with {@link RegisterMold}.
     * @param result Item produced.
     * @param in_fluid Fluid required (fluid type and amount).
     * @param in_mold Mold required.
     * @param in_extra Extra item required (null, if no extra item is required).
     */
    void addRecipe(IItemMatcher result, FluidStack in_fluid, IItemMatcher in_mold, boolean comsume_mold, IItemMatcher in_extra, int tick);

    default void addRecipe(IItemMatcher result, FluidStack in_fluid, ItemMold.SubItem in_mold, boolean comsume_mold, IItemMatcher in_extra)
    {
        addRecipe(result, in_fluid, in_mold, comsume_mold, in_extra, FoundryUtils.getCastTime(in_fluid));
    }

    void addRecipe(IItemMatcher result, FluidStack in_fluid, ItemMold.SubItem in_mold, boolean comsume_mold, IItemMatcher in_extra, int tick);

    /**
     * Find a casting recipe given a FluidStack and a mold.
     * @param fluid FluidStack that contains the recipe's required fluid.
     * @param mold Mold used by the recipe.
     * @return The casting recipe, or null if no matching recipe.
     */
    ICastingRecipe findRecipe(FluidStack fluid, ItemStack mold, ItemStack extra);

    default ICastingRecipe findRecipe(FluidStack fluid, ItemMold.SubItem mold, ItemStack extra)
    {
        return findRecipe(fluid, mold.getMatcher().getItem(), extra);
    }

    /**
     * Get a list of all the recipes.
     * @return List of all the recipes.
     */
    List<ICastingRecipe> getRecipes();

    /**
     * Check if an item is registered as a mold.
     * @param stack Item to check.
     * @return true if an item is registered, false if not.
     */
    boolean isItemMold(ItemStack stack);

    /**
     * Removes a recipe.
     * @param The recipe to remove.
     */
    void removeRecipe(ICastingRecipe recipe);
}
