package exter.foundry.integration.top;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.apiimpl.client.ElementProgressRender;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import mcjty.theoneprobe.network.NetworkTools;
import mcjty.theoneprobe.rendering.RenderHelper;

import java.awt.*;

public class ElementFluid implements IElement
{
    public static int ELEMENT_FLUID;
    private final String fluidName;
    private final int amount, capacity, color1, color2;
    private final boolean sneaking;

    public ElementFluid(String fluidName, int amount, int capacity, int color1, boolean sneaking)
    {
        this.fluidName = fluidName;
        this.amount = amount;
        this.capacity = capacity;
        this.color1 = color1;
        this.color2 = new Color(this.color1).darker().hashCode();
        this.sneaking = sneaking;
    }

    public ElementFluid(ByteBuf buf)
    {
        this.fluidName = NetworkTools.readString(buf);
        this.amount = buf.readInt();
        this.capacity = buf.readInt();
        this.color1 = buf.readInt();
        this.color2 = new Color(this.color1).darker().hashCode();
        this.sneaking = buf.readBoolean();
    }

    @Override
    public void render(int x, int y)
    {
        ElementProgressRender.render(new ProgressStyle().filledColor(color1).alternateFilledColor(color2).showText(false), amount, capacity, x, y, 100, 12);
        for (int i = 1; i < 10; i++)
        {
            RenderHelper.drawVerticalLine(x + i * 10, y + 1, y + (i == 5 ? 11 : 6), 0xff767676);
        }
        if (sneaking)
            ElementTextRender.render((amount > 0) ? amount + " mB / " + capacity + " mB" : "0 mB", x + 3, y + 2);
        else
            ElementTextRender.render(fluidName, x + 3, y + 2);
    }

    @Override
    public int getWidth()
    {
        return 100;
    }

    @Override
    public int getHeight()
    {
        return 12;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        NetworkTools.writeString(buf, this.fluidName);
        buf.writeInt(this.amount);
        buf.writeInt(this.capacity);
        buf.writeInt(this.color1);
        buf.writeBoolean(sneaking);
    }

    @Override
    public int getID()
    {
        return ELEMENT_FLUID;
    }
}
