package exter.foundry.api;

import exter.foundry.api.recipe.matcher.OreMatcher;
import exter.foundry.util.MiscUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class FoundryUtils
{

    private static boolean exists(String ore)
    {
        return OreDictionary.doesOreNameExist(ore);
    }

    /**
     * Check if an item is registered in the Ore Dictionary.
     * @param name Ore name to check.
     * @param stack Item to check.
     * @return true if the item is registered, false otherwise.
     */
    static public boolean isItemInOreDictionary(String name, ItemStack stack)
    {
        for (ItemStack i : OreDictionary.getOres(name, false))
            if (OreDictionary.itemMatches(i, stack, false))
                return true;
        return false;
    }

    /**
     * Helper method for registering basic melting recipes for a given metal.
     * @param partial_name The partial ore dictionary name e.g. "Copper" for "ingotCopper","oreCopper", etc.
     * @param fluid The liquid created by the smelter.
     */
    static public void registerBasicMeltingRecipes(String partial_name, Fluid fluid)
    {
        if (FoundryAPI.MELTING_MANAGER != null)
        {
            partial_name = MiscUtil.upperCaseFirstChar(partial_name);
            if (exists("ingot" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("ingot" + partial_name),
                        new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT));

            if (exists("block" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("block" + partial_name),
                        new FluidStack(fluid, FoundryAPI.getAmountBlock()));

            if (exists("nugget" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("nugget" + partial_name),
                        new FluidStack(fluid, FoundryAPI.getAmountNugget()));

            if (exists("dust" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("dust" + partial_name),
                        new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT));

            if (exists("ore" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("ore" + partial_name),
                        new FluidStack(fluid, FoundryAPI.getAmountOre()));

            if (exists("orePoor" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("orePoor" + partial_name),
                        new FluidStack(fluid, FoundryAPI.getAmountNugget() * 2));

            if (exists("dustSmall" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("dustSmall" + partial_name),
                        new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT / 4));

            if (exists("dustTiny" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("dustTiny" + partial_name),
                        new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT / 4));

            if (exists("plate" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("plate" + partial_name),
                        new FluidStack(fluid, FoundryAPI.getAmountPlate()));

            if (exists("gear" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("gear" + partial_name),
                        new FluidStack(fluid, FoundryAPI.getAmountGear()));
        }
    }

}
