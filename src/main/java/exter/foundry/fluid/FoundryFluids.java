package exter.foundry.fluid;

import exter.foundry.config.FoundryConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber
public class FoundryFluids
{
    public static Fluid liquid_iron;
    public static Fluid liquid_gold;
    public static Fluid liquid_copper;
    public static Fluid liquid_tin;
    public static Fluid liquid_bronze;
    public static Fluid liquid_electrum;
    public static Fluid liquid_invar;
    public static Fluid liquid_nickel;
    public static Fluid liquid_zinc;
    public static Fluid liquid_brass;
    public static Fluid liquid_silver;
    public static Fluid liquid_steel;
    public static Fluid liquid_constantan;
    public static Fluid liquid_lead;
    public static Fluid liquid_platinum;
    public static Fluid liquid_aluminium;
    public static Fluid liquid_alumina;
    public static Fluid liquid_signalum;
    public static Fluid liquid_lumium;
    public static Fluid liquid_enderium;
    public static Fluid liquid_uranium;
    public static Fluid liquid_cobalt;
    public static Fluid liquid_iridium;
    public static Fluid liquid_glass;
    public static Fluid[] liquid_glass_colored = new Fluid[16];

    @SubscribeEvent
    public static void registerFluids(Register<Block> event)
    {
        IForgeRegistry<Block> registry = event.getRegistry();
        String category = "Base Metals";
        liquid_iron = FoundryFluidRegistry.registerLiquidMetal(registry, "iron", category, 1540, 10, 0xa81212);
        liquid_gold = FoundryFluidRegistry.registerLiquidMetal(registry, "gold", category, 1350, 15, 0xf6d609);
        liquid_copper = FoundryFluidRegistry.registerLiquidMetal(registry, "copper", category, 1300, 15, 0xed9f07);
        liquid_tin = FoundryFluidRegistry.registerLiquidMetal(registry, "tin", category, 550, 0, 0xc1cddc);
        liquid_bronze = FoundryFluidRegistry.registerLiquidMetal(registry, "bronze", category, 1200, 15, 0xe3bd68);
        liquid_electrum = FoundryFluidRegistry.registerLiquidMetal(registry, "electrum", category, 1350, 15, 0xe8db49);
        liquid_invar = FoundryFluidRegistry.registerLiquidMetal(registry, "invar", category, 1780, 15, 0x7F907F);
        liquid_nickel = FoundryFluidRegistry.registerLiquidMetal(registry, "nickel", category, 1750, 15, 0xc8d683);
        liquid_zinc = FoundryFluidRegistry.registerLiquidMetal(registry, "zinc", category, 700, 0, 0xd3efe8);
        liquid_brass = FoundryFluidRegistry.registerLiquidMetal(registry, "brass", category, 1200, 15, 0xede38b);
        liquid_silver = FoundryFluidRegistry.registerLiquidMetal(registry, "silver", category, 1250, 15, 0xd1ecf6);
        liquid_steel = FoundryFluidRegistry.registerLiquidMetal(registry, "steel", category, 1800, 15, 0xa7a7a7);
        liquid_constantan = FoundryFluidRegistry.registerLiquidMetal(registry, "constantan", category, 1280, 15, 0xf7866c);
        liquid_uranium = FoundryFluidRegistry.registerLiquidMetal(registry, "uranium", category, 1150, 15, 0x596552);
        liquid_lead = FoundryFluidRegistry.registerLiquidMetal(registry, "lead", category, 650, 0, 0x4d4968);
        liquid_platinum = FoundryFluidRegistry.registerLiquidMetal(registry, "platinum", category, 2100, 15, 0xB7E7FF);
        liquid_aluminium = FoundryFluidRegistry.registerLiquidMetal(registry, "aluminium", category, 900, 0, 0xefe0d5);
        liquid_cobalt = FoundryFluidRegistry.registerLiquidMetal(registry, "cobalt", category, 1500, 8, 0x2882d4);
        liquid_iridium = FoundryFluidRegistry.registerLiquidMetal(registry, "iridium", category, 2450, 12, 0xBABADA);
        liquid_signalum = FoundryFluidRegistry.registerLiquidMetal(registry, "signalum", category, 2800, 15, 0xD84100);
        liquid_lumium = FoundryFluidRegistry.registerLiquidMetal(registry, "lumium", category, 2500, 15, 0xFFFF7F);
        liquid_enderium = FoundryFluidRegistry.registerLiquidMetal(registry, "enderium", category, 3800, 12, 0x007068);

        if (!FoundryConfig.recipe_alumina_melts_to_aluminium)
        {
            liquid_alumina = FoundryFluidRegistry.registerLiquidMetal(registry, "alumina", category, 2100, 12, 0xFFFFFF);
        }

        /*
        LiquidMetalRegistry.registerLiquidMetal(registry, "Manganese", 1550, 15);
        LiquidMetalRegistry.registerLiquidMetal(registry, "Rubber", 460, 0);
        LiquidMetalRegistry.registerLiquidMetal(registry, "StainlessSteel", 1900, 15);
        LiquidMetalRegistry.registerLiquidMetal(registry, "Kanthal", 1900, 15);
        LiquidMetalRegistry.registerLiquidMetal(registry, "Nichrome", 1950, 15);
         */

        category = "glass";
        int temp = 1550;
        liquid_glass = FoundryFluidRegistry.registerLiquidGlass(registry, "glass", category, temp, 12, "glass", 0x40FFFFFF,
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

            stained_glass = stained_glass.withProperty(BlockColored.COLOR, dye);
            liquid_glass_colored[meta] = FoundryFluidRegistry.registerLiquidGlass(registry, "glass_" + name, category, temp,
                    12, "glass", fluid_color, stained_glass);
        }
    }
}
