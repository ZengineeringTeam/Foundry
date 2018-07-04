package exter.foundry.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileEntityRefractoryTankBasic extends TileEntityFoundry
{
    static public final int INVENTORY_CONTAINER_DRAIN = 0;
    static public final int INVENTORY_CONTAINER_FILL = 1;

    private final FluidTank tank;
    private final IFluidHandler fluid_handler;

    public TileEntityRefractoryTankBasic()
    {

        tank = new FluidTank(getTankCapacity());
        fluid_handler = new FluidHandler(0, 0);

        addContainerSlot(new ContainerSlot(0, INVENTORY_CONTAINER_DRAIN, false));
        addContainerSlot(new ContainerSlot(0, INVENTORY_CONTAINER_FILL, true));
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {

    }

    @Override
    protected IFluidHandler getFluidHandler(EnumFacing facing)
    {
        return fluid_handler;
    }

    @Override
    public int getSizeInventory()
    {
        return 2;
    }

    @Override
    public FluidTank getTank(int slot)
    {
        return tank;
    }

    protected int getTankCapacity()
    {
        return 16000;
    }

    @Override
    public int getTankCount()
    {
        return 1;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return false;
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
    protected void updateClient()
    {

    }

    @Override
    protected void updateServer()
    {
    }
}
