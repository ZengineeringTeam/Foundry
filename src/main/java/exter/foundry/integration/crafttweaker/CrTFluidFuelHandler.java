package exter.foundry.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.liquid.ILiquidDefinition;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.IFluidHeaterFuel;
import exter.foundry.integration.ModIntegrationCrafttweaker;
import exter.foundry.recipes.FluidHeaterFuel;
import exter.foundry.recipes.FluidHeaterFuelImpl;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.foundry.FluidHeater")
public class CrTFluidFuelHandler
{
    // I dont know why do this but well
    public static class FluidFuelAction extends AddRemoveAction
    {
        IFluidHeaterFuel fuel;

        public FluidFuelAction(IFluidHeaterFuel fuel)
        {
            this.fuel = fuel;
        }

        @Override
        protected void add()
        {
            FoundryAPI.FLUID_HEATER_FUEL.addFuel(fuel);
        }

        @Override
        public String getDescription()
        {
            return String.format("%s -> (Heat: %s)", fuel.getFluid(), fuel.getHeat());
        }

        @Override
        public String getRecipeType()
        {
            return "fluid heater fuel";
        }

        @Override
        protected void remove()
        {
            FoundryAPI.FLUID_HEATER_FUEL.removeFuel(fuel);
        }
    }

    @ZenMethod
    public static void addFuel(ILiquidDefinition fuel)
    {
        ModIntegrationCrafttweaker.queueAdd(() -> {
            Fluid fluid = FluidRegistry.getFluid(fuel.getName());
            if (fluid != null)
            {
                CraftTweakerAPI.apply(new FluidFuelAction(new FluidHeaterFuel(fluid)).action_add);
            }
        });
    }

    @ZenMethod
    public static void addFuel(ILiquidDefinition fuel, int heat)
    {
        ModIntegrationCrafttweaker.queueAdd(() -> {
            Fluid fluid = FluidRegistry.getFluid(fuel.getName());
            if (fluid != null)
            {
                CraftTweakerAPI.apply(new FluidFuelAction(new FluidHeaterFuelImpl(fluid, heat)).action_add);
            }
        });
    }

    @ZenMethod
    public static void removeFuel(ILiquidDefinition liquid)
    {
        ModIntegrationCrafttweaker.queueRemove(() -> {
            Fluid fluid = FluidRegistry.getFluid(liquid.getName());
            if (fluid == null)
            {
                CrTHelper.printCrt("No fluid: " + liquid);
                return;
            }
            IFluidHeaterFuel fuel = FoundryAPI.FLUID_HEATER_FUEL.getFuel(fluid);

            if (fuel != null)
                CraftTweakerAPI.apply(new FluidFuelAction(fuel).action_remove);
            else
                CrTHelper.printCrt("No fluid fuel found for " + liquid);
        });
    }

    @ZenMethod
    public static void clear()
    {
        ModIntegrationCrafttweaker.queueClear(FoundryAPI.FLUID_HEATER_FUEL.getFuels());
    }
}
