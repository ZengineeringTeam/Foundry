package exter.foundry.api;

import cofh.core.util.helpers.StringHelper;
import cofh.thermalfoundation.ThermalFoundation;
import cofh.thermalfoundation.init.TFProps;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.api.recipe.matcher.OreMatcher;
import exter.foundry.item.ItemMold.SubItem;
import exter.foundry.util.MiscUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
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

    public static void tryAddToolArmorRecipes(Configuration cfg, String name, Fluid fluid)
    {
        if (fluid == null)
        {
            return;
        }
        final String tools = "Equipment.Tools." + StringHelper.titleCase(name);
        final String armor = "Equipment.Armor." + StringHelper.titleCase(name);
        OreMatcher stick = new OreMatcher("stickWood", 2);

        if (Loader.isModLoaded("thermalfoundation")) {
            if (!TFProps.disableAllArmor) {
                Item helm = ForgeRegistries.ITEMS
                        .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "armor.helmet_" + name));
                if (helm != null && cfg.get(armor, "Helmet", true).getBoolean(true)) {
                    FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(helm),
                            new FluidStack(fluid, FoundryAPI.getAmountHelm()));
                    FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(helm),
                            new FluidStack(fluid, FoundryAPI.getAmountHelm()), SubItem.HELMET.getItem(), false, null);
                }

                Item chest = ForgeRegistries.ITEMS
                        .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "armor.plate_" + name));
                if (chest != null && cfg.get(armor, "Chestplate", true).getBoolean(true)) {
                    FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(chest),
                            new FluidStack(fluid, FoundryAPI.getAmountChest()));
                    FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(chest),
                            new FluidStack(fluid, FoundryAPI.getAmountChest()), SubItem.CHESTPLATE.getItem(), false, null);
                }

                Item legs = ForgeRegistries.ITEMS
                        .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "armor.legs_" + name));
                if (legs != null && cfg.get(armor, "Leggings", true).getBoolean(true)) {
                    FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(legs),
                            new FluidStack(fluid, FoundryAPI.getAmountLegs()));
                    FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(legs),
                            new FluidStack(fluid, FoundryAPI.getAmountLegs()), SubItem.LEGGINGS.getItem(), false, null);
                }

                Item boots = ForgeRegistries.ITEMS
                        .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "armor.boots_" + name));
                if (boots != null && cfg.get(armor, "Boots", true).getBoolean(true)) {
                    FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(boots),
                            new FluidStack(fluid, FoundryAPI.getAmountBoots()));
                    FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(boots),
                            new FluidStack(fluid, FoundryAPI.getAmountBoots()), SubItem.BOOTS.getItem(), false, null);
                }
            }

            if (!TFProps.disableAllTools) {
                if (!TFProps.disableAllShears) {
                    Item shears = ForgeRegistries.ITEMS
                            .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.shears_" + name));
                    if (shears != null && cfg.get(tools, "Shears", true).getBoolean(true)) {
                        FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(shears),
                                new FluidStack(fluid, FoundryAPI.getAmountShears()));
                        FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(shears),
                                new FluidStack(fluid, FoundryAPI.getAmountShears()), SubItem.SHEARS.getItem(), false, null);
                    }
                }

                if (!TFProps.disableAllShields) {
                    Item shield = ForgeRegistries.ITEMS
                            .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.shield_" + name));
                    if (shield != null && cfg.get(tools, "Shield", true).getBoolean(true)) {
                        FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(shield),
                                new FluidStack(fluid, FoundryAPI.getAmountSword()));
                        FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(shield),
                                new FluidStack(fluid, FoundryAPI.getAmountSword()), SubItem.SHIELD.getItem(), true, null);
                    }
                }

                Item pickaxe = ForgeRegistries.ITEMS
                        .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.pickaxe_" + name));
                if (pickaxe != null && cfg.get(tools, "Pickaxe", true).getBoolean(true)) {
                    FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(pickaxe),
                            new FluidStack(fluid, FoundryAPI.getAmountPickaxe()));
                    FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(pickaxe),
                            new FluidStack(fluid, FoundryAPI.getAmountPickaxe()), SubItem.PICKAXE.getItem(), false, stick);
                }

                Item axe = ForgeRegistries.ITEMS
                        .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.axe_" + name));
                if (axe != null && cfg.get(tools, "Axe", true).getBoolean(true)) {
                    FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(axe),
                            new FluidStack(fluid, FoundryAPI.getAmountAxe()));
                    FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(axe),
                            new FluidStack(fluid, FoundryAPI.getAmountAxe()), SubItem.AXE.getItem(), false, stick);
                }
                Item shovel = ForgeRegistries.ITEMS
                        .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.shovel_" + name));
                if (shovel != null && cfg.get(tools, "Shovel", true).getBoolean(true)) {
                    FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(shovel),
                            new FluidStack(fluid, FoundryAPI.getAmountShovel()));
                    FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(shovel),
                            new FluidStack(fluid, FoundryAPI.getAmountShovel()), SubItem.SHOVEL.getItem(), false, stick);
                }

                Item hoe = ForgeRegistries.ITEMS
                        .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.hoe_" + name));
                if (hoe != null && cfg.get(tools, "Hoe", true).getBoolean(true)) {
                    FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(hoe),
                            new FluidStack(fluid, FoundryAPI.getAmountHoe()));
                    FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(hoe),
                            new FluidStack(fluid, FoundryAPI.getAmountHoe()), SubItem.HOE.getItem(), false, stick);
                }
                Item sword = ForgeRegistries.ITEMS
                        .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.sword_" + name));
                if (sword != null && cfg.get(tools, "Sword", true).getBoolean(true)) {
                    FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(sword),
                            new FluidStack(fluid, FoundryAPI.getAmountSword()));
                    FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(sword),
                            new FluidStack(fluid, FoundryAPI.getAmountSword()), SubItem.SWORD.getItem(), false,
                            new OreMatcher("stickWood"));
                }

                Item sickle = ForgeRegistries.ITEMS
                        .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.sickle_" + name));
                if (sickle != null && cfg.get(tools, "Sickle", true).getBoolean(true)) {
                    FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(sickle),
                            new FluidStack(fluid, FoundryAPI.getAmountSickle()));
                    FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(sickle),
                            new FluidStack(fluid, FoundryAPI.getAmountSickle()), SubItem.SICKLE.getItem(), false,
                            new OreMatcher("stickWood"));
                }

                Item hammer = ForgeRegistries.ITEMS
                        .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.hammer_" + name));
                if (hammer != null && cfg.get(tools, "Hammer", true).getBoolean(true)) {
                    FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(hammer),
                            new FluidStack(fluid, FoundryAPI.getAmountHammer()));
                    FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(hammer),
                            new FluidStack(fluid, FoundryAPI.getAmountHammer()), SubItem.HAMMER.getItem(), false, stick);
                }

                Item excavator = ForgeRegistries.ITEMS
                        .getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.excavator_" + name));
                if (excavator != null && cfg.get(tools, "Excavator", true).getBoolean(true)) {
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
