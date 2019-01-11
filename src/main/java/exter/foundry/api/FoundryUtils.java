package exter.foundry.api;

import exter.foundry.api.recipe.matcher.OreMatcher;
import exter.foundry.config.FoundryConfig;
import exter.foundry.util.MiscUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class FoundryUtils
{

    public static boolean exists(String ore)
    {
        return !OreDictionary.getOres(ore, false).isEmpty();
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

            if (exists("crushed" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("crushed" + partial_name),
                        new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT));

            if (exists("crushedPurified" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("crushedPurified" + partial_name),
                        new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT));

            if (exists("orePoor" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("orePoor" + partial_name),
                        new FluidStack(fluid, FoundryAPI.getAmountNugget() * 2));

            if (exists("dustSmall" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("dustSmall" + partial_name),
                        new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT / 4));

            if (exists("dustTiny" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("dustTiny" + partial_name),
                        new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT / 9));

            if (exists("plate" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("plate" + partial_name),
                        new FluidStack(fluid, FoundryAPI.getAmountPlate()));

            if (exists("gear" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("gear" + partial_name),
                        new FluidStack(fluid, FoundryAPI.getAmountGear()));

            if (exists("wall" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("wall" + partial_name),
                        new FluidStack(fluid, FoundryAPI.getAmountWall()));

            if (exists("button" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("button" + partial_name),
                        new FluidStack(fluid, FoundryAPI.getAmountButton()));

            if (exists("trapdoor" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("trapdoor" + partial_name),
                        new FluidStack(fluid, FoundryAPI.getAmountTrapdoor()));

            if (exists("lever" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("lever" + partial_name),
                        new FluidStack(fluid, FoundryAPI.getAmountLever()));

            if (exists("casing" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("casing" + partial_name),
                        new FluidStack(fluid, FoundryAPI.getAmountCasing()));

            if (exists("plateDense" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("plateDense" + partial_name),
                        new FluidStack(fluid, FoundryAPI.getAmountDensePlate()));

            if (exists("bars" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("bars" + partial_name),
                        new FluidStack(fluid, FoundryAPI.getAmountBars()));

            if (exists("rod" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("rod" + partial_name),
                        new FluidStack(fluid, FoundryAPI.getAmountRod()));

            if (exists("slab" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("slab" + partial_name),
                        new FluidStack(fluid, FoundryAPI.getAmountSlab()));

            if (exists("stairs" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("stairs" + partial_name),
                        new FluidStack(fluid, FoundryAPI.getAmountStairs()));
        }
    }

    public static int getCastTime(FluidStack fluidStack)
    {
        if (fluidStack == null)
        {
            return 0;
        }
        return Math.max(1, FoundryConfig.castingTick * fluidStack.amount / FoundryAPI.FLUID_AMOUNT_INGOT);
    }

}
