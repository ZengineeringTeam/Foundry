package exter.foundry.integration;

import static exter.foundry.api.FoundryAPI.FLUID_AMOUNT_INGOT;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.ICastingTableRecipe.TableType;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.api.recipe.matcher.OreMatcher;
import exter.foundry.fluid.FoundryFluidRegistry;
import exter.foundry.init.InitRecipes;
import exter.foundry.item.ItemMold;
import exter.foundry.item.ItemMold.SubItem;
import exter.foundry.util.MiscUtil;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class Helper
{
    public static void registerOSRecipes(String modid, String material)
    {
        Fluid fluid = FluidRegistry.getFluid(material);
        if (fluid == null)
        {
            return;
        }
        if (!FoundryFluidRegistry.getStrategy(material).registerRecipes())
        {
            FoundryUtils.registerBasicMeltingRecipes(material, fluid);
            InitRecipes.addDefaultCasting(fluid, material);
        }
        OreMatcher stick = new OreMatcher("stickWood", 2);
        Item pickaxe = ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, material + "_pickaxe"));
        if (pickaxe != null)
        {
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(pickaxe), new FluidStack(fluid, FoundryAPI.getAmountPickaxe()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(pickaxe), new FluidStack(fluid, FoundryAPI.getAmountPickaxe()), SubItem.PICKAXE, false, stick);
        }
        Item shovel = ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, material + "_shovel"));
        if (shovel != null)
        {
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(shovel), new FluidStack(fluid, FoundryAPI.getAmountShovel()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(shovel), new FluidStack(fluid, FoundryAPI.getAmountShovel()), SubItem.SHOVEL, false, stick);
        }
        Item axe = ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, material + "_axe"));
        if (axe != null)
        {
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(axe), new FluidStack(fluid, FoundryAPI.getAmountAxe()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(axe), new FluidStack(fluid, FoundryAPI.getAmountAxe()), SubItem.AXE, false, stick);
        }
        Item sword = ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, material + "_sword"));
        if (sword != null)
        {
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(sword), new FluidStack(fluid, FoundryAPI.getAmountSword()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(sword), new FluidStack(fluid, FoundryAPI.getAmountSword()), SubItem.SWORD, false, new OreMatcher("stickWood"));
        }
        Item hoe = ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, material + "_hoe"));
        if (hoe != null)
        {
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(hoe), new FluidStack(fluid, FoundryAPI.getAmountHoe()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(hoe), new FluidStack(fluid, FoundryAPI.getAmountHoe()), SubItem.HOE, false, stick);
        }
        Item helm = ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, material + "_helmet"));
        if (helm != null)
        {
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(helm), new FluidStack(fluid, FoundryAPI.getAmountHelm()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(helm), new FluidStack(fluid, FoundryAPI.getAmountHelm()), SubItem.HELMET, false, null);
        }
        Item chest = ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, material + "_chestplate"));
        if (chest != null)
        {
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(chest), new FluidStack(fluid, FoundryAPI.getAmountChest()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(chest), new FluidStack(fluid, FoundryAPI.getAmountChest()), SubItem.CHESTPLATE, false, null);
        }
        Item legg = ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, material + "_leggings"));
        if (legg != null)
        {
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(legg), new FluidStack(fluid, FoundryAPI.getAmountLegs()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(legg), new FluidStack(fluid, FoundryAPI.getAmountLegs()), SubItem.LEGGINGS, false, null);
        }
        Item boots = ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, material + "_boots"));
        if (boots != null)
        {
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(boots), new FluidStack(fluid, FoundryAPI.getAmountBoots()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(boots), new FluidStack(fluid, FoundryAPI.getAmountBoots()), SubItem.BOOTS, false, null);
        }
        Item shears = ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, material + "_shears"));
        if (shears != null)
        {
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(shears), new FluidStack(fluid, FoundryAPI.getAmountShears()));
            FoundryAPI.CASTING_MANAGER.addRecipe(new ItemStackMatcher(shears), new FluidStack(fluid, FoundryAPI.getAmountShears()), SubItem.SHEARS, false, null);
        }
        Item pressureplate = ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, material + "_pressure_plate"));
        if (pressureplate != null)
        {
            FoundryAPI.MELTING_MANAGER.addRecipe(new ItemStackMatcher(pressureplate), new FluidStack(fluid, FoundryAPI.getAmountPressurePlate()));
        }
    }
}
