package exter.foundry.init;

import exter.foundry.api.recipe.matcher.IItemMatcher;
import exter.foundry.api.recipe.matcher.OreMatcher;
import exter.foundry.config.FoundryConfig;
import exter.foundry.config.MetalConfig;
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

import java.util.ArrayList;

import static exter.foundry.api.FoundryAPI.FLUID_AMOUNT_INGOT;

public class InitAlloyRecipes
{
    // Create recipes for all alloy making machines.
    static private void addSimpleAlloy(String output, String input_a, int amount_a, String input_b, int amount_b)
    {
        Fluid fluid_out = FoundryFluidRegistry.INSTANCE.getFluid(output);
        Fluid fluid_in_a = FoundryFluidRegistry.INSTANCE.getFluid(input_a);
        Fluid fluid_in_b = FoundryFluidRegistry.INSTANCE.getFluid(input_b);

        if (fluid_out != null && fluid_in_a != null && fluid_in_b != null) {
            ItemStack alloy_ingot = MiscUtil.getModItemFromOreDictionary("ingot" + output, amount_a + amount_b);
            if (!alloy_ingot.isEmpty())
            {
                checkAndAddRecipe(alloy_ingot, input_a, amount_a, input_b, amount_b);
            }

            AlloyingCrucibleRecipeManager.INSTANCE.addRecipe(new FluidStack(fluid_out, (amount_a + amount_b) * 3),
                    new FluidStack(fluid_in_a, amount_a * 3), new FluidStack(fluid_in_b, amount_b * 3));

            AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(fluid_out, amount_a + amount_b),
                    new FluidStack[]{new FluidStack(fluid_in_a, amount_a), new FluidStack(fluid_in_b, amount_b)});
        }
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
        if (MetalConfig.metals.get("bronze") == MetalConfig.IntegrationStrategy.ENABLED)
            addSimpleAlloy("bronze", "copper", 3, "tin", 1);

        if (MetalConfig.metals.get("brass") == MetalConfig.IntegrationStrategy.ENABLED)
            addSimpleAlloy("brass", "copper", 3,
                !OreDictionary.getOres("ingotZinc", false).isEmpty() ? "zinc" : "aluminium", 1);

        if (MetalConfig.metals.get("invar") == MetalConfig.IntegrationStrategy.ENABLED)
            addSimpleAlloy("invar", "iron", 2, "nickel", 1);

        if (MetalConfig.metals.get("electrum") == MetalConfig.IntegrationStrategy.ENABLED)
            addSimpleAlloy("electrum", "gold", 1, "silver", 1);

        if (MetalConfig.metals.get("constantan") == MetalConfig.IntegrationStrategy.ENABLED)
            addSimpleAlloy("constantan", "copper", 1, "nickel", 1);

        if (MetalConfig.metals.get("steel") == MetalConfig.IntegrationStrategy.ENABLED && FoundryConfig.recipe_steel && FoundryFluids.liquid_steel != null && FoundryFluids.liquid_iron != null)
        {
            if (OreDictionary.doesOreNameExist("dustCoal"))
                InfuserRecipeManager.INSTANCE.addRecipe(
                        new FluidStack(FoundryFluids.liquid_steel, FLUID_AMOUNT_INGOT / 4),
                        new FluidStack(FoundryFluids.liquid_iron, FLUID_AMOUNT_INGOT / 4), new OreMatcher("dustCoal"),
                        2000);
            if (OreDictionary.doesOreNameExist("dustCharcoal"))
                InfuserRecipeManager.INSTANCE.addRecipe(
                        new FluidStack(FoundryFluids.liquid_steel, FLUID_AMOUNT_INGOT / 4),
                        new FluidStack(FoundryFluids.liquid_iron, FLUID_AMOUNT_INGOT / 4),
                        new OreMatcher("dustCharcoal"), 4000);
            if (OreDictionary.doesOreNameExist("dustSmallCoal"))
                InfuserRecipeManager.INSTANCE.addRecipe(
                        new FluidStack(FoundryFluids.liquid_steel, FLUID_AMOUNT_INGOT / 16),
                        new FluidStack(FoundryFluids.liquid_iron, FLUID_AMOUNT_INGOT / 16),
                        new OreMatcher("dustSmallCoal"), 500);
            if (OreDictionary.doesOreNameExist("dustSmallCharcoal"))
                InfuserRecipeManager.INSTANCE.addRecipe(
                        new FluidStack(FoundryFluids.liquid_steel, FLUID_AMOUNT_INGOT / 16),
                        new FluidStack(FoundryFluids.liquid_iron, FLUID_AMOUNT_INGOT / 16),
                        new OreMatcher("dustSmallCharcoal"), 1000);
            if (OreDictionary.doesOreNameExist("fuelCoke"))
                InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(FoundryFluids.liquid_steel, FLUID_AMOUNT_INGOT),
                        new FluidStack(FoundryFluids.liquid_iron, FLUID_AMOUNT_INGOT), new OreMatcher("fuelCoke"),
                        5000);
            if (OreDictionary.doesOreNameExist("dustCoke"))
                InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(FoundryFluids.liquid_steel, FLUID_AMOUNT_INGOT),
                        new FluidStack(FoundryFluids.liquid_iron, FLUID_AMOUNT_INGOT), new OreMatcher("dustCoke"),
                        4000);
        }
    }
}
