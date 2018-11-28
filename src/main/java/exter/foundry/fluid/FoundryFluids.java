package exter.foundry.fluid;

import exter.foundry.config.FoundryConfig;
import exter.foundry.config.MetalConfig;
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
        if (MetalConfig.metals.get("iron") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_iron = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "iron", 1540, 10, 0xa81212);

        if (MetalConfig.metals.get("gold") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_gold = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "gold", 1350, 15, 0xf6d609);

        if (MetalConfig.metals.get("copper") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_copper = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "copper", 1300, 15, 0xed9f07);

        if (MetalConfig.metals.get("tin") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_tin = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "tin", 550, 0, 0xc1cddc);

        if (MetalConfig.metals.get("bronze") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_bronze = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "bronze", 1200, 15, 0xe3bd68);

        if (MetalConfig.metals.get("electrum") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_electrum = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "electrum", 1350, 15, 0xe8db49);

        if (MetalConfig.metals.get("invar") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_invar = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "invar", 1780, 15, 0x7F907F);

        if (MetalConfig.metals.get("nickel") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_nickel = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "nickel", 1750, 15, 0xc8d683);

        if (MetalConfig.metals.get("zinc") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_zinc = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "zinc", 700, 0, 0xd3efe8);

        if (MetalConfig.metals.get("brass") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_brass = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "brass", 1200, 15, 0xede38b);

        if (MetalConfig.metals.get("silver") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_silver = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "silver", 1250, 15, 0xd1ecf6);

        if (MetalConfig.metals.get("steel") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_steel = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "steel", 1800, 15, 0xa7a7a7);

        if (MetalConfig.metals.get("constantan") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_constantan = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "constantan", 1280, 15,
                0xf7866c);

        if (MetalConfig.metals.get("uranium") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_uranium = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "uranium", 1150, 15, 0x596552);

        if (MetalConfig.metals.get("lead") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_lead = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "lead", 650, 0, 0x4d4968);

        if (MetalConfig.metals.get("platinum") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_platinum = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "platinum", 2100, 15, 0xB7E7FF);

        if (MetalConfig.metals.get("aluminium") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_aluminium = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "aluminium", 900, 0, 0xefe0d5);

        if (MetalConfig.metals.get("cobalt") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_cobalt = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "cobalt", 1500, 8, 0x2882d4);

        if (MetalConfig.metals.get("iridium") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_iridium = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "iridium", 2450, 12, 0xBABADA);

        if (MetalConfig.metals.get("signalum") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_signalum = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "signalum", 2800, 15, 0xD84100);

        if (MetalConfig.metals.get("lumium") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_lumium = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "lumium", 2500, 15, 0xFFFF7F);

        if (MetalConfig.metals.get("enderium") != MetalConfig.IntegrationStrategy.DISABLED)
            liquid_enderium = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "enderium", 3800, 12, 0x007068);

        if (!FoundryConfig.recipe_alumina_melts_to_aluminium && MetalConfig.metals.get("alumina") != MetalConfig.IntegrationStrategy.DISABLED)
        {
            liquid_alumina = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "alumina", 2100, 12, 0xFFFFFF);
        }

        /*
        LiquidMetalRegistry.instance.registerLiquidMetal(registry, "Manganese", 1550, 15);
        LiquidMetalRegistry.instance.registerLiquidMetal(registry, "Rubber", 460, 0);
        LiquidMetalRegistry.instance.registerLiquidMetal(registry, "StainlessSteel", 1900, 15);
        LiquidMetalRegistry.instance.registerLiquidMetal(registry, "Kanthal", 1900, 15);
        LiquidMetalRegistry.instance.registerLiquidMetal(registry, "Nichrome", 1950, 15);
         */

        int temp = 1550;
        liquid_glass = FoundryFluidRegistry.INSTANCE.registerLiquidGlass(registry, "glass", temp, 12, "glass",
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

            stained_glass = stained_glass.withProperty(BlockColored.COLOR, dye);
            liquid_glass_colored[meta] = FoundryFluidRegistry.INSTANCE.registerLiquidGlass(registry, "glass_" + name,
                    temp, 12, "glass", fluid_color, stained_glass);
        }
    }
}
