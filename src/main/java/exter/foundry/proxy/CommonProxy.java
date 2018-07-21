package exter.foundry.proxy;

import exter.foundry.client.gui.GuiAlloyMixer;
import exter.foundry.client.gui.GuiAlloyingCrucible;
import exter.foundry.client.gui.GuiBurnerHeater;
import exter.foundry.client.gui.GuiFluidHeater;
import exter.foundry.client.gui.GuiMaterialRouter;
import exter.foundry.client.gui.GuiMeltingCrucible;
import exter.foundry.client.gui.GuiMetalCaster;
import exter.foundry.client.gui.GuiMetalInfuser;
import exter.foundry.client.gui.GuiMoldStation;
import exter.foundry.container.ContainerAlloyMixer;
import exter.foundry.container.ContainerAlloyingCrucible;
import exter.foundry.container.ContainerBurnerHeater;
import exter.foundry.container.ContainerFluidHeater;
import exter.foundry.container.ContainerMaterialRouter;
import exter.foundry.container.ContainerMeltingCrucible;
import exter.foundry.container.ContainerMetalCaster;
import exter.foundry.container.ContainerMetalInfuser;
import exter.foundry.container.ContainerMoldStation;
import exter.foundry.tileentity.TileEntityAlloyMixer;
import exter.foundry.tileentity.TileEntityAlloyingCrucible;
import exter.foundry.tileentity.TileEntityBurnerHeater;
import exter.foundry.tileentity.TileEntityFluidHeater;
import exter.foundry.tileentity.TileEntityMaterialRouter;
import exter.foundry.tileentity.TileEntityMeltingCrucibleBasic;
import exter.foundry.tileentity.TileEntityMetalCaster;
import exter.foundry.tileentity.TileEntityMetalInfuser;
import exter.foundry.tileentity.TileEntityMoldStation;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler
{
    public static final int GUI_CRUCIBLE = 0;
    public static final int GUI_CASTER = 1;
    public static final int GUI_ALLOY_MIXER = 2;
    public static final int GUI_INFUSER = 3;
    public static final int GUI_MATERIAL_ROUTER = 4;
    public static final int GUI_MOLD_STATION = 5;
    public static final int GUI_BURNER_HEATER = 6;
    public static final int GUI_ALLOYING_CRUCIBLE = 7;
    public static final int GUI_FLUID_HEATER = 8;

    @Override
    public GuiContainer getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);
        switch (ID)
        {
        case GUI_CRUCIBLE:
        {
            TileEntityMeltingCrucibleBasic te = (TileEntityMeltingCrucibleBasic) world.getTileEntity(pos);
            return new GuiMeltingCrucible(te, player);
        }
        case GUI_CASTER:
        {
            TileEntityMetalCaster te = (TileEntityMetalCaster) world.getTileEntity(pos);
            return new GuiMetalCaster(te, player);
        }
        case GUI_ALLOY_MIXER:
        {
            TileEntityAlloyMixer te = (TileEntityAlloyMixer) world.getTileEntity(pos);
            return new GuiAlloyMixer(te, player);
        }
        case GUI_INFUSER:
        {
            TileEntityMetalInfuser te = (TileEntityMetalInfuser) world.getTileEntity(pos);
            return new GuiMetalInfuser(te, player);
        }
        case GUI_MATERIAL_ROUTER:
        {
            TileEntityMaterialRouter te = (TileEntityMaterialRouter) world.getTileEntity(pos);
            return new GuiMaterialRouter(te, player);
        }
        case GUI_MOLD_STATION:
        {
            TileEntityMoldStation te = (TileEntityMoldStation) world.getTileEntity(pos);
            return new GuiMoldStation(te, player);
        }
        case GUI_BURNER_HEATER:
        {
            TileEntityBurnerHeater te = (TileEntityBurnerHeater) world.getTileEntity(pos);
            return new GuiBurnerHeater(te, player);
        }
        case GUI_ALLOYING_CRUCIBLE:
        {
            TileEntityAlloyingCrucible te = (TileEntityAlloyingCrucible) world.getTileEntity(pos);
            return new GuiAlloyingCrucible(te, player);
        }
        case GUI_FLUID_HEATER:
        {
            TileEntityFluidHeater te = (TileEntityFluidHeater) world.getTileEntity(pos);
            return new GuiFluidHeater(te, player);
        }
        }
        return null;
    }

    @Override
    public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);
        switch (ID)
        {
        case GUI_CRUCIBLE:
            return new ContainerMeltingCrucible((TileEntityMeltingCrucibleBasic) world.getTileEntity(pos), player);
        case GUI_CASTER:
            return new ContainerMetalCaster((TileEntityMetalCaster) world.getTileEntity(pos), player);
        case GUI_ALLOY_MIXER:
            return new ContainerAlloyMixer((TileEntityAlloyMixer) world.getTileEntity(pos), player);
        case GUI_INFUSER:
            return new ContainerMetalInfuser((TileEntityMetalInfuser) world.getTileEntity(pos), player);
        case GUI_MATERIAL_ROUTER:
            return new ContainerMaterialRouter((TileEntityMaterialRouter) world.getTileEntity(pos), player);
        case GUI_MOLD_STATION:
            return new ContainerMoldStation((TileEntityMoldStation) world.getTileEntity(pos), player);
        case GUI_BURNER_HEATER:
            return new ContainerBurnerHeater((TileEntityBurnerHeater) world.getTileEntity(pos), player);
        case GUI_ALLOYING_CRUCIBLE:
            return new ContainerAlloyingCrucible((TileEntityAlloyingCrucible) world.getTileEntity(pos), player);
        case GUI_FLUID_HEATER:
            return new ContainerFluidHeater((TileEntityFluidHeater) world.getTileEntity(pos), player);
        }
        return null;
    }

    public void init()
    {
    }

    public void postInit()
    {
    }

    public void preInit()
    {
    }

    public String translate(String string, Object... args)
    {
        return I18n.format(string, args);
    }
}
