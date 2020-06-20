package exter.foundry.integration;

import static exter.foundry.api.FoundryAPI.FLUID_AMOUNT_INGOT;

import com.google.common.collect.ImmutableList;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.api.recipe.matcher.OreMatcher;
import exter.foundry.config.FoundryConfig;
import exter.foundry.fluid.FoundryFluidRegistry;
import exter.foundry.fluid.FoundryFluids;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.*;
import exter.foundry.util.MiscUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

public class ModIntegrationEnderIO implements IModIntegration
{

    public static final String ENDERIO = "enderio";

    public static Fluid liquid_electrical_steel;
    public static Fluid liquid_energetic_alloy;
    public static Fluid liquid_vibrant_alloy;
    public static Fluid liquid_redstone_alloy;
    public static Fluid liquid_conductive_iron;
    public static Fluid liquid_pulsating_iron;
    public static Fluid liquid_dark_steel;
    public static Fluid liquid_soularium;
    public static Fluid liquid_endstone;
    public static Fluid liquid_end_steel;
    public static Fluid liquid_iron_alloy;

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
            ItemStack dark_steel_shears = getItemStack("item_dark_steel_shears");

            ItemStack dark_steel_helmet = getItemStack("item_dark_steel_helmet");
            ItemStack dark_steel_chestplate = getItemStack("item_dark_steel_chestplate");
            ItemStack dark_steel_leggings = getItemStack("item_dark_steel_leggings");
            ItemStack dark_steel_boots = getItemStack("item_dark_steel_boots");

            OreMatcher infinity_rod1 = new OreMatcher("itemInfinityRod", 1);
            OreMatcher infinity_rod2 = new OreMatcher("itemInfinityRod", 2);
            OreMatcher guardian_diode = new OreMatcher("skullGuardianDiode");

            ItemStack end_steel_pickaxe = getItemStack("item_end_steel_pickaxe");
            ItemStack end_steel_axe = getItemStack("item_end_steel_axe");
            ItemStack end_steel_sword = getItemStack("item_end_steel_sword");

            ItemStack end_steel_helmet = getItemStack("item_end_steel_helmet");
            ItemStack end_steel_chestplate = getItemStack("item_end_steel_chestplate");
            ItemStack end_steel_leggings = getItemStack("item_end_steel_leggings");
            ItemStack end_steel_boots = getItemStack("item_end_steel_boots");

            if (FoundryFluidRegistry.getStrategy("dark_steel").registerRecipes())
            {
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(dark_steel_helmet), new FluidStack(liquid_dark_steel, FoundryAPI.getAmountHelm()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(dark_steel_helmet), new FluidStack(liquid_dark_steel, FoundryAPI.getAmountHelm()), ItemMold.SubItem.HELMET, false, null);
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(dark_steel_chestplate), new FluidStack(liquid_dark_steel, FoundryAPI.getAmountChest()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(dark_steel_chestplate), new FluidStack(liquid_dark_steel, FoundryAPI.getAmountChest()), ItemMold.SubItem.CHESTPLATE, false, null);
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(dark_steel_leggings), new FluidStack(liquid_dark_steel, FoundryAPI.getAmountLegs()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(dark_steel_leggings), new FluidStack(liquid_dark_steel, FoundryAPI.getAmountLegs()), ItemMold.SubItem.LEGGINGS, false, null);
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(dark_steel_boots), new FluidStack(liquid_dark_steel, FoundryAPI.getAmountBoots()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(dark_steel_boots), new FluidStack(liquid_dark_steel, FoundryAPI.getAmountBoots()), ItemMold.SubItem.BOOTS, false, null);

                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(dark_steel_pickaxe), new FluidStack(liquid_dark_steel, FoundryAPI.getAmountPickaxe()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(dark_steel_pickaxe), new FluidStack(liquid_dark_steel, FoundryAPI.getAmountPickaxe()), ItemMold.SubItem.PICKAXE, false, extra_sticks2);
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(dark_steel_axe), new FluidStack(liquid_dark_steel, FoundryAPI.getAmountAxe()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(dark_steel_axe), new FluidStack(liquid_dark_steel, FoundryAPI.getAmountAxe()), ItemMold.SubItem.AXE, false, extra_sticks2);
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(dark_steel_sword), new FluidStack(liquid_dark_steel, FoundryAPI.getAmountSword()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(dark_steel_sword), new FluidStack(liquid_dark_steel, FoundryAPI.getAmountSword()), ItemMold.SubItem.SWORD, false, extra_sticks1);
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(dark_steel_shears), new FluidStack(liquid_dark_steel, FoundryAPI.getAmountShears()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(dark_steel_shears), new FluidStack(liquid_dark_steel, FoundryAPI.getAmountShears()), ItemMold.SubItem.SHEARS, false, null);
            }

            if (FoundryFluidRegistry.getStrategy("end_steel").registerRecipes())
            {
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(end_steel_helmet), new FluidStack(liquid_end_steel, FoundryAPI.getAmountHelm()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(end_steel_helmet), new FluidStack(liquid_end_steel, FoundryAPI.getAmountHelm()), ItemMold.SubItem.HELMET, false, guardian_diode);
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(end_steel_chestplate), new FluidStack(liquid_end_steel, FoundryAPI.getAmountChest()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(end_steel_chestplate), new FluidStack(liquid_end_steel, FoundryAPI.getAmountChest()), ItemMold.SubItem.CHESTPLATE, false, guardian_diode);
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(end_steel_leggings), new FluidStack(liquid_end_steel, FoundryAPI.getAmountLegs()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(end_steel_leggings), new FluidStack(liquid_end_steel, FoundryAPI.getAmountLegs()), ItemMold.SubItem.LEGGINGS, false, guardian_diode);
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(end_steel_boots), new FluidStack(liquid_end_steel, FoundryAPI.getAmountBoots()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(end_steel_boots), new FluidStack(liquid_end_steel, FoundryAPI.getAmountBoots()), ItemMold.SubItem.BOOTS, false, guardian_diode);

                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(end_steel_pickaxe), new FluidStack(liquid_end_steel, FoundryAPI.getAmountPickaxe()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(end_steel_pickaxe), new FluidStack(liquid_end_steel, FoundryAPI.getAmountPickaxe()), ItemMold.SubItem.PICKAXE, false, infinity_rod2);
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(end_steel_axe), new FluidStack(liquid_end_steel, FoundryAPI.getAmountAxe()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(end_steel_axe), new FluidStack(liquid_end_steel, FoundryAPI.getAmountAxe()), ItemMold.SubItem.AXE, false, infinity_rod2);
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(end_steel_sword), new FluidStack(liquid_end_steel, FoundryAPI.getAmountSword()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(end_steel_sword), new FluidStack(liquid_end_steel, FoundryAPI.getAmountSword()), ItemMold.SubItem.SWORD, false, infinity_rod1);
            }
        }

        ItemStack silicon = getItemStack("item_material", 5);
        ItemStack redstone_dust = new ItemStack(Items.REDSTONE);

        Fluid liquid_redstone = FluidRegistry.getFluid("redstone");
        Fluid liquid_enderpearl = FluidRegistry.getFluid("ender");
        Fluid liquid_glowstone = FluidRegistry.getFluid("glowstone");
        Fluid liquid_coal = FluidRegistry.getFluid("coal");

        if (FoundryFluidRegistry.getStrategy("electrical_steel").registerRecipes())
        {
            if (!silicon.isEmpty() && FoundryFluids.liquid_steel != null)
            {
                InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_electrical_steel, FLUID_AMOUNT_INGOT), new FluidStack(FoundryFluids.liquid_steel, FLUID_AMOUNT_INGOT), new ItemStackMatcher(silicon), 12000);
            }
        }

        if (FoundryFluidRegistry.getStrategy("energetic_alloy").registerRecipes())
        {
            if (FoundryFluids.liquid_gold != null && liquid_redstone != null && liquid_glowstone != null)
            {
                AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_energetic_alloy, FLUID_AMOUNT_INGOT / 2), new FluidStack(FoundryFluids.liquid_gold, FLUID_AMOUNT_INGOT / 2), new FluidStack(liquid_redstone, 50), new FluidStack(liquid_glowstone, 125));
            }
        }

        if (FoundryFluidRegistry.getStrategy("vibrant_alloy").registerRecipes())
        {
            FoundryUtils.registerBasicMeltingRecipes("phased_gold", liquid_vibrant_alloy); // what's this?
            if (liquid_enderpearl != null)
            {
                AlloyingCrucibleRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_vibrant_alloy, FLUID_AMOUNT_INGOT), new FluidStack(liquid_energetic_alloy, FLUID_AMOUNT_INGOT), new FluidStack(liquid_enderpearl, 250));
                AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_vibrant_alloy, FLUID_AMOUNT_INGOT / 2), new FluidStack(liquid_energetic_alloy, FLUID_AMOUNT_INGOT / 2), new FluidStack(liquid_enderpearl, 125));
            }
        }

        if (FoundryFluidRegistry.getStrategy("redstone_alloy").registerRecipes())
        {
            if (!silicon.isEmpty() && liquid_redstone != null)
            {
                InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_redstone_alloy, FLUID_AMOUNT_INGOT), new FluidStack(liquid_redstone, 100), new ItemStackMatcher(silicon), 12000);
            }
        }

        if (FoundryFluidRegistry.getStrategy("conductive_iron").registerRecipes())
        {
            if (!redstone_dust.isEmpty() && FoundryFluids.liquid_iron != null)
            {
                InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_conductive_iron, FLUID_AMOUNT_INGOT), new FluidStack(FoundryFluids.liquid_iron, FLUID_AMOUNT_INGOT), new ItemStackMatcher(redstone_dust), 12000);
            }
        }

        if (FoundryFluidRegistry.getStrategy("pulsating_iron").registerRecipes())
        {
            if (FoundryFluids.liquid_iron != null && liquid_enderpearl != null)
            {
                AlloyingCrucibleRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_pulsating_iron, FLUID_AMOUNT_INGOT), new FluidStack(FoundryFluids.liquid_iron, FLUID_AMOUNT_INGOT), new FluidStack(liquid_enderpearl, 250));
                AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_pulsating_iron, FLUID_AMOUNT_INGOT / 2), new FluidStack(FoundryFluids.liquid_iron, FLUID_AMOUNT_INGOT / 2), new FluidStack(liquid_enderpearl, 125));
            }
        }

        if (FoundryFluidRegistry.getStrategy("dark_steel").registerRecipes())
        {
            if (FoundryFluids.liquid_steel != null)
            {
                AlloyingCrucibleRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_dark_steel, FLUID_AMOUNT_INGOT / 2), new FluidStack(FoundryFluids.liquid_steel, FLUID_AMOUNT_INGOT / 2), new FluidStack(FluidRegistry.LAVA, 500));
                AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_dark_steel, FLUID_AMOUNT_INGOT / 4), new FluidStack(FoundryFluids.liquid_steel, FLUID_AMOUNT_INGOT / 4), new FluidStack(FluidRegistry.LAVA, 250));
            }
            if (FoundryFluids.liquid_iron != null && liquid_coal != null)
            {
                AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_dark_steel, FLUID_AMOUNT_INGOT / 4), new FluidStack(FoundryFluids.liquid_iron, FLUID_AMOUNT_INGOT / 4), new FluidStack(liquid_coal, 25), new FluidStack(FluidRegistry.LAVA, 250));
            }
        }

        if (FoundryFluidRegistry.getStrategy("soularium").registerRecipes())
        {
            if (FoundryFluids.liquid_gold != null)
            {
                InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_soularium, FLUID_AMOUNT_INGOT), new FluidStack(FoundryFluids.liquid_gold, FLUID_AMOUNT_INGOT), new ItemStackMatcher(new ItemStack(Blocks.SOUL_SAND)), 12000);
            }
        }

        if (FoundryFluidRegistry.getStrategy("endstone").registerRecipes())
        {
            MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(Blocks.END_STONE), new FluidStack(liquid_endstone, 1000), 2000, 200);
        }

        if (FoundryFluidRegistry.getStrategy("end_steel").registerRecipes())
        {
            if (liquid_endstone != null && liquid_dark_steel != null)
            {
                AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_end_steel, FLUID_AMOUNT_INGOT / 4), new FluidStack(liquid_endstone, 250), new FluidStack(liquid_dark_steel, FLUID_AMOUNT_INGOT / 4), new FluidStack(FluidRegistry.LAVA, 250));
            }
        }

        if (FoundryFluidRegistry.getStrategy("construction_alloy").registerRecipes())
        {
            List<Fluid> metals = new ArrayList<>();
            for (String name : new String[] { "lead", "copper", "silver", "aluminium", "tin", "nickel", "zinc", "platinum", "osmium", "cobalt", "titanium", "liquid_tungsten" })
            {
                Fluid fluid = FluidRegistry.getFluid(name);
                if (fluid != null)
                {
                    metals.add(fluid);
                }
            }
            for (Fluid x : metals)
            {
                for (Fluid y : metals)
                {
                    if (x == y && x != FoundryFluids.liquid_nickel)
                    {
                        AlloyingCrucibleRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_iron_alloy, 3), new FluidStack(x, 6), new FluidStack(FoundryFluids.liquid_iron, 3));
                        AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_iron_alloy, 1), new FluidStack(x, 2), new FluidStack(FoundryFluids.liquid_iron, 1));
                    }
                    else
                    {
                        AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_iron_alloy, 1), new FluidStack(x, 1), new FluidStack(FoundryFluids.liquid_iron, 1), new FluidStack(y, 1));
                    }
                }
            }
        }

        MoldRecipeManager.INSTANCE.addRecipe(ItemMold.SubItem.GRINDINGBALL.getMatcher().getItem(), 6, 6, new int[] { 0, 0, 1, 1, 0, 0, 0, 2, 3, 3, 2, 0, 1, 3, 4, 4, 3, 1, 1, 3, 4, 4, 3, 1, 0, 2, 3, 3, 2, 0, 0, 0, 1, 1, 0, 0 });
        for (String name : FoundryFluidRegistry.getFluidNames())
        {
            Fluid fluid = FluidRegistry.getFluid(name);
            if (FoundryFluidRegistry.getStrategy(name).registerRecipes())
            {
                name = MiscUtil.upperCaseFirstChar(name);
                if (!OreDictionary.getOres("ball" + name, false).isEmpty())
                {
                    FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("ball" + name), new FluidStack(fluid, FLUID_AMOUNT_INGOT * 5 / 24));
                    FoundryAPI.CASTING_MANAGER.addRecipe(new OreMatcher("ball" + name, 24), new FluidStack(fluid, FLUID_AMOUNT_INGOT * 5), ItemMold.SubItem.GRINDINGBALL, false, null);
                    FoundryUtils.registerBasicMeltingRecipes(name, fluid);
                }
                if (!OreDictionary.getOres("itemGrindingBall" + name, false).isEmpty())
                {
                    FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("itemGrindingBall" + name), new FluidStack(fluid, FLUID_AMOUNT_INGOT * 5 / 24));
                    FoundryAPI.CASTING_MANAGER.addRecipe(new OreMatcher("itemGrindingBall" + name, 24), new FluidStack(fluid, FLUID_AMOUNT_INGOT * 5), ItemMold.SubItem.GRINDINGBALL, false, null);
                    FoundryUtils.registerBasicMeltingRecipes(name, fluid);
                }
            }
        }
    }

    @SubscribeEvent
    public void registerFluids(Register<Block> e)
    {
        IForgeRegistry<Block> registry = e.getRegistry();

        liquid_electrical_steel = FoundryFluidRegistry.registerLiquidMetal(registry, "electrical_steel", "EnderIO", 1850, 15, 0x747474);
        liquid_energetic_alloy = FoundryFluidRegistry.registerLiquidMetal(registry, "energetic_alloy", "EnderIO", 2200, 15, 0xF05A0A);
        liquid_vibrant_alloy = FoundryFluidRegistry.registerLiquidMetal(registry, "vibrant_alloy", "EnderIO", 2500, 15, 0xBEFA00);
        liquid_redstone_alloy = FoundryFluidRegistry.registerLiquidMetal(registry, "redstone_alloy", "EnderIO", 1000, 14, 0x732828);
        liquid_conductive_iron = FoundryFluidRegistry.registerLiquidMetal(registry, "conductive_iron", "EnderIO", 1200, 15, 0xFFB9B9);
        liquid_pulsating_iron = FoundryFluidRegistry.registerLiquidMetal(registry, "pulsating_iron", "EnderIO", 1850, 15, 0x69EB87);
        liquid_dark_steel = FoundryFluidRegistry.registerLiquidMetal(registry, "dark_steel", "EnderIO", 1850, 12, 0x333333);
        liquid_soularium = FoundryFluidRegistry.registerLiquidMetal(registry, "soularium", "EnderIO", 1350, 12, 0x5A3228);
        liquid_endstone = FoundryFluidRegistry.registerLiquidMetal(registry, "endstone", "EnderIO", 2000, 15, 0xFFFFDC);
        liquid_end_steel = FoundryFluidRegistry.registerLiquidMetal(registry, "end_steel", "EnderIO", 2500, 15, 0xDCD7A0);
        liquid_iron_alloy = FoundryFluidRegistry.registerLiquidMetal(registry, "construction_alloy", "EnderIO", 650, 12, 0x3C3C3C);
    }

    @Override
    public void onPreInit(Configuration config)
    {
    }
}
