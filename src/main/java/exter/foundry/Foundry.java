package exter.foundry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import exter.foundry.api.FoundryAPI;
import exter.foundry.capability.CapabilityHeatProvider;
import exter.foundry.config.FoundryConfig;
import exter.foundry.fluid.FoundryFluidRegistry;
import exter.foundry.init.InitRecipes;
import exter.foundry.integration.ModIntegrationBotania;
import exter.foundry.integration.ModIntegrationCrafttweaker;
import exter.foundry.integration.ModIntegrationEnderIO;
import exter.foundry.integration.ModIntegrationManager;
import exter.foundry.material.MaterialRegistry;
import exter.foundry.network.MessageTileEntitySync;
import exter.foundry.proxy.CommonProxy;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.AlloyingCrucibleRecipeManager;
import exter.foundry.recipes.manager.BurnerHeaterFuelManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.CastingTableRecipeManager;
import exter.foundry.recipes.manager.FluidHeaterFuelManager;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.recipes.manager.MoldRecipeManager;
import exter.foundry.tileentity.TileEntityAlloyMixer;
import exter.foundry.tileentity.TileEntityAlloyingCrucible;
import exter.foundry.tileentity.TileEntityBurnerHeater;
import exter.foundry.tileentity.TileEntityCastingTableBlock;
import exter.foundry.tileentity.TileEntityCastingTableIngot;
import exter.foundry.tileentity.TileEntityCastingTablePlate;
import exter.foundry.tileentity.TileEntityCastingTableRod;
import exter.foundry.tileentity.TileEntityFluidHeater;
import exter.foundry.tileentity.TileEntityInductionHeater;
import exter.foundry.tileentity.TileEntityMaterialRouter;
import exter.foundry.tileentity.TileEntityMeltingCrucibleAdvanced;
import exter.foundry.tileentity.TileEntityMeltingCrucibleBasic;
import exter.foundry.tileentity.TileEntityMeltingCrucibleStandard;
import exter.foundry.tileentity.TileEntityMetalCaster;
import exter.foundry.tileentity.TileEntityMetalInfuser;
import exter.foundry.tileentity.TileEntityMoldStation;
import net.minecraft.util.ResourceLocation;
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

@Mod(
        modid = Foundry.MODID,
        name = Foundry.MODNAME,
        version = "@VERSION_INJECT@",
        dependencies = "required-after:ceramics;required-after:thermalfoundation;after:mekanism"
)
public class Foundry
{
    public static final String MODID = "foundry";
    public static final String MODNAME = "Zen: Foundry";

    @SidedProxy(clientSide = "exter.foundry.proxy.ClientProxy", serverSide = "exter.foundry.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Instance
    public static Foundry INSTANCE = null;

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

    static
    {
        FluidRegistry.enableUniversalBucket();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        ModIntegrationManager.init();
        InitRecipes.init();

        GameRegistry.registerTileEntity(TileEntityMeltingCrucibleBasic.class,
                new ResourceLocation(MODID, "melt_crucible_basic"));
        GameRegistry.registerTileEntity(TileEntityMeltingCrucibleStandard.class,
                new ResourceLocation(MODID, "melt_crucible_standard"));
        GameRegistry.registerTileEntity(TileEntityMetalCaster.class, new ResourceLocation(MODID, "metal_caster"));
        GameRegistry.registerTileEntity(TileEntityAlloyMixer.class, new ResourceLocation(MODID, "alloy_mixer"));
        GameRegistry.registerTileEntity(TileEntityMetalInfuser.class, new ResourceLocation(MODID, "metal_infuser"));
        GameRegistry.registerTileEntity(TileEntityMoldStation.class, new ResourceLocation(MODID, "mold_station"));
        GameRegistry.registerTileEntity(TileEntityMaterialRouter.class, new ResourceLocation(MODID, "material_router"));
        GameRegistry.registerTileEntity(TileEntityInductionHeater.class,
                new ResourceLocation(MODID, "induction_heater"));
        GameRegistry.registerTileEntity(TileEntityBurnerHeater.class, new ResourceLocation(MODID, "burner_heater"));
        GameRegistry.registerTileEntity(TileEntityCastingTableIngot.class,
                new ResourceLocation(MODID, "cast_table_ingot"));
        GameRegistry.registerTileEntity(TileEntityCastingTablePlate.class,
                new ResourceLocation(MODID, "cast_table_plate"));
        GameRegistry.registerTileEntity(TileEntityCastingTableRod.class, new ResourceLocation(MODID, "cast_table_rod"));
        GameRegistry.registerTileEntity(TileEntityCastingTableBlock.class,
                new ResourceLocation(MODID, "cast_table_block"));
        GameRegistry.registerTileEntity(TileEntityMeltingCrucibleAdvanced.class,
                new ResourceLocation(MODID, "melt_crucible_advanced"));
        GameRegistry.registerTileEntity(TileEntityAlloyingCrucible.class,
                new ResourceLocation(MODID, "alloy_crucible"));
        GameRegistry.registerTileEntity(TileEntityFluidHeater.class, new ResourceLocation(MODID, "fluid_heater"));

        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        ModIntegrationManager.postInit();
        InitRecipes.postInit();
        proxy.postInit();
        ModIntegrationManager.finalStep();
        if (FoundryConfig.config.hasChanged())
        {
            FoundryConfig.config.save();
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        FoundryConfig.load(event.getSuggestedConfigurationFile());

        if (Loader.isModLoaded("enderio"))
            ModIntegrationManager.registerIntegration(FoundryConfig.config, new ModIntegrationEnderIO());
        if (Loader.isModLoaded("botania"))
            ModIntegrationManager.registerIntegration(FoundryConfig.config, new ModIntegrationBotania());
        if (Loader.isModLoaded("crafttweaker"))
            ModIntegrationManager.registerIntegration(FoundryConfig.config, new ModIntegrationCrafttweaker());

        FoundryAPI.FLUIDS = FoundryFluidRegistry.INSTANCE;

        FoundryAPI.MELTING_MANAGER = MeltingRecipeManager.INSTANCE;
        FoundryAPI.CASTING_MANAGER = CastingRecipeManager.INSTANCE;
        FoundryAPI.CASTING_TABLE_MANAGER = CastingTableRecipeManager.INSTANCE;
        FoundryAPI.ALLOY_MIXER_MANAGER = AlloyMixerRecipeManager.INSTANCE;
        FoundryAPI.INFUSER_MANAGER = InfuserRecipeManager.INSTANCE;
        FoundryAPI.MOLD_MANAGER = MoldRecipeManager.INSTANCE;
        FoundryAPI.ALLOYING_CRUCIBLE_MANAGER = AlloyingCrucibleRecipeManager.INSTANCE;

        FoundryAPI.MATERIALS = MaterialRegistry.INSTANCE;
        FoundryAPI.BURNER_HEATER_FUEL = BurnerHeaterFuelManager.INSTANCE;
        FoundryAPI.FLUID_HEATER_FUEL = FluidHeaterFuelManager.INSTANCE;

        CapabilityHeatProvider.init();

        ModIntegrationManager.preInit(FoundryConfig.config);

        NETWORK.registerMessage(MessageTileEntitySync.Handler.class, MessageTileEntitySync.class, 0, Side.SERVER);
        NETWORK.registerMessage(MessageTileEntitySync.Handler.class, MessageTileEntitySync.class, 0, Side.CLIENT);

        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
        proxy.preInit();
    }
}
