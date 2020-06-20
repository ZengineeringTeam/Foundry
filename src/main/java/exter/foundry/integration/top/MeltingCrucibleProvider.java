package exter.foundry.integration.top;

import exter.foundry.Foundry;
import exter.foundry.block.BlockMachine;
import exter.foundry.tileentity.TileEntityMeltingCrucibleBasic;
import mcjty.theoneprobe.api.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.awt.*;

public class MeltingCrucibleProvider implements IProbeInfoProvider
{
    @Override
    public String getID() {
        return Foundry.MODID + ":melting_crucible";
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data)
    {
        if (blockState.getBlock() instanceof BlockMachine)
        {
            TileEntity tile = world.getTileEntity(data.getPos());
            if (tile instanceof TileEntityMeltingCrucibleBasic)
            {
                TileEntityMeltingCrucibleBasic tileMeltingCrucible = (TileEntityMeltingCrucibleBasic) tile;
                //Draw Temperature
                probeInfo.text(TextFormatting.GOLD + I18n.translateToLocalFormatted(Foundry.MODID + ".compat.top.temperature"))
                        .element(new ElementTemperature(tileMeltingCrucible.getTemperature(), tileMeltingCrucible.getMaxTemperature()));
                //Draw Progress
                int progress = tileMeltingCrucible.getProgress() * 100 / TileEntityMeltingCrucibleBasic.SMELT_TIME;
                if (progress != 0)
                {
                    probeInfo.text(TextFormatting.GREEN + I18n.translateToLocalFormatted(Foundry.MODID + ".compat.top.progress"))
                            .horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                            .item(tileMeltingCrucible.getStackInSlot(TileEntityMeltingCrucibleBasic.INVENTORY_INPUT))
                            .progress(progress, 100, probeInfo.defaultProgressStyle().suffix("%"));
                }
                //Draw Fluid
                FluidTank fluidTank = tileMeltingCrucible.getTank(0);
                FluidStack fluidStack = fluidTank.getFluid();
                if (fluidStack != null)
                {
                    int color;
                    if (fluidStack.getFluid().getColor(fluidStack) != 0xffffffff)
                        color = fluidStack.getFluid().getColor(fluidStack);
                    else if (fluidStack.getFluid() == FluidRegistry.WATER)
                        color = new Color(52, 95, 218).hashCode();
                    else if (fluidStack.getFluid() == FluidRegistry.LAVA)
                        color = new Color(230, 145, 60).hashCode();
                    else
                        color = 0xff777777;
                    probeInfo.text(TextFormatting.WHITE + I18n.translateToLocalFormatted(Foundry.MODID + ".compat.top.fluid"))
                            .element(new ElementFluid(fluidStack.getLocalizedName(), fluidTank.getFluidAmount(), fluidTank.getCapacity(), color, player.isSneaking()));
                }
            }
        }
    }
}
