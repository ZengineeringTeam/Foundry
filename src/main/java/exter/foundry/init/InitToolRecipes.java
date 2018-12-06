package exter.foundry.init;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.matcher.OreMatcher;
import exter.foundry.fluid.FoundryFluidRegistry;
import exter.foundry.fluid.FoundryFluids;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.MoldRecipeManager;
import exter.foundry.util.MiscUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class InitToolRecipes
{
    static public void init()
    {
        OreMatcher extra_sticks1 = new OreMatcher("stickWood", 1);
        OreMatcher extra_sticks2 = new OreMatcher("stickWood", 2);

        ItemStack mold_chestplate = ItemMold.SubItem.CHESTPLATE.getItem();
        ItemStack mold_pickaxe = ItemMold.SubItem.PICKAXE.getItem();
        ItemStack mold_axe = ItemMold.SubItem.AXE.getItem();
        ItemStack mold_shovel = ItemMold.SubItem.SHOVEL.getItem();
        ItemStack mold_hoe = ItemMold.SubItem.HOE.getItem();
        ItemStack mold_sword = ItemMold.SubItem.SWORD.getItem();
        ItemStack mold_leggings = ItemMold.SubItem.LEGGINGS.getItem();
        ItemStack mold_helmet = ItemMold.SubItem.HELMET.getItem();
        ItemStack mold_boots = ItemMold.SubItem.BOOTS.getItem();
        ItemStack mold_hammer = ItemMold.SubItem.HAMMER.getItem();
        ItemStack mold_sickle = ItemMold.SubItem.SICKLE.getItem();
        ItemStack mold_shears = ItemMold.SubItem.SHEARS.getItem();
        ItemStack mold_excavator = ItemMold.SubItem.EXCAVATOR.getItem();
        ItemStack mold_shield = ItemMold.SubItem.SHIELD.getItem();

        MoldRecipeManager.INSTANCE.addRecipe(mold_helmet, 4, 3, new int[] { 3, 3, 3, 3, 3, 1, 1, 3, 3, 1, 1, 3 });

        MoldRecipeManager.INSTANCE.addRecipe(mold_chestplate, 6, 6, new int[] { 3, 1, 0, 0, 1, 3, 3, 1, 0, 0, 1, 3, 3,
                1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 3 });

        MoldRecipeManager.INSTANCE.addRecipe(mold_leggings, 6, 6, new int[] { 3, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 3, 3, 1,
                0, 0, 1, 3, 3, 1, 0, 0, 1, 3, 3, 1, 0, 0, 1, 3, 3, 1, 0, 0, 1, 3 });

        MoldRecipeManager.INSTANCE.addRecipe(mold_boots, 6, 6, new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 2, 3, 0, 0, 3, 2, 3, 3, 0, 0, 3, 3, 3, 3, 0, 0, 3, 3 });

        MoldRecipeManager.INSTANCE.addRecipe(mold_pickaxe, 5, 5,
                new int[] { 0, 2, 2, 2, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0 });

        MoldRecipeManager.INSTANCE.addRecipe(mold_axe, 3, 5, new int[] { 1, 2, 2, 1, 2, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1 });

        MoldRecipeManager.INSTANCE.addRecipe(mold_shovel, 3, 6,
                new int[] { 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0 });

        MoldRecipeManager.INSTANCE.addRecipe(mold_hoe, 3, 5, new int[] { 0, 2, 2, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1 });

        MoldRecipeManager.INSTANCE.addRecipe(mold_sword, 3, 6,
                new int[] { 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 2, 1, 0, 1, 0 });

        MoldRecipeManager.INSTANCE.addRecipe(mold_hammer, 5, 6,
                new int[] { 3, 2, 2, 2, 3, 3, 4, 4, 4, 3, 3, 2, 3, 2, 3, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0 });

        MoldRecipeManager.INSTANCE.addRecipe(mold_sickle, 5, 6,
                new int[] { 3, 3, 3, 3, 3, 0, 0, 0, 0, 3, 0, 0, 0, 0, 3, 0, 0, 2, 3, 3, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0 });

        MoldRecipeManager.INSTANCE.addRecipe(mold_shears, 5, 6,
                new int[] { 2, 3, 0, 3, 2, 2, 3, 0, 3, 2, 2, 3, 0, 3, 2, 2, 0, 0, 0, 2, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0 });

        MoldRecipeManager.INSTANCE.addRecipe(mold_excavator, 6, 6, new int[] { 0, 0, 0, 3, 3, 3, 0, 0, 3, 3, 3, 3, 0, 3,
                3, 3, 3, 3, 0, 0, 3, 3, 3, 0, 0, 1, 0, 3, 0, 0, 1, 0, 0, 0, 0, 0 });

        // TODO: mold recipes
        // TODO: melting recipes
        if (FoundryFluidRegistry.getStrategy("iron").registerRecipes())
        {
            MiscUtil.registerCasting(new ItemStack(Items.IRON_PICKAXE), FoundryFluids.liquid_iron,
                    FoundryAPI.getAmountPickaxe(), ItemMold.SubItem.PICKAXE, extra_sticks2);
            MiscUtil.registerCasting(new ItemStack(Items.IRON_AXE), FoundryFluids.liquid_iron,
                    FoundryAPI.getAmountAxe(), ItemMold.SubItem.AXE, extra_sticks2);
            MiscUtil.registerCasting(new ItemStack(Items.IRON_SHOVEL), FoundryFluids.liquid_iron,
                    FoundryAPI.getAmountShovel(), ItemMold.SubItem.SHOVEL, extra_sticks2);
            MiscUtil.registerCasting(new ItemStack(Items.IRON_HOE), FoundryFluids.liquid_iron,
                    FoundryAPI.getAmountHoe(), ItemMold.SubItem.HOE, extra_sticks2);
            MiscUtil.registerCasting(new ItemStack(Items.IRON_SWORD), FoundryFluids.liquid_iron,
                    FoundryAPI.getAmountSword(), ItemMold.SubItem.SWORD, extra_sticks1);
            MiscUtil.registerCasting(new ItemStack(Items.IRON_HELMET), FoundryFluids.liquid_iron,
                    FoundryAPI.getAmountHelm(), ItemMold.SubItem.HELMET);
            MiscUtil.registerCasting(new ItemStack(Items.IRON_CHESTPLATE), FoundryFluids.liquid_iron,
                    FoundryAPI.getAmountChest(), ItemMold.SubItem.CHESTPLATE);
            MiscUtil.registerCasting(new ItemStack(Items.IRON_LEGGINGS), FoundryFluids.liquid_iron,
                    FoundryAPI.getAmountLegs(), ItemMold.SubItem.LEGGINGS);
            MiscUtil.registerCasting(new ItemStack(Items.IRON_BOOTS), FoundryFluids.liquid_iron,
                    FoundryAPI.getAmountBoots(), ItemMold.SubItem.BOOTS);
            MiscUtil.registerCasting(new ItemStack(Items.SHEARS), FoundryFluids.liquid_iron,
                    FoundryAPI.getAmountShears(), ItemMold.SubItem.SHEARS);

            // iron shield?
            // MiscUtil.registerCasting(new ItemStack(Items.IRON_BOOTS), FoundryFluids.liquid_iron, 4, ItemMold.SubItem.BOOTS);
            //        MiscUtil.registerCasting(
            //                new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "???"))),
            //                FoundryFluids.liquid_gold, 6, ItemMold.SubItem.BOOTS);
        }

        if (FoundryFluidRegistry.getStrategy("gold").registerRecipes())
        {
            MiscUtil.registerCasting(new ItemStack(Items.GOLDEN_PICKAXE), FoundryFluids.liquid_gold,
                    FoundryAPI.getAmountPickaxe(), ItemMold.SubItem.PICKAXE, extra_sticks2);
            MiscUtil.registerCasting(new ItemStack(Items.GOLDEN_AXE), FoundryFluids.liquid_gold,
                    FoundryAPI.getAmountAxe(), ItemMold.SubItem.AXE, extra_sticks2);
            MiscUtil.registerCasting(new ItemStack(Items.GOLDEN_SHOVEL), FoundryFluids.liquid_gold,
                    FoundryAPI.getAmountShovel(), ItemMold.SubItem.SHOVEL, extra_sticks2);
            MiscUtil.registerCasting(new ItemStack(Items.GOLDEN_HOE), FoundryFluids.liquid_gold,
                    FoundryAPI.getAmountHoe(), ItemMold.SubItem.HOE, extra_sticks2);
            MiscUtil.registerCasting(new ItemStack(Items.GOLDEN_SWORD), FoundryFluids.liquid_gold,
                    FoundryAPI.getAmountSword(), ItemMold.SubItem.SWORD, extra_sticks1);
            MiscUtil.registerCasting(new ItemStack(Items.GOLDEN_HELMET), FoundryFluids.liquid_gold,
                    FoundryAPI.getAmountHelm(), ItemMold.SubItem.HELMET);
            MiscUtil.registerCasting(new ItemStack(Items.GOLDEN_CHESTPLATE), FoundryFluids.liquid_gold,
                    FoundryAPI.getAmountChest(), ItemMold.SubItem.CHESTPLATE);
            MiscUtil.registerCasting(new ItemStack(Items.GOLDEN_LEGGINGS), FoundryFluids.liquid_gold,
                    FoundryAPI.getAmountLegs(), ItemMold.SubItem.LEGGINGS);
            MiscUtil.registerCasting(new ItemStack(Items.GOLDEN_BOOTS), FoundryFluids.liquid_gold,
                    FoundryAPI.getAmountBoots(), ItemMold.SubItem.BOOTS);
        }
    }
}
