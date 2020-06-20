package exter.foundry.tileentity;

import exter.foundry.api.heatable.IHeatProvider;

public class ConstantsHeatProvider implements IHeatProvider
{
    private final int heat;

    public ConstantsHeatProvider(int heat)
    {
        this.heat = heat;
    }

    @Override
    public int provideHeat(int max_heat, int heat)
    {
        return this.heat;
    }

}
