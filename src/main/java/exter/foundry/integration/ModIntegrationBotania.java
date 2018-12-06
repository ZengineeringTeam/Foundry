package exter.foundry.integration;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.ICastingTableRecipe.TableType;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.config.FoundryConfig;
import exter.foundry.fluid.FluidLiquidMetal;
import exter.foundry.fluid.FoundryFluidRegistry;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.CastingTableRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.util.MiscUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.IForgeRegistry;

public class ModIntegrationBotania implements IModIntegration
{

    public static final String BOTANIA = "botania";

    private FluidLiquidMetal liquid_manasteel;
    private FluidLiquidMetal liquid_terrasteel;
    private FluidLiquidMetal liquid_elementium;

    private ItemStack getItemStack(String name)
    {
        return getItemStack(name, 0);
    }

    private ItemStack getItemStack(String name, int meta)
    {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(BOTANIA, name));
        if (item == null)
        {
            return ItemStack.EMPTY;
        }
        return new ItemStack(item, 1, meta);
    }

    @Override
    public String getName()
    {
        return "Botania";
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
        if (!Loader.isModLoaded(BOTANIA))
        {
            return;
        }

        ItemStack mold_block = ItemMold.SubItem.BLOCK.getItem();

        if (FoundryFluidRegistry.getStrategy("manasteel").registerRecipes())
        {
            // FIXME: duplicated recipe
            FoundryUtils.registerBasicMeltingRecipes("manasteel", liquid_manasteel);

            ItemStackMatcher manasteel_block = new ItemStackMatcher(getItemStack("storage", 0));
            FluidStack manasteel_liquid = new FluidStack(liquid_manasteel, FoundryAPI.getAmountBlock());

            MeltingRecipeManager.INSTANCE.addRecipe(manasteel_block, manasteel_liquid);
            CastingRecipeManager.INSTANCE.addRecipe(manasteel_block, manasteel_liquid, mold_block, false, null);
            CastingTableRecipeManager.INSTANCE.addRecipe(manasteel_block, manasteel_liquid, TableType.BLOCK);
        }

        if (FoundryFluidRegistry.getStrategy("terrasteel").registerRecipes())
        {
            FoundryUtils.registerBasicMeltingRecipes("terrasteel", liquid_terrasteel);

            ItemStackMatcher terrasteel_block = new ItemStackMatcher(getItemStack("storage", 1));
            FluidStack terrasteel_liquid = new FluidStack(liquid_terrasteel, FoundryAPI.getAmountBlock());

            MeltingRecipeManager.INSTANCE.addRecipe(terrasteel_block, terrasteel_liquid);
            CastingRecipeManager.INSTANCE.addRecipe(terrasteel_block, terrasteel_liquid, mold_block, false, null);
            CastingTableRecipeManager.INSTANCE.addRecipe(terrasteel_block, terrasteel_liquid, TableType.BLOCK);
        }

        if (FoundryFluidRegistry.getStrategy("elven_elementium").registerRecipes())
        {
            FoundryUtils.registerBasicMeltingRecipes("elven_elementium", liquid_elementium);

            ItemStackMatcher elementium_block = new ItemStackMatcher(getItemStack("storage", 2));
            FluidStack elementium_liquid = new FluidStack(liquid_elementium, FoundryAPI.getAmountBlock());

            MeltingRecipeManager.INSTANCE.addRecipe(elementium_block, elementium_liquid);
            CastingRecipeManager.INSTANCE.addRecipe(elementium_block, elementium_liquid, mold_block, false, null);
            CastingTableRecipeManager.INSTANCE.addRecipe(elementium_block, elementium_liquid, TableType.BLOCK);
        }

        if (FoundryConfig.recipe_equipment)
        {
            ItemStack manasteel_pickaxe = getItemStack("manasteelpick");
            ItemStack manasteel_axe = getItemStack("manasteelaxe");
            ItemStack manasteel_shovel = getItemStack("manasteelshovel");
            ItemStack manasteel_sword = getItemStack("manasteelsword");

            ItemStack manasteel_helmet = getItemStack("manasteelhelm");
            ItemStack manasteel_chestplate = getItemStack("manasteelchest");
            ItemStack manasteel_leggings = getItemStack("manasteellegs");
            ItemStack manasteel_boots = getItemStack("manasteelboots");

            ItemStack terrasteel_sword = getItemStack("terrasword");

            ItemStack elementium_pickaxe = getItemStack("elementiumpick");
            ItemStack elementium_axe = getItemStack("elementiumaxe");
            ItemStack elementium_shovel = getItemStack("elementiumshovel");
            ItemStack elementium_sword = getItemStack("elementiumsword");

            ItemStack elementium_helmet = getItemStack("elementiumhelm");
            ItemStack elementium_chestplate = getItemStack("elementiumchest");
            ItemStack elementium_leggings = getItemStack("elementiumlegs");
            ItemStack elementium_boots = getItemStack("elementiumboots");

            ItemStack livingsticks1 = getItemStack("manaresource", 3);
            ItemStack livingsticks2 = ItemHandlerHelper.copyStackWithSize(livingsticks1, 2);
            ItemStackMatcher extra_sticks1 = new ItemStackMatcher(livingsticks1);
            ItemStackMatcher extra_sticks2 = new ItemStackMatcher(livingsticks2);

            ItemStack dreamsticks1 = getItemStack("manaresource", 13);
            ItemStack dreamsticks2 = ItemHandlerHelper.copyStackWithSize(dreamsticks1, 2);
            ItemStackMatcher extra_dreamsticks1 = new ItemStackMatcher(dreamsticks1);
            ItemStackMatcher extra_dreamsticks2 = new ItemStackMatcher(dreamsticks2);

            if (FoundryFluidRegistry.getStrategy("manasteel").registerRecipes())
            {
                MiscUtil.registerCasting(manasteel_pickaxe, liquid_manasteel, FoundryAPI.getAmountPickaxe(),
                        ItemMold.SubItem.PICKAXE, extra_sticks2);
                MiscUtil.registerCasting(manasteel_axe, liquid_manasteel, FoundryAPI.getAmountAxe(),
                        ItemMold.SubItem.AXE, extra_sticks2);
                MiscUtil.registerCasting(manasteel_shovel, liquid_manasteel, FoundryAPI.getAmountShovel(),
                        ItemMold.SubItem.SHOVEL, extra_sticks2);
                MiscUtil.registerCasting(manasteel_sword, liquid_manasteel, FoundryAPI.getAmountSword(),
                        ItemMold.SubItem.SWORD, extra_sticks1);
                MiscUtil.registerCasting(manasteel_chestplate, liquid_manasteel, FoundryAPI.getAmountChest(),
                        ItemMold.SubItem.CHESTPLATE);
                MiscUtil.registerCasting(manasteel_leggings, liquid_manasteel, FoundryAPI.getAmountLegs(),
                        ItemMold.SubItem.LEGGINGS);
                MiscUtil.registerCasting(manasteel_helmet, liquid_manasteel, FoundryAPI.getAmountHelm(),
                        ItemMold.SubItem.HELMET);
                MiscUtil.registerCasting(manasteel_boots, liquid_manasteel, FoundryAPI.getAmountBoots(),
                        ItemMold.SubItem.BOOTS);
            }

            if (FoundryFluidRegistry.getStrategy("terrasteel").registerRecipes())
                MiscUtil.registerCasting(terrasteel_sword,
                        new FluidStack(liquid_terrasteel, FoundryAPI.getAmountSword()), ItemMold.SubItem.SWORD,
                        extra_sticks1);

            if (FoundryFluidRegistry.getStrategy("elven_elementium").registerRecipes())
            {
                MiscUtil.registerCasting(elementium_pickaxe, liquid_elementium, FoundryAPI.getAmountPickaxe(),
                        ItemMold.SubItem.PICKAXE, extra_dreamsticks2);
                MiscUtil.registerCasting(elementium_axe, liquid_elementium, FoundryAPI.getAmountAxe(),
                        ItemMold.SubItem.AXE, extra_dreamsticks2);
                MiscUtil.registerCasting(elementium_shovel, liquid_elementium, FoundryAPI.getAmountShovel(),
                        ItemMold.SubItem.SHOVEL, extra_dreamsticks2);
                MiscUtil.registerCasting(elementium_sword, liquid_elementium, FoundryAPI.getAmountSword(),
                        ItemMold.SubItem.SWORD, extra_dreamsticks1);
                MiscUtil.registerCasting(elementium_chestplate, liquid_elementium, FoundryAPI.getAmountChest(),
                        ItemMold.SubItem.CHESTPLATE);
                MiscUtil.registerCasting(elementium_leggings, liquid_elementium, FoundryAPI.getAmountLegs(),
                        ItemMold.SubItem.LEGGINGS);
                MiscUtil.registerCasting(elementium_helmet, liquid_elementium, FoundryAPI.getAmountHelm(),
                        ItemMold.SubItem.HELMET);
                MiscUtil.registerCasting(elementium_boots, liquid_elementium, FoundryAPI.getAmountBoots(),
                        ItemMold.SubItem.BOOTS);
            }
        }
    }

    @Override
    public void onPreInit(Configuration config)
    {
    }

    @SubscribeEvent
    public void registerFluids(Register<Block> e)
    {
        IForgeRegistry<Block> registry = e.getRegistry();

        liquid_manasteel = FoundryFluidRegistry.registerLiquidMetal(registry, "manasteel", "Botania", 1950, 15,
                0x2050EA);
        liquid_terrasteel = FoundryFluidRegistry.registerLiquidMetal(registry, "terrasteel", "Botania", 2100, 15,
                0x5AFF0A);
        liquid_elementium = FoundryFluidRegistry.registerLiquidMetal(registry, "elven_elementium", "Botania", 2400, 15,
                0xE60082);
    }
}
