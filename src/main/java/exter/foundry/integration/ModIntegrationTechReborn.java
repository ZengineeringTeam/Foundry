package exter.foundry.integration;

import exter.foundry.api.FoundryUtils;
import exter.foundry.fluid.FluidLiquidMetal;
import exter.foundry.fluid.FoundryFluidRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class ModIntegrationTechReborn implements IModIntegration
{
    public static final String TECHREBORN = "techreborn";

    public static Fluid liquid_chrome;
    public static Fluid liquid_titanium;
    public static Fluid liquid_tungsten;
    public static Fluid liquid_refined_iron;

    @Override
    public String getName()
    {
        return TECHREBORN;
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
    }

    @SubscribeEvent
    public void registerFluids(RegistryEvent.Register<Block> e)
    {
        IForgeRegistry<Block> registry = e.getRegistry();

        liquid_chrome = FoundryFluidRegistry.registerLiquidMetal(registry, "chrome", "Tech Reborn", 3400, 15, 0xF9AEAE);
        liquid_titanium = FoundryFluidRegistry.registerLiquidMetal(registry, "titanium", "Tech Reborn", 3000, 15, 0x999BFF);
        liquid_tungsten = FoundryFluidRegistry.registerLiquidMetal(registry, "tungsten", "Tech Reborn", 3950, 15, 0x4A4E51);
        liquid_refined_iron = FoundryFluidRegistry.registerLiquidMetal(registry, "refined_iron", "Tech Reborn", 1940, 15, 0x76A6E9);
    }
}
