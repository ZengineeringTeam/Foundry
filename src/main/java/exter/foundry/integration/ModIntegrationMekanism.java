package exter.foundry.integration;

import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.matcher.OreMatcher;
import exter.foundry.config.FoundryConfig;
import exter.foundry.fluid.FluidLiquidMetal;
import exter.foundry.fluid.FoundryFluidRegistry;
import exter.foundry.fluid.FoundryFluids;
import exter.foundry.item.ItemMold;
import exter.foundry.util.MiscUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class ModIntegrationMekanism implements IModIntegration
{

    public static final String MEKANISM = "mekanism";
    public static final String MEKANISMTOOLS = "mekanismtools";

    private FluidLiquidMetal liquid_osmium;
    private FluidLiquidMetal liquid_refined_obsidian;
    private FluidLiquidMetal liquid_refined_glowstone;

    private ItemStack getItemStack(String name)
    {
        return getItemStack(name, 0);
    }

    private ItemStack getItemStack(String name, int meta)
    {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(MEKANISMTOOLS, name));
        if (item == null)
        {
            return ItemStack.EMPTY;
        }
        return new ItemStack(item, 1, meta);
    }

    @Override
    public String getName()
    {
        return "Mekanism";
    }

    @Override
    public void onAfterPostInit()
    {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onClientInit()
    {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onClientPostInit()
    {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onClientPreInit()
    {

    }

    @Override
    public void onInit()
    {

    }

    @Override
    public void onPostInit()
    {
        if (!Loader.isModLoaded(MEKANISM))
        {
            return;
        }

        if (FoundryConfig.recipe_equipment && Loader.isModLoaded(MEKANISMTOOLS))
        {
            ItemStack osmium_pickaxe = getItemStack("osmiumpickaxe");
            ItemStack osmium_axe = getItemStack("osmiumaxe");
            ItemStack osmium_shovel = getItemStack("osmiumshovel");
            ItemStack osmium_hoe = getItemStack("osmiumhoe");
            ItemStack osmium_sword = getItemStack("osmiumsword");
            ItemStack osmium_helmet = getItemStack("osmiumhelmet");
            ItemStack osmium_chestplate = getItemStack("osmiumchestplate");
            ItemStack osmium_leggings = getItemStack("osmiumleggings");
            ItemStack osmium_boots = getItemStack("osmiumboots");

            ItemStack refined_obsidian_pickaxe = getItemStack("obsidianpickaxe");
            ItemStack refined_obsidian_axe = getItemStack("obsidianaxe");
            ItemStack refined_obsidian_shovel = getItemStack("obsidianshovel");
            ItemStack refined_obsidian_hoe = getItemStack("obsidianhoe");
            ItemStack refined_obsidian_sword = getItemStack("obsidiansword");
            ItemStack refined_obsidian_helmet = getItemStack("obsidianhelmet");
            ItemStack refined_obsidian_chestplate = getItemStack("obsidianchestplate");
            ItemStack refined_obsidian_leggings = getItemStack("obsidianleggings");
            ItemStack refined_obsidian_boots = getItemStack("obsidianboots");

            ItemStack refined_glowstone_pickaxe = getItemStack("glowstonepickaxe");
            ItemStack refined_glowstone_axe = getItemStack("glowstoneaxe");
            ItemStack refined_glowstone_shovel = getItemStack("glowstoneshovel");
            ItemStack refined_glowstone_hoe = getItemStack("glowstonehoe");
            ItemStack refined_glowstone_sword = getItemStack("glowstonesword");
            ItemStack refined_glowstone_helmet = getItemStack("glowstonehelmet");
            ItemStack refined_glowstone_chestplate = getItemStack("glowstonechestplate");
            ItemStack refined_glowstone_leggings = getItemStack("glowstoneleggings");
            ItemStack refined_glowstone_boots = getItemStack("glowstoneboots");

            ItemStack bronze_pickaxe = getItemStack("bronzepickaxe");
            ItemStack bronze_axe = getItemStack("bronzeaxe");
            ItemStack bronze_shovel = getItemStack("bronzeshovel");
            ItemStack bronze_hoe = getItemStack("bronzehoe");
            ItemStack bronze_sword = getItemStack("bronzesword");
            ItemStack bronze_helmet = getItemStack("bronzehelmet");
            ItemStack bronze_chestplate = getItemStack("bronzechestplate");
            ItemStack bronze_leggings = getItemStack("bronzeleggings");
            ItemStack bronze_boots = getItemStack("bronzeboots");

            OreMatcher extra_sticks1 = new OreMatcher("stickWood", 1);
            OreMatcher extra_sticks2 = new OreMatcher("stickWood", 2);
            
            // TODO: melting recipes

            if (FoundryFluidRegistry.getStrategy("osmium").registerRecipes())
            {
                MiscUtil.registerCasting(osmium_pickaxe, liquid_osmium, 3, ItemMold.SubItem.PICKAXE, extra_sticks2);
                MiscUtil.registerCasting(osmium_axe, liquid_osmium, 3, ItemMold.SubItem.AXE, extra_sticks2);
                MiscUtil.registerCasting(osmium_shovel, liquid_osmium, 1, ItemMold.SubItem.SHOVEL, extra_sticks2);
                MiscUtil.registerCasting(osmium_hoe, liquid_osmium, 2, ItemMold.SubItem.HOE, extra_sticks1);
                MiscUtil.registerCasting(osmium_sword, liquid_osmium, 2, ItemMold.SubItem.SWORD, extra_sticks1);
                MiscUtil.registerCasting(osmium_chestplate, liquid_osmium, 8, ItemMold.SubItem.CHESTPLATE);
                MiscUtil.registerCasting(osmium_leggings, liquid_osmium, 7, ItemMold.SubItem.LEGGINGS);
                MiscUtil.registerCasting(osmium_helmet, liquid_osmium, 5, ItemMold.SubItem.HELMET);
                MiscUtil.registerCasting(osmium_boots, liquid_osmium, 4, ItemMold.SubItem.BOOTS);
            }

            if (FoundryFluidRegistry.getStrategy("refined_obsidian").registerRecipes())
            {
                MiscUtil.registerCasting(refined_obsidian_pickaxe, liquid_refined_obsidian, 3, ItemMold.SubItem.PICKAXE,
                        extra_sticks2);
                MiscUtil.registerCasting(refined_obsidian_axe, liquid_refined_obsidian, 3, ItemMold.SubItem.AXE,
                        extra_sticks2);
                MiscUtil.registerCasting(refined_obsidian_shovel, liquid_refined_obsidian, 1, ItemMold.SubItem.SHOVEL,
                        extra_sticks2);
                MiscUtil.registerCasting(refined_obsidian_hoe, liquid_refined_obsidian, 2, ItemMold.SubItem.HOE,
                        extra_sticks1);
                MiscUtil.registerCasting(refined_obsidian_sword, liquid_refined_obsidian, 2, ItemMold.SubItem.SWORD,
                        extra_sticks1);
                MiscUtil.registerCasting(refined_obsidian_chestplate, liquid_refined_obsidian, 8,
                        ItemMold.SubItem.CHESTPLATE);
                MiscUtil.registerCasting(refined_obsidian_leggings, liquid_refined_obsidian, 7,
                        ItemMold.SubItem.LEGGINGS);
                MiscUtil.registerCasting(refined_obsidian_helmet, liquid_refined_obsidian, 5, ItemMold.SubItem.HELMET);
                MiscUtil.registerCasting(refined_obsidian_boots, liquid_refined_obsidian, 4, ItemMold.SubItem.BOOTS);
            }

            if (FoundryFluidRegistry.getStrategy("refined_glowstone").registerRecipes())
            {
                MiscUtil.registerCasting(refined_glowstone_pickaxe, liquid_refined_glowstone, 3,
                        ItemMold.SubItem.PICKAXE, extra_sticks2);
                MiscUtil.registerCasting(refined_glowstone_axe, liquid_refined_glowstone, 3, ItemMold.SubItem.AXE,
                        extra_sticks2);
                MiscUtil.registerCasting(refined_glowstone_shovel, liquid_refined_glowstone, 1, ItemMold.SubItem.SHOVEL,
                        extra_sticks2);
                MiscUtil.registerCasting(refined_glowstone_hoe, liquid_refined_glowstone, 2, ItemMold.SubItem.HOE,
                        extra_sticks1);
                MiscUtil.registerCasting(refined_glowstone_sword, liquid_refined_glowstone, 2, ItemMold.SubItem.SWORD,
                        extra_sticks1);
                MiscUtil.registerCasting(refined_glowstone_chestplate, liquid_refined_glowstone, 8,
                        ItemMold.SubItem.CHESTPLATE);
                MiscUtil.registerCasting(refined_glowstone_leggings, liquid_refined_glowstone, 7,
                        ItemMold.SubItem.LEGGINGS);
                MiscUtil.registerCasting(refined_glowstone_helmet, liquid_refined_glowstone, 5,
                        ItemMold.SubItem.HELMET);
                MiscUtil.registerCasting(refined_glowstone_boots, liquid_refined_glowstone, 4, ItemMold.SubItem.BOOTS);
            }

            if (FoundryFluidRegistry.getStrategy("bronze").registerRecipes()
                    && !Loader.isModLoaded("thermalfoundation"))
            {
                Fluid liquid_bronze = FoundryFluids.liquid_bronze;

                MiscUtil.registerCasting(bronze_pickaxe, liquid_bronze, 3, ItemMold.SubItem.PICKAXE, extra_sticks2);
                MiscUtil.registerCasting(bronze_axe, liquid_bronze, 3, ItemMold.SubItem.AXE, extra_sticks2);
                MiscUtil.registerCasting(bronze_shovel, liquid_bronze, 1, ItemMold.SubItem.SHOVEL, extra_sticks2);
                MiscUtil.registerCasting(bronze_hoe, liquid_bronze, 2, ItemMold.SubItem.HOE, extra_sticks1);
                MiscUtil.registerCasting(bronze_sword, liquid_bronze, 2, ItemMold.SubItem.SWORD, extra_sticks1);
                MiscUtil.registerCasting(bronze_chestplate, liquid_bronze, 8, ItemMold.SubItem.CHESTPLATE);
                MiscUtil.registerCasting(bronze_leggings, liquid_bronze, 7, ItemMold.SubItem.LEGGINGS);
                MiscUtil.registerCasting(bronze_helmet, liquid_bronze, 5, ItemMold.SubItem.HELMET);
                MiscUtil.registerCasting(bronze_boots, liquid_bronze, 4, ItemMold.SubItem.BOOTS);
            }
        }

        if (FoundryFluidRegistry.getStrategy("osmium").registerRecipes())
            FoundryUtils.registerBasicMeltingRecipes("osmium", liquid_osmium);
        if (FoundryFluidRegistry.getStrategy("refined_obsidian").registerRecipes())
            FoundryUtils.registerBasicMeltingRecipes("refined_obsidian", liquid_refined_obsidian);
        if (FoundryFluidRegistry.getStrategy("refined_glowstone").registerRecipes())
            FoundryUtils.registerBasicMeltingRecipes("refined_glowstone", liquid_refined_glowstone);
    }

    @Override
    public void onPreInit(Configuration config)
    {
    }

    @SubscribeEvent
    public void registerFluids(RegistryEvent.Register<Block> e)
    {
        IForgeRegistry<Block> registry = e.getRegistry();

        liquid_osmium = FoundryFluidRegistry.registerLiquidMetal(registry, "osmium", "Mekanism", 3300, 15, 0xBFD0FF);
        liquid_refined_obsidian = FoundryFluidRegistry.registerLiquidMetal(registry, "refined_obsidian", "Mekanism",
                3420, 15, 0x5D00FF);
        liquid_refined_glowstone = FoundryFluidRegistry.registerLiquidMetal(registry, "refined_glowstone", "Mekanism",
                3922, 15, 0xFFFF00);
    }
}
