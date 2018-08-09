package exter.foundry.config;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import cofh.thermalfoundation.ThermalFoundation;
import exter.foundry.Foundry;
import exter.foundry.api.FoundryAPI;
import exter.foundry.block.BlockMachine;
import exter.foundry.tileentity.TileEntityHeatable;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
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

    public static boolean hardcore_furnace_remove_ingots = false;
    public static Set<String> hardcore_furnace_keep_ingots;
    public static boolean hardcore_remove_ingot_nugget = false;
    public static boolean hardcore_remove_block_ingot = false;

    public static int default_burner_fuel_heat;
    public static int default_burner_exoflame_heat;

    public static boolean metalCasterPower = true;
    public static boolean crtError = true;

    public static String prefModID = ThermalFoundation.MOD_ID;

    public static boolean castingTableUncrafting = true;

    static public void load(File file)
    {
        config = new Configuration(file);
        config.load();
        debug = config.getBoolean("debug", "debug", false, "Enable debug logging.");
        recipe_equipment = config.getBoolean("equipment", "recipes", recipe_equipment,
                "Enable/disable casting recipes for equipment");
        recipe_glass = config.getBoolean("glass", "recipes", recipe_glass,
                "Enable/disable glass melting and casting recipes");
        recipe_steel = config.getBoolean("steel", "recipes", recipe_steel, "Enable/disable steel infuser recipes");
        recipe_alumina_melts_to_aluminium = config.getBoolean("alumina_melts_to_aluminium", "recipes", false,
                "Enable/disable alumina melting directly into aluminium.");

        config.addCustomCategoryComment("hardcore",
                "These settings increase the game harder by altering vanilla recipes.");
        hardcore_furnace_remove_ingots = config.getBoolean("remove_ingots_from_furnace.enable", "hardcore", false,
                "Remove furnace recipes that produce ingots.");

        hardcore_remove_ingot_nugget = config.getBoolean("remove_ingot_from_nuggets", "hardcore", false,
                "Remove 9 nuggets to ingot crafting recipes.");
        hardcore_remove_block_ingot = config.getBoolean("remove_block_from_ingots", "hardcore", false,
                "Remove 9 ingots to block crafting recipes.");

        String[] keep_ingots = config.getStringList("remove_ingots_from_furnace.keep", "hardcore",
                new String[] { "Copper", "Tin", "Zinc", "Bronze", "Brass", "Lead" },
                "Material names of ingots to keep furnace recipes (case sensitive).");

        hardcore_furnace_keep_ingots = new HashSet<>();
        for (String name : keep_ingots)
        {
            hardcore_furnace_keep_ingots.add("ingot" + name);
        }

        prefModID = config.getString("Preferred Mod ID", "recipes", ThermalFoundation.MOD_ID,
                "The priority MODID for Foundry recipes to try using.");

        metalCasterPower = config.getBoolean("Metal Caster Power", "general", true,
                "If the Metal Caster requires power to operate.");
        if (!metalCasterPower)
            BlockMachine.EnumMachine.CASTER.setTooltip("caster2");
        default_burner_fuel_heat = config.getInt("default_burner_fuel_heat", "general", 1000,
                TileEntityHeatable.TEMP_MIN, Integer.MAX_VALUE, "default_burner_fuel_heat");
        default_burner_exoflame_heat = config.getInt("default_burner_exoflame_heat", "general",
                default_burner_fuel_heat * 3, TileEntityHeatable.TEMP_MIN, Integer.MAX_VALUE,
                "default_burner_exoflame_heat");

        crtError = config.getBoolean("CrT Errors", "general", true,
                "If foundry's CraftTweaker integration logs errors instead of info");

        castingTableUncrafting = config.getBoolean("Casting Table Uncrafting", "general", true,
                "Casting Table Uncrafting");

        FoundryAPI.FLUID_AMOUNT_INGOT = config.getInt("Fluid Ingot Value", "general", FoundryAPI.FLUID_AMOUNT_INGOT, 36,
                Integer.MAX_VALUE, "The value, in mB, of an ingot.");
        FoundryAPI.FLUID_AMOUNT_ORE = config.getInt("Fluid Ore Value", "general", FoundryAPI.FLUID_AMOUNT_ORE, 0,
                Integer.MAX_VALUE, "The value, in mB, of an ore.  Set to 0 to use default values.");
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
