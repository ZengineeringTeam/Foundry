package exter.foundry.integration;

import static exter.foundry.api.FoundryAPI.FLUID_AMOUNT_INGOT;

import cofh.thermalfoundation.init.TFFluids;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.api.recipe.matcher.OreMatcher;
import exter.foundry.config.FoundryConfig;
import exter.foundry.fluid.FoundryFluidRegistry;
import exter.foundry.fluid.FoundryFluids;
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

        if (FoundryConfig.recipe_equipment && FoundryFluidRegistry.getStrategy("dark_steel").registerRecipes())
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

        Fluid liquid_redstone = TFFluids.fluidRedstone;
        Fluid liquid_enderpearl = TFFluids.fluidEnder;
        Fluid liquid_glowstone = TFFluids.fluidGlowstone;

        if (FoundryFluidRegistry.getStrategy("redstone_alloy").registerRecipes())
        {
            FoundryUtils.registerBasicMeltingRecipes("redstone_alloy", liquid_redstone_alloy);
            if (silicon != null)
            {
                InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_redstone_alloy, FLUID_AMOUNT_INGOT),
                        new FluidStack(liquid_redstone, 100), new ItemStackMatcher(silicon), 12000);
            }
        }

        if (FoundryFluidRegistry.getStrategy("electrical_steel").registerRecipes())
        {
            FoundryUtils.registerBasicMeltingRecipes("electrical_steel", liquid_electrical_steel);
            if (silicon != null && FoundryFluids.liquid_steel != null)
            {
                InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_electrical_steel, FLUID_AMOUNT_INGOT),
                        new FluidStack(FoundryFluids.liquid_steel, FLUID_AMOUNT_INGOT), new ItemStackMatcher(silicon),
                        12000);
            }
        }

        if (FoundryFluidRegistry.getStrategy("energetic_alloy").registerRecipes())
        {
            FoundryUtils.registerBasicMeltingRecipes("energetic_alloy", liquid_energetic_alloy);
            if (FoundryFluids.liquid_gold != null)
            {
                AlloyMixerRecipeManager.INSTANCE.addRecipe(
                        new FluidStack(liquid_energetic_alloy, FLUID_AMOUNT_INGOT / 2),
                        new FluidStack[] { new FluidStack(FoundryFluids.liquid_gold, FLUID_AMOUNT_INGOT / 2),
                                new FluidStack(liquid_redstone, 50), new FluidStack(liquid_glowstone, 125) });
            }
        }

        if (FoundryFluidRegistry.getStrategy("vibrant_alloy").registerRecipes())
        {
            FoundryUtils.registerBasicMeltingRecipes("phased_gold", liquid_vibrant_alloy); // what's this?
            FoundryUtils.registerBasicMeltingRecipes("vibrant_alloy", liquid_vibrant_alloy);
            AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_vibrant_alloy, FLUID_AMOUNT_INGOT / 2),
                    new FluidStack[] { new FluidStack(liquid_energetic_alloy, FLUID_AMOUNT_INGOT / 2),
                            new FluidStack(liquid_enderpearl, 125) });
        }

        if (FoundryFluidRegistry.getStrategy("pulsating_iron").registerRecipes())
        {
            FoundryUtils.registerBasicMeltingRecipes("pulsating_iron", liquid_phased_iron);
            if (FoundryFluids.liquid_iron != null)
            {
                AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_phased_iron, FLUID_AMOUNT_INGOT / 2),
                        new FluidStack[] { new FluidStack(FoundryFluids.liquid_iron, FLUID_AMOUNT_INGOT / 2),
                                new FluidStack(liquid_enderpearl, 125) });
            }
        }

        if (FoundryFluidRegistry.getStrategy("dark_steel").registerRecipes())
        {
            FoundryUtils.registerBasicMeltingRecipes("dark_steel", liquid_dark_steel);
            if (FoundryFluids.liquid_steel != null)
            {
                AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_dark_steel, FLUID_AMOUNT_INGOT / 4),
                        new FluidStack[] { new FluidStack(FoundryFluids.liquid_steel, FLUID_AMOUNT_INGOT / 4),
                                new FluidStack(FluidRegistry.LAVA, 250), });
            }
        }

        if (FoundryFluidRegistry.getStrategy("soularium").registerRecipes())
        {
            FoundryUtils.registerBasicMeltingRecipes("soularium", liquid_soularium);
            if (FoundryFluids.liquid_gold != null)
            {
                InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_soularium, FLUID_AMOUNT_INGOT),
                        new FluidStack(FoundryFluids.liquid_gold, FLUID_AMOUNT_INGOT),
                        new ItemStackMatcher(new ItemStack(Blocks.SOUL_SAND)), 12000);
            }
        }
    }

    @SubscribeEvent
    public void registerFluids(Register<Block> e)
    {
        IForgeRegistry<Block> registry = e.getRegistry();

        liquid_redstone_alloy = FoundryFluidRegistry.registerLiquidMetal(registry, "redstone_alloy", "EnderIO", 1000,
                14, 0x732828);
        liquid_energetic_alloy = FoundryFluidRegistry.registerLiquidMetal(registry, "energetic_alloy", "EnderIO", 2500,
                15, 0xF05A0A);
        liquid_vibrant_alloy = FoundryFluidRegistry.registerLiquidMetal(registry, "vibrant_alloy", "EnderIO", 2500, 15,
                0x82A532);
        liquid_dark_steel = FoundryFluidRegistry.registerLiquidMetal(registry, "dark_steel", "EnderIO", 1850, 12,
                0x333333);
        liquid_electrical_steel = FoundryFluidRegistry.registerLiquidMetal(registry, "electrical_steel", "EnderIO",
                1850, 15, 0x747474);
        liquid_phased_iron = FoundryFluidRegistry.registerLiquidMetal(registry, "pulsating_iron", "EnderIO", 1850, 15,
                0x69EB87);
        liquid_soularium = FoundryFluidRegistry.registerLiquidMetal(registry, "soularium", "EnderIO", 1350, 12,
                0x5A3228);
    }

    @Override
    public void onPreInit(Configuration config)
    {
    }
}
