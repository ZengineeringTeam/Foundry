package exter.foundry.integration;

import cofh.thermalfoundation.init.TFFluids;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.api.recipe.matcher.OreMatcher;
import exter.foundry.config.FoundryConfig;
import exter.foundry.fluid.FoundryFluids;
import exter.foundry.fluid.LiquidMetalRegistry;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

        if (FoundryConfig.recipe_equipment)
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

            FoundryMiscUtils.registerCasting(dark_steel_chestplate, liquid_dark_steel, 8, ItemMold.SubItem.CHESTPLATE,
                    null);
            FoundryMiscUtils.registerCasting(dark_steel_helmet, liquid_dark_steel, 5, ItemMold.SubItem.HELMET, null);
            FoundryMiscUtils.registerCasting(dark_steel_leggings, liquid_dark_steel, 7, ItemMold.SubItem.LEGGINGS,
                    null);
            FoundryMiscUtils.registerCasting(dark_steel_boots, liquid_dark_steel, 4, ItemMold.SubItem.BOOTS, null);

            FoundryMiscUtils.registerCasting(dark_steel_pickaxe, liquid_dark_steel, 3, ItemMold.SubItem.PICKAXE,
                    extra_sticks2);
            FoundryMiscUtils.registerCasting(dark_steel_axe, liquid_dark_steel, 3, ItemMold.SubItem.AXE, extra_sticks2);
            FoundryMiscUtils.registerCasting(dark_steel_sword, liquid_dark_steel, 2, ItemMold.SubItem.SWORD,
                    extra_sticks1);

        }
        ItemStack silicon = getItemStack("item_material", 5);

        Fluid liquid_redstone = TFFluids.fluidRedstone;
        Fluid liquid_enderpearl = TFFluids.fluidEnder;
        Fluid liquid_glowstone = TFFluids.fluidGlowstone;

        if (silicon != null)
        {
            InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_redstone_alloy, 108),
                    new FluidStack(liquid_redstone, 100), new ItemStackMatcher(silicon), 50000);

            InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_electrical_steel, 108),
                    new FluidStack(FoundryFluids.liquid_steel, 108), new ItemStackMatcher(silicon), 30000);
        }

        AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_energetic_alloy, 54),
                new FluidStack[] { new FluidStack(FoundryFluids.liquid_gold, 54), new FluidStack(liquid_redstone, 50),
                        new FluidStack(liquid_glowstone, 125) });

        AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_vibrant_alloy, 54), new FluidStack[] {
                new FluidStack(liquid_energetic_alloy, 54), new FluidStack(liquid_enderpearl, 125) });

        AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_phased_iron, 54), new FluidStack[] {
                new FluidStack(FoundryFluids.liquid_iron, 54), new FluidStack(liquid_enderpearl, 125) });

        AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_dark_steel, 27), new FluidStack[] {
                new FluidStack(FoundryFluids.liquid_steel, 27), new FluidStack(FluidRegistry.LAVA, 250), });

        InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_soularium, 108),
                new FluidStack(FoundryFluids.liquid_gold, 108), new ItemStackMatcher(new ItemStack(Blocks.SOUL_SAND)),
                50000);
    }

    @Override
    public void onPreInit(Configuration config)
    {
        liquid_redstone_alloy = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("redstone_alloy", 1000, 14,
                0xFFFFFFFF);
        liquid_energetic_alloy = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("energetic_alloy", 2500, 15,
                0xFFFFFFFF);
        liquid_vibrant_alloy = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("vibrant_alloy", 2500, 15, 0xFFFFFF);
        liquid_dark_steel = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("dark_steel", 1850, 12, 0xFFFFFF);
        liquid_electrical_steel = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("electrical_steel", 1850, 15,
                0xFFFFFFFF);
        liquid_phased_iron = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("pulsating_iron", 1850, 15, 0xFFFFFF);
        liquid_soularium = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("soularium", 1350, 12, 0xFFFFFF);

        FoundryUtils.registerBasicMeltingRecipes("redstone_alloy", liquid_redstone_alloy);
        FoundryUtils.registerBasicMeltingRecipes("energetic_alloy", liquid_energetic_alloy);
        FoundryUtils.registerBasicMeltingRecipes("vibrant_alloy", liquid_vibrant_alloy);
        FoundryUtils.registerBasicMeltingRecipes("phased_gold", liquid_vibrant_alloy);
        FoundryUtils.registerBasicMeltingRecipes("dark_steel", liquid_dark_steel);
        FoundryUtils.registerBasicMeltingRecipes("pulsating_iron", liquid_phased_iron);
        FoundryUtils.registerBasicMeltingRecipes("electrical_steel", liquid_electrical_steel);
        FoundryUtils.registerBasicMeltingRecipes("soularium", liquid_soularium);
    }
}
