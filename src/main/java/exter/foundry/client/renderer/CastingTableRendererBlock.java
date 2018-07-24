package exter.foundry.client.renderer;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CastingTableRendererBlock extends CastingTableRenderer
{

    public CastingTableRendererBlock()
    {
        super(2, 14, 2, 14, 2, 14);
    }

    @Override
    protected int getItemColor(ItemStack stack)
    {
        return 0xFFFFFFFF;
    }

    protected TextureAtlasSprite getItemTexture(ItemStack stack)
    {
        List<BakedQuad> quads = Minecraft.getMinecraft().getRenderItem()
                .getItemModelWithOverrides(stack, getWorld(), Minecraft.getMinecraft().player)
                .getQuads(null, EnumFacing.UP, 0);
        if (quads != null && quads.size() > 0)
        {
            return quads.get(0).getSprite();
        }
        return null;
    }

    protected void renderItem(ItemStack item, FluidStack fluid)
    {
        int color = getItemColor(item);
        TextureAtlasSprite texture = getItemTexture(item);
        float alpha = (color >> 24 & 255) / 255.0F;
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        boolean lock = uvLockItem();
        double min_u = texture.getInterpolatedU((lock ? left : 0) * 16);
        double min_v = texture.getInterpolatedV((lock ? top : 0) * 16);
        double max_u = texture.getInterpolatedU((lock ? right : 1) * 16);
        double max_v = texture.getInterpolatedV((lock ? bottom : 1) * 16);
        if (fluid != null)
        {
            GlStateManager.depthMask(false);
        }
        BufferBuilder tessellator = Tessellator.getInstance().getBuffer();
        tessellator.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        tessellator.pos(left, high, bottom).tex(min_u, max_v).color(red, green, blue, alpha).endVertex();
        tessellator.pos(right, high, bottom).tex(max_u, max_v).color(red, green, blue, alpha).endVertex();
        tessellator.pos(right, high, top).tex(max_u, min_v).color(red, green, blue, alpha).endVertex();
        tessellator.pos(left, high, top).tex(min_u, min_v).color(red, green, blue, alpha).endVertex();
        Tessellator.getInstance().draw();
        if (fluid != null)
        {
            GlStateManager.depthMask(true);
        }
    }

    @Override
    protected boolean uvLockItem()
    {
        return false;
    }
}
