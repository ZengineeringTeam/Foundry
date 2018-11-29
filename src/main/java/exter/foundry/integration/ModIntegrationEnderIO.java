package exter.foundry.integration;

import cofh.thermalfoundation.init.TFFluids;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.api.recipe.matcher.OreMatcher;
import exter.foundry.config.FoundryConfig;
import exter.foundry.config.MetalConfig;
import exter.foundry.fluid.FoundryFluids;
import exter.foundry.fluid.FoundryFluidRegistry;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.util.MiscUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashMap;
import java.util.Map;

import static exter.foundry.api.FoundryAPI.FLUID_AMOUNT_INGOT;

public class ModIntegrationEnderIO implements IModIntegration
{

    public static final String ENDERIO = "enderio";

    private Fluid liquid_redstone_alloy;
    private Fluid liquid_energetic_alloy;
    private Fluid liquid_vibrant_alloy;
    private Fluid liquid_dark_steel;
    private Fluid liquid_electrical_steel;
    private Fluid liquid_phased_iron;
    private Fluid liquid_soularium;

    private Map<String, MetalConfig.IntegrationStrategy> metalConfigs = new HashMap<>();

    private ItemStack getItemStack(String name)
    {
        return getItemStack(name, 0);
    }

    private ItemStack getItemStack(String name, int meta)
    {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(ENDERIO, name));
        return new ItemStack(item, 1, meta);
    }

    @Override
    public String getName()
    {
        return ENDERIO;
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
        if (!Loader.isModLoaded(ENDERIO))
        {
            return;
        }

        if (FoundryConfig.recipe_equipment && MetalConfig.metals.get("dark_steel") == MetalConfig.IntegrationStrategy.ENABLED)
        {
            OreMatcher extra_sticks1 = new OreMatcher("stickWood", 1);
            OreMatcher extra_sticks2 = new OreMatcher("stickWood", 2);

            ItemStack dark_steel_pickaxe = getItemStack("item_dark_steel_pickaxe");
            ItemStack dark_steel_axe = getItemStack("item_dark_steel_axe");
            ItemStack dark_steel_sword = getItemStack("item_dark_steel_sword");

            ItemStack dark_steel_helmet = getItemStack("item_dark_steel_helmet");
            ItemStack dark_steel_chestplate = getItemStack("item_dark_steel_chestplate");
            ItemStack dark_steel_leggings = getItemStack("item_dark_steel_leggings");
            ItemStack dark_steel_boots = getItemStack("item_dark_steel_boots");

            MiscUtil.registerCasting(dark_steel_chestplate, liquid_dark_steel, 8, ItemMold.SubItem.CHESTPLATE, null);
            MiscUtil.registerCasting(dark_steel_helmet, liquid_dark_steel, 5, ItemMold.SubItem.HELMET, null);
            MiscUtil.registerCasting(dark_steel_leggings, liquid_dark_steel, 7, ItemMold.SubItem.LEGGINGS, null);
            MiscUtil.registerCasting(dark_steel_boots, liquid_dark_steel, 4, ItemMold.SubItem.BOOTS, null);

            MiscUtil.registerCasting(dark_steel_pickaxe, liquid_dark_steel, 3, ItemMold.SubItem.PICKAXE, extra_sticks2);
            MiscUtil.registerCasting(dark_steel_axe, liquid_dark_steel, 3, ItemMold.SubItem.AXE, extra_sticks2);
            MiscUtil.registerCasting(dark_steel_sword, liquid_dark_steel, 2, ItemMold.SubItem.SWORD, extra_sticks1);

        }
        ItemStack silicon = getItemStack("item_material", 5);

        if (Loader.isModLoaded("thermalfoundation")) {
            Fluid liquid_redstone = TFFluids.fluidRedstone;
            Fluid liquid_enderpearl = TFFluids.fluidEnder;
            Fluid liquid_glowstone = TFFluids.fluidGlowstone;


            if (silicon != null) {
                if (MetalConfig.metals.get("redstone_alloy") == MetalConfig.IntegrationStrategy.ENABLED)
                    InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_redstone_alloy, FLUID_AMOUNT_INGOT),
                        new FluidStack(liquid_redstone, 100), new ItemStackMatcher(silicon), 12000);

                if (MetalConfig.metals.get("electrical_steel") == MetalConfig.IntegrationStrategy.ENABLED && FoundryFluids.liquid_steel != null)
                    InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_electrical_steel, FLUID_AMOUNT_INGOT),
                        new FluidStack(FoundryFluids.liquid_steel, FLUID_AMOUNT_INGOT), new ItemStackMatcher(silicon), 12000);
            }

            if (MetalConfig.metals.get("energetic_alloy") == MetalConfig.IntegrationStrategy.ENABLED && FoundryFluids.liquid_gold != null)
                AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_energetic_alloy, FLUID_AMOUNT_INGOT / 2),
                    new FluidStack[]{new FluidStack(FoundryFluids.liquid_gold, FLUID_AMOUNT_INGOT / 2), new FluidStack(liquid_redstone, 50),
                            new FluidStack(liquid_glowstone, 125)});

            if (MetalConfig.metals.get("vibrant_alloy") == MetalConfig.IntegrationStrategy.ENABLED)
                AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_vibrant_alloy, FLUID_AMOUNT_INGOT / 2), new FluidStack[]{
                    new FluidStack(liquid_energetic_alloy, FLUID_AMOUNT_INGOT / 2), new FluidStack(liquid_enderpearl, 125)});

            if (MetalConfig.metals.get("pulsating_iron") == MetalConfig.IntegrationStrategy.ENABLED && FoundryFluids.liquid_iron != null)AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_phased_iron, FLUID_AMOUNT_INGOT / 2), new FluidStack[]{
                    new FluidStack(FoundryFluids.liquid_iron, FLUID_AMOUNT_INGOT / 2), new FluidStack(liquid_enderpearl, 125)});
        }
        if (MetalConfig.metals.get("dark_steel") == MetalConfig.IntegrationStrategy.ENABLED && FoundryFluids.liquid_steel != null)
            AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_dark_steel, FLUID_AMOUNT_INGOT / 4), new FluidStack[] {
                new FluidStack(FoundryFluids.liquid_steel, FLUID_AMOUNT_INGOT / 4), new FluidStack(FluidRegistry.LAVA, 250), });

        if (MetalConfig.metals.get("soularium") == MetalConfig.IntegrationStrategy.ENABLED && FoundryFluids.liquid_gold != null)
            InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_soularium, FLUID_AMOUNT_INGOT),
                new FluidStack(FoundryFluids.liquid_gold, FLUID_AMOUNT_INGOT), new ItemStackMatcher(new ItemStack(Blocks.SOUL_SAND)),
                12000);
    }

    @SubscribeEvent
    public void registerFluids(Register<Block> e)
    {
        IForgeRegistry<Block> registry = e.getRegistry();

        if (MetalConfig.metals.get("redstone_alloy") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_redstone_alloy = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "redstone_alloy", 1000, 14,
                0x732828);
        if (MetalConfig.metals.get("energetic_alloy") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_energetic_alloy = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "energetic_alloy", 2500,
                15, 0xF05A0A);
        if (MetalConfig.metals.get("vibrant_alloy") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_vibrant_alloy = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "vibrant_alloy", 2500, 15,
                0x82A532);
        if (MetalConfig.metals.get("dark_steel") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_dark_steel = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "dark_steel", 1850, 12,
                0x333333);
        if (MetalConfig.metals.get("electrical_steel") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_electrical_steel = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "electrical_steel", 1850,
                15, 0x747474);
        if (MetalConfig.metals.get("pulsating_iron") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_phased_iron = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "pulsating_iron", 1850, 15,
                0x69EB87);
        if (MetalConfig.metals.get("soularium") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_soularium = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "soularium", 1350, 12, 0x5A3228);

        if (MetalConfig.metals.get("redstone_alloy") == MetalConfig.IntegrationStrategy.ENABLED)
            FoundryUtils.registerBasicMeltingRecipes("redstone_alloy", liquid_redstone_alloy);
        if (MetalConfig.metals.get("energetic_alloy") == MetalConfig.IntegrationStrategy.ENABLED)
            FoundryUtils.registerBasicMeltingRecipes("energetic_alloy", liquid_energetic_alloy);
        if (MetalConfig.metals.get("vibrant_alloy") == MetalConfig.IntegrationStrategy.ENABLED)
            FoundryUtils.registerBasicMeltingRecipes("vibrant_alloy", liquid_vibrant_alloy);
        FoundryUtils.registerBasicMeltingRecipes("phased_gold", liquid_vibrant_alloy);
        if (MetalConfig.metals.get("dark_steel") == MetalConfig.IntegrationStrategy.ENABLED)
            FoundryUtils.registerBasicMeltingRecipes("dark_steel", liquid_dark_steel);
        if (MetalConfig.metals.get("pulsating_iron") == MetalConfig.IntegrationStrategy.ENABLED)
            FoundryUtils.registerBasicMeltingRecipes("pulsating_iron", liquid_phased_iron);
        if (MetalConfig.metals.get("electrical_steel") == MetalConfig.IntegrationStrategy.ENABLED)
            FoundryUtils.registerBasicMeltingRecipes("electrical_steel", liquid_electrical_steel);
        if (MetalConfig.metals.get("soularium") == MetalConfig.IntegrationStrategy.ENABLED)
            FoundryUtils.registerBasicMeltingRecipes("soularium", liquid_soularium);
    }

    @Override
    public void onPreInit(Configuration config)
    {
        metalConfigs.put("redstone_alloy", MetalConfig.IntegrationStrategy.valueOf(config.getString("redstone_alloy", "Ender IO", MetalConfig.IntegrationStrategy.ENABLED.name(), "Valid values: ENABLED, DISABLED, NO_RECIPES")));

        metalConfigs.put("energetic_alloy", MetalConfig.IntegrationStrategy.valueOf(config.getString("energetic_alloy", "Ender IO", MetalConfig.IntegrationStrategy.ENABLED.name(), "Valid values: ENABLED, DISABLED, NO_RECIPES")));

        metalConfigs.put("vibrant_alloy", MetalConfig.IntegrationStrategy.valueOf(config.getString("vibrant_alloy", "Ender IO", MetalConfig.IntegrationStrategy.ENABLED.name(), "Valid values: ENABLED, DISABLED, NO_RECIPES")));

        metalConfigs.put("dark_steel", MetalConfig.IntegrationStrategy.valueOf(config.getString("dark_steel", "Ender IO", MetalConfig.IntegrationStrategy.ENABLED.name(), "Valid values: ENABLED, DISABLED, NO_RECIPES")));

        metalConfigs.put("electrical_steel", MetalConfig.IntegrationStrategy.valueOf(config.getString("electrical_steel", "Ender IO", MetalConfig.IntegrationStrategy.ENABLED.name(), "Valid values: ENABLED, DISABLED, NO_RECIPES")));

        metalConfigs.put("pulsating_iron", MetalConfig.IntegrationStrategy.valueOf(config.getString("pulsating_iron", "Ender IO", MetalConfig.IntegrationStrategy.ENABLED.name(), "Valid values: ENABLED, DISABLED, NO_RECIPES")));

        metalConfigs.put("soularium", MetalConfig.IntegrationStrategy.valueOf(config.getString("soularium", "Ender IO", MetalConfig.IntegrationStrategy.ENABLED.name(), "Valid values: ENABLED, DISABLED, NO_RECIPES")));
    }
}
