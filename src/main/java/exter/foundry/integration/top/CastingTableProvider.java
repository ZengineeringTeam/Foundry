package exter.foundry.integration.top;

import exter.foundry.Foundry;
import exter.foundry.block.BlockCastingTable;
import exter.foundry.tileentity.TileEntityCastingTableBase;
import mcjty.theoneprobe.api.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class CastingTableProvider implements IProbeInfoProvider
{
    @Override
    public String getID() {
        return Foundry.MODID + ":casting_table";
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data)
    {
        if (blockState.getBlock() instanceof BlockCastingTable)
        {
            TileEntity tile = world.getTileEntity(data.getPos());
            if (tile instanceof TileEntityCastingTableBase)
            {
                TileEntityCastingTableBase tileCastingTable = (TileEntityCastingTableBase) tile;
                
                if (tileCastingTable.getProgress() != 0)
                {
                    probeInfo.text(TextFormatting.GREEN + I18n.translateToLocalFormatted(Foundry.MODID + ".compat.top.progress"))
                            .progress(100 - tileCastingTable.getProgress() * 100 / tileCastingTable.getTotalTick(), 100, probeInfo.defaultProgressStyle().suffix("%"));
                }

                FluidTank fluidTank = tileCastingTable.getTank(0);
                FluidStack fluidStack = fluidTank.getFluid();
                if (fluidStack != null)
                {
                    int color;
                    if (fluidStack.getFluid().getColor(fluidStack) != 0xffffffff)
                        color = fluidStack.getFluid().getColor(fluidStack);
                    else
                        color = 0xff777777;
                    probeInfo.text(TextFormatting.WHITE + I18n.translateToLocalFormatted(Foundry.MODID + ".compat.top.fluid"))
                            .element(new ElementFluid(fluidStack.getLocalizedName(), fluidTank.getFluidAmount(), fluidTank.getCapacity(), color, player.isSneaking()));
                }
            }
        }
    }
}
