package exter.foundry.fluid;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import exter.foundry.api.registry.IFluidRegistry;
import exter.foundry.block.BlockLiquidMetal;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Utility class for registering a metal's corresponding block, items, fluid, and recipes.
 */
public class FoundryFluidRegistry implements IFluidRegistry
{

    public static final FoundryFluidRegistry INSTANCE = new FoundryFluidRegistry();

    private final Map<String, FluidLiquidMetal> map = new WeakHashMap<>();

    private FoundryFluidRegistry()
    {
    }

    @Override
    public FluidLiquidMetal getFluid(String name)
    {
        return map.get(name);
    }

    public Set<String> getFluidNames()
    {
        return Collections.unmodifiableSet(map.keySet());
    }

    public Collection<FluidLiquidMetal> getFluids()
    {
        return Collections.unmodifiableCollection(map.values());
    }

    public Map<String, FluidLiquidMetal> getMap()
    {
        return Collections.unmodifiableMap(map);
    }

    /**
     * Helper method to register a metal's fluid, and block.
     * @param metal_name Name of the metal e.g: "Copper" for "oreCopper" in the Ore Dictionary.
     */
    public FluidLiquidMetal registerLiquidMetal(IForgeRegistry<Block> registry, String metal_name, int temperature, int luminosity, int color)
    {
        return registerLiquidMetal(registry, metal_name, temperature, luminosity, metal_name, color | 0xFF000000);
    }

    public FluidLiquidMetal registerLiquidMetal(IForgeRegistry<Block> registry, String metal_name, int temperature, int luminosity, String texture, int color)
    {
        FluidLiquidMetal fluid = new FluidLiquidMetal(metal_name, color, false, temperature, luminosity);
        FluidRegistry.registerFluid(fluid);
        FluidRegistry.addBucketForFluid(fluid);

        Block liquid_block = new BlockLiquidMetal(fluid, metal_name, null);
        registry.register(liquid_block);
        fluid.setBlock(liquid_block);

        map.put(fluid.getName(), fluid);

        return fluid;
    }

    public FluidLiquidMetal registerSpecialLiquidMetal(IForgeRegistry<Block> registry, String metal_name, int temperature, int luminosity, IBlockState solid)
    {
        return registerSpecialLiquidMetal(registry, metal_name, temperature, luminosity, metal_name, 0xFFFFFF, solid);
    }

    public FluidLiquidMetal registerSpecialLiquidMetal(IForgeRegistry<Block> registry, String metal_name, int temperature, int luminosity, String texture, int color, IBlockState solid)
    {
        FluidLiquidMetal fluid = new FluidLiquidMetal(metal_name, color, true, temperature, luminosity);
        FluidRegistry.registerFluid(fluid);
        FluidRegistry.addBucketForFluid(fluid);

        Block liquid_block = new BlockLiquidMetal(fluid, metal_name, solid);
        registry.register(liquid_block);
        fluid.setBlock(liquid_block);

        map.put(fluid.getName(), fluid);

        return fluid;
    }
}
