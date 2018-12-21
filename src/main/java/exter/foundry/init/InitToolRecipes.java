package exter.foundry.init;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.api.recipe.matcher.OreMatcher;
import exter.foundry.fluid.FoundryFluidRegistry;
import exter.foundry.fluid.FoundryFluids;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.MoldRecipeManager;
import net.minecraft.init.Items;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class InitToolRecipes
{
    static public void init()
    {
        Fluid liquid_iron = FoundryFluids.liquid_iron;
        Fluid liquid_gold = FoundryFluids.liquid_gold;

        OreMatcher extra_sticks1 = new OreMatcher("stickWood", 1);
        OreMatcher extra_sticks2 = new OreMatcher("stickWood", 2);

        ItemStackMatcher mold_chestplate = ItemMold.SubItem.CHESTPLATE.getMatcher();
        ItemStackMatcher mold_pickaxe = ItemMold.SubItem.PICKAXE.getMatcher();
        ItemStackMatcher mold_axe = ItemMold.SubItem.AXE.getMatcher();
        ItemStackMatcher mold_shovel = ItemMold.SubItem.SHOVEL.getMatcher();
        ItemStackMatcher mold_hoe = ItemMold.SubItem.HOE.getMatcher();
        ItemStackMatcher mold_sword = ItemMold.SubItem.SWORD.getMatcher();
        ItemStackMatcher mold_leggings = ItemMold.SubItem.LEGGINGS.getMatcher();
        ItemStackMatcher mold_helmet = ItemMold.SubItem.HELMET.getMatcher();
        ItemStackMatcher mold_boots = ItemMold.SubItem.BOOTS.getMatcher();
        ItemStackMatcher mold_shears = ItemMold.SubItem.SHEARS.getMatcher();

        MoldRecipeManager.INSTANCE.addRecipe(mold_helmet.getItem(), 4, 3,
                new int[] { 3, 3, 3, 3, 3, 1, 1, 3, 3, 1, 1, 3 });

        MoldRecipeManager.INSTANCE.addRecipe(mold_chestplate.getItem(), 6, 6, new int[] { 3, 1, 0, 0, 1, 3, 3, 1, 0, 0,
                1, 3, 3, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 3 });

        MoldRecipeManager.INSTANCE.addRecipe(mold_leggings.getItem(), 6, 6, new int[] { 3, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1,
                3, 3, 1, 0, 0, 1, 3, 3, 1, 0, 0, 1, 3, 3, 1, 0, 0, 1, 3, 3, 1, 0, 0, 1, 3 });

        MoldRecipeManager.INSTANCE.addRecipe(mold_boots.getItem(), 6, 6, new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 2, 3, 0, 0, 3, 2, 3, 3, 0, 0, 3, 3, 3, 3, 0, 0, 3, 3 });

        MoldRecipeManager.INSTANCE.addRecipe(mold_pickaxe.getItem(), 5, 5,
                new int[] { 0, 2, 2, 2, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0 });

        MoldRecipeManager.INSTANCE.addRecipe(mold_axe.getItem(), 3, 5,
                new int[] { 1, 2, 2, 1, 2, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1 });

        MoldRecipeManager.INSTANCE.addRecipe(mold_shovel.getItem(), 3, 6,
                new int[] { 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0 });

        MoldRecipeManager.INSTANCE.addRecipe(mold_hoe.getItem(), 3, 5,
                new int[] { 0, 2, 2, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1 });

        MoldRecipeManager.INSTANCE.addRecipe(mold_sword.getItem(), 3, 6,
                new int[] { 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 2, 1, 0, 1, 0 });

        MoldRecipeManager.INSTANCE.addRecipe(mold_shears.getItem(), 5, 6,
                new int[] { 2, 3, 0, 3, 2, 2, 3, 0, 3, 2, 2, 3, 0, 3, 2, 2, 0, 0, 0, 2, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0 });

        // TODO: mold recipes
        if (FoundryFluidRegistry.getStrategy("iron").registerRecipes())
        {
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(Items.IRON_PICKAXE),
                    new FluidStack(liquid_iron, FoundryAPI.getAmountPickaxe()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(Items.IRON_PICKAXE),
                    new FluidStack(liquid_iron, FoundryAPI.getAmountPickaxe()), ItemMold.SubItem.PICKAXE, false, extra_sticks2);
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(Items.IRON_AXE),
                    new FluidStack(liquid_iron, FoundryAPI.getAmountAxe()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(Items.IRON_AXE),
                    new FluidStack(liquid_iron, FoundryAPI.getAmountAxe()), ItemMold.SubItem.AXE, false, extra_sticks2);
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(Items.IRON_SHOVEL),
                    new FluidStack(liquid_iron, FoundryAPI.getAmountShovel()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(Items.IRON_SHOVEL),
                    new FluidStack(liquid_iron, FoundryAPI.getAmountShovel()), ItemMold.SubItem.SHOVEL, false, extra_sticks2);
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(Items.IRON_HOE),
                    new FluidStack(liquid_iron, FoundryAPI.getAmountHoe()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(Items.IRON_HOE),
                    new FluidStack(liquid_iron, FoundryAPI.getAmountHoe()), ItemMold.SubItem.HOE, false, extra_sticks2);
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(Items.IRON_SWORD),
                    new FluidStack(liquid_iron, FoundryAPI.getAmountSword()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(Items.IRON_SWORD),
                    new FluidStack(liquid_iron, FoundryAPI.getAmountSword()), ItemMold.SubItem.SWORD, false, extra_sticks1);
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(Items.SHEARS),
                    new FluidStack(liquid_iron, FoundryAPI.getAmountShears()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(Items.SHEARS),
                    new FluidStack(liquid_iron, FoundryAPI.getAmountShears()), ItemMold.SubItem.SHEARS, false, null);
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(Items.IRON_HELMET),
                    new FluidStack(liquid_iron, FoundryAPI.getAmountHelm()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(Items.IRON_HELMET),
                    new FluidStack(liquid_iron, FoundryAPI.getAmountHelm()), ItemMold.SubItem.HELMET, false, null);
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(Items.IRON_CHESTPLATE),
                    new FluidStack(liquid_iron, FoundryAPI.getAmountChest()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(Items.IRON_CHESTPLATE),
                    new FluidStack(liquid_iron, FoundryAPI.getAmountChest()), ItemMold.SubItem.CHESTPLATE, false, null);
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(Items.IRON_LEGGINGS),
                    new FluidStack(liquid_iron, FoundryAPI.getAmountLegs()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(Items.IRON_LEGGINGS),
                    new FluidStack(liquid_iron, FoundryAPI.getAmountLegs()), ItemMold.SubItem.LEGGINGS, false, null);
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(Items.IRON_BOOTS),
                    new FluidStack(liquid_iron, FoundryAPI.getAmountBoots()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(Items.IRON_BOOTS),
                    new FluidStack(liquid_iron, FoundryAPI.getAmountBoots()), ItemMold.SubItem.BOOTS, false, null);
        }

        if (FoundryFluidRegistry.getStrategy("gold").registerRecipes())
        {
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(Items.GOLDEN_PICKAXE),
                    new FluidStack(liquid_gold, FoundryAPI.getAmountPickaxe()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(Items.GOLDEN_PICKAXE),
                    new FluidStack(liquid_gold, FoundryAPI.getAmountPickaxe()), ItemMold.SubItem.PICKAXE, false, extra_sticks2);
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(Items.GOLDEN_AXE),
                    new FluidStack(liquid_gold, FoundryAPI.getAmountAxe()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(Items.GOLDEN_AXE),
                    new FluidStack(liquid_gold, FoundryAPI.getAmountAxe()), ItemMold.SubItem.AXE, false, extra_sticks2);
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(Items.GOLDEN_SHOVEL),
                    new FluidStack(liquid_gold, FoundryAPI.getAmountShovel()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(Items.GOLDEN_SHOVEL),
                    new FluidStack(liquid_gold, FoundryAPI.getAmountShovel()), ItemMold.SubItem.SHOVEL, false, extra_sticks2);
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(Items.GOLDEN_HOE),
                    new FluidStack(liquid_gold, FoundryAPI.getAmountHoe()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(Items.GOLDEN_HOE),
                    new FluidStack(liquid_gold, FoundryAPI.getAmountHoe()), ItemMold.SubItem.HOE, false, extra_sticks2);
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(Items.GOLDEN_SWORD),
                    new FluidStack(liquid_gold, FoundryAPI.getAmountSword()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(Items.GOLDEN_SWORD),
                    new FluidStack(liquid_gold, FoundryAPI.getAmountSword()), ItemMold.SubItem.SWORD, false, extra_sticks1);
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(Items.GOLDEN_HELMET),
                    new FluidStack(liquid_gold, FoundryAPI.getAmountHelm()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(Items.GOLDEN_HELMET),
                    new FluidStack(liquid_gold, FoundryAPI.getAmountHelm()), ItemMold.SubItem.HELMET, false, null);
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(Items.GOLDEN_CHESTPLATE),
                    new FluidStack(liquid_gold, FoundryAPI.getAmountChest()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(Items.GOLDEN_CHESTPLATE),
                    new FluidStack(liquid_gold, FoundryAPI.getAmountChest()), ItemMold.SubItem.CHESTPLATE, false, null);
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(Items.GOLDEN_LEGGINGS),
                    new FluidStack(liquid_gold, FoundryAPI.getAmountLegs()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(Items.GOLDEN_LEGGINGS),
                    new FluidStack(liquid_gold, FoundryAPI.getAmountLegs()), ItemMold.SubItem.LEGGINGS, false, null);
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(Items.GOLDEN_BOOTS),
                    new FluidStack(liquid_gold, FoundryAPI.getAmountBoots()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(Items.GOLDEN_BOOTS),
                    new FluidStack(liquid_gold, FoundryAPI.getAmountBoots()), ItemMold.SubItem.BOOTS, false, null);
        }
    }
}
