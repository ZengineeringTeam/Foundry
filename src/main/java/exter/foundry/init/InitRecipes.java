package exter.foundry.init;

import java.util.ArrayList;
import java.util.List;

import cofh.thermalfoundation.init.TFFluids;
import exter.foundry.Foundry;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.ICastingTableRecipe;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.api.recipe.matcher.OreMatcher;
import exter.foundry.block.BlockCastingTable.EnumTable;
import exter.foundry.block.BlockComponent;
import exter.foundry.block.BlockFoundryMachine.EnumMachine;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.config.FoundryConfig;
import exter.foundry.fluid.FluidLiquidMetal;
import exter.foundry.fluid.FoundryFluids;
import exter.foundry.fluid.LiquidMetalRegistry;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import exter.foundry.item.ItemMold;
import exter.foundry.material.MaterialRegistry;
import exter.foundry.material.OreDictMaterial;
import exter.foundry.material.OreDictType;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.AtomizerRecipeManager;
import exter.foundry.recipes.manager.BurnerHeaterFuelManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.CastingTableRecipeManager;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.recipes.manager.MoldRecipeManager;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class InitRecipes
{

    public static void init()
    {
        registerMachineRecipes();
    }

    static public void postInit()
    {
        for (OreDictType type : OreDictType.TYPES)
        {
            for (OreDictMaterial material : OreDictMaterial.MATERIALS)
            {
                String od_name = type.prefix + material.suffix;
                if (OreDictionary.doesOreNameExist(od_name))
                {
                    for (ItemStack item : OreDictionary.getOres(od_name, false))
                    {
                        MaterialRegistry.INSTANCE.registerItem(item, material.suffix, type.name);
                    }
                }
                if (material.suffix_alias != null)
                {
                    od_name = type.prefix + material.suffix_alias;
                    if (OreDictionary.doesOreNameExist(od_name))
                    {
                        for (ItemStack item : OreDictionary.getOres(od_name, false))
                        {
                            MaterialRegistry.INSTANCE.registerItem(item, material.suffix, type.name);
                        }
                    }
                }
            }
        }

        /*  I Don't really know what this does.  It doesn't make much sense.
        for (Map.Entry<ItemStack, ItemStack> entry : FurnaceRecipes.instance().getSmeltingList().entrySet()) {
        	ItemStack input = entry.getKey();
        	Item item = entry.getValue().getItem();
        
        	if (item == Items.GOLD_NUGGET || item == Items.IRON_NUGGET) continue;
        
        	if (!input.isEmpty() && MeltingRecipeManager.INSTANCE.findRecipe(input) == null) {
        		ItemStack result = entry.getValue();
        		IMeltingRecipe recipe = MeltingRecipeManager.INSTANCE.findRecipe(result);
        		if (recipe != null) {
        			Fluid liquid_metal = recipe.getOutput().getFluid();
        			int base_amount = recipe.getOutput().amount;
        
        			int[] ids = OreDictionary.getOreIDs(input);
        			for (int j : ids) {
        				if (OreDictionary.getOreName(j).startsWith("ore") && !OreDictionary.getOreName(j).startsWith("orePoor")) {
        					base_amount = FoundryAPI.getAmountOre();
        					break;
        				}
        			}
        			MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(input), new FluidStack(liquid_metal, base_amount * result.getCount()), recipe.getMeltingPoint(), recipe.getMeltingSpeed());
        		}
        	}
        }
        */

        ItemStack ingot_mold = FoundryItems.mold(ItemMold.SubItem.INGOT);
        ItemStack block_mold = FoundryItems.mold(ItemMold.SubItem.BLOCK);
        for (String name : LiquidMetalRegistry.INSTANCE.getFluidNames())
        {
            FluidLiquidMetal fluid = LiquidMetalRegistry.INSTANCE.getFluid(name);
            if (!fluid.special)
            {
                FluidStack fluidstack = new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT);
                List<ItemStack> ores = OreDictionary.doesOreNameExist("ingot" + name)
                        ? OreDictionary.getOres("ingot" + name, false)
                        : new ArrayList<>();
                if (ores != null && ores.size() > 0)
                {
                    if (CastingRecipeManager.INSTANCE.findRecipe(fluidstack, ingot_mold, null) == null)
                    {
                        CastingRecipeManager.INSTANCE.addRecipe(new OreMatcher("ingot" + name), fluidstack, ingot_mold,
                                null);
                    }
                    if (CastingTableRecipeManager.INSTANCE.findRecipe(fluidstack,
                            ICastingTableRecipe.TableType.INGOT) == null)
                    {
                        CastingTableRecipeManager.INSTANCE.addRecipe(new OreMatcher("ingot" + name), fluidstack,
                                ICastingTableRecipe.TableType.INGOT);
                    }
                }
                ores = OreDictionary.doesOreNameExist("dust" + name) ? OreDictionary.getOres("dust" + name, false)
                        : new ArrayList<>();
                if (ores != null && ores.size() > 0)
                {
                    if (AtomizerRecipeManager.INSTANCE.findRecipe(fluidstack) == null)
                    {
                        AtomizerRecipeManager.INSTANCE.addRecipe(new OreMatcher("dust" + name), fluidstack);
                    }
                }

                ores = OreDictionary.doesOreNameExist("block" + name) ? OreDictionary.getOres("block" + name, false)
                        : new ArrayList<>();
                fluidstack = new FluidStack(LiquidMetalRegistry.INSTANCE.getFluid(name), FoundryAPI.getAmountBlock());
                if (ores != null && ores.size() > 0)
                {
                    if (CastingRecipeManager.INSTANCE.findRecipe(fluidstack, block_mold, null) == null)
                    {
                        CastingRecipeManager.INSTANCE.addRecipe(new OreMatcher("block" + name), fluidstack, block_mold,
                                null);
                    }
                    if (CastingTableRecipeManager.INSTANCE.findRecipe(fluidstack,
                            ICastingTableRecipe.TableType.BLOCK) == null)
                    {
                        CastingTableRecipeManager.INSTANCE.addRecipe(new OreMatcher("block" + name), fluidstack,
                                ICastingTableRecipe.TableType.BLOCK);
                    }
                }
            }
        }

        InitHardCore.init();
    }

    static public void preInit()
    {

        if (FoundryConfig.recipe_glass)
        {
            final String[] oredict_names = { "dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple",
                    "dyeCyan", "dyeLightGray", "dyeGray", "dyePink", "dyeLime", "dyeYellow", "dyeLightBlue",
                    "dyeMagenta", "dyeOrange", "dyeWhite" };

            int temp = 1550;
            int melt = 500;
            Fluid liquid_glass = FoundryFluids.liquid_glass;

            MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(Blocks.SAND),
                    new FluidStack(liquid_glass, 1000), temp, melt);
            MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(Blocks.GLASS),
                    new FluidStack(liquid_glass, 1000), temp, melt);
            MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(Blocks.GLASS_PANE),
                    new FluidStack(liquid_glass, 375), temp, melt);
            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(Blocks.GLASS),
                    new FluidStack(liquid_glass, 1000), FoundryItems.mold(ItemMold.SubItem.BLOCK), null, 400);
            CastingTableRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(Blocks.GLASS),
                    new FluidStack(liquid_glass, 1000), ICastingTableRecipe.TableType.BLOCK);
            for (EnumDyeColor dye : EnumDyeColor.values())
            {

                int meta = dye.getMetadata();
                ItemStack stained_glass = new ItemStack(Blocks.STAINED_GLASS, 1, meta);

                Fluid liquid_glass_colored = FoundryFluids.liquid_glass_colored[meta];

                MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(stained_glass),
                        new FluidStack(liquid_glass_colored, 1000), temp, melt);
                MeltingRecipeManager.INSTANCE.addRecipe(
                        new ItemStackMatcher(new ItemStack(Blocks.STAINED_GLASS_PANE, 1, meta)),
                        new FluidStack(liquid_glass_colored, 375), temp, melt);
                CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(stained_glass),
                        new FluidStack(liquid_glass_colored, 1000), FoundryItems.mold(ItemMold.SubItem.BLOCK), null,
                        400);
                CastingTableRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(stained_glass),
                        new FluidStack(liquid_glass_colored, 1000), ICastingTableRecipe.TableType.BLOCK);

                InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_glass_colored, 2000),
                        new FluidStack(liquid_glass, 2000), new OreMatcher(oredict_names[dye.getDyeDamage()]), 5000);
            }
        }
        registerCrafting();
    }

    public static void registerCrafting()
    {

        ItemStack redstone_stack = new ItemStack(Items.REDSTONE);
        ItemStack furnace_stack = new ItemStack(Blocks.FURNACE);
        ItemStack clay_stack = new ItemStack(Items.CLAY_BALL);
        ItemStack sand_stack = new ItemStack(Blocks.SAND, 1, -1);
        ItemStack soulsand_stack = new ItemStack(Blocks.SOUL_SAND);
        ItemStack clayblock_stack = new ItemStack(Blocks.CLAY, 1, -1);
        ItemStack casing_basic_stack = FoundryBlocks.block_component
                .asItemStack(BlockComponent.EnumVariant.CASING_BASIC);
        ItemStack casing_stack = FoundryBlocks.block_component.asItemStack(BlockComponent.EnumVariant.CASING_STANDARD);
        ItemStack casing_inferno_stack = FoundryBlocks.block_component
                .asItemStack(BlockComponent.EnumVariant.CASING_ADVANCED);
        ItemStack piston_stack = new ItemStack(Blocks.PISTON);
        ItemStack bronze_cauldron_stack = new ItemStack(FoundryBlocks.block_cauldron_bronze);
        ItemStack cauldron_stack = new ItemStack(Items.CAULDRON);
        ItemStack chest_stack = new ItemStack(Blocks.CHEST);
        ItemStack refclay_stack = FoundryItems.component(ItemComponent.SubItem.REFRACTORYCLAY);
        ItemStack refclay_small_stack = FoundryItems.component(ItemComponent.SubItem.REFRACTORYCLAY_SMALL);
        ItemStack refractoryclay8_stack = FoundryItems.component(ItemComponent.SubItem.REFRACTORYCLAY, 8);
        ItemStack refbrick_stack = FoundryItems.component(ItemComponent.SubItem.REFRACTORYBRICK);
        ItemStack refglass_stack = new ItemStack(FoundryBlocks.block_refractory_glass);
        ItemStack heatingcoil_stack = FoundryItems.component(ItemComponent.SubItem.HEATINGCOIL);
        ItemStack comparator_stack = new ItemStack(Items.COMPARATOR);
        ItemStack repeater_stack = new ItemStack(Items.REPEATER);
        ItemStack bucket_stack = new ItemStack(Items.BUCKET);
        ItemStack magmacream_stack = new ItemStack(Items.MAGMA_CREAM);
        ItemStack infernoclay8_stack = FoundryItems.component(ItemComponent.SubItem.INFERNOCLAY, 8);
        ItemStack infbrick_stack = FoundryItems.component(ItemComponent.SubItem.INFERNOBRICK);
        ItemStack mold_ingot = FoundryItems.mold(ItemMold.SubItem.INGOT);
        ItemStack mold_plate = FoundryItems.mold(ItemMold.SubItem.PLATE);
        ItemStack mold_block = FoundryItems.mold(ItemMold.SubItem.BLOCK);
        ItemStack mold_rod = FoundryItems.mold(ItemMold.SubItem.ROD);

        Foundry.HELPER.addForgeShaped(refractoryclay8_stack, "CCC", "CSC", "CCC", 'C', clay_stack, 'S', sand_stack);

        Foundry.HELPER.addForgeShaped(
                FoundryBlocks.block_component.asItemStack(BlockComponent.EnumVariant.REFRACTORY_CLAY), "CC", "CC", 'C',
                refclay_stack);

        Foundry.HELPER.addForgeShaped(refclay_stack, "CC", "CC", 'C', refclay_small_stack);

        Foundry.HELPER.addForgeShaped(infernoclay8_stack, "COC", "CSC", "CMC", 'C', refclay_stack, 'S', soulsand_stack,
                'M', magmacream_stack, 'O', "dustObsidian");

        Foundry.HELPER.addForgeShapeless(refractoryclay8_stack, clayblock_stack, clayblock_stack, sand_stack);

        GameRegistry.addSmelting(FoundryItems.component(ItemComponent.SubItem.REFRACTORYCLAY), refbrick_stack, 0.0f);

        GameRegistry.addSmelting(FoundryItems.component(ItemComponent.SubItem.INFERNOCLAY), infbrick_stack, 0.0f);

        Foundry.HELPER.addForgeShaped(FoundryItems.component(ItemComponent.SubItem.HEATINGCOIL, 2), "CCC", "CRC", "CCC",
                'C', "rodCupronickel", 'R', redstone_stack);

        Foundry.HELPER.addForgeShaped(casing_basic_stack, "IBI", "B B", "IBI", 'I', "ingotBronze", 'B', refbrick_stack);

        Foundry.HELPER.addForgeShaped(casing_stack, "IBI", "B B", "IBI", 'I', "plateIron", 'B', refbrick_stack);

        Foundry.HELPER.addForgeShaped(casing_inferno_stack, "IBI", "B B", "IBI", 'I', "plateSteel", 'B',
                infbrick_stack);

        Foundry.HELPER.addForgeShaped(new ItemStack(FoundryBlocks.block_refractory_tank_basic), "BPB", "G G", "BPB",
                'G', refglass_stack, 'P', "plateBronze", 'B', refbrick_stack);

        Foundry.HELPER.addForgeShaped(new ItemStack(FoundryBlocks.block_refractory_tank_standard), "BPB", "G G", "BPB",
                'G', refglass_stack, 'P', "plateIron", 'B', refbrick_stack);

        Foundry.HELPER.addForgeShaped(new ItemStack(FoundryBlocks.block_refractory_tank_advanced), "BPB", "G G", "BPB",
                'G', refglass_stack, 'P', "plateSteel", 'B', infbrick_stack);

        Foundry.HELPER.addForgeShaped(new ItemStack(FoundryBlocks.block_burner_heater), "I", "C", "F", 'F',
                furnace_stack, 'I', "ingotCopper", 'C', casing_basic_stack);

        Foundry.HELPER.addForgeShaped(FoundryBlocks.block_casting_table.asItemStack(EnumTable.INGOT), "BMB", " S ", 'S',
                new ItemStack(Blocks.STONE_SLAB), 'B', refbrick_stack, 'M', mold_ingot);

        Foundry.HELPER.addForgeShaped(FoundryBlocks.block_casting_table.asItemStack(EnumTable.PLATE), "BMB", " S ", 'S',
                new ItemStack(Blocks.STONE_SLAB), 'B', refbrick_stack, 'M', mold_plate);

        Foundry.HELPER.addForgeShaped(FoundryBlocks.block_casting_table.asItemStack(EnumTable.ROD), "BMB", " S ", 'S',
                new ItemStack(Blocks.STONE_SLAB), 'B', refbrick_stack, 'M', mold_rod);

        Foundry.HELPER.addForgeShaped(FoundryBlocks.block_casting_table.asItemStack(EnumTable.BLOCK), "BMB", " S ", 'S',
                new ItemStack(Blocks.STONE_SLAB), 'B', refbrick_stack, 'M', mold_block);

        Foundry.HELPER.addForgeShaped(FoundryBlocks.block_refractory_spout, "RL", "BB", "R ", 'R', "ingotBronze", 'B',
                refbrick_stack, 'L', new ItemStack(Blocks.LEVER));

        Foundry.HELPER.addForgeShaped(FoundryBlocks.block_machine.asItemStack(EnumMachine.INDUCTIONHEATER), "HIH",
                "RCR", "HRH", 'H', heatingcoil_stack, 'R', redstone_stack, 'I', "plateCopper", 'C', casing_stack);

        Foundry.HELPER.addForgeShaped(FoundryBlocks.block_machine.asItemStack(EnumMachine.CRUCIBLE_BASIC), "BAB", "BCB",
                "BIB", 'B', refbrick_stack, 'I', "ingotCopper", 'C', casing_basic_stack, 'A', bronze_cauldron_stack);

        Foundry.HELPER.addForgeShaped(FoundryBlocks.block_machine.asItemStack(EnumMachine.CRUCIBLE_STANDARD), "BAB",
                "BCB", "BIB", 'B', refbrick_stack, 'I', "plateCopper", 'C', casing_stack, 'A', cauldron_stack);

        Foundry.HELPER.addForgeShaped(FoundryBlocks.block_machine.asItemStack(EnumMachine.CRUCIBLE_ADVANCED), "BAB",
                "BCB", "BIB", 'B', infbrick_stack, 'I', "plateSilver", 'C', casing_inferno_stack, 'A', cauldron_stack);

        Foundry.HELPER.addForgeShaped(FoundryBlocks.block_machine.asItemStack(EnumMachine.INFUSER), " R ", "GCG", "HRH",
                'R', redstone_stack, 'C', casing_stack, 'G', "gearInvar", 'H', heatingcoil_stack);

        Foundry.HELPER.addForgeShaped(new ItemStack(FoundryBlocks.block_alloy_furnace), "BBB", "BFB", "BBB", 'B',
                refbrick_stack, 'F', furnace_stack);

        Foundry.HELPER.addForgeShaped(new ItemStack(FoundryBlocks.block_refractory_hopper), "R R", "RBR", " R ", 'R',
                refbrick_stack, 'B', bucket_stack);

        Foundry.HELPER.addForgeShaped(FoundryBlocks.block_machine.asItemStack(EnumMachine.ATOMIZER), "GHG", "RCR",
                " B ", 'H', new ItemStack(FoundryBlocks.block_refractory_hopper), 'B', Items.BUCKET, 'R',
                Items.REDSTONE, 'C', casing_stack, 'G', "gearBronze");

        Foundry.HELPER.addForgeShaped(FoundryBlocks.block_machine.asItemStack(EnumMachine.ALLOYING_CRUCIBLE), "HRH",
                "BCB", "BBB", 'H', new ItemStack(FoundryBlocks.block_refractory_spout), 'B', refbrick_stack, 'R',
                cauldron_stack, 'C', casing_stack);

        Foundry.HELPER.addForgeShaped(FoundryBlocks.block_machine.asItemStack(EnumMachine.CASTER), " H ", "RCR", "GPG",
                'H', chest_stack, 'G', "gearIron", 'P', piston_stack, 'C', casing_stack, 'R', redstone_stack);

        Foundry.HELPER.addForgeShaped(FoundryBlocks.block_machine.asItemStack(EnumMachine.ALLOYMIXER), " P ", "GCG",
                " R ", 'C', casing_stack, 'R', redstone_stack, 'G', "gearInvar", 'P', "plateInvar");

        Foundry.HELPER.addForgeShaped(FoundryBlocks.block_machine.asItemStack(EnumMachine.MATERIALROUTER), "GEG", "PRP",
                "GCG", 'R', casing_stack, 'P', "plateSignalum", 'C', comparator_stack, 'E', repeater_stack, 'G',
                "gearGold");

        Foundry.HELPER.addForgeShaped(new ItemStack(FoundryBlocks.block_mold_station), "BWB", "BSB", "BFB", 'B',
                refbrick_stack, 'W', new ItemStack(Blocks.CRAFTING_TABLE), 'S', new ItemStack(Blocks.STONE_SLAB), 'F',
                furnace_stack);

        Foundry.HELPER.addForgeShaped(new ItemStack(FoundryBlocks.block_cauldron_bronze), "I I", "I I", "III", 'I',
                "ingotBronze");

    }

    static public void registerMachineRecipes()
    {

        for (String name : LiquidMetalRegistry.INSTANCE.getFluidNames())
        {
            FluidLiquidMetal fluid = LiquidMetalRegistry.INSTANCE.getFluid(name);
            if (!fluid.special)
            {
                FoundryUtils.registerBasicMeltingRecipes(name, fluid);
            }
        }
        //FoundryUtils.registerBasicMeltingRecipes("Chromium", LiquidMetalRegistry.instance.getFluid("Chrome"));
        FoundryUtils.registerBasicMeltingRecipes("Aluminum", LiquidMetalRegistry.INSTANCE.getFluid("Aluminium"));
        FoundryUtils.registerBasicMeltingRecipes("Constantan", LiquidMetalRegistry.INSTANCE.getFluid("Cupronickel"));

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

        MoldRecipeManager.INSTANCE.addRecipe(FoundryItems.mold(ItemMold.SubItem.INGOT), 2, 4,
                new int[] { 2, 2, 2, 2, 2, 2, 2, 2 });

        MoldRecipeManager.INSTANCE.addRecipe(FoundryItems.mold(ItemMold.SubItem.PLATE), 4, 4,
                new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 });

        MoldRecipeManager.INSTANCE.addRecipe(FoundryItems.mold(ItemMold.SubItem.ROD), 1, 6,
                new int[] { 1, 1, 1, 1, 1, 1 });

        MoldRecipeManager.INSTANCE.addRecipe(FoundryItems.mold(ItemMold.SubItem.GEAR), 5, 5,
                new int[] { 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1 });

        MoldRecipeManager.INSTANCE.addRecipe(FoundryItems.mold(ItemMold.SubItem.BLOCK), 6, 6, new int[] { 4, 4, 4, 4, 4,
                4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4 });

        MoldRecipeManager.INSTANCE.addRecipe(FoundryItems.mold(ItemMold.SubItem.SLAB), 6, 6, new int[] { 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4 });

        MoldRecipeManager.INSTANCE.addRecipe(FoundryItems.mold(ItemMold.SubItem.STAIRS), 6, 6, new int[] { 0, 0, 0, 4,
                4, 4, 0, 0, 0, 4, 4, 4, 0, 0, 0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4 });

        MoldRecipeManager.INSTANCE.addRecipe(FoundryItems.mold(ItemMold.SubItem.NUGGET), 3, 3,
                new int[] { 1, 1, 1, 1, 1, 1, 0, 1, 0 });

        InitAlloyRecipes.init();

        for (ItemMold.SubItem sub : ItemMold.SubItem.values())
        {
            CastingRecipeManager.INSTANCE.addMold(FoundryItems.mold(sub));
        }

        if (FoundryConfig.recipe_equipment)
        {
            InitToolRecipes.init();
        }

        //Base casting recipes.
        for (String name : LiquidMetalRegistry.INSTANCE.getFluidNames())
        {
            addDefaultCasting(LiquidMetalRegistry.INSTANCE.getFluid(name), name);
        }

        addDefaultCasting(LiquidMetalRegistry.INSTANCE.getFluid("Aluminium"), "Aluminum");
        addDefaultCasting(LiquidMetalRegistry.INSTANCE.getFluid("Cupronickel"), "Constantan");

        AlloyMixerRecipeManager.INSTANCE.addRecipe(
                new FluidStack(FoundryFluids.liquid_lumium, FoundryAPI.FLUID_AMOUNT_INGOT),
                new FluidStack(TFFluids.fluidGlowstone, 250),
                new FluidStack(FoundryFluids.liquid_tin, FoundryAPI.FLUID_AMOUNT_INGOT / 4 * 3),
                new FluidStack(FoundryFluids.liquid_silver, FoundryAPI.FLUID_AMOUNT_INGOT / 4));
        AlloyMixerRecipeManager.INSTANCE.addRecipe(
                new FluidStack(FoundryFluids.liquid_signalum, FoundryAPI.FLUID_AMOUNT_INGOT),
                new FluidStack(TFFluids.fluidRedstone, 250),
                new FluidStack(FoundryFluids.liquid_copper, FoundryAPI.FLUID_AMOUNT_INGOT / 4 * 3),
                new FluidStack(FoundryFluids.liquid_silver, FoundryAPI.FLUID_AMOUNT_INGOT / 4));
        AlloyMixerRecipeManager.INSTANCE.addRecipe(
                new FluidStack(FoundryFluids.liquid_enderium, FoundryAPI.FLUID_AMOUNT_INGOT),
                new FluidStack(TFFluids.fluidEnder, 250),
                new FluidStack(FoundryFluids.liquid_lead, FoundryAPI.FLUID_AMOUNT_INGOT / 4 * 3),
                new FluidStack(FoundryFluids.liquid_platinum, FoundryAPI.FLUID_AMOUNT_INGOT / 4));

        if (FoundryConfig.recipe_alumina_melts_to_aluminium)
        {
            MeltingRecipeManager.INSTANCE.addRecipe(new OreMatcher("ingotAlumina"),
                    new FluidStack(FoundryFluids.liquid_aluminium, FoundryAPI.FLUID_AMOUNT_INGOT), 2100);
            MeltingRecipeManager.INSTANCE.addRecipe(new OreMatcher("nuggetAlumina"),
                    new FluidStack(FoundryFluids.liquid_aluminium, FoundryAPI.getAmountNugget()), 2100);
            MeltingRecipeManager.INSTANCE.addRecipe(new OreMatcher("dustAlumina"),
                    new FluidStack(FoundryFluids.liquid_aluminium, FoundryAPI.FLUID_AMOUNT_INGOT), 2100);
            MeltingRecipeManager.INSTANCE.addRecipe(new OreMatcher("oreAlumina"),
                    new FluidStack(FoundryFluids.liquid_aluminium, FoundryAPI.getAmountOre()), 2100);
        }
        else
        {
            InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(FoundryFluids.liquid_aluminium, 216),
                    new FluidStack(FoundryFluids.liquid_alumina, 216), new OreMatcher("dustCoal"), 240000);
            InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(FoundryFluids.liquid_aluminium, 216),
                    new FluidStack(FoundryFluids.liquid_alumina, 216), new OreMatcher("dustCharcoal"), 240000);
            if (OreDictionary.doesOreNameExist("dustSmallCoal"))
                InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(FoundryFluids.liquid_aluminium, 54),
                        new FluidStack(FoundryFluids.liquid_alumina, 54), new OreMatcher("dustSmallCoal"), 60000);
            if (OreDictionary.doesOreNameExist("dustSmallCharcoal"))
                InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(FoundryFluids.liquid_aluminium, 54),
                        new FluidStack(FoundryFluids.liquid_iron, 54), new OreMatcher("dustSmallCharcoal"), 60000);
        }

        BurnerHeaterFuelManager.INSTANCE.addFuel(new ItemStackMatcher(new ItemStack(Items.COAL, 1, 0)), // Coal
                1600, BurnerHeaterFuelManager.INSTANCE.getHeatNeeded(187000, FoundryAPI.CRUCIBLE_BASIC_TEMP_LOSS_RATE));

        BurnerHeaterFuelManager.INSTANCE.addFuel(new ItemStackMatcher(new ItemStack(Items.COAL, 1, 1)), // Charcoal
                1200, BurnerHeaterFuelManager.INSTANCE.getHeatNeeded(187000, FoundryAPI.CRUCIBLE_BASIC_TEMP_LOSS_RATE));

        BurnerHeaterFuelManager.INSTANCE.addFuel(new ItemStackMatcher(new ItemStack(Blocks.COAL_BLOCK, 1, 0)), // Coal Block
                16000,
                BurnerHeaterFuelManager.INSTANCE.getHeatNeeded(200000, FoundryAPI.CRUCIBLE_BASIC_TEMP_LOSS_RATE));

        if (OreDictionary.doesOreNameExist("blockCharcoal"))
            BurnerHeaterFuelManager.INSTANCE.addFuel(new OreMatcher("blockCharcoal"), 12000,
                    BurnerHeaterFuelManager.INSTANCE.getHeatNeeded(200000, FoundryAPI.CRUCIBLE_BASIC_TEMP_LOSS_RATE));
        if (OreDictionary.doesOreNameExist("fuelCoke"))
            BurnerHeaterFuelManager.INSTANCE.addFuel(new OreMatcher("fuelCoke"), 3200,
                    BurnerHeaterFuelManager.INSTANCE.getHeatNeeded(215000, FoundryAPI.CRUCIBLE_BASIC_TEMP_LOSS_RATE));
        if (OreDictionary.doesOreNameExist("dustCoal"))
            BurnerHeaterFuelManager.INSTANCE.addFuel(new OreMatcher("dustCoal"), 800,
                    BurnerHeaterFuelManager.INSTANCE.getHeatNeeded(195000, FoundryAPI.CRUCIBLE_BASIC_TEMP_LOSS_RATE));
        if (OreDictionary.doesOreNameExist("dustCharcoal"))
            BurnerHeaterFuelManager.INSTANCE.addFuel(new OreMatcher("dustCharcoal"), 800,
                    BurnerHeaterFuelManager.INSTANCE.getHeatNeeded(192000, FoundryAPI.CRUCIBLE_BASIC_TEMP_LOSS_RATE));
        if (OreDictionary.doesOreNameExist("dustSmallCoal"))
            BurnerHeaterFuelManager.INSTANCE.addFuel(new OreMatcher("dustSmallCoal"), 200,
                    BurnerHeaterFuelManager.INSTANCE.getHeatNeeded(195000, FoundryAPI.CRUCIBLE_BASIC_TEMP_LOSS_RATE));
        if (OreDictionary.doesOreNameExist("dustSmallCharcoal"))
            BurnerHeaterFuelManager.INSTANCE.addFuel(new OreMatcher("dustSmallCharcoal"), 200,
                    BurnerHeaterFuelManager.INSTANCE.getHeatNeeded(192000, FoundryAPI.CRUCIBLE_BASIC_TEMP_LOSS_RATE));

        BurnerHeaterFuelManager.INSTANCE.addFuel(new ItemStackMatcher(Items.BLAZE_ROD), 2000,
                BurnerHeaterFuelManager.INSTANCE.getHeatNeeded(220000, FoundryAPI.CRUCIBLE_BASIC_TEMP_LOSS_RATE));

    }

    static ItemStack mold_ingot = FoundryItems.mold(ItemMold.SubItem.INGOT);
    static ItemStack mold_slab = FoundryItems.mold(ItemMold.SubItem.SLAB);
    static ItemStack mold_stairs = FoundryItems.mold(ItemMold.SubItem.STAIRS);
    static ItemStack mold_plate = FoundryItems.mold(ItemMold.SubItem.PLATE);
    static ItemStack mold_block = FoundryItems.mold(ItemMold.SubItem.BLOCK);
    static ItemStack mold_gear = FoundryItems.mold(ItemMold.SubItem.GEAR);
    static ItemStack mold_rod = FoundryItems.mold(ItemMold.SubItem.ROD);
    static ItemStack mold_nugget = FoundryItems.mold(ItemMold.SubItem.NUGGET);

    public static void addDefaultCasting(FluidLiquidMetal fluid, String name)
    {
        if (fluid.special)
        {
            return;
        }

        // Ingot
        ItemStack ingot = FoundryMiscUtils.getModItemFromOreDictionary(FoundryConfig.prefModID, "ingot" + name);
        if (!ingot.isEmpty())
        {
            FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT);
            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(ingot), fluid_stack, mold_ingot, null);
            CastingTableRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(ingot), fluid_stack,
                    ICastingTableRecipe.TableType.INGOT);
        }

        // Block
        ItemStack block = FoundryMiscUtils.getModItemFromOreDictionary(FoundryConfig.prefModID, "block" + name);
        if (!block.isEmpty())
        {
            FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.getAmountBlock());
            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(block), fluid_stack, mold_block, null);
            CastingTableRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(block), fluid_stack,
                    ICastingTableRecipe.TableType.BLOCK);
        }

        // Slab
        ItemStack slab = FoundryMiscUtils.getModItemFromOreDictionary(FoundryConfig.prefModID, "slab" + name);
        if (!slab.isEmpty())
        {
            FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.getAmountBlock() / 2);

            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(slab), fluid_stack, mold_slab, null);
            MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(slab), fluid_stack);
        }

        // Stairs
        ItemStack stairs = FoundryMiscUtils.getModItemFromOreDictionary(FoundryConfig.prefModID, "stairs" + name);
        if (!stairs.isEmpty())
        {
            FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.getAmountBlock() * 3 / 4);
            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(stairs), fluid_stack, mold_stairs, null);
            MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(stairs), fluid_stack);
        }

        // Dust
        ItemStack dust = FoundryMiscUtils.getModItemFromOreDictionary(FoundryConfig.prefModID, "dust" + name);
        if (!dust.isEmpty())
        {
            AtomizerRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(dust),
                    new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT));
        }

        // Gear
        ItemStack gear = FoundryMiscUtils.getModItemFromOreDictionary(FoundryConfig.prefModID, "gear" + name);
        if (!gear.isEmpty())
        {
            FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.getAmountGear());
            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(gear), fluid_stack, mold_gear, null);
        }

        // Nugget
        ItemStack nugget = FoundryMiscUtils.getModItemFromOreDictionary(FoundryConfig.prefModID, "nugget" + name);
        if (!nugget.isEmpty())
        {
            FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.getAmountNugget());
            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(nugget), fluid_stack, mold_nugget, null);
        }

        // Plate
        ItemStack plate = FoundryMiscUtils.getModItemFromOreDictionary(FoundryConfig.prefModID, "plate" + name);
        if (!plate.isEmpty())
        {
            FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.getAmountPlate());

            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(plate), fluid_stack, mold_plate, null);
            CastingTableRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(plate), fluid_stack,
                    ICastingTableRecipe.TableType.PLATE);
        }

        // Rod
        ItemStack rod = FoundryMiscUtils.getModItemFromOreDictionary(FoundryConfig.prefModID, "rod" + name);
        if (!rod.isEmpty())
        {
            FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.getAmountRod());

            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(rod), fluid_stack, mold_rod, null);
            CastingTableRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(rod), fluid_stack,
                    ICastingTableRecipe.TableType.ROD);
        }
    }
}
