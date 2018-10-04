package exter.foundry.api;

import cofh.thermalfoundation.ThermalFoundation;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.api.recipe.matcher.OreMatcher;
import exter.foundry.item.ItemMold.SubItem;
import exter.foundry.util.MiscUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public class FoundryUtils
{

    private static boolean exists(String ore)
    {
        return OreDictionary.doesOreNameExist(ore);
    }

    /**
     * Check if an item is registered in the Ore Dictionary.
     * @param name Ore name to check.
     * @param stack Item to check.
     * @return true if the item is registered, false otherwise.
     */
    static public boolean isItemInOreDictionary(String name, ItemStack stack)
    {
        for (ItemStack i : OreDictionary.getOres(name, false))
            if (OreDictionary.itemMatches(i, stack, false))
                return true;
        return false;
    }

    /**
     * Helper method for registering basic melting recipes for a given metal.
     * @param partial_name The partial ore dictionary name e.g. "Copper" for "ingotCopper","oreCopper", etc.
     * @param fluid The liquid created by the smelter.
     */
    static public void registerBasicMeltingRecipes(String partial_name, Fluid fluid)
    {
        if (FoundryAPI.MELTING_MANAGER != null)
        {
            partial_name = MiscUtil.upperCaseFirstChar(partial_name);
            if (exists("ingot" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("ingot" + partial_name),
                        new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT));

            if (exists("block" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("block" + partial_name),
                        new FluidStack(fluid, FoundryAPI.getAmountBlock()));

            if (exists("nugget" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("nugget" + partial_name),
                        new FluidStack(fluid, FoundryAPI.getAmountNugget()));

            if (exists("dust" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("dust" + partial_name),
                        new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT));

            if (exists("ore" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("ore" + partial_name),
                        new FluidStack(fluid, FoundryAPI.getAmountOre()));

            if (exists("orePoor" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("orePoor" + partial_name),
                        new FluidStack(fluid, FoundryAPI.getAmountNugget() * 2));

            if (exists("dustSmall" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("dustSmall" + partial_name),
                        new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT / 4));

            if (exists("dustTiny" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("dustTiny" + partial_name),
                        new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT / 4));

            if (exists("plate" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("plate" + partial_name),
                        new FluidStack(fluid, FoundryAPI.getAmountPlate()));

            if (exists("gear" + partial_name))
                FoundryAPI.MELTING_MANAGER.addRecipe(new OreMatcher("gear" + partial_name),
                        new FluidStack(fluid, FoundryAPI.getAmountGear()));
        }
    }

    public static void tryAddToolArmorRecipes(String name, Fluid fluid)
    {
        ItemStack helm = new ItemStack(
                ForgeRegistries.ITEMS.getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "armor.helmet_" + name)));
        ItemStack chest = new ItemStack(
                ForgeRegistries.ITEMS.getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "armor.plate_" + name)));
        ItemStack legs = new ItemStack(
                ForgeRegistries.ITEMS.getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "armor.legs_" + name)));
        ItemStack boots = new ItemStack(
                ForgeRegistries.ITEMS.getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "armor.boots_" + name)));
        ItemStack hammer = new ItemStack(
                ForgeRegistries.ITEMS.getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.hammer_" + name)));
        ItemStack shield = new ItemStack(
                ForgeRegistries.ITEMS.getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.shield_" + name)));
        ItemStack sickle = new ItemStack(
                ForgeRegistries.ITEMS.getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.sickle_" + name)));
        ItemStack shears = new ItemStack(
                ForgeRegistries.ITEMS.getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.shears_" + name)));
        ItemStack excavator = new ItemStack(
                ForgeRegistries.ITEMS.getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.excavator_" + name)));

        if (!helm.isEmpty())
        {
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(helm),
                    new FluidStack(fluid, FoundryAPI.getAmountHelm()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(helm),
                    new FluidStack(fluid, FoundryAPI.getAmountHelm()), SubItem.HELMET.getItem(), false, null);
        }

        if (!chest.isEmpty())
        {
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(chest),
                    new FluidStack(fluid, FoundryAPI.getAmountChest()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(chest),
                    new FluidStack(fluid, FoundryAPI.getAmountChest()), SubItem.CHESTPLATE.getItem(), false, null);
        }

        if (!legs.isEmpty())
        {
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(legs),
                    new FluidStack(fluid, FoundryAPI.getAmountLegs()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(legs),
                    new FluidStack(fluid, FoundryAPI.getAmountLegs()), SubItem.LEGGINGS.getItem(), false, null);
        }

        if (!boots.isEmpty())
        {
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(boots),
                    new FluidStack(fluid, FoundryAPI.getAmountBoots()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(boots),
                    new FluidStack(fluid, FoundryAPI.getAmountBoots()), SubItem.BOOTS.getItem(), false, null);
        }

        ItemStack pickaxe = new ItemStack(
                ForgeRegistries.ITEMS.getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.pickaxe_" + name)));
        ItemStack axe = new ItemStack(
                ForgeRegistries.ITEMS.getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.axe_" + name)));
        ItemStack shovel = new ItemStack(
                ForgeRegistries.ITEMS.getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.shovel_" + name)));
        ItemStack hoe = new ItemStack(
                ForgeRegistries.ITEMS.getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.hoe_" + name)));
        ItemStack sword = new ItemStack(
                ForgeRegistries.ITEMS.getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.sword_" + name)));
        OreMatcher stick = new OreMatcher("stickWood", 2);

        if (!pickaxe.isEmpty())
        {
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(pickaxe),
                    new FluidStack(fluid, FoundryAPI.getAmountPickaxe()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(pickaxe),
                    new FluidStack(fluid, FoundryAPI.getAmountPickaxe()), SubItem.PICKAXE.getItem(), false, stick);
        }

        if (!axe.isEmpty())
        {
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(axe),
                    new FluidStack(fluid, FoundryAPI.getAmountAxe()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(axe),
                    new FluidStack(fluid, FoundryAPI.getAmountAxe()), SubItem.AXE.getItem(), false, stick);
        }

        if (!shovel.isEmpty())
        {
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(shovel),
                    new FluidStack(fluid, FoundryAPI.getAmountShovel()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(shovel),
                    new FluidStack(fluid, FoundryAPI.getAmountShovel()), SubItem.SHOVEL.getItem(), false, stick);
        }

        if (!hoe.isEmpty())
        {
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(hoe),
                    new FluidStack(fluid, FoundryAPI.getAmountHoe()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(hoe),
                    new FluidStack(fluid, FoundryAPI.getAmountHoe()), SubItem.HOE.getItem(), false, stick);
        }

        if (!sword.isEmpty())
        {
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(sword),
                    new FluidStack(fluid, FoundryAPI.getAmountSword()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(sword),
                    new FluidStack(fluid, FoundryAPI.getAmountSword()), SubItem.SWORD.getItem(), false,
                    new OreMatcher("stickWood"));
        }

        if (!sickle.isEmpty())
        {
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(sickle),
                    new FluidStack(fluid, FoundryAPI.getAmountSickle()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(sickle),
                    new FluidStack(fluid, FoundryAPI.getAmountSickle()), SubItem.SICKLE.getItem(), false,
                    new OreMatcher("stickWood"));
        }

        if (!shears.isEmpty())
        {
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(shears),
                    new FluidStack(fluid, FoundryAPI.getAmountShears()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(shears),
                    new FluidStack(fluid, FoundryAPI.getAmountShears()), SubItem.SHEARS.getItem(), false, null);
        }

        if (!hammer.isEmpty())
        {
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(hammer),
                    new FluidStack(fluid, FoundryAPI.getAmountHammer()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(hammer),
                    new FluidStack(fluid, FoundryAPI.getAmountHammer()), SubItem.HAMMER.getItem(), false, stick);
        }

        if (!excavator.isEmpty())
        {
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(excavator),
                    new FluidStack(fluid, FoundryAPI.getAmountExcavator()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(excavator),
                    new FluidStack(fluid, FoundryAPI.getAmountExcavator()), SubItem.EXCAVATOR.getItem(), false, stick);
        }

        if (!shield.isEmpty())
        {
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(shield),
                    new FluidStack(fluid, FoundryAPI.getAmountSword()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(shield),
                    new FluidStack(fluid, FoundryAPI.getAmountSword()), SubItem.SHIELD.getItem(), true, null);
        }
    }
}
