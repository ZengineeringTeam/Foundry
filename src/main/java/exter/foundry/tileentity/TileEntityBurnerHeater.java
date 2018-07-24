package exter.foundry.tileentity;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.heatable.IHeatProvider;
import exter.foundry.api.recipe.IBurnerHeaterFuel;
import exter.foundry.block.BlockBurnerHeater;
import exter.foundry.recipes.manager.BurnerHeaterFuelManager;
import exter.foundry.tileentity.itemhandler.ItemHandlerFuel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.IItemHandler;
import vazkii.botania.api.item.IExoflameHeatable;

@Optional.Interface(iface = "vazkii.botania.api.item.IExoflameHeatable", modid = "Botania")
public class TileEntityBurnerHeater extends TileEntityFoundry implements IExoflameHeatable
{
    private class HeatProvider implements IHeatProvider
    {
        @Override
        public int provideHeat(int max_heat, int heat)
        {
            if (isBurning())
            {
                heat = (int) (heat_provide * (0.4D + ((double) burn_time / (double) item_burn_time) * 0.6D));
                return heat;
            }

            return 0;
        }
    }

    static private final Set<Integer> IH_SLOTS_INPUT = ImmutableSet.of(0, 1, 2, 3);
    static private final Set<Integer> IH_SLOTS_OUTPUT = ImmutableSet.of();
    static private final Set<Integer> IH_SLOTS_FUEL = ImmutableSet.of(0, 1, 2, 3);

    private int burn_time;
    private int item_burn_time;

    private int heat_provide;

    private final HeatProvider heat_provider;
    private final ItemHandlerFuel item_handler;

    public TileEntityBurnerHeater()
    {
        burn_time = item_burn_time = 0;
//        heat_provide = DEFAULT_HEAT_PROVIDE;
        heat_provider = new HeatProvider();
        item_handler = new ItemHandlerFuel(this, getSizeInventory(), IH_SLOTS_INPUT, IH_SLOTS_OUTPUT, IH_SLOTS_FUEL);
    }

    @Optional.Method(modid = "Botania")
    @Override
    public void boostBurnTime()
    {
        if (!world.isRemote)
        {
//            heat_provide = DEFAULT_HEAT_PROVIDE;
            item_burn_time = burn_time = 200;
            ((BlockBurnerHeater) getBlockType()).setMachineState(world, getPos(), world.getBlockState(getPos()),
                    burn_time > 0);
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
        return true;
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {

    }

    public int getBurningTime()
    {
        return burn_time;
    }

    @Optional.Method(modid = "Botania")
    @Override
    public int getBurnTime()
    {
        return burn_time <= 1 ? 0 : burn_time - 1;
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
    public int getInventoryStackLimit()
    {
        return 64;
    }

    public int getItemBurnTime()
    {
        return item_burn_time;
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
        return super.hasCapability(cap, facing) || cap == FoundryAPI.HEAT_PROVIDER_CAP && facing == EnumFacing.UP;
    }

    public boolean isBurning()
    {
        return burn_time > 0;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        return TileEntityFurnace.isItemFuel(stack);
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return world.getTileEntity(getPos()) != this ? false : par1EntityPlayer.getDistanceSq(getPos()) <= 64.0D;
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
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        if (tag.hasKey("BurnTime"))
        {
            burn_time = tag.getInteger("BurnTime");
        }
        if (tag.hasKey("ItemBurnTime"))
        {
            item_burn_time = tag.getInteger("ItemBurnTime");
        }
        if (tag.hasKey("HeatProvide"))
        {
            heat_provide = tag.getInteger("HeatProvide");
        }
        if (world != null && !world.isRemote)
        {
            ((BlockBurnerHeater) getBlockType()).setMachineState(world, getPos(), world.getBlockState(getPos()),
                    burn_time > 0);
        }
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
        int last_burn_time = burn_time;

        if (--burn_time <= 0) // TODO: do not consume when no receiver
        {
            for (int i = 0; i < 4; i++)
            {
                ItemStack item = getStackInSlot(i);
                if (!item.isEmpty())
                {
                    int burn = 0;
                    IBurnerHeaterFuel fuel = BurnerHeaterFuelManager.INSTANCE.getFuel(item);
                    if (fuel != null)
                    {
                        burn = fuel.getBurnTime();
                        heat_provide = fuel.getHeat();
                    }
                    else
                    {
                        burn = TileEntityFurnace.getItemBurnTime(item);
//                        heat_provide = DEFAULT_HEAT_PROVIDE;
                    }
                    if (burn > 0)
                    {
                        burn_time += burn;
                        item_burn_time = burn_time;
                        item.shrink(1);
                        if (item.isEmpty())
                        {
                            setStackInSlot(i, item.getItem().getContainerItem(item));
                        }
                        updateInventoryItem(i);
                        break;
                    }
                }
            }
        }

        if (last_burn_time != burn_time)
        {
            if (last_burn_time == 0 || burn_time == 0)
            {
                ((BlockBurnerHeater) getBlockType()).setMachineState(world, getPos(), world.getBlockState(getPos()),
                        burn_time > 0);
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        if (compound == null)
        {
            compound = new NBTTagCompound();
        }
        super.writeToNBT(compound);
        compound.setInteger("BurnTime", burn_time);
        compound.setInteger("ItemBurnTime", item_burn_time);
        compound.setInteger("HeatProvide", heat_provide);
        return compound;
    }
}
