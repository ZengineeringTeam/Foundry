package exter.foundry.client.renderer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import exter.foundry.tileentity.TileEntityCastingTableBase;
import exter.foundry.util.hashstack.HashableItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CastingTableRenderer extends TileEntitySpecialRenderer<TileEntityCastingTableBase>
{
    static private final EnumFacing[] facings = new EnumFacing[] { null, EnumFacing.DOWN, EnumFacing.UP,
            EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST };
    protected final double left;
    protected final double right;
    protected final double top;
    protected final double bottom;
    protected final double low;
    protected final double high;

    private final static Map<HashableItem, Integer> colors;

    static
    {
        colors = new HashMap<>(17);
        colors.put(new HashableItem(new ItemStack(Blocks.GLASS_PANE)), 0x40FFFFFF);
        for (EnumDyeColor dye : EnumDyeColor.values())
        {
            int color = ItemDye.DYE_COLORS[dye.getDyeDamage()];
            int c1 = 63 + (color & 0xFF) * 3 / 4;
            int c2 = 63 + (color >> 8 & 0xFF) * 3 / 4;
            int c3 = 63 + (color >> 16 & 0xFF) * 3 / 4;
            int fluid_color = c1 | c2 << 8 | c3 << 16 | 100 << 24;
            colors.put(new HashableItem(new ItemStack(Blocks.STAINED_GLASS_PANE, 1, dye.getMetadata())), fluid_color);
        }
    }

    public CastingTableRenderer(int left, int right, int top, int bottom, int low, int high)
    {
        this.left = (double) left / 16 - 0.005;
        this.right = (double) right / 16 + 0.005;
        this.top = (double) top / 16 - 0.005;
        this.bottom = (double) bottom / 16 + 0.005;
        this.low = (double) low / 16 + 0.01;
        this.high = (high - 0.1) / 16;
    }

    protected int getItemColor(ItemStack stack)
    {
        Integer color = HashableItem.getFromMap(colors, stack);
        if (color == null)
        {
            int r = 0;
            int g = 0;
            int b = 0;
            int count = 0;
            for (EnumFacing facing : facings)
            {
                List<BakedQuad> quads = Minecraft.getMinecraft().getRenderItem()
                        .getItemModelWithOverrides(stack, getWorld(), Minecraft.getMinecraft().player)
                        .getQuads(null, facing, 0);
                if (quads != null)
                {
                    for (BakedQuad q : quads)
                    {
                        TextureAtlasSprite texture = q.getSprite();
                        for (int i = 0; i < texture.getFrameCount(); i++)
                        {
                            int[] pixels = texture.getFrameTextureData(i)[0];
                            int w = texture.getIconWidth();
                            int h = texture.getIconHeight();
                            for (int y = 1; y < h - 1; y++)
                            {
                                for (int x = 1; x < w - 1; x++)
                                {
                                    int j = y * w + x;
                                    int p = pixels[j];
                                    int a = p >>> 24 & 0xFF;
                                    if (a > 127)
                                    {
                                        int a1 = pixels[j - 1] >>> 24 & 0xFF;
                                        int a2 = pixels[j + 1] >>> 24 & 0xFF;
                                        int a3 = pixels[j - w] >>> 24 & 0xFF;
                                        int a4 = pixels[j + w] >>> 24 & 0xFF;
                                        if (a1 > 127 && a2 > 127 && a3 > 127 && a4 > 127)
                                        {
                                            r += p & 0xFF;
                                            g += p >>> 8 & 0xFF;
                                            b += p >>> 16 & 0xFF;
                                            count++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (count > 0)
            {
                r /= count;
                g /= count;
                b /= count;
            }
            color = 0xFF000000 | r & 0xFF | (g & 0xFF) << 8 | (b & 0xFF) << 16;
            colors.put(new HashableItem(stack), color);
        }
        return color;
    }

    @Override
    public void render(TileEntityCastingTableBase te, double x, double y, double z, float partialTicks, int destroyStage, float a)
    {
        FluidStack fluid = te.getTank(0).getFluid();
        GL11.glPushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableAlpha();
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.resetColor();
        GL11.glTranslatef((float) x, (float) y, (float) z);
        if (!te.getStackInSlot(0).isEmpty())
        {
            renderItem(te.getStackInSlot(0), fluid);
        }
        else if (fluid != null)
        {
            TextureAtlasSprite texture = Minecraft.getMinecraft().getTextureMapBlocks()
                    .getAtlasSprite(fluid.getFluid().getStill(fluid).toString());

            int color = fluid.getFluid().getColor(fluid);
            int light = te.getWorld().getCombinedLight(te.getPos(), fluid.getFluid().getLuminosity(fluid));

            double progress;
            if (te.getStackInSlot(0).isEmpty())
            {
                progress = 1.0f;
            }
            else
            {
                progress = (double) te.getProgress() / TileEntityCastingTableBase.CAST_TIME;
                progress = Math.sqrt(progress);
            }
            float alpha = (color >> 24 & 255) / 255.0F * (float) progress;
            float red = (color >> 16 & 255) / 255.0F;
            float green = (color >> 8 & 255) / 255.0F;
            float blue = (color & 255) / 255.0F;
            int l1 = light >> 0x10 & 0xFFFF;
            int l2 = light & 0xFFFF;

            double min_u = texture.getInterpolatedU(left * 16);
            double min_v = texture.getInterpolatedV(top * 16);
            double max_u = texture.getInterpolatedU(right * 16);
            double max_v = texture.getInterpolatedV(bottom * 16);
            double fluid_y = (double) fluid.amount / te.getTank(0).getCapacity() * (high - low) + low;
            BufferBuilder tessellator = Tessellator.getInstance().getBuffer();
            tessellator.begin(7, DefaultVertexFormats.BLOCK);
            tessellator.pos(left, fluid_y, bottom).color(red, green, blue, alpha).tex(min_u, max_v).lightmap(l1, l2)
                    .endVertex();
            tessellator.pos(right, fluid_y, bottom).color(red, green, blue, alpha).tex(max_u, max_v).lightmap(l1, l2)
                    .endVertex();
            tessellator.pos(right, fluid_y, top).color(red, green, blue, alpha).tex(max_u, min_v).lightmap(l1, l2)
                    .endVertex();
            tessellator.pos(left, fluid_y, top).color(red, green, blue, alpha).tex(min_u, min_v).lightmap(l1, l2)
                    .endVertex();
            Tessellator.getInstance().draw();
        }
        GlStateManager.enableAlpha();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

    protected void renderItem(ItemStack item, FluidStack fluid)
    {
        int color = getItemColor(item);
        float alpha = (color >> 24 & 255) / 255.0F;
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        if (fluid != null)
        {
            GlStateManager.depthMask(false);
        }

        GlStateManager.disableTexture2D();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_COLOR); // TODO
        buffer.pos(left, high, bottom).color(red, green, blue, alpha).endVertex();
        buffer.pos(right, high, bottom).color(red, green, blue, alpha).endVertex();
        buffer.pos(right, high, top).color(red, green, blue, alpha).endVertex();
        buffer.pos(left, high, top).color(red, green, blue, alpha).endVertex();
        Tessellator.getInstance().draw();
        if (fluid != null)
        {
            GlStateManager.depthMask(true);
        }

        GlStateManager.enableTexture2D();
    }

    protected boolean uvLockItem()
    {
        return true;
    }
}
