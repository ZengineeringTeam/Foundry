package exter.foundry.integration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.CraftTweaker;
import exter.foundry.integration.crafttweaker.CrTAlloyMixerHandler;
import exter.foundry.integration.crafttweaker.CrTAlloyingCrucibleHandler;
import exter.foundry.integration.crafttweaker.CrTBurnerFuelHandler;
import exter.foundry.integration.crafttweaker.CrTCastingHandler;
import exter.foundry.integration.crafttweaker.CrTCastingTableHandler;
import exter.foundry.integration.crafttweaker.CrTFluidFuelHandler;
import exter.foundry.integration.crafttweaker.CrTInfuserHandler;
import exter.foundry.integration.crafttweaker.CrTMeltingHandler;
import exter.foundry.integration.crafttweaker.CrTMoldStationHandler;
import exter.foundry.integration.crafttweaker.orestack.OreStackBracketHandler;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModIntegrationCrafttweaker implements IModIntegration
{

    private static List<Runnable> addQueue = new ArrayList<>();
    private static List<Runnable> removeQueue = new ArrayList<>();
    private static List<Runnable> clearQueue = new ArrayList<>();

    @Override
    public String getName()
    {
        return CraftTweaker.NAME;
    }

    @Override
    public void onAfterPostInit()
    {
        for (Runnable r : clearQueue)
            r.run();
        for (Runnable r : removeQueue)
            r.run();
        for (Runnable r : addQueue)
            r.run();
        addQueue = removeQueue = clearQueue = null;
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
        CraftTweakerAPI.registerBracketHandler(new OreStackBracketHandler());
        CraftTweakerAPI.registerClass(CrTMeltingHandler.class);
        CraftTweakerAPI.registerClass(CrTCastingHandler.class);
        CraftTweakerAPI.registerClass(CrTCastingTableHandler.class);
        CraftTweakerAPI.registerClass(CrTAlloyMixerHandler.class);
        CraftTweakerAPI.registerClass(CrTAlloyingCrucibleHandler.class);
        CraftTweakerAPI.registerClass(CrTInfuserHandler.class);
        CraftTweakerAPI.registerClass(CrTMoldStationHandler.class);
        CraftTweakerAPI.registerClass(CrTBurnerFuelHandler.class);
        CraftTweakerAPI.registerClass(CrTFluidFuelHandler.class);
    }

    public static void queueAdd(Runnable action)
    {
        addQueue.add(action);
    }

    public static void queueRemove(Runnable action)
    {
        removeQueue.add(action);
    }

    public static void queueClear(Collection<?> recipes)
    {
        clearQueue.add(() -> recipes.clear());
    }

    public static void queueClear(Runnable run)
    {
        clearQueue.add(run);
    }
}
