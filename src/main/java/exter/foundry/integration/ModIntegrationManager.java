package exter.foundry.integration;

import java.util.ArrayList;
import java.util.List;

import exter.foundry.Foundry;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ModIntegrationManager
{
    private static List<IModIntegration> integrations = new ArrayList<>();

    static public void finalStep()
    {
        Foundry.LOGGER.info("Integration Final Step");
        for (IModIntegration m : integrations)
        {
            m.onAfterPostInit();
        }
    }

    @SideOnly(Side.CLIENT)
    static public void clientInit()
    {
        for (IModIntegration m : integrations)
        {
            m.onClientInit();
        }
    }

    @SideOnly(Side.CLIENT)
    static public void clientPostInit()
    {
        for (IModIntegration m : integrations)
        {
            m.onClientPostInit();
        }
    }

    @SideOnly(Side.CLIENT)
    static public void clientPreInit()
    {
        for (IModIntegration m : integrations)
        {
            m.onClientPreInit();
        }
    }

    static public void init()
    {
        Foundry.LOGGER.info("Integration Init");
        for (IModIntegration m : integrations)
        {
            m.onInit();
        }
    }

    static public void postInit()
    {
        Foundry.LOGGER.info("Integration Post Init");
        for (IModIntegration m : integrations)
        {
            m.onPostInit();
        }
    }

    static public void preInit(Configuration config)
    {
        Foundry.LOGGER.info("Integration Pre Init");
        for (IModIntegration m : integrations)
        {
            m.onPreInit(config);
        }
    }

    static public void registerIntegration(Configuration config, IModIntegration imod)
    {
        String name = imod.getName();
        if (config.getBoolean("enable", "integration." + name, true, "Enable/disable mod integration."))
        {
            integrations.add(imod);
        }
    }

}
