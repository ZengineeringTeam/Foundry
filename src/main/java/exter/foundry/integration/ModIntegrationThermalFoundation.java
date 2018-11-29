package exter.foundry.integration;

import static exter.foundry.api.FoundryAPI.FLUID_AMOUNT_INGOT;

import com.google.common.collect.ImmutableList;

import cofh.core.fluid.BlockFluidInteractive;
import cofh.core.util.helpers.StringHelper;
import cofh.thermalfoundation.ThermalFoundation;
import cofh.thermalfoundation.init.TFFluids;
import cofh.thermalfoundation.init.TFProps;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.api.recipe.matcher.OreMatcher;
import exter.foundry.config.MetalConfig;
import exter.foundry.fluid.FoundryFluidRegistry;
import exter.foundry.fluid.FoundryFluids;
import exter.foundry.item.ItemMold.SubItem;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.FluidHeaterFuelManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockSponge;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModIntegrationThermalFoundation implements IModIntegration
{
    private static final String THERMALFOUNDATION = "thermalfoundation";

    @Override
    public String getName()
    {
        return THERMALFOUNDATION;
    }

    @Override
    public void onAfterPostInit()
    {
    }

    @Override
    public void onClientInit()
    {
    }

    @Override
    public void onClientPostInit()
    {
    }

    @Override
    public void onClientPreInit()
    {
    }

    @Override
    public void onInit()
    {
        MeltingRecipeManager.INSTANCE.addRecipe(new OreMatcher("dustRedstone"),
                new FluidStack(TFFluids.fluidRedstone, 100));
        MeltingRecipeManager.INSTANCE.addRecipe(new OreMatcher("blockRedstone"),
                new FluidStack(TFFluids.fluidRedstone, 900));
        MeltingRecipeManager.INSTANCE.addRecipe(new OreMatcher("dustGlowstone"),
                new FluidStack(TFFluids.fluidGlowstone, 250), TFFluids.fluidGlowstone.getTemperature(), 90);
        MeltingRecipeManager.INSTANCE.addRecipe(new OreMatcher("glowstone"),
                new FluidStack(TFFluids.fluidGlowstone, 1000), TFFluids.fluidGlowstone.getTemperature(), 90);
        MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(Items.ENDER_PEARL),
                new FluidStack(TFFluids.fluidEnder, 250), TFFluids.fluidEnder.getTemperature(), 75);

        if (MetalConfig.metals.get("lumium") == MetalConfig.IntegrationStrategy.ENABLED && FoundryFluids.liquid_lumium != null && FoundryFluids.liquid_tin != null && FoundryFluids.liquid_silver != null)
            AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(FoundryFluids.liquid_lumium, FLUID_AMOUNT_INGOT),
                    new FluidStack(TFFluids.fluidGlowstone, 250),
                    new FluidStack(FoundryFluids.liquid_tin, FLUID_AMOUNT_INGOT / 4 * 3),
                    new FluidStack(FoundryFluids.liquid_silver, FLUID_AMOUNT_INGOT / 4));
        if (MetalConfig.metals.get("signalum") == MetalConfig.IntegrationStrategy.ENABLED && FoundryFluids.liquid_signalum != null && FoundryFluids.liquid_copper != null && FoundryFluids.liquid_silver != null)
            AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(FoundryFluids.liquid_signalum, FLUID_AMOUNT_INGOT),
                    new FluidStack(TFFluids.fluidRedstone, 250),
                    new FluidStack(FoundryFluids.liquid_copper, FLUID_AMOUNT_INGOT / 4 * 3),
                    new FluidStack(FoundryFluids.liquid_silver, FLUID_AMOUNT_INGOT / 4));
        if (MetalConfig.metals.get("enderium") == MetalConfig.IntegrationStrategy.ENABLED && FoundryFluids.liquid_enderium != null && FoundryFluids.liquid_tin != null && FoundryFluids.liquid_silver != null && FoundryFluids.liquid_platinum != null)
            AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(FoundryFluids.liquid_enderium, FLUID_AMOUNT_INGOT),
                    new FluidStack(TFFluids.fluidEnder, 250),
                    new FluidStack(FoundryFluids.liquid_lead, FLUID_AMOUNT_INGOT / 4 * 3),
                    new FluidStack(FoundryFluids.liquid_platinum, FLUID_AMOUNT_INGOT / 4));

        FluidHeaterFuelManager.INSTANCE.addFuel(TFFluids.fluidPyrotheum);

        Configuration cfg = ThermalFoundation.CONFIG.getConfiguration();
        for (String name : ImmutableList.of("copper", "tin", "silver", "lead", "aluminum", "nickel", "platinum",
                "steel", "electrum", "invar", "bronze", "constantan", "iron", "gold"))
        {
            tryAddToolArmorRecipes(cfg, name,
                    FoundryFluidRegistry.INSTANCE.getFluid(name.equals("aluminum") ? "aluminium" : name));
        }

        BlockFluidInteractive pyrotheum = (BlockFluidInteractive) TFFluids.blockFluidPyrotheum;
        BlockFluidInteractive mana = (BlockFluidInteractive) TFFluids.blockFluidMana;

        pyrotheum.addInteraction(Blocks.SAND, FoundryFluids.liquid_glass.getBlock());
        pyrotheum.addInteraction(Blocks.GLASS, FoundryFluids.liquid_glass.getBlock());
        pyrotheum.addInteraction(Blocks.SPONGE.getDefaultState().withProperty(BlockSponge.WET, true),
                Blocks.SPONGE.getDefaultState().withProperty(BlockSponge.WET, false));
        mana.addInteraction(FoundryFluids.liquid_glass.getBlock(), Blocks.SAND);
        mana.addInteraction(Blocks.STAINED_GLASS, Blocks.SAND);

        IBlockState stained_glass = Blocks.STAINED_GLASS.getDefaultState();
        for (EnumDyeColor dye : EnumDyeColor.values())
        {
            int meta = dye.getMetadata();
            stained_glass = stained_glass.withProperty(BlockColored.COLOR, dye);
            pyrotheum.addInteraction(stained_glass, FoundryFluids.liquid_glass_colored[meta].getBlock());
            mana.addInteraction(FoundryFluids.liquid_glass_colored[meta].getBlock(), Blocks.SAND);
        }
    }

    @Override
    public void onPostInit()
    {
    }

    @Override
    public void onPreInit(Configuration config)
    {
    }

    public static void tryAddToolArmorRecipes(Configuration cfg, String name, Fluid fluid)
    {
        if (fluid == null || MetalConfig.metals.get(name) != MetalConfig.IntegrationStrategy.ENABLED)
        {
            return;
        }
        final String tools = "Equipment.Tools." + StringHelper.titleCase(name);
        final String armor = "Equipment.Armor." + StringHelper.titleCase(name);
        OreMatcher stick = new OreMatcher("stickWood", 2);

        if (Loader.isModLoaded("thermalfoundation"))
        {
            if (!TFProps.disableAllArmor)
            {
                Item helm = ForgeRegistries.ITEMS
                        .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "armor.helmet_" + name));
                if (helm != null && cfg.get(armor, "Helmet", true).getBoolean(true))
                {
                    FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(helm),
                            new FluidStack(fluid, FoundryAPI.getAmountHelm()));
                    FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(helm),
                            new FluidStack(fluid, FoundryAPI.getAmountHelm()), SubItem.HELMET.getItem(), false, null);
                }

                Item chest = ForgeRegistries.ITEMS
                        .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "armor.plate_" + name));
                if (chest != null && cfg.get(armor, "Chestplate", true).getBoolean(true))
                {
                    FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(chest),
                            new FluidStack(fluid, FoundryAPI.getAmountChest()));
                    FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(chest),
                            new FluidStack(fluid, FoundryAPI.getAmountChest()), SubItem.CHESTPLATE.getItem(), false,
                            null);
                }

                Item legs = ForgeRegistries.ITEMS
                        .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "armor.legs_" + name));
                if (legs != null && cfg.get(armor, "Leggings", true).getBoolean(true))
                {
                    FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(legs),
                            new FluidStack(fluid, FoundryAPI.getAmountLegs()));
                    FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(legs),
                            new FluidStack(fluid, FoundryAPI.getAmountLegs()), SubItem.LEGGINGS.getItem(), false, null);
                }

                Item boots = ForgeRegistries.ITEMS
                        .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "armor.boots_" + name));
                if (boots != null && cfg.get(armor, "Boots", true).getBoolean(true))
                {
                    FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(boots),
                            new FluidStack(fluid, FoundryAPI.getAmountBoots()));
                    FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(boots),
                            new FluidStack(fluid, FoundryAPI.getAmountBoots()), SubItem.BOOTS.getItem(), false, null);
                }
            }

            if (!TFProps.disableAllTools)
            {
                if (!TFProps.disableAllShears)
                {
                    Item shears = ForgeRegistries.ITEMS
                            .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.shears_" + name));
                    if (shears != null && cfg.get(tools, "Shears", true).getBoolean(true))
                    {
                        FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(shears),
                                new FluidStack(fluid, FoundryAPI.getAmountShears()));
                        FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(shears),
                                new FluidStack(fluid, FoundryAPI.getAmountShears()), SubItem.SHEARS.getItem(), false,
                                null);
                    }
                }

                if (!TFProps.disableAllShields)
                {
                    Item shield = ForgeRegistries.ITEMS
                            .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.shield_" + name));
                    if (shield != null && cfg.get(tools, "Shield", true).getBoolean(true))
                    {
                        FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(shield),
                                new FluidStack(fluid, FoundryAPI.getAmountSword()));
                        FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(shield),
                                new FluidStack(fluid, FoundryAPI.getAmountSword()), SubItem.SHIELD.getItem(), true,
                                null);
                    }
                }

                Item pickaxe = ForgeRegistries.ITEMS
                        .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.pickaxe_" + name));
                if (pickaxe != null && cfg.get(tools, "Pickaxe", true).getBoolean(true))
                {
                    FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(pickaxe),
                            new FluidStack(fluid, FoundryAPI.getAmountPickaxe()));
                    FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(pickaxe),
                            new FluidStack(fluid, FoundryAPI.getAmountPickaxe()), SubItem.PICKAXE.getItem(), false,
                            stick);
                }

                Item axe = ForgeRegistries.ITEMS
                        .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.axe_" + name));
                if (axe != null && cfg.get(tools, "Axe", true).getBoolean(true))
                {
                    FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(axe),
                            new FluidStack(fluid, FoundryAPI.getAmountAxe()));
                    FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(axe),
                            new FluidStack(fluid, FoundryAPI.getAmountAxe()), SubItem.AXE.getItem(), false, stick);
                }
                Item shovel = ForgeRegistries.ITEMS
                        .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.shovel_" + name));
                if (shovel != null && cfg.get(tools, "Shovel", true).getBoolean(true))
                {
                    FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(shovel),
                            new FluidStack(fluid, FoundryAPI.getAmountShovel()));
                    FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(shovel),
                            new FluidStack(fluid, FoundryAPI.getAmountShovel()), SubItem.SHOVEL.getItem(), false,
                            stick);
                }

                Item hoe = ForgeRegistries.ITEMS
                        .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.hoe_" + name));
                if (hoe != null && cfg.get(tools, "Hoe", true).getBoolean(true))
                {
                    FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(hoe),
                            new FluidStack(fluid, FoundryAPI.getAmountHoe()));
                    FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(hoe),
                            new FluidStack(fluid, FoundryAPI.getAmountHoe()), SubItem.HOE.getItem(), false, stick);
                }
                Item sword = ForgeRegistries.ITEMS
                        .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.sword_" + name));
                if (sword != null && cfg.get(tools, "Sword", true).getBoolean(true))
                {
                    FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(sword),
                            new FluidStack(fluid, FoundryAPI.getAmountSword()));
                    FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(sword),
                            new FluidStack(fluid, FoundryAPI.getAmountSword()), SubItem.SWORD.getItem(), false,
                            new OreMatcher("stickWood"));
                }

                Item sickle = ForgeRegistries.ITEMS
                        .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.sickle_" + name));
                if (sickle != null && cfg.get(tools, "Sickle", true).getBoolean(true))
                {
                    FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(sickle),
                            new FluidStack(fluid, FoundryAPI.getAmountSickle()));
                    FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(sickle),
                            new FluidStack(fluid, FoundryAPI.getAmountSickle()), SubItem.SICKLE.getItem(), false,
                            new OreMatcher("stickWood"));
                }

                Item hammer = ForgeRegistries.ITEMS
                        .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.hammer_" + name));
                if (hammer != null && cfg.get(tools, "Hammer", true).getBoolean(true))
                {
                    FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(hammer),
                            new FluidStack(fluid, FoundryAPI.getAmountHammer()));
                    FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(hammer),
                            new FluidStack(fluid, FoundryAPI.getAmountHammer()), SubItem.HAMMER.getItem(), false,
                            stick);
                }

                Item excavator = ForgeRegistries.ITEMS
                        .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.excavator_" + name));
                if (excavator != null && cfg.get(tools, "Excavator", true).getBoolean(true))
                {
                    FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(excavator),
                            new FluidStack(fluid, FoundryAPI.getAmountExcavator()));
                    FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(excavator),
                            new FluidStack(fluid, FoundryAPI.getAmountExcavator()), SubItem.EXCAVATOR.getItem(), false,
                            stick);
                }
            }
        }
    }
}
