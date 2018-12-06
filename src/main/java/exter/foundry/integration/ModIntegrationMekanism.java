package exter.foundry.integration;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.api.recipe.matcher.OreMatcher;
import exter.foundry.config.FoundryConfig;
import exter.foundry.fluid.FluidLiquidMetal;
import exter.foundry.fluid.FoundryFluidRegistry;
import exter.foundry.item.ItemMold;
import exter.foundry.util.MiscUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
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

    private static ItemStack getItemStack(String name)
    {
        return getItemStack(name, 0);
    }

    private static ItemStack getItemStack(String name, int meta)
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

            registerTools("osmium", "osmium", extra_sticks1, extra_sticks2);
            registerTools("refined_obsidian", "obsidian", extra_sticks1, extra_sticks2);
            registerTools("refined_glowstone", "glowstone", extra_sticks1, extra_sticks2);
            if (!Loader.isModLoaded("thermalfoundation"))
            {
                registerTools("bronze", "bronze", extra_sticks1, extra_sticks2);
            }
        }

        if (FoundryFluidRegistry.getStrategy("osmium").registerRecipes())
            FoundryUtils.registerBasicMeltingRecipes("osmium", liquid_osmium);
        if (FoundryFluidRegistry.getStrategy("refined_obsidian").registerRecipes())
            FoundryUtils.registerBasicMeltingRecipes("refined_obsidian", liquid_refined_obsidian);
        if (FoundryFluidRegistry.getStrategy("refined_glowstone").registerRecipes())
            FoundryUtils.registerBasicMeltingRecipes("refined_glowstone", liquid_refined_glowstone);
    }

    private static void registerTools(String name, String itemPrefix, OreMatcher extra_sticks1, OreMatcher extra_sticks2)
    {
        if (FoundryFluidRegistry.getStrategy(name).registerRecipes())
        {
            ItemStack pickaxe = getItemStack(itemPrefix + "pickaxe");
            ItemStack axe = getItemStack(itemPrefix + "axe");
            ItemStack shovel = getItemStack(itemPrefix + "shovel");
            ItemStack hoe = getItemStack(itemPrefix + "hoe");
            ItemStack sword = getItemStack(itemPrefix + "sword");
            ItemStack helmet = getItemStack(itemPrefix + "helmet");
            ItemStack chestplate = getItemStack(itemPrefix + "chestplate");
            ItemStack leggings = getItemStack(itemPrefix + "leggings");
            ItemStack boots = getItemStack(itemPrefix + "boots");

            Fluid fluid = FluidRegistry.getFluid(name);
            FluidStack fluidStack;
            fluidStack = new FluidStack(fluid, FoundryAPI.getAmountPickaxe());
            MiscUtil.registerCasting(pickaxe, fluidStack, ItemMold.SubItem.PICKAXE, extra_sticks2);
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(pickaxe), fluidStack);
            fluidStack = new FluidStack(fluid, FoundryAPI.getAmountAxe());
            MiscUtil.registerCasting(axe, fluidStack, ItemMold.SubItem.AXE, extra_sticks2);
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(axe), fluidStack);
            fluidStack = new FluidStack(fluid, FoundryAPI.getAmountShovel());
            MiscUtil.registerCasting(shovel, fluidStack, ItemMold.SubItem.SHOVEL, extra_sticks2);
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(shovel), fluidStack);
            fluidStack = new FluidStack(fluid, FoundryAPI.getAmountHoe());
            MiscUtil.registerCasting(hoe, fluidStack, ItemMold.SubItem.HOE, extra_sticks1);
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(hoe), fluidStack);
            fluidStack = new FluidStack(fluid, FoundryAPI.getAmountSword());
            MiscUtil.registerCasting(sword, fluidStack, ItemMold.SubItem.SWORD, extra_sticks1);
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(sword), fluidStack);
            fluidStack = new FluidStack(fluid, FoundryAPI.getAmountChest());
            MiscUtil.registerCasting(chestplate, fluidStack, ItemMold.SubItem.CHESTPLATE, null);
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(chestplate), fluidStack);
            fluidStack = new FluidStack(fluid, FoundryAPI.getAmountLegs());
            MiscUtil.registerCasting(leggings, fluidStack, ItemMold.SubItem.LEGGINGS, null);
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(leggings), fluidStack);
            fluidStack = new FluidStack(fluid, FoundryAPI.getAmountHelm());
            MiscUtil.registerCasting(helmet, fluidStack, ItemMold.SubItem.HELMET, null);
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(helmet), fluidStack);
            fluidStack = new FluidStack(fluid, FoundryAPI.getAmountBoots());
            MiscUtil.registerCasting(boots, fluidStack, ItemMold.SubItem.BOOTS, null);
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(boots), fluidStack);
        }
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
