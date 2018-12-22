package exter.foundry.init;

import static exter.foundry.api.FoundryAPI.FLUID_AMOUNT_INGOT;

import java.util.ArrayList;
import java.util.List;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.ICastingTableRecipe;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.api.recipe.matcher.OreMatcher;
import exter.foundry.config.FoundryConfig;
import exter.foundry.fluid.FoundryFluidRegistry;
import exter.foundry.fluid.FoundryFluids;
import exter.foundry.item.ItemMold;
import exter.foundry.material.MaterialRegistry;
import exter.foundry.material.OreDictMaterial;
import exter.foundry.material.OreDictType;
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
    }

    static public void postInit()
    {
        registerMachineRecipes();
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

        for (String name : FoundryFluidRegistry.getFluidNames())
        {
            Fluid fluid = FluidRegistry.getFluid(name);
            if (fluid == null)
            {
                continue;
            }
            name = MiscUtil.upperCaseFirstChar(name);
            if (!fluid.getName().startsWith("glass") && !fluid.getName().equals("redstone") && !fluid.getName().equals("glowstone")
                    && FoundryFluidRegistry.getStrategy(fluid.getName()).registerRecipes())
            {
                FluidStack fluidstack = new FluidStack(fluid, FLUID_AMOUNT_INGOT);
                List<ItemStack> ores = OreDictionary.doesOreNameExist("ingot" + name)
                        ? OreDictionary.getOres("ingot" + name, false)
                        : new ArrayList<>();
                if (ores != null && ores.size() > 0)
                {
                    if (CastingRecipeManager.INSTANCE.findRecipe(fluidstack, ItemMold.SubItem.INGOT, null) == null)
                    {
                        CastingRecipeManager.INSTANCE.addRecipe(new OreMatcher("ingot" + name), fluidstack,
                                ItemMold.SubItem.INGOT, false, null);
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
                    if (CastingRecipeManager.INSTANCE.findRecipe(fluidstack, ItemMold.SubItem.BLOCK, null) == null)
                    {
                        CastingRecipeManager.INSTANCE.addRecipe(new OreMatcher("block" + name), fluidstack,
                                ItemMold.SubItem.BLOCK, false, null);
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
                ItemMold.SubItem.BLOCK, false, null, 400);
        CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(Blocks.GLASS_PANE),
                new FluidStack(liquid_glass, 375), ItemMold.SubItem.PLATE, false, null, 100);
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
                    new FluidStack(liquid_glass_colored, 1000), ItemMold.SubItem.BLOCK, false, null, 400);
            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(stained_glass_pane),
                    new FluidStack(liquid_glass_colored, 375), ItemMold.SubItem.PLATE, false, null, 100);
            CastingTableRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(stained_glass),
                    new FluidStack(liquid_glass_colored, 1000), ICastingTableRecipe.TableType.BLOCK);
            CastingTableRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(stained_glass_pane),
                    new FluidStack(liquid_glass_colored, 375), ICastingTableRecipe.TableType.PLATE);

            InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(liquid_glass_colored, 2000),
                    new FluidStack(liquid_glass, 2000), new OreMatcher("dye" + oredict_names[dye.getDyeDamage()]), 500);
        }
    }

    static public void registerMachineRecipes()
    {

        for (String name : FoundryFluidRegistry.getFluidNames())
        {
            Fluid fluid = FluidRegistry.getFluid(name);
            if (FoundryFluidRegistry.getStrategy(name).registerRecipes() && !fluid.getName().startsWith("glass") && !fluid.getName().equals("redstone")
                    && !fluid.getName().equals("glowstone") && !fluid.getName().equals("ender") && !fluid.getName().equals("endstone"))
            {
                FoundryUtils.registerBasicMeltingRecipes(name, fluid);
            }
        }
        // FoundryUtils.registerBasicMeltingRecipes("Chromium", LiquidMetalRegistry.instance.getFluid("chrome"));
        if (FoundryFluidRegistry.getStrategy("aluminium").registerRecipes())
        {
            FoundryUtils.registerBasicMeltingRecipes("Aluminum", FluidRegistry.getFluid("aluminium"));
        }
        // FoundryUtils.registerBasicMeltingRecipes("Constantan", FoundryFluidRegistry.INSTANCE.getFluid("constantan"));

        if (FoundryFluidRegistry.getStrategy("redstone").registerRecipes())
        {
            MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(Items.REDSTONE),
                new FluidStack(FoundryFluids.liquid_redstone, 100));
            MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(Blocks.REDSTONE_BLOCK),
                    new FluidStack(FoundryFluids.liquid_redstone, 900));
            if (OreDictionary.doesOreNameExist("dustSmallRedstone"))
            {
                MeltingRecipeManager.INSTANCE.addRecipe(new OreMatcher("dustSmallRedstone"),
                        new FluidStack(FoundryFluids.liquid_redstone, 25));
            }
        }

        if (FoundryFluidRegistry.getStrategy("glowstone").registerRecipes())
        {
            MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(Items.GLOWSTONE_DUST),
                    new FluidStack(FoundryFluids.liquid_glowstone, 250));
            MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(Blocks.GLOWSTONE),
                    new FluidStack(FoundryFluids.liquid_glowstone, 1000));
        }

        if (FoundryFluidRegistry.getStrategy("ender").registerRecipes())
        {
            MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(Items.ENDER_PEARL),
                    new FluidStack(FoundryFluids.liquid_enderpearl, 250));
        }

        MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(Blocks.ICE),
                new FluidStack(FluidRegistry.WATER, 1000), 350, 200);
        MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(Blocks.PACKED_ICE),
                new FluidStack(FluidRegistry.WATER, 6000), 500, 200);
        MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(Blocks.SNOW),
                new FluidStack(FluidRegistry.WATER, 500), 325, 200);
        MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(Items.SNOWBALL),
                new FluidStack(FluidRegistry.WATER, 125), 325, 200);

        MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(Blocks.MAGMA),
                new FluidStack(FluidRegistry.LAVA, 1000), 1250, 200);

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

        if (FoundryConfig.recipe_equipment)
        {
            InitToolRecipes.init();
        }

        if (FoundryConfig.recipe_glass)
        {
            registerGlassRecipes();
        }

        //Base casting recipes.
        for (String name : FoundryFluidRegistry.getFluidNames())
        {
            if (FoundryFluidRegistry.getStrategy(name).registerRecipes())
                addDefaultCasting(FluidRegistry.getFluid(name), name);
        }

        // why?
        if (FoundryFluidRegistry.getStrategy("aluminium").registerRecipes())
            addDefaultCasting(FluidRegistry.getFluid("aluminium"), "Aluminum");
        if (FoundryFluidRegistry.getStrategy("constantan").registerRecipes())
            addDefaultCasting(FluidRegistry.getFluid("constantan"), "Constantan");

        if (FoundryConfig.recipe_alumina_melts_to_aluminium)
        {
            if (FoundryFluids.liquid_aluminium != null)
            {
                MeltingRecipeManager.INSTANCE.addRecipe(new OreMatcher("ingotAlumina"),
                        new FluidStack(FoundryFluids.liquid_aluminium, FLUID_AMOUNT_INGOT), 2100);
                MeltingRecipeManager.INSTANCE.addRecipe(new OreMatcher("nuggetAlumina"),
                        new FluidStack(FoundryFluids.liquid_aluminium, FoundryAPI.getAmountNugget()), 2100);
                MeltingRecipeManager.INSTANCE.addRecipe(new OreMatcher("dustAlumina"),
                        new FluidStack(FoundryFluids.liquid_aluminium, FLUID_AMOUNT_INGOT), 2100);
                MeltingRecipeManager.INSTANCE.addRecipe(new OreMatcher("oreAlumina"),
                        new FluidStack(FoundryFluids.liquid_aluminium, FoundryAPI.getAmountOre()), 2100);
            }
        }
        else
        {
            if (FoundryFluids.liquid_aluminium != null && FoundryFluids.liquid_alumina != null)
            {
                if (OreDictionary.doesOreNameExist("dustCoal"))
                    InfuserRecipeManager.INSTANCE.addRecipe(
                            new FluidStack(FoundryFluids.liquid_aluminium, FLUID_AMOUNT_INGOT * 2),
                            new FluidStack(FoundryFluids.liquid_alumina, FLUID_AMOUNT_INGOT * 2),
                            new OreMatcher("dustCoal"), 2400);
                if (OreDictionary.doesOreNameExist("dustCharcoal"))
                    InfuserRecipeManager.INSTANCE.addRecipe(
                            new FluidStack(FoundryFluids.liquid_aluminium, FLUID_AMOUNT_INGOT * 2),
                            new FluidStack(FoundryFluids.liquid_alumina, FLUID_AMOUNT_INGOT * 2),
                            new OreMatcher("dustCharcoal"), 2400);
                if (OreDictionary.doesOreNameExist("dustSmallCoal"))
                    InfuserRecipeManager.INSTANCE.addRecipe(
                            new FluidStack(FoundryFluids.liquid_aluminium, FLUID_AMOUNT_INGOT / 2),
                            new FluidStack(FoundryFluids.liquid_alumina, FLUID_AMOUNT_INGOT / 2),
                            new OreMatcher("dustSmallCoal"), 600);
                if (OreDictionary.doesOreNameExist("dustSmallCharcoal"))
                    InfuserRecipeManager.INSTANCE.addRecipe(
                            new FluidStack(FoundryFluids.liquid_aluminium, FLUID_AMOUNT_INGOT / 2),
                            new FluidStack(FoundryFluids.liquid_alumina, FLUID_AMOUNT_INGOT / 2),
                            new OreMatcher("dustSmallCharcoal"), 600);
            }
        }

        BurnerHeaterFuelManager.INSTANCE.addFuel(new ItemStackMatcher(new ItemStack(Items.COAL, 1, 0)), // Coal
                1600, 187000);

        BurnerHeaterFuelManager.INSTANCE.addFuel(new ItemStackMatcher(new ItemStack(Items.COAL, 1, 1)), // Charcoal
                1200, 187000);

        BurnerHeaterFuelManager.INSTANCE.addFuel(new ItemStackMatcher(new ItemStack(Blocks.COAL_BLOCK, 1, 0)), // Coal Block
                16000, 200000);

        if (OreDictionary.doesOreNameExist("blockCharcoal"))
            BurnerHeaterFuelManager.INSTANCE.addFuel(new OreMatcher("blockCharcoal"), 12000, 200000);
        if (OreDictionary.doesOreNameExist("fuelCoke"))
            BurnerHeaterFuelManager.INSTANCE.addFuel(new OreMatcher("fuelCoke"), 3200, 215000);
        if (OreDictionary.doesOreNameExist("dustCoal"))
            BurnerHeaterFuelManager.INSTANCE.addFuel(new OreMatcher("dustCoal"), 800, 195000);
        if (OreDictionary.doesOreNameExist("dustCharcoal"))
            BurnerHeaterFuelManager.INSTANCE.addFuel(new OreMatcher("dustCharcoal"), 800, 192000);
        if (OreDictionary.doesOreNameExist("dustSmallCoal"))
            BurnerHeaterFuelManager.INSTANCE.addFuel(new OreMatcher("dustSmallCoal"), 200, 195000);
        if (OreDictionary.doesOreNameExist("dustSmallCharcoal"))
            BurnerHeaterFuelManager.INSTANCE.addFuel(new OreMatcher("dustSmallCharcoal"), 200, 192000);

        BurnerHeaterFuelManager.INSTANCE.addFuel(new ItemStackMatcher(Items.BLAZE_ROD), 2000, 220000);

        FluidHeaterFuelManager.INSTANCE.addFuel(FluidRegistry.LAVA);
    }

    public static void addDefaultCasting(Fluid fluid, String name)
    {
        name = MiscUtil.upperCaseFirstChar(name);

        if (fluid.getName().startsWith("glass") || fluid.getName().equals("redstone") || fluid.getName().equals("glowstone"))
        {
            return;
        }

        // Ingot
        ItemStack ingot = MiscUtil.getModItemFromOreDictionary("ingot" + name);
        if (!ingot.isEmpty())
        {
            FluidStack fluid_stack = new FluidStack(fluid, FLUID_AMOUNT_INGOT);
            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(ingot), fluid_stack, ItemMold.SubItem.INGOT,
                    false, null);
            CastingTableRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(ingot), fluid_stack,
                    ICastingTableRecipe.TableType.INGOT);
        }

        // Block
        ItemStack block = MiscUtil.getModItemFromOreDictionary("block" + name);
        if (!block.isEmpty())
        {
            FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.getAmountBlock());
            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(block), fluid_stack, ItemMold.SubItem.BLOCK,
                    false, null);
            CastingTableRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(block), fluid_stack,
                    ICastingTableRecipe.TableType.BLOCK);
        }

        // Slab
        ItemStack slab = MiscUtil.getModItemFromOreDictionary("slab" + name);
        if (!slab.isEmpty())
        {
            FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.getAmountBlock() / 2);

            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(slab), fluid_stack, ItemMold.SubItem.SLAB,
                    false, null);
            MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(slab), fluid_stack);
        }

        // Stairs
        ItemStack stairs = MiscUtil.getModItemFromOreDictionary("stairs" + name);
        if (!stairs.isEmpty())
        {
            FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.getAmountBlock() * 3 / 4);
            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(stairs), fluid_stack, ItemMold.SubItem.STAIRS,
                    false, null);
            MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(stairs), fluid_stack);
        }

        // Gear
        ItemStack gear = MiscUtil.getModItemFromOreDictionary("gear" + name);
        if (!gear.isEmpty())
        {
            FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.getAmountGear());
            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(gear), fluid_stack, ItemMold.SubItem.GEAR,
                    false, null);
        }

        // Nugget
        ItemStack nugget = MiscUtil.getModItemFromOreDictionary("nugget" + name);
        if (!nugget.isEmpty())
        {
            FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.getAmountNugget());
            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(nugget), fluid_stack, ItemMold.SubItem.NUGGET,
                    false, null);
        }

        // Plate
        ItemStack plate = MiscUtil.getModItemFromOreDictionary("plate" + name);
        if (!plate.isEmpty())
        {
            FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.getAmountPlate());

            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(plate), fluid_stack, ItemMold.SubItem.PLATE,
                    false, null);
            CastingTableRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(plate), fluid_stack,
                    ICastingTableRecipe.TableType.PLATE);
        }

        // Rod
        ItemStack rod = MiscUtil.getModItemFromOreDictionary("rod" + name);
        if (!rod.isEmpty())
        {
            FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.getAmountRod());

            CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(rod), fluid_stack, ItemMold.SubItem.ROD, false,
                    null);
            CastingTableRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(rod), fluid_stack,
                    ICastingTableRecipe.TableType.ROD);
        }
    }
}
