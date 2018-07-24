package exter.foundry.tileentity;

import exter.foundry.api.FoundryAPI;

public class TileEntityMeltingCrucibleAdvanced extends TileEntityMeltingCrucibleBasic
{
    @Override
    public int getMaxTemperature()
    {
        return FoundryAPI.CRUCIBLE_ADVANCED_MAX_TEMP;
    }

    @Override
    public int getTemperatureLossRate()
    {
        return FoundryAPI.CRUCIBLE_ADVANCED_TEMP_LOSS_RATE;
    }
}
