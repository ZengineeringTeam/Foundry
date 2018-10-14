package exter.foundry.init;

import exter.foundry.api.recipe.matcher.IItemMatcher;
import exter.foundry.api.recipe.matcher.OreMatcher;
import exter.foundry.config.FoundryConfig;
import exter.foundry.fluid.FoundryFluids;
import exter.foundry.fluid.FoundryFluidRegistry;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.AlloyingCrucibleRecipeManager;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.util.MiscUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import static exter.foundry.api.FoundryAPI.FLUID_AMOUNT_INGOT;

public class InitAlloyRecipes
{
    // Create recipes for all alloy making machines.
    static private void addSimpleAlloy(String output, String input_a, int amount_a, String input_b, int amount_b)
    {
        ItemStack alloy_ingot = MiscUtil.getModItemFromOreDictionary(FoundryConfig.prefModID, "ingot" + output,
                amount_a + amount_b);
        if (!alloy_ingot.isEmpty())
        {
            checkAndAddRecipe(alloy_ingot, input_a, amount_a, input_b, amount_b);
        }

        Fluid fluid_out = FoundryFluidRegistry.INSTANCE.getFluid(output);
        Fluid fluid_in_a = FoundryFluidRegistry.INSTANCE.getFluid(input_a);
        Fluid fluid_in_b = FoundryFluidRegistry.INSTANCE.getFluid(input_b);

        AlloyingCrucibleRecipeManager.INSTANCE.addRecipe(new FluidStack(fluid_out, (amount_a + amount_b) * 3),
                new FluidStack(fluid_in_a, amount_a * 3), new FluidStack(fluid_in_b, amount_b * 3));

        AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(fluid_out, amount_a + amount_b),
                new FluidStack[] { new FluidStack(fluid_in_a, amount_a), new FluidStack(fluid_in_b, amount_b) });
    }

    private static void checkAndAddRecipe(ItemStack alloy_ingot, String input_a, int amount_a, String input_b, int amount_b)
    {
        IItemMatcher[] a = new IItemMatcher[2];
        IItemMatcher[] b = new IItemMatcher[2];
        if (OreDictionary.doesOreNameExist("ingot" + input_a))
            a[0] = new OreMatcher("ingot" + input_a, amount_a);
        if (OreDictionary.doesOreNameExist("dust" + input_a))
            a[1] = new OreMatcher("dust" + input_a, amount_a);
        if (OreDictionary.doesOreNameExist("ingot" + input_b))
            b[0] = new OreMatcher("ingot" + input_b, amount_b);
        if (OreDictionary.doesOreNameExist("dust" + input_b))
            b[1] = new OreMatcher("dust" + input_b, amount_b);
    }

    static public void init()
    {
        addSimpleAlloy("bronze", "copper", 3, "tin", 1);

        addSimpleAlloy("brass", "copper", 3,
                !OreDictionary.getOres("ingotZinc", false).isEmpty() ? "zinc" : "aluminium", 1);

        addSimpleAlloy("invar", "iron", 2, "nickel", 1);

        addSimpleAlloy("electrum", "gold", 1, "silver", 1);

        addSimpleAlloy("constantan", "copper", 1, "nickel", 1);

        Fluid liquid_redstone = FluidRegistry.getFluid("liquidredstone");
        Fluid liquid_glowstone = FluidRegistry.getFluid("liquidglowstone");
        Fluid liquid_enderpearl = FluidRegistry.getFluid("liquidenderpearl");

        if (liquid_redstone != null)
            AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(FoundryFluids.liquid_signalum, 108),
                    new FluidStack[] { new FluidStack(FoundryFluids.liquid_copper, 81),
                            new FluidStack(FoundryFluids.liquid_silver, 27), new FluidStack(liquid_redstone, 250) });

        if (liquid_glowstone != null)
            AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(FoundryFluids.liquid_lumium, 108),
                    new FluidStack[] { new FluidStack(FoundryFluids.liquid_tin, 81),
                            new FluidStack(FoundryFluids.liquid_silver, 27), new FluidStack(liquid_glowstone, 250) });

        if (liquid_enderpearl != null)
            AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(FoundryFluids.liquid_enderium, 108),
                    new FluidStack[] { new FluidStack(FoundryFluids.liquid_tin, 54),
                            new FluidStack(FoundryFluids.liquid_silver, 27),
                            new FluidStack(FoundryFluids.liquid_platinum, 27),
                            new FluidStack(liquid_enderpearl, 250) });

        if (FoundryConfig.recipe_steel)
        {
            InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(FoundryFluids.liquid_steel, FLUID_AMOUNT_INGOT / 4),
                    new FluidStack(FoundryFluids.liquid_iron, FLUID_AMOUNT_INGOT / 4), new OreMatcher("dustCoal"), 2000);
            InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(FoundryFluids.liquid_steel, FLUID_AMOUNT_INGOT / 4),
                    new FluidStack(FoundryFluids.liquid_iron, FLUID_AMOUNT_INGOT / 4), new OreMatcher("dustCharcoal"), 4000);
            if (OreDictionary.doesOreNameExist("dustSmallCoal"))
                InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(FoundryFluids.liquid_steel, FLUID_AMOUNT_INGOT / 16),
                        new FluidStack(FoundryFluids.liquid_iron, FLUID_AMOUNT_INGOT / 16), new OreMatcher("dustSmallCoal"), 500);
            if (OreDictionary.doesOreNameExist("dustSmallCharcoal"))
                InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(FoundryFluids.liquid_steel, FLUID_AMOUNT_INGOT / 16),
                        new FluidStack(FoundryFluids.liquid_iron, FLUID_AMOUNT_INGOT / 16), new OreMatcher("dustSmallCharcoal"), 1000);
            if (OreDictionary.doesOreNameExist("fuelCoke"))
                InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(FoundryFluids.liquid_steel, FLUID_AMOUNT_INGOT),
                        new FluidStack(FoundryFluids.liquid_iron, FLUID_AMOUNT_INGOT), new OreMatcher("fuelCoke"), 5000);
            if (OreDictionary.doesOreNameExist("dustCoke"))
                InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(FoundryFluids.liquid_steel, FLUID_AMOUNT_INGOT),
                        new FluidStack(FoundryFluids.liquid_iron, FLUID_AMOUNT_INGOT), new OreMatcher("dustCoke"), 4000);
        }
    }
}
