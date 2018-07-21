package exter.foundry.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import exter.foundry.Foundry;
import exter.foundry.container.ContainerFluidHeater;
import exter.foundry.tileentity.TileEntityFluidHeater;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiFluidHeater extends GuiFoundry
{
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Foundry.MODID, "textures/gui/tank.png");

    private static final int TANK_HEIGHT = 44;

    private static final int TANK_X = 64;
    private static final int TANK_Y = 20;

    private static final int TANK_OVERLAY_X = 176;
    private static final int TANK_OVERLAY_Y = 0;

    private final TileEntityFluidHeater te_heater;
    private final String STRING_MACHINE;

    public GuiFluidHeater(TileEntityFluidHeater heater, EntityPlayer player)
    {
        super(new ContainerFluidHeater(heater, player), player.inventory);
        allowUserInput = false;
        ySize = 165;
        te_heater = heater;
        STRING_MACHINE = I18n.format("tile.foundry.fluid_heater.name");
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(GUI_TEXTURE);
        int window_x = (width - xSize) / 2;
        int window_y = (height - ySize) / 2;
        drawTexturedModalRect(window_x, window_y, 0, 0, xSize, ySize);

        displayTank(window_x, window_y, TANK_X, TANK_Y, TANK_HEIGHT, TANK_OVERLAY_X, TANK_OVERLAY_Y,
                te_heater.getTank(0));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouse_x, int mouse_y)
    {
        super.drawGuiContainerForegroundLayer(mouse_x, mouse_y);

        fontRenderer.drawString(STRING_MACHINE, xSize / 2 - fontRenderer.getStringWidth(STRING_MACHINE) / 2, 6,
                0x404040);
        fontRenderer.drawString(getInventoryName(), 8, ySize - 96 + 2, 0x404040);
    }

    @Override
    public void drawScreen(int mousex, int mousey, float par3)
    {
        super.drawScreen(mousex, mousey, par3);

        if (isPointInRegion(TANK_X, TANK_Y, 16, TANK_HEIGHT, mousex, mousey))
        {
            List<String> currenttip = new ArrayList<>(1);
            addTankTooltip(currenttip, mousex, mousey, te_heater.getTank(0));
            drawHoveringText(currenttip, mousex, mousey, fontRenderer);
        }
    }

    @Override
    protected ResourceLocation getGUITexture()
    {
        return GUI_TEXTURE;
    }
}
