package exter.foundry.fluid;

import exter.foundry.config.FoundryConfig;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraftforge.fluids.Fluid;

public class FoundryFluids
{
    static public Fluid liquid_iron;
    static public Fluid liquid_gold;
    static public Fluid liquid_copper;
    static public Fluid liquid_tin;
    static public Fluid liquid_bronze;
    static public Fluid liquid_electrum;
    static public Fluid liquid_invar;
    static public Fluid liquid_nickel;
    static public Fluid liquid_zinc;
    static public Fluid liquid_brass;
    static public Fluid liquid_silver;
    static public Fluid liquid_steel;
    static public Fluid liquid_constantan;
    static public Fluid liquid_lead;
    static public Fluid liquid_platinum;
    static public Fluid liquid_aluminium;
    static public Fluid liquid_alumina;
    static public Fluid liquid_chromium;
    static public Fluid liquid_signalum;
    static public Fluid liquid_lumium;
    static public Fluid liquid_enderium;
    public static Fluid liquid_osmium;
    static public Fluid liquid_glass;
    static public Fluid[] liquid_glass_colored = new Fluid[16];

    static public void init()
    {
        liquid_iron = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("iron", 1800, 15);
        liquid_gold = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("gold", 1350, 15);
        liquid_copper = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("copper", 1300, 15);
        liquid_tin = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("tin", 550, 0);
        liquid_bronze = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("bronze", 1200, 15);
        liquid_electrum = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("electrum", 1350, 15);
        liquid_invar = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("invar", 1780, 15);
        liquid_nickel = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("nickel", 1750, 15);
        liquid_zinc = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("zinc", 700, 0);
        liquid_brass = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("brass", 1200, 15);
        liquid_silver = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("silver", 1250, 15);
        liquid_steel = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("steel", 1800, 15);
        liquid_constantan = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("constantan", 1750, 15);
        liquid_lead = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("lead", 650, 0);
        liquid_platinum = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("platinum", 2100, 15);
        liquid_aluminium = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("aluminium", 900, 0);
        //liquid_chromium = LiquidMetalRegistry.instance.registerLiquidMetal("chrome", 2200, 10);
        liquid_signalum = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("signalum", 2800, 12);
        liquid_lumium = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("lumium", 2500, 15);
        liquid_enderium = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("enderium", 3800, 12);
        liquid_osmium = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("osmium", 3300, 15);
        if (!FoundryConfig.recipe_alumina_melts_to_aluminium)
        {
            liquid_alumina = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("alumina", 2100, 12);
        }

        /*
        LiquidMetalRegistry.instance.registerLiquidMetal("Manganese", 1550, 15);
        LiquidMetalRegistry.instance.registerLiquidMetal("Titanium", 2000, 15);
        LiquidMetalRegistry.instance.registerLiquidMetal("Rubber", 460, 0);
        LiquidMetalRegistry.instance.registerLiquidMetal("StainlessSteel", 1900, 15);
        LiquidMetalRegistry.instance.registerLiquidMetal("Kanthal", 1900, 15);
        LiquidMetalRegistry.instance.registerLiquidMetal("Nichrome", 1950, 15);
         */

        int temp = 1550;
        liquid_glass = LiquidMetalRegistry.INSTANCE.registerSpecialLiquidMetal("glass", temp, 12, "glass",
                0x40FFFFFF, Blocks.GLASS.getDefaultState());
        IBlockState stained_glass = Blocks.STAINED_GLASS.getDefaultState();
        for (EnumDyeColor dye : EnumDyeColor.values())
        {
            String name = dye.getName();

            int color = ItemDye.DYE_COLORS[dye.getDyeDamage()];
            int c1 = 63 + (color & 0xFF) * 3 / 4;
            int c2 = 63 + (color >> 8 & 0xFF) * 3 / 4;
            int c3 = 63 + (color >> 16 & 0xFF) * 3 / 4;
            int fluid_color = c1 | c2 << 8 | c3 << 16 | 100 << 24;

            int meta = dye.getMetadata();

            liquid_glass_colored[meta] = LiquidMetalRegistry.INSTANCE.registerSpecialLiquidMetal("glass" + name, temp,
                    12, "glass", fluid_color, stained_glass.withProperty(BlockColored.COLOR, dye));
        }
    }
}
