package exter.foundry.integration.top;

import exter.foundry.Foundry;
import exter.foundry.block.BlockMoldStation;
import exter.foundry.tileentity.TileEntityMoldStation;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class MoldStationProvider implements IProbeInfoProvider
{
    @Override
    public String getID() {
        return Foundry.MODID + ":mold_station";
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data)
    {
        if (blockState.getBlock() instanceof BlockMoldStation)
        {
            TileEntity tile = world.getTileEntity(data.getPos());
            if (tile instanceof TileEntityMoldStation)
            {
                TileEntityMoldStation tileMoldStation = (TileEntityMoldStation) tile;

                if (tileMoldStation.getProgress() != 0)
                {
                    probeInfo.text(TextFormatting.GREEN + I18n.translateToLocalFormatted(Foundry.MODID + ".compat.top.progress"))
                            .progress(tileMoldStation.getProgress() / 2, 100, probeInfo.defaultProgressStyle().suffix("%"));
                }
            }
        }
    }
}
