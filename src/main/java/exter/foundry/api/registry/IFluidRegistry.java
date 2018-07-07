package exter.foundry.api.registry;

import net.minecraftforge.fluids.Fluid;

/**
 * Contains all fluids Registered by foundry
 */
public interface IFluidRegistry
{
    /**
     * Get a Foundry fluid by it's name.
     * @param name Name of the fluid.
     * @return The fluid.
     */
    public Fluid getFluid(String name);
    /*
    */

}
