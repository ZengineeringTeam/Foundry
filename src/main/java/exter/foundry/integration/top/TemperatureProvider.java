package exter.foundry.integration.top;

import exter.foundry.Foundry;
import exter.foundry.block.BlockMachine;
import exter.foundry.tileentity.TileEntityHeatable;
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

import java.awt.*;

public class TemperatureProvider implements IProbeInfoProvider
{
    @Override
    public String getID() {
        return Foundry.MODID + ":temperature";
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data)
    {
        if (blockState.getBlock() instanceof BlockMachine)
        {
            TileEntity tile = world.getTileEntity(data.getPos());
            if (tile instanceof TileEntityHeatable)
            {
                TileEntityHeatable tileHeatable = (TileEntityHeatable) tile;

                probeInfo.text(TextFormatting.GOLD + I18n.translateToLocalFormatted(Foundry.MODID + ".compat.top.temperature"))
                        .element(new ElementTemperature(tileHeatable.getTemperature() / 100, tileHeatable.getMaxTemperature() / 100, new Color(255, 170, 0).hashCode()));
            }
        }
    }
}
