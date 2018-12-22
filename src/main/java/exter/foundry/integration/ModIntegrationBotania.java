package exter.foundry.integration;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.ICastingTableRecipe.TableType;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.config.FoundryConfig;
import exter.foundry.fluid.FoundryFluidRegistry;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.CastingTableRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fluids.Fluid;
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

    public static Fluid liquid_manasteel;
    public static Fluid liquid_terrasteel;
    public static Fluid liquid_elementium;

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

        if (FoundryFluidRegistry.getStrategy("manasteel").registerRecipes())
        {
            ItemStackMatcher manasteel_block = new ItemStackMatcher(getItemStack("storage", 0));
            FluidStack manasteel_liquid = new FluidStack(liquid_manasteel, FoundryAPI.getAmountBlock());

            MeltingRecipeManager.INSTANCE.addRecipe(manasteel_block, manasteel_liquid);
            CastingRecipeManager.INSTANCE.addRecipe(manasteel_block, manasteel_liquid, ItemMold.SubItem.BLOCK, false,
                    null);
            CastingTableRecipeManager.INSTANCE.addRecipe(manasteel_block, manasteel_liquid, TableType.BLOCK);
        }

        if (FoundryFluidRegistry.getStrategy("terrasteel").registerRecipes())
        {
            ItemStackMatcher terrasteel_block = new ItemStackMatcher(getItemStack("storage", 1));
            FluidStack terrasteel_liquid = new FluidStack(liquid_terrasteel, FoundryAPI.getAmountBlock());

            MeltingRecipeManager.INSTANCE.addRecipe(terrasteel_block, terrasteel_liquid);
            CastingRecipeManager.INSTANCE.addRecipe(terrasteel_block, terrasteel_liquid, ItemMold.SubItem.BLOCK, false,
                    null);
            CastingTableRecipeManager.INSTANCE.addRecipe(terrasteel_block, terrasteel_liquid, TableType.BLOCK);
        }

        if (FoundryFluidRegistry.getStrategy("elven_elementium").registerRecipes())
        {
            ItemStackMatcher elementium_block = new ItemStackMatcher(getItemStack("storage", 2));
            FluidStack elementium_liquid = new FluidStack(liquid_elementium, FoundryAPI.getAmountBlock());

            MeltingRecipeManager.INSTANCE.addRecipe(elementium_block, elementium_liquid);
            CastingRecipeManager.INSTANCE.addRecipe(elementium_block, elementium_liquid, ItemMold.SubItem.BLOCK, false,
                    null);
            CastingTableRecipeManager.INSTANCE.addRecipe(elementium_block, elementium_liquid, TableType.BLOCK);
        }

        if (FoundryConfig.recipe_equipment)
        {
            ItemStack manasteel_pickaxe = getItemStack("manasteelpick");
            ItemStack manasteel_axe = getItemStack("manasteelaxe");
            ItemStack manasteel_shovel = getItemStack("manasteelshovel");
            ItemStack manasteel_sword = getItemStack("manasteelsword");
            ItemStack manasteel_shears = getItemStack("manasteelshears");

            ItemStack manasteel_helmet = getItemStack("manasteelhelm");
            ItemStack manasteel_chestplate = getItemStack("manasteelchest");
            ItemStack manasteel_leggings = getItemStack("manasteellegs");
            ItemStack manasteel_boots = getItemStack("manasteelboots");

            ItemStack terrasteel_sword = getItemStack("terrasword");

            ItemStack elementium_pickaxe = getItemStack("elementiumpick");
            ItemStack elementium_axe = getItemStack("elementiumaxe");
            ItemStack elementium_shovel = getItemStack("elementiumshovel");
            ItemStack elementium_sword = getItemStack("elementiumsword");
            ItemStack elementium_shears = getItemStack("elementiumshears");

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
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(manasteel_pickaxe),
                        new FluidStack(liquid_manasteel, FoundryAPI.getAmountPickaxe()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(manasteel_pickaxe),
                        new FluidStack(liquid_manasteel, FoundryAPI.getAmountPickaxe()), ItemMold.SubItem.PICKAXE, false, extra_sticks2);
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(manasteel_axe),
                        new FluidStack(liquid_manasteel, FoundryAPI.getAmountAxe()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(manasteel_axe),
                        new FluidStack(liquid_manasteel, FoundryAPI.getAmountAxe()), ItemMold.SubItem.AXE, false, extra_sticks2);
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(manasteel_shovel),
                        new FluidStack(liquid_manasteel, FoundryAPI.getAmountShovel()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(manasteel_shovel),
                        new FluidStack(liquid_manasteel, FoundryAPI.getAmountShovel()), ItemMold.SubItem.SHOVEL, false, extra_sticks2);
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(manasteel_sword),
                        new FluidStack(liquid_manasteel, FoundryAPI.getAmountSword()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(manasteel_sword),
                        new FluidStack(liquid_manasteel, FoundryAPI.getAmountSword()), ItemMold.SubItem.SWORD, false, extra_sticks1);
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(manasteel_shears),
                        new FluidStack(liquid_manasteel, FoundryAPI.getAmountShears()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(manasteel_shears),
                        new FluidStack(liquid_manasteel, FoundryAPI.getAmountShears()), ItemMold.SubItem.SHEARS, false, null);
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(manasteel_helmet),
                        new FluidStack(liquid_manasteel, FoundryAPI.getAmountHelm()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(manasteel_helmet),
                        new FluidStack(liquid_manasteel, FoundryAPI.getAmountHelm()), ItemMold.SubItem.HELMET, false, null);
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(manasteel_chestplate),
                        new FluidStack(liquid_manasteel, FoundryAPI.getAmountChest()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(manasteel_chestplate),
                        new FluidStack(liquid_manasteel, FoundryAPI.getAmountChest()), ItemMold.SubItem.CHESTPLATE, false, null);
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(manasteel_leggings),
                        new FluidStack(liquid_manasteel, FoundryAPI.getAmountLegs()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(manasteel_leggings),
                        new FluidStack(liquid_manasteel, FoundryAPI.getAmountLegs()), ItemMold.SubItem.LEGGINGS, false, null);
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(manasteel_boots),
                        new FluidStack(liquid_manasteel, FoundryAPI.getAmountBoots()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(manasteel_boots),
                        new FluidStack(liquid_manasteel, FoundryAPI.getAmountBoots()), ItemMold.SubItem.BOOTS, false, null);
            }

            if (FoundryFluidRegistry.getStrategy("terrasteel").registerRecipes())
            {
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(terrasteel_sword),
                        new FluidStack(liquid_terrasteel, FoundryAPI.getAmountSword()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(terrasteel_sword),
                        new FluidStack(liquid_terrasteel, FoundryAPI.getAmountSword()), ItemMold.SubItem.SWORD, false, extra_sticks1);
            }

            if (FoundryFluidRegistry.getStrategy("elven_elementium").registerRecipes())
            {
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(elementium_pickaxe),
                        new FluidStack(liquid_elementium, FoundryAPI.getAmountPickaxe()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(elementium_pickaxe),
                        new FluidStack(liquid_elementium, FoundryAPI.getAmountPickaxe()), ItemMold.SubItem.PICKAXE, false, extra_dreamsticks2);
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(elementium_axe),
                        new FluidStack(liquid_elementium, FoundryAPI.getAmountAxe()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(elementium_axe),
                        new FluidStack(liquid_elementium, FoundryAPI.getAmountAxe()), ItemMold.SubItem.AXE, false, extra_dreamsticks2);
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(elementium_shovel),
                        new FluidStack(liquid_elementium, FoundryAPI.getAmountShovel()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(elementium_shovel),
                        new FluidStack(liquid_elementium, FoundryAPI.getAmountShovel()), ItemMold.SubItem.SHOVEL, false, extra_dreamsticks2);
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(elementium_sword),
                        new FluidStack(liquid_elementium, FoundryAPI.getAmountSword()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(elementium_sword),
                        new FluidStack(liquid_elementium, FoundryAPI.getAmountSword()), ItemMold.SubItem.SWORD, false, extra_dreamsticks1);
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(elementium_shears),
                        new FluidStack(liquid_elementium, FoundryAPI.getAmountShears()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(elementium_shears),
                        new FluidStack(liquid_elementium, FoundryAPI.getAmountShears()), ItemMold.SubItem.SHEARS, false, null);
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(elementium_helmet),
                        new FluidStack(liquid_elementium, FoundryAPI.getAmountHelm()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(elementium_helmet),
                        new FluidStack(liquid_elementium, FoundryAPI.getAmountHelm()), ItemMold.SubItem.HELMET, false, null);
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(elementium_chestplate),
                        new FluidStack(liquid_elementium, FoundryAPI.getAmountChest()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(elementium_chestplate),
                        new FluidStack(liquid_elementium, FoundryAPI.getAmountChest()), ItemMold.SubItem.CHESTPLATE, false, null);
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(elementium_leggings),
                        new FluidStack(liquid_elementium, FoundryAPI.getAmountLegs()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(elementium_leggings),
                        new FluidStack(liquid_elementium, FoundryAPI.getAmountLegs()), ItemMold.SubItem.LEGGINGS, false, null);
                FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(elementium_boots),
                        new FluidStack(liquid_elementium, FoundryAPI.getAmountBoots()));
                FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(elementium_boots),
                        new FluidStack(liquid_elementium, FoundryAPI.getAmountBoots()), ItemMold.SubItem.BOOTS, false, null);
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
