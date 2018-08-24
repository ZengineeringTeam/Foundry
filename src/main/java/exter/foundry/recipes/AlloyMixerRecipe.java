package exter.foundry.recipes;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

import exter.foundry.api.recipe.IAlloyMixerRecipe;
import net.minecraftforge.fluids.FluidStack;
import scala.actors.threadpool.Arrays;

/*
 * Alloy Mixer recipe manager
 */
public class AlloyMixerRecipe implements IAlloyMixerRecipe
{
    public List<FluidStack> inputs;

    public FluidStack output;

    public AlloyMixerRecipe(FluidStack out, FluidStack[] in)
    {
        output = out.copy();
        if (in == null || in.length == 0)
        {
            throw new IllegalArgumentException("Alloy mixer recipe inputs cannot be null");
        }
        if (in.length > 4)
        {
            throw new IllegalArgumentException("Alloy mixer recipe cannot have more the 4 inputs");
        }

        inputs = ImmutableList.copyOf(sortedFluids(Arrays.asList(in)));

        if (inputs.contains(null))
        {
            throw new IllegalArgumentException("Alloy mixer recipe input cannot be null");
        }
    }

    @Override
    public List<FluidStack> getInputs()
    {
        return inputs;
    }

    @Override
    public FluidStack getOutput()
    {
        return output.copy();
    }

    @Override
    public boolean matchesRecipe(List<FluidStack> ins)
    {
        if (inputs.size() != ins.size())
        {
            return false;
        }
        ins = sortedFluids(ins);
        for (int i = 0; i < inputs.size(); i++)
        {
            FluidStack input = inputs.get(i);
            FluidStack in = ins.get(i);
            if (!input.isFluidEqual(in) || in.amount < input.amount)
            {
                return false;
            }
        }
        return true;
    }

    public static List<FluidStack> sortedFluids(List<FluidStack> fluidStacks)
    {
        return fluidStacks.stream().sorted(new Comp()).collect(Collectors.toList());
    }

    public static void mergeFluids(List<FluidStack> fluidStacks)
    {
        for (int i = 0; i + 1 < fluidStacks.size(); i++)
        {
            FluidStack fs = fluidStacks.get(i);
            for (int j = i + 1; j < fluidStacks.size(); j++)
            {
                if (fs.isFluidEqual(fluidStacks.get(j)))
                {
                    fluidStacks.get(j).amount += fs.amount;
                    fluidStacks.set(i, null);
                }
            }
        }
    }

    public static class Comp implements Comparator<FluidStack>
    {
        @Override
        public int compare(FluidStack o1, FluidStack o2)
        {
            return Integer.compare(o1.hashCode(), o2.hashCode());
        }
    }
}
