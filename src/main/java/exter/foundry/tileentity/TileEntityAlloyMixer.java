package exter.foundry.tileentity;

import java.util.ArrayList;
import java.util.List;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.IAlloyMixerRecipe;
import exter.foundry.recipes.AlloyMixerRecipe;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.FluidTankPropertiesWrapper;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TileEntityAlloyMixer extends TileEntityPowered
{
    protected class FluidHandler implements IFluidHandler
    {
        private final IFluidTankProperties[] props;

        public FluidHandler()
        {
            props = new IFluidTankProperties[getTankCount()];
            for (int i = 0; i < props.length; i++)
            {
                props[i] = new FluidTankPropertiesWrapper(getTank(i));
            }
        }

        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain)
        {
            return drainTank(TANK_OUTPUT, resource, doDrain);
        }

        @Override
        public FluidStack drain(int maxDrain, boolean doDrain)
        {
            return drainTank(TANK_OUTPUT, maxDrain, doDrain);
        }

        @Override
        public int fill(FluidStack resource, boolean doFill)
        {
            int i;
            int empty = -1;
            int partial = -1;
            for (i = 0; i < 4; i++)
            {
                FluidTank ft = tanks[i];
                if (ft.getFluidAmount() > 0)
                {
                    if (ft.getFluid().isFluidEqual(resource))
                    {
                        if (ft.getFluidAmount() < ft.getCapacity())
                        {
                            partial = i;
                        }
                        else
                        {
                            return 0;
                        }
                    }
                }
                else
                {
                    empty = i;
                }
            }

            if (partial != -1)
            {
                return fillTank(partial, resource, doFill);
            }
            if (empty != -1)
            {
                return fillTank(empty, resource, doFill);
            }
            return 0;
        }

        @Override
        public IFluidTankProperties[] getTankProperties()
        {
            return props;
        }
    }

    static public final int INVENTORY_CONTAINER_INPUT_0_INPUT = 0;
    static public final int INVENTORY_CONTAINER_INPUT_0_OUTPUT = 1;
    static public final int INVENTORY_CONTAINER_INPUT_1_INPUT = 2;
    static public final int INVENTORY_CONTAINER_INPUT_1_OUTPUT = 3;
    static public final int INVENTORY_CONTAINER_INPUT_2_INPUT = 4;
    static public final int INVENTORY_CONTAINER_INPUT_2_OUTPUT = 5;
    static public final int INVENTORY_CONTAINER_INPUT_3_INPUT = 6;
    static public final int INVENTORY_CONTAINER_INPUT_3_OUTPUT = 7;
    static public final int INVENTORY_CONTAINER_OUTPUT_INPUT = 8;
    static public final int INVENTORY_CONTAINER_OUTPUT_OUTPUT = 9;

    static public final int TANK_INPUT_0 = 0;
    static public final int TANK_INPUT_1 = 1;
    static public final int TANK_INPUT_2 = 2;
    static public final int TANK_INPUT_3 = 3;
    static public final int TANK_OUTPUT = 4;

    private final FluidTank[] tanks;
    private final IFluidHandler fluid_handler;

    public TileEntityAlloyMixer()
    {
        super();

        tanks = new FluidTank[5];
        for (int i = 0; i < tanks.length; i++)
        {
            tanks[i] = new FluidTank(FoundryAPI.ALLOYMIXER_TANK_CAPACITY);
        }
        fluid_handler = new FluidHandler();

        addContainerSlot(new ContainerSlot(TANK_INPUT_0, INVENTORY_CONTAINER_INPUT_0_INPUT,
                INVENTORY_CONTAINER_INPUT_0_OUTPUT, false));
        addContainerSlot(new ContainerSlot(TANK_INPUT_1, INVENTORY_CONTAINER_INPUT_1_INPUT,
                INVENTORY_CONTAINER_INPUT_1_OUTPUT, false));
        addContainerSlot(new ContainerSlot(TANK_INPUT_2, INVENTORY_CONTAINER_INPUT_2_INPUT,
                INVENTORY_CONTAINER_INPUT_2_OUTPUT, false));
        addContainerSlot(new ContainerSlot(TANK_INPUT_3, INVENTORY_CONTAINER_INPUT_3_INPUT,
                INVENTORY_CONTAINER_INPUT_3_OUTPUT, false));
        addContainerSlot(new ContainerSlot(TANK_OUTPUT, INVENTORY_CONTAINER_OUTPUT_INPUT,
                INVENTORY_CONTAINER_OUTPUT_OUTPUT, false));
    }

    @Override
    protected IFluidHandler getFluidHandler(EnumFacing facing)
    {
        return fluid_handler;
    }

    @Override
    public int getSizeInventory()
    {
        return 10;
    }

    @Override
    public FluidTank getTank(int slot)
    {
        return tanks[slot];
    }

    @Override
    public int getTankCount()
    {
        return 5;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return false;
    }

    private void mixAlloy()
    {
        if (getStoredFoundryEnergy() < 10)
        {
            return;
        }
        boolean do_mix = false;
        switch (getRedstoneMode())
        {
        case RSMODE_IGNORE:
            do_mix = true;
            break;
        case RSMODE_OFF:
            if (!redstone_signal && !last_redstone_signal)
            {
                do_mix = true;
            }
            break;
        case RSMODE_ON:
            if (redstone_signal && last_redstone_signal)
            {
                do_mix = true;
            }
            break;
        default:
            break;
        }
        if (!do_mix)
        {
            return;
        }

        int i;
        List<FluidStack> input_tank_fluids = new ArrayList<>(4);
        for (i = 0; i < 4; i++)
        {
            FluidStack fs = tanks[i].getFluid();
            if (fs != null)
            {
                input_tank_fluids.add(fs.copy());
            }
        }

        AlloyMixerRecipe.mergeFluids(input_tank_fluids);
        input_tank_fluids.removeIf(f -> f == null || f.amount == 0);
        IAlloyMixerRecipe recipe = AlloyMixerRecipeManager.INSTANCE.findRecipe(input_tank_fluids);
        if (recipe == null)
        {
            return;
        }
        int energy_used = 0;
        while (true)
        {
            if (energy_used >= 2500)
            {
                return;
            }
            input_tank_fluids.removeIf(f -> f == null || f.amount == 0);
            if (!recipe.matchesRecipe(input_tank_fluids))
            {
                return;
            }
            FluidStack output = recipe.getOutput();

            if (tanks[TANK_OUTPUT].fill(output, false) < output.amount)
            {
                return;
            }
            int required_energy = 10 * output.amount;
            if (useFoundryEnergy(required_energy, false) < required_energy)
            {
                return;
            }
            useFoundryEnergy(required_energy, true);
            energy_used += required_energy;
            tanks[TANK_OUTPUT].fill(output, true);
            List<FluidStack> inputs = recipe.getInputs();
            for (FluidStack input : inputs)
            {
                FluidStack shouldDrain = input.copy();
                for (FluidTank tank : tanks)
                {
                    if (shouldDrain.isFluidEqual(tank.getFluid()))
                    {
                        FluidStack drained = tank.drain(shouldDrain, true);
                        if (drained != null)
                        {
                            shouldDrain.amount -= drained.amount;
                        }
                        if (shouldDrain.amount <= 0)
                        {
                            continue;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void updateClient()
    {

    }

    @Override
    protected void updateServer()
    {
        super.updateServer();
        if (tanks[TANK_OUTPUT].getFluidAmount() < tanks[TANK_OUTPUT].getCapacity()
                && (tanks[TANK_INPUT_0].getFluidAmount() > 0 || tanks[TANK_INPUT_1].getFluidAmount() > 0
                        || tanks[TANK_INPUT_2].getFluidAmount() > 0 || tanks[TANK_INPUT_3].getFluidAmount() > 0))
        {
            mixAlloy();
        }
    }

    @Override
    public int getFoundryEnergyCapacity()
    {
        return 3000;
    }
}
