package exter.foundry.init;

import java.util.ArrayList;
import java.util.List;

import cofh.thermalfoundation.init.TFFluids;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.ICastingTableRecipe;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.api.recipe.matcher.OreMatcher;
import exter.foundry.config.FoundryConfig;
import exter.foundry.fluid.FluidLiquidMetal;
import exter.foundry.fluid.FoundryFluidRegistry;
import exter.foundry.fluid.FoundryFluids;
import exter.foundry.item.ItemMold;
import exter.foundry.material.MaterialRegistry;
import exter.foundry.material.OreDictMaterial;
import exter.foundry.material.OreDictType;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.BurnerHeaterFuelManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.CastingTableRecipeManager;
import exter.foundry.recipes.manager.FluidHeaterFuelManager;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.recipes.manager.MoldRecipeManager;
import exter.foundry.util.MiscUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
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

        ItemStack ingot_mold = ItemMold.SubItem.INGOT.getItem();
        ItemStack block_mold = ItemMold.SubItem.BLOCK.getItem();
        for (String name : FoundryFluidRegistry.INSTANCE.getFluidNames())
        {
            FluidLiquidMetal fluid = FoundryFluidRegistry.INSTANCE.getFluid(name);
            name = MiscUtil.upperCaseFirstChar(name);
            if (!fluid.glass)
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
                                false, null);
                    }
                    if (CastingTableRecipeManager.INSTANCE.findRecipe(fluidstack,
                            ICastingTableRecipe.TableType.INGOT) == null)
                    {
                        CastingTableRecipeManager.INSTANCE.addRecipe(new OreMatcher("ingot" + name), fluidstack,
                                ICastingTableRecipe.TableType.INGOT);
                    }
                }

                ores = OreDictionary.doesOreNameExist("block" + name) ? OreDictionary.getOres("block" + name, false)
                        : new ArrayList<>();
                fluidstack.amount = FoundryAPI.getAmountBlock();
                if (ores != null && ores.size() > 0)
                {
                    if (CastingRecipeManager.INSTANCE.findRecipe(fluidstack, block_mold, null) == null)
                    {
                        CastingRecipeManager.INSTANCE.addRecipe(new OreMatcher("block" + name), fluidstack, block_mold,
                                false, null);
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

    private static void registerGlassRecipes()
    {
        final String[] oredict_names = { "Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "LightGray",
                "Gray", "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White" };

        int temp = 1550;
        int melt = 500;
        Fluid liquid_glass = FoundryFluids.liquid_glass;

        MeltingRecipeManager.INSTANCE.addRecipe(new OreMatcher("sand"), new FluidStack(liquid_glass, 1000), temp, melt);
        MeltingRecipeManager.INSTANCE.addRecipe(new OreMatcher("blockGlassColorless"),
                new FluidStack(liquid_glass, 1000), temp, melt);
        MeltingRecipeManager.INSTANCE.addRecipe(new OreMatcher("paneGlassColorless"), new FluidStack(liquid_glass, 375),
                temp, melt);
        CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(Blocks.GLASS), new FluidStack(liquid_glass, 1000),
                ItemMold.SubItem.BLOCK.getItem(), false, null, 400);
        CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(Blocks.GLASS_PANE),
                new FluidStack(liquid_glass, 375), ItemMold.SubItem.PLATE.getItem(), false, null, 100);
        CastingTableRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(Blocks.GLASS),
                new FluidStack(liquid_glass, 1000), ICastingTableRecipe.TableType.BLOCK);
        CastingTableRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(Blocks.GLASS_PANE),
                new FluidStack(liquid_glass, 375), ICastingTableRecipe.TableType.PLATE);
        for (EnumDyeColor dye : EnumDyeColor.values())
        {

            int meta = dye.getMetadata();
            ItemStack stained_glass = new ItemStack(Blocks.STAINED_GLASS, 1, meta);
            ItemStack stained_glass_pane = new ItemStack(Blocks.STAINED_GLASS_PANE, 1, meta);

            Fluid liquid_glass_colored = FoundryFluids.liquid_glass_colored[meta];

            MeltingRecipeManager.INSTANCE.addRecipe(new OreMatcher("blockGlass" + oredict_names[dye.getDyeDamage()]),
                    new FluidStack(liquid_glass_colored, 1000), temp, melt);
            MeltingRecipeManager.INSTANCE.addRecipe(new OreMatcher("paneGlass" + oredict_names[dye.getDyeDamage()]),
                    new FluidStack(liquid_glass_colored, 375), temp, melt);
            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(stained_glass),
                    new FluidStack(liquid_glass_colored, 1000), ItemMold.SubItem.BLOCK.getItem(), false, null, 400);
            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(stained_glass_pane),
                    new FluidStack(liquid_glass_colored, 375), ItemMold.SubItem.PLATE.getItem(), false, null, 100);
            CastingTableRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(stained_glass),
                    new FluidStack(liquid_glass_colored, 1000), ICastingTableRecipe.TableType.BLOCK);
            CastingTableRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(stained_glass_pane),
                    new FluidStack(liquid_glass_colored, 375), ICastingTableRecipe.TableType.PLATE);

            InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_glass_colored, 2000),
                    new FluidStack(liquid_glass, 2000), new OreMatcher("dye" + oredict_names[dye.getDyeDamage()]),
                    5000);
        }
    }

    static public void registerMachineRecipes()
    {

        for (String name : FoundryFluidRegistry.INSTANCE.getFluidNames())
        {
            FluidLiquidMetal fluid = FoundryFluidRegistry.INSTANCE.getFluid(name);
            if (!fluid.glass)
            {
                FoundryUtils.registerBasicMeltingRecipes(name, fluid);
            }
        }
        //FoundryUtils.registerBasicMeltingRecipes("Chromium", LiquidMetalRegistry.instance.getFluid("chrome"));
        FoundryUtils.registerBasicMeltingRecipes("Aluminum", FoundryFluidRegistry.INSTANCE.getFluid("aluminium"));
        FoundryUtils.registerBasicMeltingRecipes("Constantan", FoundryFluidRegistry.INSTANCE.getFluid("constantan"));

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

        MoldRecipeManager.INSTANCE.addRecipe(ItemMold.SubItem.INGOT.getItem(), 2, 4,
                new int[] { 2, 2, 2, 2, 2, 2, 2, 2 });

        MoldRecipeManager.INSTANCE.addRecipe(ItemMold.SubItem.PLATE.getItem(), 4, 4,
                new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 });

        MoldRecipeManager.INSTANCE.addRecipe(ItemMold.SubItem.ROD.getItem(), 1, 6, new int[] { 1, 1, 1, 1, 1, 1 });

        MoldRecipeManager.INSTANCE.addRecipe(ItemMold.SubItem.GEAR.getItem(), 5, 5,
                new int[] { 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1 });

        MoldRecipeManager.INSTANCE.addRecipe(ItemMold.SubItem.BLOCK.getItem(), 6, 6, new int[] { 4, 4, 4, 4, 4, 4, 4, 4,
                4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4 });

        MoldRecipeManager.INSTANCE.addRecipe(ItemMold.SubItem.SLAB.getItem(), 6, 6, new int[] { 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4 });

        MoldRecipeManager.INSTANCE.addRecipe(ItemMold.SubItem.STAIRS.getItem(), 6, 6, new int[] { 0, 0, 0, 4, 4, 4, 0,
                0, 0, 4, 4, 4, 0, 0, 0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4 });

        MoldRecipeManager.INSTANCE.addRecipe(ItemMold.SubItem.NUGGET.getItem(), 3, 3,
                new int[] { 1, 1, 1, 1, 1, 1, 0, 1, 0 });

        InitAlloyRecipes.init();

        for (ItemMold.SubItem sub : ItemMold.SubItem.values())
        {
            CastingRecipeManager.INSTANCE.addMold(sub.getItem());
        }

        if (FoundryConfig.recipe_equipment)
        {
            InitToolRecipes.init();
        }

        if (FoundryConfig.recipe_glass)
        {
            registerGlassRecipes();
        }

        //Base casting recipes.
        for (String name : FoundryFluidRegistry.INSTANCE.getFluidNames())
        {
            addDefaultCasting(FoundryFluidRegistry.INSTANCE.getFluid(name), name);
        }

        addDefaultCasting(FoundryFluidRegistry.INSTANCE.getFluid("aluminium"), "Aluminum");
        addDefaultCasting(FoundryFluidRegistry.INSTANCE.getFluid("constantan"), "Constantan");

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

        FluidHeaterFuelManager.INSTANCE.addFuel(FluidRegistry.LAVA);
        FluidHeaterFuelManager.INSTANCE.addFuel(TFFluids.fluidPetrotheum);
    }

    static ItemStack mold_ingot = ItemMold.SubItem.INGOT.getItem();
    static ItemStack mold_slab = ItemMold.SubItem.SLAB.getItem();
    static ItemStack mold_stairs = ItemMold.SubItem.STAIRS.getItem();
    static ItemStack mold_plate = ItemMold.SubItem.PLATE.getItem();
    static ItemStack mold_block = ItemMold.SubItem.BLOCK.getItem();
    static ItemStack mold_gear = ItemMold.SubItem.GEAR.getItem();
    static ItemStack mold_rod = ItemMold.SubItem.ROD.getItem();
    static ItemStack mold_nugget = ItemMold.SubItem.NUGGET.getItem();

    public static void addDefaultCasting(FluidLiquidMetal fluid, String name)
    {
        name = MiscUtil.upperCaseFirstChar(name);

        if (fluid.glass)
        {
            return;
        }

        // Ingot
        ItemStack ingot = MiscUtil.getModItemFromOreDictionary(FoundryConfig.prefModID, "ingot" + name);
        if (!ingot.isEmpty())
        {
            FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT);
            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(ingot), fluid_stack, mold_ingot, false, null);
            CastingTableRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(ingot), fluid_stack,
                    ICastingTableRecipe.TableType.INGOT);
        }

        // Block
        ItemStack block = MiscUtil.getModItemFromOreDictionary(FoundryConfig.prefModID, "block" + name);
        if (!block.isEmpty())
        {
            FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.getAmountBlock());
            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(block), fluid_stack, mold_block, false, null);
            CastingTableRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(block), fluid_stack,
                    ICastingTableRecipe.TableType.BLOCK);
        }

        // Slab
        ItemStack slab = MiscUtil.getModItemFromOreDictionary(FoundryConfig.prefModID, "slab" + name);
        if (!slab.isEmpty())
        {
            FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.getAmountBlock() / 2);

            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(slab), fluid_stack, mold_slab, false, null);
            MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(slab), fluid_stack);
        }

        // Stairs
        ItemStack stairs = MiscUtil.getModItemFromOreDictionary(FoundryConfig.prefModID, "stairs" + name);
        if (!stairs.isEmpty())
        {
            FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.getAmountBlock() * 3 / 4);
            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(stairs), fluid_stack, mold_stairs, false,
                    null);
            MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(stairs), fluid_stack);
        }

        // Gear
        ItemStack gear = MiscUtil.getModItemFromOreDictionary(FoundryConfig.prefModID, "gear" + name);
        if (!gear.isEmpty())
        {
            FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.getAmountGear());
            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(gear), fluid_stack, mold_gear, false, null);
        }

        // Nugget
        ItemStack nugget = MiscUtil.getModItemFromOreDictionary(FoundryConfig.prefModID, "nugget" + name);
        if (!nugget.isEmpty())
        {
            FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.getAmountNugget());
            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(nugget), fluid_stack, mold_nugget, false,
                    null);
        }

        // Plate
        ItemStack plate = MiscUtil.getModItemFromOreDictionary(FoundryConfig.prefModID, "plate" + name);
        if (!plate.isEmpty())
        {
            FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.getAmountPlate());

            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(plate), fluid_stack, mold_plate, false, null);
            CastingTableRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(plate), fluid_stack,
                    ICastingTableRecipe.TableType.PLATE);
        }

        // Rod
        ItemStack rod = MiscUtil.getModItemFromOreDictionary(FoundryConfig.prefModID, "rod" + name);
        if (!rod.isEmpty())
        {
            FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.getAmountRod());

            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(rod), fluid_stack, mold_rod, false, null);
            CastingTableRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(rod), fluid_stack,
                    ICastingTableRecipe.TableType.ROD);
        }
    }
}
