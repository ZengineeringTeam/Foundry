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
        liquid_iron = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("iron", 1540, 10, 0xa81212);
        liquid_iron = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("refined_iron", 2000, 10, 0xa81212); // TODO
        liquid_gold = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("gold", 1350, 15, 0xf6d609);
        liquid_copper = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("copper", 1300, 15, 0xed9f07);
        liquid_tin = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("tin", 550, 0, 0xc1cddc);
        liquid_bronze = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("bronze", 1200, 15, 0xe3bd68);
        liquid_electrum = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("electrum", 1350, 15, 0xe8db49);
        liquid_invar = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("invar", 1780, 15, 0x7F907F);
        liquid_nickel = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("nickel", 1750, 15, 0xc8d683);
        liquid_zinc = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("zinc", 700, 0, 0xd3efe8);
        liquid_brass = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("brass", 1200, 15, 0xede38b);
        liquid_silver = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("silver", 1250, 15, 0xd1ecf6);
        liquid_steel = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("steel", 1800, 15, 0xa7a7a7);
        liquid_constantan = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("constantan", 1280, 15, 0xf7866c);
        liquid_constantan = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("uranium", 1150, 15, 0x596552);
        liquid_lead = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("lead", 650, 0, 0x4d4968);
        liquid_platinum = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("platinum", 2100, 15, 0x34BFBF);
        liquid_aluminium = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("aluminium", 900, 0, 0xefe0d5);
        liquid_chromium = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("chrome", 2200, 10, 0x90C9C9);
        liquid_chromium = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("cobalt", 1500, 8, 0x2882d4);
        liquid_chromium = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("titanium", 1700, 8, 0x3C372F);
        liquid_chromium = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("iridium", 2450, 12, 0xFDFDFD);
        liquid_signalum = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("signalum", 2800, 15, 0xFFFFFF);
        liquid_lumium = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("lumium", 2500, 15, 0xFFFFFF);
        liquid_enderium = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("enderium", 3800, 12, 0xFFFFFF);
        liquid_osmium = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("osmium", 3300, 15, 0xFFFFFF);
        if (!FoundryConfig.recipe_alumina_melts_to_aluminium)
        {
            liquid_alumina = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("alumina", 2100, 12, 0xFFFFFF);
        }

        /*
        LiquidMetalRegistry.instance.registerLiquidMetal("Manganese", 1550, 15);
        LiquidMetalRegistry.instance.registerLiquidMetal("Rubber", 460, 0);
        LiquidMetalRegistry.instance.registerLiquidMetal("StainlessSteel", 1900, 15);
        LiquidMetalRegistry.instance.registerLiquidMetal("Kanthal", 1900, 15);
        LiquidMetalRegistry.instance.registerLiquidMetal("Nichrome", 1950, 15);
         */

        int temp = 1550;
        liquid_glass = LiquidMetalRegistry.INSTANCE.registerSpecialLiquidMetal("glass", temp, 12, "glass", 0x40FFFFFF,
                Blocks.GLASS.getDefaultState());
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

            liquid_glass_colored[meta] = LiquidMetalRegistry.INSTANCE.registerSpecialLiquidMetal("glass_" + name, temp,
                    12, "glass", fluid_color, stained_glass.withProperty(BlockColored.COLOR, dye));
        }
    }
}
