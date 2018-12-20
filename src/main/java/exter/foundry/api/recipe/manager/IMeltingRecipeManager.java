package exter.foundry.api.recipe.manager;

import java.util.List;

import exter.foundry.api.recipe.IMeltingRecipe;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IMeltingRecipeManager
{
    /**
     * Register a Melting Crucible recipe.
     * Uses the fluid's temperature as it's melting point.
     * @param solid The item to be melted
     * @param fluid_stack Resulting fluid
     */
    void addRecipe(IItemMatcher solid, FluidStack fluid_stack);

    /**
     * Register a Melting Crucible recipe.
     * @param solid The item to be melted
     * @param fluid_stack Resulting fluid
     * @param melting_point Temperature required for the item to melt. Must be >295 and <5000
     */
    void addRecipe(IItemMatcher solid, FluidStack fluid_stack, int melting_point);

    /**
     * Register a Melting Crucible recipe.
     * @param solid The item to be melted
     * @param fluid_stack Resulting fluid
     * @param melting_point Temperature required for the item to melt. Must be >295 and <5000
     * @param melting_speed. Speed in which the item melts. Default is 100.
     */
    void addRecipe(IItemMatcher solid, FluidStack fluid_stack, int melting_point, int melting_speed);

    /**
     * Find a valid recipe that contains the given item
     * @param item The item required in the recipe
     * @return
     */
    IMeltingRecipe findRecipe(ItemStack item);

    /**
     * Get a list of all the recipes
     * @return List of all the recipes
     */
    List<IMeltingRecipe> getRecipes();

    /**
     * Removes a recipe.
     * @param The recipe to remove.
     */
    void removeRecipe(IMeltingRecipe recipe);
}
