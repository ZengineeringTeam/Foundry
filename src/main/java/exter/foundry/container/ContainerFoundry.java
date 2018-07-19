package exter.foundry.container;

import exter.foundry.tileentity.TileEntityFoundry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;

public abstract class ContainerFoundry extends Container
{
    private final TileEntityFoundry te;

    public ContainerFoundry(TileEntityFoundry te)
    {
        this.te = te;
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        for (IContainerListener listener : listeners)
        {
            if (listener instanceof EntityPlayerMP)
            {
                te.sendDataToClientSide((EntityPlayerMP) listener);
            }
        }
    }
}
