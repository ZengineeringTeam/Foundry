package exter.foundry.integration;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.config.FoundryConfig;
import exter.foundry.fluid.FluidLiquidMetal;
import exter.foundry.fluid.FoundryFluidRegistry;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.util.FoundryMiscUtils;
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

        ItemStack manasteel_block = getItemStack("storage", 0);
        ItemStack terrasteel_block = getItemStack("storage", 1);
        ItemStack elementium_block = getItemStack("storage", 2);
        ItemStack mold_block = FoundryItems.mold(ItemMold.SubItem.BLOCK);

        MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(manasteel_block),
                new FluidStack(liquid_manasteel, FoundryAPI.getAmountBlock()));
        MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(terrasteel_block),
                new FluidStack(liquid_terrasteel, FoundryAPI.getAmountBlock()));
        MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(elementium_block),
                new FluidStack(liquid_elementium, FoundryAPI.getAmountBlock()));

        CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(manasteel_block),
                new FluidStack(liquid_manasteel, FoundryAPI.getAmountBlock()), mold_block, null);
        CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(terrasteel_block),
                new FluidStack(liquid_terrasteel, FoundryAPI.getAmountBlock()), mold_block, null);
        CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(elementium_block),
                new FluidStack(liquid_elementium, FoundryAPI.getAmountBlock()), mold_block, null);

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

            ItemStack livingwood_twig = getItemStack("manaresource", 3);
            ItemStack dreamwood_twig = getItemStack("manaresource", 13);

            ItemStack livingsticks1 = livingwood_twig.copy();
            ItemStack livingsticks2 = livingwood_twig.copy();
            livingsticks2.setCount(2);
            ItemStackMatcher extra_sticks1 = new ItemStackMatcher(livingsticks1);
            ItemStackMatcher extra_sticks2 = new ItemStackMatcher(livingsticks2);

            ItemStack dreamsticks1 = dreamwood_twig.copy();
            ItemStack dreamsticks2 = dreamwood_twig.copy();
            dreamsticks2.setCount(2);
            ItemStackMatcher extra_dreamsticks1 = new ItemStackMatcher(dreamsticks1);
            ItemStackMatcher extra_dreamsticks2 = new ItemStackMatcher(dreamsticks2);

            FoundryMiscUtils.registerCasting(manasteel_pickaxe, liquid_manasteel, 3, ItemMold.SubItem.PICKAXE,
                    extra_sticks2);
            FoundryMiscUtils.registerCasting(manasteel_axe, liquid_manasteel, 3, ItemMold.SubItem.AXE, extra_sticks2);
            FoundryMiscUtils.registerCasting(manasteel_shovel, liquid_manasteel, 1, ItemMold.SubItem.SHOVEL,
                    extra_sticks2);
            FoundryMiscUtils.registerCasting(manasteel_sword, liquid_manasteel, 2, ItemMold.SubItem.SWORD,
                    extra_sticks1);
            FoundryMiscUtils.registerCasting(manasteel_chestplate, liquid_manasteel, 8, ItemMold.SubItem.CHESTPLATE);
            FoundryMiscUtils.registerCasting(manasteel_leggings, liquid_manasteel, 7, ItemMold.SubItem.LEGGINGS);
            FoundryMiscUtils.registerCasting(manasteel_helmet, liquid_manasteel, 5, ItemMold.SubItem.HELMET);
            FoundryMiscUtils.registerCasting(manasteel_boots, liquid_manasteel, 4, ItemMold.SubItem.BOOTS);

            FoundryMiscUtils.registerCasting(terrasteel_sword,
                    new FluidStack(liquid_terrasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 2), ItemMold.SubItem.SWORD,
                    extra_sticks1);

            FoundryMiscUtils.registerCasting(elementium_pickaxe, liquid_elementium, 3, ItemMold.SubItem.PICKAXE,
                    extra_dreamsticks2);
            FoundryMiscUtils.registerCasting(elementium_axe, liquid_elementium, 3, ItemMold.SubItem.AXE,
                    extra_dreamsticks2);
            FoundryMiscUtils.registerCasting(elementium_shovel, liquid_elementium, 1, ItemMold.SubItem.SHOVEL,
                    extra_dreamsticks2);
            FoundryMiscUtils.registerCasting(elementium_sword, liquid_elementium, 2, ItemMold.SubItem.SWORD,
                    extra_dreamsticks1);
            FoundryMiscUtils.registerCasting(elementium_chestplate, liquid_elementium, 8, ItemMold.SubItem.CHESTPLATE);
            FoundryMiscUtils.registerCasting(elementium_leggings, liquid_elementium, 7, ItemMold.SubItem.LEGGINGS);
            FoundryMiscUtils.registerCasting(elementium_helmet, liquid_elementium, 5, ItemMold.SubItem.HELMET);
            FoundryMiscUtils.registerCasting(elementium_boots, liquid_elementium, 4, ItemMold.SubItem.BOOTS);
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
        liquid_manasteel = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "manasteel", 1950, 15, 0xFFFFFF);
        liquid_terrasteel = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "terrasteel", 2100, 15,
                0xFFFFFF);
        liquid_elementium = FoundryFluidRegistry.INSTANCE.registerLiquidMetal(registry, "elvenelementium", 2400, 15,
                0xFFFFFF);

        FoundryUtils.registerBasicMeltingRecipes("manasteel", liquid_manasteel);
        FoundryUtils.registerBasicMeltingRecipes("terrasteel", liquid_terrasteel);
        FoundryUtils.registerBasicMeltingRecipes("elvenelementium", liquid_elementium);
    }
}
