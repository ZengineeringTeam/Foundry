package exter.foundry.integration;

import exter.foundry.integration.top.CastingTableProvider;
import exter.foundry.integration.top.MeltingCrucibleProvider;
import exter.foundry.integration.top.MoldStationProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Function;

public class ModIntegrationTheOneProbe implements IModIntegration
{

    @Override
    public String getName()
    {
        return "TheOneProbe";
    }

    @Override
    public void onAfterPostInit()
    {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onClientInit()
    {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onClientPostInit()
    {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onClientPreInit()
    {

    }

    @Override
    public void onInit()
    {
    }

    @Override
    public void onPostInit()
    {

    }

    @Override
    public void onPreInit(Configuration config)
    {
        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "exter.foundry.integration.ModIntegrationTheOneProbe$GetTheOneProbe");
    }

    public static class GetTheOneProbe implements Function<ITheOneProbe, Void>
    {
        @Override
        public Void apply(ITheOneProbe probe)
        {
            probe.registerProvider(new CastingTableProvider());
            probe.registerProvider(new MoldStationProvider());
            probe.registerProvider(new MeltingCrucibleProvider());
            return null;
        }
    }
}
