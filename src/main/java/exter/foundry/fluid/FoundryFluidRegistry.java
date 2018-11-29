package exter.foundry.fluid;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import exter.foundry.block.BlockLiquidMetal;
import exter.foundry.config.FoundryConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Utility class for registering a metal's corresponding block, items, fluid, and recipes.
 */
public class FoundryFluidRegistry
{
    private static final Map<String, IntegrationStrategy> map = new LinkedHashMap<>();

    private FoundryFluidRegistry()
    {
    }

    public static Set<String> getFluidNames()
    {
        return Collections.unmodifiableSet(map.keySet());
    }

    public static IntegrationStrategy getStrategy(String name)
    {
        return map.getOrDefault(name, IntegrationStrategy.DISABLED);
    }

    public static Map<String, IntegrationStrategy> getMap()
    {
        return Collections.unmodifiableMap(map);
    }

    /**
     * Helper method to register a metal's fluid, and block.
     * @param metal_name Name of the metal e.g: "Copper" for "oreCopper" in the Ore Dictionary.
     */
    @Nullable
    public static FluidLiquidMetal registerLiquidMetal(IForgeRegistry<Block> registry, String metal_name, String category, int temperature, int luminosity, int color)
    {
        return registerLiquidMetal(registry, metal_name, category, temperature, luminosity, metal_name,
                color | 0xFF000000);
    }

    @Nullable
    public static FluidLiquidMetal registerLiquidMetal(IForgeRegistry<Block> registry, String metal_name, String category, int temperature, int luminosity, String texture, int color)
    {
        if (registerEntry(metal_name, category).enabled())
        {
            FluidLiquidMetal fluid = new FluidLiquidMetal(metal_name, color, false, temperature, luminosity);
            FluidRegistry.registerFluid(fluid);
            FluidRegistry.addBucketForFluid(fluid);

            Block liquid_block = new BlockLiquidMetal(fluid, metal_name, null);
            registry.register(liquid_block);
            fluid.setBlock(liquid_block);

            return fluid;
        }
        else
        {
            return null;
        }
    }

    @Nullable
    public static FluidLiquidMetal registerLiquidGlass(IForgeRegistry<Block> registry, String glass_name, String category, int temperature, int luminosity, IBlockState solid)
    {
        return registerLiquidGlass(registry, glass_name, category, temperature, luminosity, glass_name, 0xFFFFFF,
                solid);
    }

    @Nullable
    public static FluidLiquidMetal registerLiquidGlass(IForgeRegistry<Block> registry, String glass_name, String category, int temperature, int luminosity, String texture, int color, IBlockState solid)
    {
        if (registerEntry(glass_name, category).enabled())
        {
            FluidLiquidMetal fluid = new FluidLiquidMetal(glass_name, color, true, temperature, luminosity);
            FluidRegistry.registerFluid(fluid);
            FluidRegistry.addBucketForFluid(fluid);

            Block liquid_block = new BlockLiquidMetal(fluid, glass_name, solid);
            registry.register(liquid_block);
            fluid.setBlock(liquid_block);
            return fluid;
        }
        else
        {
            return null;
        }
    }

    private static IntegrationStrategy registerEntry(String name, String category)
    {
        String strategyString = FoundryConfig.config.getString(name, category, IntegrationStrategy.ENABLED.name(),
                "Valid values: ENABLED, DISABLED, NO_RECIPES");
        IntegrationStrategy strategy = IntegrationStrategy.customValueOf(strategyString);
        map.put(name, strategy);
        return strategy;

    }

    public static enum IntegrationStrategy
    {
        ENABLED, DISABLED, NO_RECIPES;

        public boolean enabled()
        {
            return this != IntegrationStrategy.DISABLED;
        }

        public boolean registerRecipes()
        {
            return this == IntegrationStrategy.ENABLED;
        }

        public static IntegrationStrategy customValueOf(String name)
        {
            switch (name.toUpperCase(Locale.ENGLISH))
            {
            default:
            case "ENABLED":
                return ENABLED;
            case "DISABLED":
                return DISABLED;
            case "NO_RECIPES":
                return NO_RECIPES;
            }
        }
    }
}
