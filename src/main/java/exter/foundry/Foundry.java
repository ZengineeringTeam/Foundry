package exter.foundry;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cofh.cofhworld.init.WorldProps;
import cofh.cofhworld.util.Utils;
import exter.foundry.api.FoundryAPI;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.capability.CapabilityHeatProvider;
import exter.foundry.config.FoundryConfig;
import exter.foundry.fluid.FoundryFluids;
import exter.foundry.fluid.LiquidMetalRegistry;
import exter.foundry.init.InitRecipes;
import exter.foundry.integration.ModIntegrationBotania;
import exter.foundry.integration.ModIntegrationEnderIO;
import exter.foundry.integration.ModIntegrationManager;
import exter.foundry.integration.ModIntegrationMinetweaker;
import exter.foundry.integration.ModIntegrationMolten;
import exter.foundry.integration.ModIntegrationTiCon;
import exter.foundry.item.FoundryItems;
import exter.foundry.material.MaterialRegistry;
import exter.foundry.network.MessageTileEntitySync;
import exter.foundry.proxy.CommonFoundryProxy;
import exter.foundry.recipes.manager.AlloyFurnaceRecipeManager;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.AlloyingCrucibleRecipeManager;
import exter.foundry.recipes.manager.AtomizerRecipeManager;
import exter.foundry.recipes.manager.BurnerHeaterFuelManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.CastingTableRecipeManager;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.recipes.manager.MoldRecipeManager;
import exter.foundry.tileentity.TileEntityAlloyFurnace;
import exter.foundry.tileentity.TileEntityAlloyMixer;
import exter.foundry.tileentity.TileEntityAlloyingCrucible;
import exter.foundry.tileentity.TileEntityBurnerHeater;
import exter.foundry.tileentity.TileEntityCastingTableBlock;
import exter.foundry.tileentity.TileEntityCastingTableIngot;
import exter.foundry.tileentity.TileEntityCastingTablePlate;
import exter.foundry.tileentity.TileEntityCastingTableRod;
import exter.foundry.tileentity.TileEntityInductionHeater;
import exter.foundry.tileentity.TileEntityMaterialRouter;
import exter.foundry.tileentity.TileEntityMeltingCrucibleAdvanced;
import exter.foundry.tileentity.TileEntityMeltingCrucibleBasic;
import exter.foundry.tileentity.TileEntityMeltingCrucibleStandard;
import exter.foundry.tileentity.TileEntityMetalAtomizer;
import exter.foundry.tileentity.TileEntityMetalCaster;
import exter.foundry.tileentity.TileEntityMetalInfuser;
import exter.foundry.tileentity.TileEntityMoldStation;
import exter.foundry.tileentity.TileEntityRefractoryHopper;
import exter.foundry.tileentity.TileEntityRefractorySpout;
import exter.foundry.tileentity.TileEntityRefractoryTankAdvanced;
import exter.foundry.tileentity.TileEntityRefractoryTankBasic;
import exter.foundry.tileentity.TileEntityRefractoryTankStandard;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import shadows.placebo.registry.RegistryInformation;
import shadows.placebo.util.RecipeHelper;

@Mod(
        modid = Foundry.MODID,
        name = Foundry.MODNAME,
        version = Foundry.MODVERSION,
        dependencies = "required-after:placebo@[1.2.0,);required-after:thermalfoundation;after:jei;after:tconstruct;after:mekanism"
)
public class Foundry
{
    public static final String MODID = "foundry";
    public static final String MODNAME = "Foundry";
    public static final String MODVERSION = "3.1.4.1";

    @SidedProxy(
            clientSide = "exter.foundry.proxy.ClientFoundryProxy",
            serverSide = "exter.foundry.proxy.CommonFoundryProxy"
    )
    public static CommonFoundryProxy proxy;

    @Instance
    public static Foundry INSTANCE = null;

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

    public static final RegistryInformation INFO = new RegistryInformation(MODID, null);

    public static final RecipeHelper HELPER = new RecipeHelper(MODID, MODNAME, INFO.getRecipeList());

    static
    {
        FluidRegistry.enableUniversalBucket();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        ModIntegrationManager.init();
        InitRecipes.init();

        GameRegistry.registerTileEntity(TileEntityMeltingCrucibleBasic.class, MODID + ":melt_crucible_basic");
        GameRegistry.registerTileEntity(TileEntityMeltingCrucibleStandard.class, MODID + ":melt_crucible_standard");
        GameRegistry.registerTileEntity(TileEntityMetalCaster.class, MODID + ":metal_caster");
        GameRegistry.registerTileEntity(TileEntityAlloyMixer.class, MODID + ":alloy_mixer");
        GameRegistry.registerTileEntity(TileEntityMetalInfuser.class, MODID + ":metal_infuser");
        GameRegistry.registerTileEntity(TileEntityAlloyFurnace.class, MODID + ":alloy_furnace");
        GameRegistry.registerTileEntity(TileEntityMoldStation.class, MODID + ":mold_station");
        GameRegistry.registerTileEntity(TileEntityMaterialRouter.class, MODID + ":material_router");
        GameRegistry.registerTileEntity(TileEntityRefractoryHopper.class, MODID + ":refractory_hopper");
        GameRegistry.registerTileEntity(TileEntityMetalAtomizer.class, MODID + ":atomizer");
        GameRegistry.registerTileEntity(TileEntityInductionHeater.class, MODID + ":induction_heater");
        GameRegistry.registerTileEntity(TileEntityBurnerHeater.class, MODID + ":burner_heater");
        GameRegistry.registerTileEntity(TileEntityCastingTableIngot.class, MODID + ":cast_table_ingot");
        GameRegistry.registerTileEntity(TileEntityCastingTablePlate.class, MODID + ":cast_table_plate");
        GameRegistry.registerTileEntity(TileEntityCastingTableRod.class, MODID + ":cast_table_rod");
        GameRegistry.registerTileEntity(TileEntityCastingTableBlock.class, MODID + ":cast_table_block");
        GameRegistry.registerTileEntity(TileEntityRefractorySpout.class, MODID + ":refractory_spout");
        GameRegistry.registerTileEntity(TileEntityMeltingCrucibleAdvanced.class, MODID + ":melt_crucible_advanced");
        GameRegistry.registerTileEntity(TileEntityRefractoryTankBasic.class, MODID + ":tank_basic");
        GameRegistry.registerTileEntity(TileEntityRefractoryTankStandard.class, MODID + ":tank_standard");
        GameRegistry.registerTileEntity(TileEntityRefractoryTankAdvanced.class, MODID + ":tank_advanced");
        GameRegistry.registerTileEntity(TileEntityAlloyingCrucible.class, MODID + ":alloy_crucible");

        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        ModIntegrationManager.postInit();
        InitRecipes.postInit();
        proxy.postInit();
        ModIntegrationManager.finalStep();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new FoundryRegistry());
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        ModIntegrationManager.registerIntegration(config, new ModIntegrationMolten());
        if (Loader.isModLoaded("tconstruct"))
            ModIntegrationManager.registerIntegration(config, new ModIntegrationTiCon());
        if (Loader.isModLoaded("enderio"))
            ModIntegrationManager.registerIntegration(config, new ModIntegrationEnderIO());
        if (Loader.isModLoaded("botania"))
            ModIntegrationManager.registerIntegration(config, new ModIntegrationBotania());
        if (Loader.isModLoaded("crafttweaker"))
            ModIntegrationManager.registerIntegration(config, new ModIntegrationMinetweaker());

        FoundryAPI.FLUIDS = LiquidMetalRegistry.INSTANCE;

        FoundryAPI.MELTING_MANAGER = MeltingRecipeManager.INSTANCE;
        FoundryAPI.CASTING_MANAGER = CastingRecipeManager.INSTANCE;
        FoundryAPI.CASTING_TABLE_MANAGER = CastingTableRecipeManager.INSTANCE;
        FoundryAPI.ALLOY_MIXER_MANAGER = AlloyMixerRecipeManager.INSTANCE;
        FoundryAPI.INFUSER_MANAGER = InfuserRecipeManager.INSTANCE;
        FoundryAPI.ALLOY_FURNACE_MANAGER = AlloyFurnaceRecipeManager.INSTANCE;
        FoundryAPI.ATOMIZER_MANAGER = AtomizerRecipeManager.INSTANCE;
        FoundryAPI.MOLD_MANAGER = MoldRecipeManager.INSTANCE;
        FoundryAPI.ALLOYING_CRUCIBLE_MANAGER = AlloyingCrucibleRecipeManager.INSTANCE;

        FoundryAPI.MATERIALS = MaterialRegistry.INSTANCE;
        FoundryAPI.BURNER_HEATER_FUEL = BurnerHeaterFuelManager.INSTANCE;

        CapabilityHeatProvider.init();

        FoundryConfig.load(config);
        FoundryItems.registerItems(config);
        FoundryBlocks.registerBlocks(config);

        FoundryFluids.init();

        ModIntegrationManager.preInit(config);

        config.save();

        NETWORK.registerMessage(MessageTileEntitySync.Handler.class, MessageTileEntitySync.class, 0, Side.SERVER);
        NETWORK.registerMessage(MessageTileEntitySync.Handler.class, MessageTileEntitySync.class, 0, Side.CLIENT);

        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
        proxy.preInit();

        try
        {
            File f = new File(WorldProps.worldGenDir, "04_foundry_aluminium.json");
            if (f.createNewFile())
                Utils.copyFileUsingStream("assets/foundry/world/04_foundry_aluminium.json", f);
        }
        catch (IOException e)
        {
            LOGGER.error("Failed to copy foundry aluminium generation file!");
            e.printStackTrace();
        }
    }
}
