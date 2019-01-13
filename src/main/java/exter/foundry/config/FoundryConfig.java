package exter.foundry.config;

import java.io.File;
import java.util.*;

import exter.foundry.Foundry;
import exter.foundry.api.FoundryAPI;
import exter.foundry.block.BlockMachine;
import exter.foundry.tileentity.TileEntityHeatable;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class FoundryConfig
{
    public static Configuration config;

    public static boolean debug = false;

    public static boolean recipe_steel = true;
    public static boolean recipe_equipment = true;
    public static boolean recipe_glass = true;
    public static boolean recipe_alumina_melts_to_aluminium = true;
    public static boolean register_tf_tool_recipes_anyway = false;

    public static boolean hardcore_furnace_remove_ingots = false;
    public static Set<String> hardcore_furnace_keep_ingots;
    public static boolean hardcore_remove_ingot_nugget = false;
    public static boolean hardcore_remove_block_ingot = false;

    public static int default_burner_fuel_heat;
    public static int default_burner_exoflame_heat;

    public static boolean metalCasterPower = true;
    public static boolean crtError = true;

    // Mirrored default settings for Unidict for maximum compatibility
    public static String[] modPriority;

    public static boolean castingTableUncrafting = true;

    public static int castingTick;

    static public void load(File file)
    {
        config = new Configuration(file);
        config.load();

        debug = config.getBoolean("debug", "debug", false, "Enable debug logging.");
        recipe_equipment = config.getBoolean("equipment", "recipes", recipe_equipment, "Enable/disable casting recipes for equipment");
        recipe_glass = config.getBoolean("glass", "recipes", recipe_glass, "Enable/disable glass melting and casting recipes");
        recipe_steel = config.getBoolean("steel", "recipes", recipe_steel, "Enable/disable steel infuser recipes");
        recipe_alumina_melts_to_aluminium = config.getBoolean("alumina_melts_to_aluminium", "recipes", false, "Enable/disable alumina melting directly into aluminium.");
        register_tf_tool_recipes_anyway = config.getBoolean("register_tf_tool_recipes_anyway", "recipes", false, "Register recipes of tools from Thermal Foundation anyway or not.");

        config.addCustomCategoryComment("hardcore", "These settings increase the game harder by altering vanilla recipes.");
        hardcore_furnace_remove_ingots = config.getBoolean("remove_ingots_from_furnace.enable", "hardcore", false, "Remove furnace recipes that produce ingots.");

        hardcore_remove_ingot_nugget = config.getBoolean("remove_ingot_from_nuggets", "hardcore", false, "Remove 9 nuggets to ingot crafting recipes.");
        hardcore_remove_block_ingot = config.getBoolean("remove_block_from_ingots", "hardcore", false, "Remove 9 ingots to block crafting recipes.");

        String[] keep_ingots = config.getStringList("remove_ingots_from_furnace.keep", "hardcore", new String[] { "Copper", "Tin", "Zinc", "Bronze", "Brass", "Lead" }, "Material names of ingots to keep furnace recipes (case sensitive).");

        hardcore_furnace_keep_ingots = new HashSet<>();
        for (String name : keep_ingots)
        {
            hardcore_furnace_keep_ingots.add("ingot" + name);
        }

        modPriority = config.getStringList("Preferred Mod ID Priority", "recipes", new String[] { "minecraft", "thermalfoundation", "substratum", "ic2", "mekanism", "immersiveengineering", "techreborn" }, "The priority sorted MODIDs for Foundry recipes to try using.");

        metalCasterPower = config.getBoolean("Metal Caster Power", "general", true, "If the Metal Caster requires power to operate.");
        if (!metalCasterPower)
            BlockMachine.EnumMachine.CASTER.setTooltip("caster2");
        default_burner_fuel_heat = config.getInt("default_burner_fuel_heat", "general", 100000, TileEntityHeatable.TEMP_MIN, Integer.MAX_VALUE, "default_burner_fuel_heat (in 1/100 deg Ks)");
        default_burner_exoflame_heat = config.getInt("default_burner_exoflame_heat", "general", default_burner_fuel_heat * 3, TileEntityHeatable.TEMP_MIN, Integer.MAX_VALUE, "default_burner_exoflame_heat (in 1/100 deg Ks)");

        crtError = config.getBoolean("CrT Errors", "general", true, "If foundry's CraftTweaker integration logs errors instead of info");

        castingTableUncrafting = config.getBoolean("Casting Table Uncrafting", "general", true, "Casting Table Uncrafting");
        castingTick = config.getInt("Casting Tick", "general", 81, 1, 20000, "Casting Tick per Ingot");

        FoundryAPI.FLUID_AMOUNT_INGOT = config.getInt("Fluid Ingot Value", "general", FoundryAPI.FLUID_AMOUNT_INGOT, 36, Integer.MAX_VALUE, "The value, in mB, of an ingot.");
        FoundryAPI.FLUID_AMOUNT_ORE = config.getInt("Fluid Ore Value", "general", FoundryAPI.FLUID_AMOUNT_ORE, 1, Integer.MAX_VALUE, "The value, in mB, of an ore.");

        FoundryAPI.CRUCIBLE_BASIC_MAX_TEMP = config.getInt("Crucible (Basic) Max Temperature", "general", FoundryAPI.CRUCIBLE_BASIC_MAX_TEMP, 0, Integer.MAX_VALUE, "Max temperatures for basic crucibles (in 1/100 deg Ks).");
        FoundryAPI.CRUCIBLE_STANDARD_MAX_TEMP = config.getInt("Crucible (Standard) Max Temperature", "general", FoundryAPI.CRUCIBLE_STANDARD_MAX_TEMP, 0, Integer.MAX_VALUE, "Max temperatures for standard crucibles (in 1/100 deg Ks).");
        FoundryAPI.CRUCIBLE_ADVANCED_MAX_TEMP = config.getInt("Crucible (Advanced) Max Temperature", "general", FoundryAPI.CRUCIBLE_ADVANCED_MAX_TEMP, 0, Integer.MAX_VALUE, "Max temperatures for advanced crucibles (in 1/100 deg Ks).");
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(Foundry.MODID))
        {
            config.save();
        }
    }
}
