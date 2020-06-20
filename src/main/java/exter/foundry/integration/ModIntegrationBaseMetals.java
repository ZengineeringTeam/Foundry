package exter.foundry.integration;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModIntegrationBaseMetals implements IModIntegration
{
    public static final String MODID = "basemetals";

    @Override
    public String getName()
    {
        return "Base Metals";
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
        String[] names = new String[] { "adamantine", "antimony", "aquarium", "bismuth", "brass", "bronze", "charcoal", "coal", "coldiron", "copper", "cupronickel", "diamond", "electrum", "emerald", "gold", "invar", "iron", "lead", "mithril", "nickel", "obsidian", "pewter", "platinum", "prismarine", "redstone", "silver", "starsteel", "steel", "tin", "zinc" };
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
