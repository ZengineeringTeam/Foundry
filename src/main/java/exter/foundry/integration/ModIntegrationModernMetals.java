package exter.foundry.integration;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModIntegrationModernMetals implements IModIntegration
{
    public static final String MODID = "modernmetals";

    @Override
    public String getName()
    {
        return "Modern Metals";
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
        String[] names = new String[] { "aluminum", "aluminumbrass", "beryllium", "boron", "cadmium", "chromium", "galvanizedsteel", "iridium", "magnesium", "manganese", "nichrome", "osmium", "plutonium", "rutile", "stainlesssteel", "tantalum", "thorium", "titanium", "tungsten", "uranium", "zirconium" };
        for (String name : names)
        {
            Helper.registerOSRecipes(MODID, name);
        }
    }

    @Override
    public void onPreInit(Configuration config)
    {
    }
}
