package exter.foundry.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import exter.foundry.Foundry;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.CastingRecipeManager;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Miscellaneous utility methods
 */
public class MiscUtil
{
    public static int divCeil(int a, int b)
    {
        return a / b + (a % b == 0 ? 0 : 1);
    }

    public static FluidStack drainFluidFromWorld(World world, BlockPos pos, boolean do_drain)
    {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof IFluidBlock)
        {
            IFluidBlock fluid_block = (IFluidBlock) state.getBlock();
            if (!fluid_block.canDrain(world, pos))
            {
                return null;
            }
            return fluid_block.drain(world, pos, do_drain);
        }

        if (state.getMaterial() == Material.WATER && state.getValue(BlockLiquid.LEVEL) == 0)
        {
            if (do_drain)
            {
                world.setBlockToAir(pos);
            }
            return new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME);
        }

        if (state.getMaterial() == Material.LAVA && state.getValue(BlockLiquid.LEVEL) == 0)
        {
            if (do_drain)
            {
                world.setBlockToAir(pos);
            }
            return new FluidStack(FluidRegistry.LAVA, Fluid.BUCKET_VOLUME);
        }
        return null;
    }

    public static Set<String> getAllItemOreDictionaryNames(ItemStack stack)
    {
        Set<String> result = new HashSet<>();
        for (String name : OreDictionary.getOreNames())
        {
            List<ItemStack> ores = MiscUtil.getOresSafe(name);
            for (ItemStack i : ores)
            {
                if (i.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(i, stack))
                {
                    result.add(name);
                }
            }
        }
        return result;
    }

    public static String getItemOreDictionaryName(ItemStack stack)
    {
        for (String name : OreDictionary.getOreNames())
        {
            List<ItemStack> ores = getOresSafe(name);
            for (ItemStack i : ores)
            {
                if (i.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(i, stack))
                {
                    return name;
                }
            }
        }
        return null;
    }

    public static ItemStack getModItemFromOreDictionary(String modid, String orename)
    {
        return getModItemFromOreDictionary(modid, orename, 1);
    }

    public static ItemStack getModItemFromOreDictionary(String modid, String orename, int amount)
    {
        return getStackFromDictWithPreference(modid, orename, amount);
    }

    public static NonNullList<ItemStack> getOresSafe(String orename)
    {
        return OreDictionary.getOres(orename, false);
    }

    public static ItemStack getStackFromDictWithPreference(String domain, String ore, int amount)
    {
        for (ItemStack is : MiscUtil.getOresSafe(ore))
        {
            if (is.getItem().getRegistryName().getResourceDomain().equals(domain))
            {
                is = is.copy();
                is.setCount(amount);
                return is;
            }
        }
        for (ItemStack is : MiscUtil.getOresSafe(ore))
        {
            is = is.copy();
            is.setCount(amount);
            return is;
        }
        return ItemStack.EMPTY;
    }

    public static boolean isInvalid(IItemMatcher matcher)
    {
        if (matcher == null)
            Foundry.LOGGER.error("Null IItemMatcher! Instance: " + matcher);
        if (matcher.getItem().isEmpty())
            Foundry.LOGGER.error("Invalid IItemMatcher with an empty match stack! Instance: " + matcher);
        if (matcher.getItems().isEmpty())
            Foundry.LOGGER.error("Invalid IItemMatcher with an empty match list! Instance: " + matcher);
        return matcher == null || matcher.getItem().isEmpty() || matcher.getItems().isEmpty();
    }

    @SideOnly(Side.CLIENT)
    public static void localizeTooltip(String key, List<String> tooltip)
    {
        // Could eventually be replaced by reflection?
        String replaced = new TextComponentTranslation(key).getUnformattedText()
                .replace("$CRUCIBLE_BASIC_MAX_TEMP", Integer.toString(FoundryAPI.CRUCIBLE_BASIC_MAX_TEMP/100))
                .replace("$CRUCIBLE_STANDARD_MAX_TEMP", Integer.toString(FoundryAPI.CRUCIBLE_STANDARD_MAX_TEMP/100))
                .replace("$CRUCIBLE_ADVANCED_MAX_TEMP", Integer.toString(FoundryAPI.CRUCIBLE_ADVANCED_MAX_TEMP/100));

        for (String str : replaced.split("//"))
        {
            tooltip.add(TextFormatting.GRAY + str);
        }
    }

    public static void registerCasting(ItemStack item, Fluid liquid_metal, int ingots, ItemMold.SubItem mold_meta)
    {
        registerCasting(item, new FluidStack(liquid_metal, FoundryAPI.FLUID_AMOUNT_INGOT * ingots), mold_meta, null);
    }

    public static void registerCasting(ItemStack item, Fluid liquid_metal, int ingots, ItemMold.SubItem mold_meta, IItemMatcher extra)
    {
        registerCasting(item, new FluidStack(liquid_metal, FoundryAPI.FLUID_AMOUNT_INGOT * ingots), mold_meta, extra);
    }

    public static void registerCasting(ItemStack item, FluidStack fluid, ItemMold.SubItem mold_meta, IItemMatcher extra)
    {
        registerCasting(item, fluid, mold_meta, false, extra);
    }

    public static void registerCasting(ItemStack item, FluidStack fluid, ItemMold.SubItem mold_meta, boolean consume_mold, IItemMatcher extra)
    {
        if (!item.isEmpty())
        {
            ItemStack mold = mold_meta.getItem();
            ItemStack extra_item = extra != null ? extra.getItem() : ItemStack.EMPTY;
            if (CastingRecipeManager.INSTANCE.findRecipe(
                    new FluidStack(fluid.getFluid(), FoundryAPI.CASTER_TANK_CAPACITY), mold, extra_item) == null)
            {
                CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(item), fluid, mold, consume_mold, extra);
            }
        }
        else
            Foundry.LOGGER.error(
                    "Attempted to add a casting recipe with an invalid output!  Item: {}, Fluid: {}, Mold: {}, Extra: {}",
                    item, fluid, mold_meta, extra);
    }

    /**
     * Register item in the ore dictionary only if it's not already registered.
     * @param name Ore Dictionary name.
     * @param stack Item to register.
     */
    public static void registerInOreDictionary(String name, ItemStack stack)
    {
        if (!stack.isEmpty() && !FoundryUtils.isItemInOreDictionary(name, stack))
            OreDictionary.registerOre(name, stack);
    }

    public static String upperCaseFirstChar(String s)
    {
        StringBuffer sb = new StringBuffer(s);
        for (int i = 0; i < sb.length() - 1; i++)
        {
            if (i == 0)
            {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
            else if (sb.charAt(i) == '_')
            {
                sb.deleteCharAt(i);
                sb.setCharAt(i, Character.toUpperCase(sb.charAt(i)));
            }
        }
        return sb.toString();
    }

}
