package exter.foundry.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.opengl.GL11;

import exter.foundry.container.ContainerMoldStation;
import exter.foundry.gui.button.GuiButtonFoundry;
import exter.foundry.tileentity.TileEntityMoldStation;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMoldStation extends GuiFoundry {

	public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("foundry:textures/gui/moldstation.png");

	public static final int BURN_X = 119;
	public static final int BURN_Y = 59;
	public static final int BURN_WIDTH = 14;
	public static final int BURN_HEIGHT = 14;

	public static final int PROGRESS_X = 116;
	public static final int PROGRESS_Y = 39;
	public static final int PROGRESS_WIDTH = 22;
	public static final int PROGRESS_HEIGHT = 15;

	public static final int PROGRESS_OVERLAY_X = 176;
	public static final int PROGRESS_OVERLAY_Y = 14;

	public static final int BURN_OVERLAY_X = 176;
	public static final int BURN_OVERLAY_Y = 0;

	public static final int BLOCK_X = 38;
	public static final int BLOCK_Y = 16;

	public static final int BLOCK_SIZE = 76;

	public static final int BLOCK_OVERLAY_X = 176;
	public static final int BLOCK_OVERLAY_Y = 31;

	public static final int GRID_X = BLOCK_X + 5;
	public static final int GRID_Y = BLOCK_Y + 5;

	public static final int GRID_SLOT_SIZE = 11;
	public static final int GRID_SIZE = GRID_SLOT_SIZE * 6;

	public static final int GRID_OVERLAY_X = 176;
	public static final int GRID_OVERLAY_Y = 107;

	private final TileEntityMoldStation te_ms;
	private GuiButtonFoundry button_fire;
	private final NonNullList<Boolean> pattern;
	private int processingState;
	
	private final String STRING_MOLD_STATION;
	private final String STRING_INVENTORY;
	private final String STRING_FIRE;

	public GuiMoldStation(TileEntityMoldStation af, EntityPlayer player) {
		super(new ContainerMoldStation(af, player));
		allowUserInput = false;
		ySize = 190;
		te_ms = af;
		pattern = NonNullList.withSize(36, Boolean.FALSE);
		processingState = -1; // No state.
		STRING_MOLD_STATION = I18n.format("gui.foundry.mold");
		STRING_INVENTORY = I18n.format("container.inventory");
		STRING_FIRE = I18n.format("gui.foundry.mold.fire");
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == button_fire.id) {
			te_ms.fire();
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(GUI_TEXTURE);
		int window_x = (width - xSize) / 2;
		int window_y = (height - ySize) / 2;
		drawTexturedModalRect(window_x, window_y, 0, 0, xSize, ySize);

		if (te_ms.getItemBurnTime() > 0) {
			int burn = te_ms.getBurningTime() * PROGRESS_HEIGHT / te_ms.getItemBurnTime();

			if (burn > 0) {
				drawTexturedModalRect(window_x + BURN_X, window_y + BURN_Y + BURN_HEIGHT - burn, BURN_OVERLAY_X, BURN_OVERLAY_Y + BURN_HEIGHT - burn, BURN_WIDTH, burn);
			}
		}
		if (te_ms.getProgress() > 0) {
			int progress = te_ms.getProgress() * PROGRESS_WIDTH / 200;
			drawTexturedModalRect(window_x + PROGRESS_X, window_y + PROGRESS_Y, PROGRESS_OVERLAY_X, PROGRESS_OVERLAY_Y, progress, PROGRESS_HEIGHT);
		}

		if (te_ms.hasBlock()) {
			drawTexturedModalRect(window_x + BLOCK_X, window_y + BLOCK_Y, BLOCK_OVERLAY_X, BLOCK_OVERLAY_Y, BLOCK_SIZE, BLOCK_SIZE);
			for (int i = 0; i < 36; i++) {
				int gx = i % 6;
				int gy = i / 6;
				int sv = te_ms.getGridSlot(i);
				if (processingState != -1 && pattern.get(i) == Boolean.TRUE)
                {
                    sv += (processingState == 0 ? 1 : -1) * (isShiftKeyDown() ? 4 : 1);
                }
				sv = MathHelper.clamp(sv, TileEntityMoldStation.MIN_DEPTH, TileEntityMoldStation.MAX_DEPTH);
				if (sv > 0)
                {
				    drawTexturedModalRect(window_x + GRID_X + gx * GRID_SLOT_SIZE, window_y + GRID_Y + gy * GRID_SLOT_SIZE, GRID_OVERLAY_X, GRID_OVERLAY_Y + (sv - 1) * GRID_SLOT_SIZE, GRID_SLOT_SIZE, GRID_SLOT_SIZE);
                }
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouse_x, int mouse_y) {
		super.drawGuiContainerForegroundLayer(mouse_x, mouse_y);

		fontRenderer.drawString(STRING_MOLD_STATION, 8, 6, 0x404040);
		fontRenderer.drawString(STRING_INVENTORY, 8, ySize - 96 + 2, 0x404040);
	}

	@Override
	public void drawScreen(int mousex, int mousey, float par3) {
		super.drawScreen(mousex, mousey, par3);

		//Draw tool tips.
		if (te_ms.hasBlock() && isPointInRegion(GRID_X, GRID_Y, GRID_SIZE - 1, GRID_SIZE - 1, mousex, mousey)) {
			int x = (mousex - GRID_X - guiLeft) / GRID_SLOT_SIZE;
			int y = (mousey - GRID_Y - guiTop) / GRID_SLOT_SIZE;

			List<String> currenttip = new ArrayList<>(1);
			int depth = te_ms.getGridSlot(y * 6 + x);
			if (processingState != -1 && pattern.get(y * 6 + x) == Boolean.TRUE)
            {
			    depth += (processingState == 0 ? 1 : -1) * (isShiftKeyDown() ? 4 : 1);
            }
			depth = MathHelper.clamp(depth, TileEntityMoldStation.MIN_DEPTH, TileEntityMoldStation.MAX_DEPTH);
			currenttip.add(I18n.format("gui.foundry.mold.depth", depth));
			drawHoveringText(currenttip, mousex, mousey, fontRenderer);
		}

		if (isPointInRegion(117, 15, button_fire.width, button_fire.height, mousex, mousey)) {
			List<String> currenttip = new ArrayList<>(1);
			currenttip.add(STRING_FIRE);
			drawHoveringText(currenttip, mousex, mousey, fontRenderer);
		}
	}

	@Override
	protected ResourceLocation getGUITexture() {
		return GUI_TEXTURE;
	}

	@Override
	public void initGui() {
		super.initGui();
		int window_x = (width - xSize) / 2;
		int window_y = (height - ySize) / 2;
		button_fire = new GuiButtonFoundry(1, 117 + window_x, 15 + window_y, 17, 17, GUI_TEXTURE, 187, 107, 204, 107);
		buttonList.add(button_fire);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (processingState == -1 &&
		        te_ms.hasBlock() &&
		        isPointInRegion(GRID_X, GRID_Y, GRID_SIZE - 1, GRID_SIZE - 1, mouseX, mouseY) &&
		        mouseButton >= 0 &&
		        mouseButton <= 1) {
		    processingState = mouseButton;
			selectSlot(mouseX, mouseY);
		}
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
	{
	    super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	    if (processingState == clickedMouseButton)
        {
            selectSlot(mouseX, mouseY);
        }
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state)
	{
	    super.mouseReleased(mouseX, mouseY, state);
	    if (processingState == state)
        {
	        if (pattern.contains(Boolean.TRUE))
            {
	            int depth = (processingState == 0 ? 1 : -1) * (isShiftKeyDown() ? 4 : 1);
	            te_ms.carve(pattern, depth);
	            Collections.fill(pattern, Boolean.FALSE);
            }
	        processingState = -1; // No state.
        }
	}

	private void selectSlot(int mouseX, int mouseY)
    {
        if (processingState != -1 && isPointInRegion(GRID_X, GRID_Y, GRID_SIZE - 1, GRID_SIZE - 1, mouseX, mouseY))
        {
            int x = (mouseX - GRID_X - guiLeft) / GRID_SLOT_SIZE;
            int y = (mouseY - GRID_Y - guiTop) / GRID_SLOT_SIZE;
            pattern.set(y * 6 + x, Boolean.TRUE);
        }
    }
}
