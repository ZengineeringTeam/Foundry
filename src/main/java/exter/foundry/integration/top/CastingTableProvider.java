package exter.foundry.integration.top;

import exter.foundry.Foundry;
import exter.foundry.block.BlockCastingTable;
import exter.foundry.tileentity.TileEntityCastingTableBase;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.config.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

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
                            .progress(100 - tileCastingTable.getProgress() / 2, 100, probeInfo.defaultProgressStyle().suffix("%"));
                }
            }
        }
    }
}
