package exter.foundry.tileentity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.heatable.IHeatProvider;
import exter.foundry.api.recipe.IBurnerHeaterFuel;
import exter.foundry.block.BlockBurnerHeater;
import exter.foundry.config.FoundryConfig;
import exter.foundry.recipes.manager.BurnerHeaterFuelManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.IItemHandler;
import vazkii.botania.api.item.IExoflameHeatable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Optional.Interface(iface = "vazkii.botania.api.item.IExoflameHeatable", modid = "Botania")
public class TileEntityBurnerHeater extends TileEntityFoundry implements IExoflameHeatable
{
    private boolean updated = false;

    private class HeatProvider implements IHeatProvider
    {
        @Override
        public int provideHeat(int max_heat, int heat)
        {
            if (fuels.stream().filter(Fuel::isBurning).count() < 4)
            {
                tryBurnNextFuel();
            }
            if (isBurning())
            {
                heat = (int) (getSumBoost() * getHeat());
                return heat;
            }
            return 0;
        }
    }

    public double getSumBoost()
    {
        int count = (int) fuels.stream().filter(Fuel::isBurning).count();
        if (count == 0)
        {
            return 0;
        }
        if (count == fuels.size())
        {
            return 1;
        }
        double sum = 0;
        for (Fuel fuel : fuels)
        {
            if (fuel.isBurning())
            {
                sum += fuel.getBoost();
            }
        }
        return 0.4D + count * 0.15D + sum * (fuels.size() - count) * 0.15D / count;
    }

    public int getHeat()
    {
        int sum = 0;
        int count = 0;
        for (Fuel fuel : fuels)
        {
            if (fuel.isBurning())
            {
                ++count;
                sum += fuel.heat;
            }
        }
        return count == 0 ? TileEntityHeatable.TEMP_MIN : sum / count;
    }

    private static final Set<Integer> IH_SLOTS_FUEL = ImmutableSet.of(0, 1, 2, 3);

    public static class Fuel implements INBTSerializable<NBTTagCompound>
    {
        public int burnTime;
        public int totalBurnTime;
        public int heat;

        public boolean isBurning()
        {
            return totalBurnTime > 0;
        }

        public double getBoost()
        {
            if (totalBurnTime == 0)
            {
                return 0;
            }
            return (double) burnTime / totalBurnTime;
        }

        @Override
        public NBTTagCompound serializeNBT()
        {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("time", burnTime);
            tag.setInteger("total", totalBurnTime);
            return tag;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt)
        {
            if (nbt != null)
            {
                burnTime = nbt.hasKey("time") ? nbt.getInteger("time") : burnTime;
                totalBurnTime = nbt.hasKey("total") ? nbt.getInteger("total") : totalBurnTime;
            }
        }
    }

    private List<Fuel> fuels = ImmutableList.of(new Fuel(), new Fuel(), new Fuel(), new Fuel());

    private final HeatProvider heat_provider;
    private final ItemHandler item_handler;

    public TileEntityBurnerHeater()
    {
        heat_provider = new HeatProvider();
        item_handler = new ItemHandler(getSizeInventory(), IH_SLOTS_FUEL, IH_SLOTS_FUEL);
    }

    @Optional.Method(modid = "Botania")
    @Override
    public void boostBurnTime()
    {
        if (!world.isRemote)
        {
            for (Fuel fuel : fuels)
            {
                if (!fuel.isBurning())
                {
                    fuel.burnTime = fuel.totalBurnTime = 200;
                    fuel.heat = FoundryConfig.default_burner_exoflame_heat;
                }
            }
            ((BlockBurnerHeater) getBlockType()).setMachineState(world, getPos(), world.getBlockState(getPos()),
                    isBurning());
            markDirty();
        }
    }

    @Optional.Method(modid = "Botania")
    @Override
    public void boostCookTime()
    {
    }

    @Optional.Method(modid = "Botania")
    @Override
    public boolean canSmelt()
    {
        return !fuels.stream().allMatch(Fuel::isBurning);
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
    }

    @Optional.Method(modid = "Botania")
    @Override
    public int getBurnTime()
    {
        int time = 0;
        for (Fuel fuel : fuels)
        {
            if (time == 0 || time > fuel.burnTime)
            {
                time = fuel.burnTime;
            }
        }
        return time;
    }

    @Override
    public <T> T getCapability(Capability<T> cap, EnumFacing facing)
    {
        if (cap == FoundryAPI.HEAT_PROVIDER_CAP && facing == EnumFacing.UP)
        {
            return FoundryAPI.HEAT_PROVIDER_CAP.cast(heat_provider);
        }
        return super.getCapability(cap, facing);
    }

    @Override
    protected IItemHandler getItemHandler(EnumFacing side)
    {
        return item_handler;
    }

    @Override
    public int getSizeInventory()
    {
        return 4;
    }

    @Override
    public FluidTank getTank(int slot)
    {
        return null;
    }

    @Override
    public int getTankCount()
    {
        return 0;
    }

    @Override
    public boolean hasCapability(Capability<?> cap, EnumFacing facing)
    {
        return super.hasCapability(cap, facing) || (cap == FoundryAPI.HEAT_PROVIDER_CAP && facing == EnumFacing.UP);
    }

    public boolean isBurning()
    {
        return fuels.stream().anyMatch(Fuel::isBurning);
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        if (stack.getItem() == Items.LAVA_BUCKET)
        {
            return false;
        }
        return BurnerHeaterFuelManager.INSTANCE.getFuel(stack) != null || TileEntityFurnace.isItemFuel(stack);
    }

    @Override
    protected void onInitialize()
    {
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    protected void updateClient()
    {
    }

    @Override
    protected void updateServer()
    {
        List<Fuel> burnings = fuels.stream().filter(Fuel::isBurning).collect(Collectors.toList());
        if (burnings.size() > 0)
        {
            int times = Math.max(1, burnings.size() - 1);
            for (int i = 0; i < times; i++)
            {
                int index = getWorld().rand.nextInt(burnings.size());
                Fuel fuel = burnings.get(index);
                if (--fuel.burnTime == 0)
                {
                    fuel.heat = TileEntityHeatable.TEMP_MIN;
                    fuel.totalBurnTime = 0;
                    burnings.remove(index);
                    updated = true;
                }
            }
        }

        if (updated)
        {
            ((BlockBurnerHeater) getBlockType()).setMachineState(world, getPos(), world.getBlockState(getPos()),
                    isBurning());
            updated = false;
        }
    }

    private void tryBurnNextFuel()
    {
        for (int i = 0; i < fuels.size(); i++)
        {
            if (!fuels.get(i).isBurning())
            {
                if (tryBurnItemInSlot(i))
                {
                    updated = true;
                }
            }
        }
    }

    private boolean tryBurnItemInSlot(int slot)
    {
        ItemStack stack = getStackInSlot(slot);
        if (!stack.isEmpty())
        {
            if (stack.getItem() == Items.LAVA_BUCKET)
            {
                return false;
            }
            Fuel fuel = fuels.get(slot);
            IBurnerHeaterFuel ifuel = BurnerHeaterFuelManager.INSTANCE.getFuel(stack);
            if (ifuel != null)
            {
                fuel.totalBurnTime = fuel.burnTime = ifuel.getBurnTime();
                fuel.heat = ifuel.getHeat();
            }
            else
            {
                int time = TileEntityFurnace.getItemBurnTime(stack);
                if (time <= 0)
                {
                    return false;
                }
                fuel.totalBurnTime = fuel.burnTime = time;
                fuel.heat = FoundryConfig.default_burner_fuel_heat;
            }
            ItemStack stackCopy = stack.copy();
            stack.shrink(1);
            if (stack.isEmpty())
            {
                setInventorySlotContents(slot, stackCopy.getItem().getContainerItem(stackCopy));
            }
            updateInventoryItem(slot);
            return true;
        }
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        for (int i = 0; i < fuels.size(); i++)
        {
            fuels.get(i).deserializeNBT(tag.getCompoundTag("Fuel_" + i));
        }
        if (world != null && !world.isRemote)
        {
            ((BlockBurnerHeater) getBlockType()).setMachineState(world, getPos(), world.getBlockState(getPos()),
                    isBurning());
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        for (int i = 0; i < fuels.size(); i++)
        {
            tag.setTag("Fuel_" + i, fuels.get(i).serializeNBT());
        }

        return tag;
    }
}
