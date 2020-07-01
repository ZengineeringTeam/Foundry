package exter.foundry.integration.crafttweaker;

import com.google.common.base.Preconditions;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.oredict.IOreDictEntry;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.api.recipe.matcher.OreMatcher;
import exter.foundry.config.FoundryConfig;
import exter.foundry.integration.crafttweaker.orestack.CrTOreStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CrTHelper
{

    public static String getFluidDescription(ILiquidStack stack)
    {
        return getFluidDescription(CraftTweakerMC.getLiquidStack(stack));
    }

    public static String getFluidDescription(FluidStack stack)
    {
        return String.format("<liquid:%s> * %d", stack.getFluid().getName(), stack.amount);
    }

    public static IItemMatcher getIngredient(IIngredient ingr)
    {
        if (ingr instanceof IItemStack)
        {
            return new ItemStackMatcher(CraftTweakerMC.getItemStack((IItemStack) ingr));
        }
        return new CrTItemMatcher(ingr);
        //throw new IllegalArgumentException("Invalid IIngredient passed to a foundry method, " + ingr);
    }

    public static String getItemDescription(IItemMatcher obj)
    {
        Preconditions.checkNotNull(obj, "Cannot get description from a null matcher!");
        if (obj instanceof OreMatcher)
        {
            OreMatcher stack = (OreMatcher) obj;
            return String.format("<ore:%s> * %d", stack.getOreName(), stack.getAmount());
        }
        else
        {
            ItemStack stack = ((ItemStackMatcher) obj).getItem();
            String desc = String.format("<%s:%d>", stack.getItem().getRegistryName(), stack.getItemDamage());
            if (stack.getCount() > 1)
                desc += " * " + stack.getCount();
            if (stack.hasTagCompound())
                desc += " with tag " + stack.getTagCompound().toString();
            return desc;
        }
    }

    public static String getItemDescription(IItemStack stack)
    {
        return getItemDescription(CraftTweakerMC.getItemStack(stack));
    }

    public static String getItemDescription(ItemStack stack)
    {
        String desc = String.format("<%s:%d> * %d", stack.getItem().getRegistryName(), stack.getItemDamage(),
                stack.getCount());
        if (stack.hasTagCompound())
            desc += " with tag " + stack.getTagCompound().toString();
        return desc;
    }

    public static void printCrt(String print)
    {
        if (FoundryConfig.crtError)
            CraftTweakerAPI.logError(print);
        else
            CraftTweakerAPI.logInfo(print);
    }
}
